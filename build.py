import subprocess
from init import doInit

doInit()
subprocess.call(["mvn", "package"], stdin=subprocess.PIPE, stderr=subprocess.STDOUT, shell=True)
