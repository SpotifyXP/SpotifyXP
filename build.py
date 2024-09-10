import subprocess

subprocess.call(["mvn", "package"], stdin=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True)
