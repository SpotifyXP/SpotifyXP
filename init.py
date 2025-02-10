import shutil
import os
import fnmatch
import errno

# Function to copy a directory and its contents
def copy_directory(src, dst):
    """Copy a directory and its contents."""
    try:
        shutil.copytree(src, dst)
    except OSError as exc:
        if exc.errno in (errno.ENOTDIR, errno.EINVAL):
            shutil.copy(src, dst)
        else:
            raise

# Function to remove a directory and its contents
def remove_directory(src):
    """Remove a directory and its contents."""
    if os.path.exists(src):
        try:
            shutil.rmtree(src)
        except OSError as e:
            print(f"Error removing directory {src}: {e}")

# Function to perform mass text replacement in files matching a pattern
def mass_replace(directory, file_pattern, search_string, replace_string):
    """Perform mass text replacement in files matching a pattern."""
    for root, _, files in os.walk(directory):
        for filename in fnmatch.filter(files, file_pattern):
            filepath = os.path.join(root, filename)
            try:
                with open(filepath, 'r', encoding='utf-8') as file:
                    lines = file.readlines()

                modified_lines = [line.replace(search_string, replace_string) for line in lines]

                with open(filepath, 'w', encoding='utf-8') as file:
                    file.writelines(modified_lines)
            except Exception as e:
                print(f"Error performing replacement in file {filepath}: {e}")

# Function to handle MPRIS Java files copy and replacements
def handle_mpris_java():
    """Handle copying and replacing MPRIS Java files."""
    src = "deps/mpris-java/src/main/java/org"
    dst = "src/main/java/com/spotifyxp/deps/org"
    if os.path.exists(dst):
        inp = input(f"Directory {dst} already exists. Overwrite? [Y/N]").strip().lower()
        if inp not in ["y", ""]:
            return

    remove_directory(dst)
    copy_directory(src, dst)
    mass_replace(dst, "*.java", "import org.mpris", "import com.spotifyxp.deps.org.mpris")
    mass_replace(dst, "*.java", "package org.mpris", "package com.spotifyxp.deps.org.mpris")

# Function to handle Java Setup Tool files copy and replacements
def handle_java_setup_tool():
    """Handle copying and replacing Java Setup Tool files."""
    src = "deps/JavaSetupTool/src/main/java/de/werwolf2303/javasetuptool"
    dst = "src/main/java/com/spotifyxp/deps/de/werwolf2303/javasetuptool"
    if os.path.exists(dst):
        inp = input(f"Directory {dst} already exists. Overwrite? [Y/N]").strip().lower()
        if inp not in ["y", ""]:
            return

    remove_directory(dst)
    copy_directory(src, dst)
    mass_replace(dst, "*.java", "import de.werwolf2303.javasetuptool", "import com.spotifyxp.deps.de.werwolf2303.javasetuptool")
    mass_replace(dst, "*.java", "package de.werwolf2303.javasetuptool", "package com.spotifyxp.deps.de.werwolf2303.javasetuptool")

# Function to handle vlcj files copy and replacements
def handle_vlcj():
    """Handle copying and replacing vlcj files."""
    src_java = "deps/vlcj/src/main/java/uk"
    dst_java = "src/main/java/com/spotifyxp/deps/uk"
    src_vlc = "deps/vlcj/src/main/resources/vlc"
    dst_vlc = "src/main/resources/vlc"

    if os.path.exists(dst_java):
        inp = input(f"Directory {dst_java} already exists. Overwrite? [Y/N]").strip().lower()
        if inp not in ["y", ""]:
            return

    remove_directory(dst_java)
    remove_directory(dst_vlc)
    copy_directory(src_java, dst_java)
    copy_directory(src_vlc, dst_vlc)
    mass_replace(dst_java, "*.java", "import uk.co.caprica", "import com.spotifyxp.deps.uk.co.caprica")
    mass_replace(dst_java, "*.java", "import static uk.co.caprica", "import static com.spotifyxp.deps.uk.co.caprica")
    mass_replace(dst_java, "*.java", "package uk.co.caprica", "package com.spotifyxp.deps.uk.co.caprica")

# Function to initialize the project by running all setup operations
def initialize_project():
    """Run all the setup tasks for the project."""
    handle_mpris_java()
    handle_java_setup_tool()
    handle_vlcj()

# Start the initialization process
initialize_project()
