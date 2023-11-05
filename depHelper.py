import subprocess
import shutil
import os
import requests
from bs4 import BeautifulSoup


try:
    os.remove("src/main/resources/licenses.xml")
except:
    pass
try:
    os.remove("src/main/resources/setup/thirdparty.html")
except:
    pass
subprocess.run(["mvn", "org.codehaus.mojo:license-maven-plugin:2.0.0:aggregate-download-licenses", "-Dlicense.excludedScopes=system,test -Dlicense.sortByGroupIdAndArtifactId=true"])
shutil.copyfile("target/generated-resources/licenses.xml", "src/main/resources/licenses.xml")

thirdparty = '<!DOCTYPE html>\n<html lang="en">\n<head>\n<meta charset="UTF-8">\n<title>Title</title>\n</head>\n<body>\n<h1>ThirdParty Licenses</h1>\n<br>\n<br>\n<div style="text-align: center;">'
end = "\n</div>\n</body>\n</html>"


class LicenseEntry:
    def __init__(self, name, url):
        self.name = name
        self.url = url


tofind = []

with open("licenses.xml") as f:
    out = BeautifulSoup(f.read(), features="xml")
    for s in out.find_all("dependency"):
        soup = BeautifulSoup(str(s), features="xml")
        groupId = soup.find("groupId")
        artifactId = soup.find("artifactId")
        alicense = soup.find("licenses").find("license")
        alicenseName = alicense.find("name")
        alicenseURL = alicense.find("url")
        line = f"\n<h3>{groupId}:{artifactId}</h3>\n<br>\n<a>{alicenseName}</a>\n<br>\n<br>"
        thirdparty += line

    # Dependency parsing done now insert all the license
    thirdparty += "<br>\n<br>\n<br>\n<br>\n<br>"
    counter = 0
    for s in out.find_all("dependency"):
        soup = BeautifulSoup(str(s), features="xml")
        alicense = soup.find("licenses").find("license")
        alicenseName = str(alicense.find("name")).replace("<name>", "").replace("</name>", "")
        alicenseURL = alicense.find("url")
        contains = False
        for entry in tofind:
            if (str(entry.name).__eq__(alicenseName)):
                contains = True
                break
        if not contains:
            if(alicenseName.__eq__("BSD-3-Clause")):
                alicenseURL = "https://raw.githubusercontent.com/s-a/license/master/_licenses/bsd-3-clause.txt"
            if(alicenseName.__eq__("LGPL, version 2.1")):
                alicenseURL = "https://raw.githubusercontent.com/s-a/license/master/_licenses/lgpl-2.1.txt"
            if(alicenseName.__eq__("GNU Lesser General Public License, Version 2.1")):
                alicenseURL = "https://raw.githubusercontent.com/s-a/license/master/_licenses/lgpl-2.1.txt"
            if(alicenseName.__eq__("GNU Lesser General Public License")):
                alicenseURL = "https://raw.githubusercontent.com/s-a/license/master/_licenses/gpl-3.0.txt"
            if(alicenseName.__eq__("Public Domain")):
                alicenseURL = "https://raw.githubusercontent.com/s-a/license/master/_licenses/cc0-1.0.txt"
            if(alicenseName.__eq__("GPL v3")):
                alicenseURL = "https://raw.githubusercontent.com/s-a/license/master/_licenses/gpl-3.0.txt"
            if(alicenseName.__eq__("GNU General Public License")):
                alicenseURL = "https://raw.githubusercontent.com/s-a/license/master/_licenses/gpl-3.0.txt"
            lentry = LicenseEntry(alicenseName, alicenseURL)
            tofind.append(lentry)
            print(f"Adding license ({lentry.name}) : {counter}/{len(out.find_all('dependency'))}")
        else:
            print(f"Skipping {counter}")
        counter += 1
    counter = 0
    for entry in tofind:
        down = requests.get(str(entry.url).replace("<url>", "").replace("</url>", "")).text
        if BeautifulSoup(down, features='lxml').find(attrs={'id':'LicenseText'}) == None:
            thirdparty += f"\n<h2>{entry.name}</h2>\n<br>\n<br>\n<a>{down}</a>\n<br>\n<br>\n<br>"
        else:
            thirdparty += f"\n<h2>{entry.name}</h2>\n<br>\n<br>\n<a>{BeautifulSoup(down, features='lxml').find(attrs={'id':'LicenseText'})}</a>\n<br>\n<br>\n<br>"
        print(f"Downloading license ({entry.name}) : {counter}/{len(tofind)}")
        counter += 1
    thirdparty += end
f = open("src/main/resources/setup/thirdparty.html", "w")
f.write(thirdparty)
f.close()