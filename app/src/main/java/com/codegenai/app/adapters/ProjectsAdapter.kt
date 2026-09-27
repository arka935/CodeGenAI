package com.codegenai.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.codegenai.app.databinding.ProjectItemBinding
import com.codegenai.app.utils.ProjectManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProjectsAdapter(
    private val projects: List<ProjectManager.Project>,
    private val onAction: (ProjectManager.Project, String) -> Unit
) : RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val binding = ProjectItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProjectViewHolder(binding, onAction)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(projects[position])
    }

    override fun getItemCount(): Int = projects.size

    class ProjectViewHolder(
        private val binding: ProjectItemBinding,
        private val onAction: (ProjectManager.Project, String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(project: ProjectManager.Project) {
            binding.projectName.text = project.name
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            binding.projectDate.text = "Created: ${dateFormat.format(Date(project.createdAt))}"

            binding.btnDownload.setOnClickListener {
                onAction(project, "download")
            }
            binding.btnDelete.setOnClickListener {
                onAction(project, "delete")
            }
        }
    }
}
