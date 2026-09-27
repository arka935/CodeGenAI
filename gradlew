#!/usr/bin/env bash

##############################################################################
##
##  Gradle start up script for UN*X
##
##############################################################################

# Attempt to set APP_HOME
# Resolve links: $0 may be a link
PRG="$0"
# Need this for relative symlinks.
while [ -h "$PRG" ] ; do
    ls -ld "$PRG"
    link=`expr "$PRG" : '.*-> \(.*\)$'`
    if expr "$link" : '/.*' > /dev/null; then
        PRG="$link"
    else
        PRG=`dirname "$PRG"`"/$link"
    fi
done
SAVEPWD=`pwd`
cd "`dirname "$PRG"`" >/dev/null
APP_HOME=`pwd -P`
cd "$SAVEPWD" >/dev/null

APP_NAME="Gradle"
APP_BASE_NAME=`basename "$0"`

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS=''-Xmx64m -Xms64m''

# Use the maximum available, or set MAX_FD != maximum.
MAX_FD="maximum"

warn ( ) {
    echo "$*"
}

die ( ) {
    echo
    echo "$*"
    echo
    exit 1
}

# OS specific support (must be 'true' or 'false').
CYGWIN=false
MSYS=false
DARWIN=false
NATIVE=false
case "`uname`" in
  CYGWIN* )
    CYGWIN=true
    ;;
  Darwin* )
    DARWIN=true
    ;;
  MSYS* )
    MSYS=true
    ;;
  NATIVEWIN )
    NATIVE=true
    ;;
esac

# Determine the Java command to use to start the JVM.
if [ -n "$JAVA_HOME" ] ; then
    if [ -x "$JAVA_HOME/jre/sh/java" ] ; then
        # IBM's JDK on AIX uses strange locations for the executables
        JAVACMD="$JAVA_HOME/jre/sh/java"
    else
        JAVACMD="$JAVA_HOME/bin/java"
    fi
    if [ ! -x "$JAVACMD" ] ; then
        die "ERROR: JAVA_HOME is set to an invalid directory: $JAVA_HOME

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
    fi
else
    JAVACMD="java"
    which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.

Please set the JAVA_HOME variable in your environment to match the
location of your Java installation."
fi

# Increase the maximum file descriptors if we can.
if [ "$CYGWIN" = "false" -a "$DARWIN" = "false" -a "$NATIVEWIN" = "false" ] ; then
    MAX_FD_LIMIT=`ulimit -H -n`
    if [ $? -eq 0 ] ; then
        if [ "$MAX_FD" = "maximum" -o "$MAX_FD" = "max" ] ; then
            MAX_FD="$MAX_FD_LIMIT"
        fi
        ulimit -n $MAX_FD
        if [ $? -ne 0 ] ; then
            warn "Could not set maximum file descriptor limit: $MAX_FD"
        fi
    else
        warn "Could not query maximum file descriptor limit: $MAX_FD_LIMIT"
    fi
fi

# For Darwin, add options to specify how the application appears in the dock
if $DARWIN; then
    GRADLE_OPTS="$GRADLE_OPTS \"-Xdock:name=$APP_NAME\" \"-Xdock:icon=$APP_HOME/media/gradle.icns\""
fi

# For Cygwin, switch paths to Windows format before running java
if $CYGWIN ; then
    APP_HOME=`echo "$APP_HOME" | sed 's|\\|/|g'`
    CP=`echo "$CP" | sed 's|\\|/|g'`

    # We build the pattern for arguments to be converted via cygpath
    ROOTDIRSRAW=`find -L / -maxdepth 3 -type d -name src 2>/dev/null`
    SEP=""
    for dir in $ROOTDIRSRAW ; do
        ROOTDIRS="$ROOTDIRS$SEP$dir"
        SEP="|"
    done
    OURCYGPATTERN="(^($ROOTDIRS))"
    # Add a user-defined pattern to the cygpath arguments
    if [ "$GRADLE_CYGPATTERN" != "" ] ; then
        OURCYGPATTERN="$OURCYGPATTERN|($GRADLE_CYGPATTERN)"
    fi
    # Now convert the arguments - kludge to limit ourselves to /bin/sh
    i=0
    for arg in "$@" ; do
        CHECK=`echo "$arg"|egrep -c "$OURCYGPATTERN"` -
        CHECK2=`echo "$arg"|egrep -c "^-"`                                 # count windows-style long options
        if [ $CHECK -ne 0 ] && [ $CHECK2 -eq 0 ] ; then                    # filter out -
            arg=`cygpath --path --ignore --mixed "$arg"`
        fi
        ARGS="$ARGS \"$arg\""
        i=$((i+1))
    done
    # We need to be careful to have the correct number of elements in ARGS, as we will be using
    # ARGS directly in the eval command.
fi

if [ ! -x "${JAVA_HOME}/bin/java" ] && [ ! -x "${JAVA_HOME}/jre/bin/java" ] ; then
  die "JAVA_HOME environment variable is not properly set."
fi

if [ -z "$GRADLE_HOME" ] ; then
  GRADLE_HOME="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
fi

cd "$GRADLE_HOME" || exit

eval "$JAVACMD" "$@"

