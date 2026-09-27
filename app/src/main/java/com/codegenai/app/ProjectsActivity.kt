package com.codegenai.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codegenai.app.databinding.ActivityProjectsBinding
import com.codegenai.app.adapters.ProjectsAdapter
import com.codegenai.app.utils.ProjectManager
import kotlinx.coroutines.launch

class ProjectsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProjectsBinding
    private lateinit var projectsAdapter: ProjectsAdapter
    private lateinit var projectManager: ProjectManager
    private val projects = mutableListOf<ProjectManager.Project>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        projectManager = ProjectManager(this)
        setupToolbar()
        setupRecyclerView()
        loadProjects()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Projects"
    }

    private fun setupRecyclerView() {
        projectsAdapter = ProjectsAdapter(projects) { project, action ->
            handleProjectAction(project, action)
        }
        binding.projectsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProjectsActivity)
            adapter = projectsAdapter
        }
    }

    private fun loadProjects() {
        lifecycleScope.launch {
            try {
                projects.clear()
                projects.addAll(projectManager.getAllProjects())
                if (projects.isEmpty()) {
                    binding.emptyState.visibility = android.view.View.VISIBLE
                    binding.projectsRecyclerView.visibility = android.view.View.GONE
                } else {
                    binding.emptyState.visibility = android.view.View.GONE
                    binding.projectsRecyclerView.visibility = android.view.View.VISIBLE
                }
                projectsAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@ProjectsActivity, "Error loading projects", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleProjectAction(project: ProjectManager.Project, action: String) {
        when (action) {
            "download" -> downloadProject(project)
            "delete" -> deleteProject(project)
        }
    }

    private fun downloadProject(project: ProjectManager.Project) {
        Toast.makeText(this, "Downloading ${project.name}...", Toast.LENGTH_SHORT).show()
        // Trigger download in file manager
    }

    private fun deleteProject(project: ProjectManager.Project) {
        lifecycleScope.launch {
            try {
                projectManager.deleteProject(project.id)
                projects.remove(project)
                projectsAdapter.notifyDataSetChanged()
                if (projects.isEmpty()) {
                    binding.emptyState.visibility = android.view.View.VISIBLE
                    binding.projectsRecyclerView.visibility = android.view.View.GONE
                }
                Toast.makeText(this@ProjectsActivity, "Project deleted", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this@ProjectsActivity, "Error deleting project", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
