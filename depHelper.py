import subprocess
import shutil
import sys
import os
import requests
from bs4 import BeautifulSoup

access_token = ""

if os.path.exists("access_token"):
    with open("access_token") as tokenfile:
        access_token = tokenfile.read()
else:
    if len(sys.argv) < 0:
        access_token = sys.argv[1]
    else:
        print("Please provide the GitHub access token as a parameter")
        sys.exit(1)


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
    def __init__(self, name, url, isjson = False):
        self.name = name
        self.url = url
        self.isjson = isjson


def traverse_directory(directory):
    githuburls = []
    for root, dirs, files in os.walk(directory):
        for file_name in files:
            if str(file_name).__eq__(".DEPINFO"):
                with open(os.path.join(root, file_name)) as depinfo:
                    if not (githuburls.__contains__(depinfo.read())): githuburls.append(depinfo.read())
                break

        for dir_name in dirs:
            githuburls.append(traverse_directory(os.path.join(root, dir_name)))
    return githuburls

def get_github_repository_license(repo_url):
    _, _, _, username, repo_name = repo_url.rstrip('/').split('/')
    api_url = f'https://api.github.com/repos/{username}/{repo_name}'
    response = requests.get(api_url)
    if response.status_code == 200:
        repository_info = response.json()
        if 'license' in repository_info:
            license_name = repository_info['license']['spdx_id']
            return f"{license_name}"
        else:
            return ""
    else:
        return f""


tofind = []
depspath = "src/main/java/com/spotifyxp/deps"
github_urls = []

def traverse_directory(directory):
    for root, dirs, files in os.walk(directory):
        for file_name in files:
            if file_name == ".DEPINFO":
                file_path = os.path.join(root, file_name)
                with open(file_path, 'r') as depinfo:
                    content = depinfo.read().strip()
                    if content and content not in github_urls:
                        if not content.__eq__("NONE"):
                            github_urls.append(str(content))
        for dir_name in dirs:
            traverse_directory(os.path.join(root, dir_name))


def get_github_repository_license(repo_url):
    parts = str(repo_url).rstrip('/').split('/')
    if len(parts) == 5 and parts[2] == 'github.com':
        username, repo_name = parts[3], parts[4]
    else:
        return "", ""
    api_url = f'https://api.github.com/repos/{username}/{repo_name}'
    headers = {
        'Authorization': f'Token {access_token}',
        'Accept': 'application/vnd.github.v3+json'  # Specify the API version
    }
    print(f"Getting license at: {api_url}")
    response = requests.get(api_url, headers=headers)
    if response.status_code == 200:
        repository_info = response.json()
        if 'license' in repository_info:
            license_url = repository_info['license']['url']
            return f"{license_url}", repository_info["license"]["spdx_id"]
        else:
            return "", ""
    else:
        return "", ""


def get_body_of_license(url):
    if url.__eq__(""): return ""
    response = requests.get(url)
    return response.json()["body"]

with open("src/main/resources/licenses.xml") as f:
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
    traverse_directory(depspath)

    for url in github_urls:
        licenseURL, licenseName = get_github_repository_license(url)
        tofind.append(LicenseEntry(licenseName, licenseURL, True))

    # Dependency parsing done now insert all the licenses
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
        if entry.isjson:
            print(f"Downloading license ({entry.name}) : {counter}/{len(tofind)}")
            down = get_body_of_license(entry.url)
            if str(down).__eq__(""):
                print(f"Skipping {counter} because it's empty")
                counter+=1
                continue
            thirdparty += f"\n<h2>{entry.name}</h2>\n<br>\n<br>\n<a>{down}</a>\n<br>\n<br>\n<br>"
            counter+=1
            continue
        print(f"Downloading license ({entry.name}) : {counter}/{len(tofind)}")
        down = requests.get(str(entry.url).replace("<url>", "").replace("</url>", "")).text
        if BeautifulSoup(down, features='lxml').find(attrs={'id':'LicenseText'}) == None:
            thirdparty += f"\n<h2>{entry.name}</h2>\n<br>\n<br>\n<a>{down}</a>\n<br>\n<br>\n<br>"
        else:
            thirdparty += f"\n<h2>{entry.name}</h2>\n<br>\n<br>\n<a>{BeautifulSoup(down, features='lxml').find(attrs={'id':'LicenseText'})}</a>\n<br>\n<br>\n<br>"
        counter += 1
    thirdparty += end
f = open("src/main/resources/setup/thirdparty.html", "w")
f.write(thirdparty)
f.close()