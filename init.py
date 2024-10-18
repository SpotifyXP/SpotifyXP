import shutil, errno
import os
import fnmatch
import fileinput

def copyDirectory(src, dst):
    try:
        shutil.copytree(src, dst)
    except OSError as exc:
        if exc.errno in (errno.ENOTDIR, errno.EINVAL):
            shutil.copy(src, dst)
        else: raise

def removeDirectory(src):
    try:
        shutil.rmtree(src)
    except OSError as exc:
        raise

def mass_replace(directory, file_pattern, search_string, replace_string):
    for root, dirs, files in os.walk(directory):
        for filename in fnmatch.filter(files, file_pattern):
            filepath = os.path.join(root, filename)
            with fileinput.FileInput(filepath, inplace=True, backup=False) as file:
                for line in file:
                    print(line.replace(search_string, replace_string), end='')


def doMPRISJava():
    def copyMPRISJava():
        if os.path.exists("src/main/java/com/spotifyxp/deps/org"): removeDirectory("src/main/java/com/spotifyxp/deps/org")
        copyDirectory("deps/mpris-java/src/main/java/org", "src/main/java/com/spotifyxp/deps/org")
        mass_replace("src/main/java/com/spotifyxp/deps/org/mpris", "*.java", "import org.mpris", "import com.spotifyxp.deps.org.mpris")
        mass_replace("src/main/java/com/spotifyxp/deps/org/mpris", "*.java", "package org.mpris", "package com.spotifyxp.deps.org.mpris")
    if os.path.exists("src/main/java/com/spotifyxp/deps/org"):
        inp = input("Overwrite mpris-java? [Y/N]")
        if inp.lower().__eq__("y"):
            copyMPRISJava()
        elif inp.lower().__eq__(""):
            copyMPRISJava()
        elif inp.lower().__eq__("n"):
            return
        else:
            doMPRISJava()
    else:
        copyMPRISJava()

def doInit():
    doMPRISJava()

doInit()