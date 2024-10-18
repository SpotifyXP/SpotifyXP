# DepHelper version 2.0
#
# Info:
# It gets all dependencies and their licenses and copies them to resources for SpotifyXP
# In every deps directory there is a .DEPINFO file that has the link of the GitHub repository
# Output: src/main/resources/setup/thirdparty.html
import asyncio
import github.GithubException
import json
import os
import requests
import subprocess
import threading
import warnings
import webbrowser
from bs4 import BeautifulSoup, XMLParsedAsHTMLWarning
from github import Github, Auth
from simple_http_server import route, Parameter, server, Response


class DependencyEntry:
    licenseName: str = "No license"
    licenseUrl: str = ""
    groupId: str = ""
    artifactId: str = ""

    def toPrintableArray(self):
        return ["licenseName", self.licenseName, "groupId", self.groupId, "artifactId", self.artifactId]


def getLinksForEmbedded(directory: str, links=None) -> []:
    if links is None:
        links = []
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.__eq__(".DEPINFO"):
                with open(os.path.join(root, file), "r") as fileobject:
                    content = fileobject.read()
                    if not links.__contains__(content):
                        links.append(content)
    return links


def authenticateWithGitHub() -> str:
    # Callback url: http://127.0.0.1:41305
    git = Github()
    app = git.get_oauth_application("Ov23litYkJBtG5B6rfXE", "34f1e0261af4625c4e580735ff88b447f0840e2d")
    loginUrl = app.get_login_url("http://127.0.0.1:41305")
    running = threading.Event()

    @route("/", method=["GET"])
    def index(response: Response, code=Parameter("code", default="null")):
        global codeReturn
        codeReturn = code
        response.status_code = 200
        response.set_header("Content-Type", "text/html")
        response.write_bytes(b"<html><a>Ok</a><script>window.close();</script></html>")
        response.close()
        server.stop()

    webbrowser.open(loginUrl)

    running.set()
    try:
        asyncio.run(server.start_async("127.0.0.1", port=41305))
    except RuntimeError:
        pass

    return app.get_access_token(codeReturn).token


def convertToRepo(url: str) -> str:
    url = url.replace("https://github.com/", "")
    return url.split("/")[0] + "/" + url.split("/")[1]


def getLicenseOfGitHubRepository(git: Github, repo: str):
    repo = git.get_repo(convertToRepo(repo))
    return repo.license


def getReadableForEmbedded(token: str, entries=None) -> []:
    if entries is None:
        entries = []
    git = Github(auth=Auth.Token(token))
    for link in getLinksForEmbedded("src/main/java/com/spotifyxp/deps"):
        entry: DependencyEntry = DependencyEntry()
        try:
            licenseResult = getLicenseOfGitHubRepository(git, link)
            if licenseResult:
                entry.licenseName = licenseResult.name
            if licenseResult:
                entry.licenseUrl = licenseResult.url
        except github.GithubException:
            pass
        raw = convertToRepo(link)
        entry.groupId = raw.split("/")[0]
        entry.artifactId = raw.split("/")[1]
        entries.append(entry)
    return entries


def getLinksForMaven() -> []:
    links = []
    try:
        subprocess.run(["mvn", "org.codehaus.mojo:license-maven-plugin:2.0.0:aggregate-download-licenses",
                        "-Dlicense.excludedScopes=system,test -Dlicense.sortByGroupIdAndArtifactId=true"],
                       capture_output=True)
    except subprocess.CalledProcessError as e:
        print(e.output)
        exit(-1)
    links.append(os.listdir("target/generated-resources/licenses"))
    return links


def getReadableForMaven(dependencyEntries=[]) -> []:
    warnings.filterwarnings("ignore", category=XMLParsedAsHTMLWarning)
    parsed = BeautifulSoup(open("target/generated-resources/licenses.xml", "r").read(), "lxml")
    for dependency in parsed.find_all("dependency"):
        entry: DependencyEntry = DependencyEntry()
        entry.licenseName = dependency.find_next("name").text
        entry.groupId = dependency.find_next("groupid").text
        entry.artifactId = dependency.find_next("artifactid").text
        entry.licenseUrl = dependency.find_next("file").text
        dependencyEntries.append(entry)
    return dependencyEntries


def generateHTML(dependencyEntries: []) -> str:
    baseHtml = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
    <meta charset="UTF-8">
    <title>Thirdparty</title>
    </head>
    <body>
    <center>
    <h1>ThirdParty</h1>
    </center>
    <br>
    <br>
    <center>
    <h2>Dependencies</h2>
    </center>
    <br>
    <br>
    <div style="text-align: center;">
    """
    end = """
    </div>
    </body>
    </html>
    """
    licenseContents: [] = []
    licenses: [] = []
    for dependencyEntry in dependencyEntries:
        baseHtml += "<h3>" + dependencyEntry.groupId + ":" + dependencyEntry.artifactId + "</h3>\n"
        baseHtml += "<br>\n"
        baseHtml += "<a>" + dependencyEntry.licenseName + "</a>\n"
        baseHtml += "<br>\n"
        baseHtml += "<br>\n"
        if not licenses.__contains__(dependencyEntry.licenseName):
            licenses.append(dependencyEntry.licenseName)
            if dependencyEntry.licenseUrl.__contains__("http"):
                licenseContents.append([
                    dependencyEntry.licenseName,
                    requests.get(dependencyEntry.licenseUrl).content.decode("utf-8")
                ])
    baseHtml += "<br>\n"
    baseHtml += "<br>\n"
    baseHtml += "<br>\n"
    baseHtml += "<center><h2>Licenses</h2></center>\n"
    baseHtml += "<br>\n"
    baseHtml += "<br>\n"
    baseHtml += "<br>\n"
    for licenseContent in licenseContents:
        baseHtml += "<h2>" + licenseContent[0] + "</h2>\n"
        baseHtml += "<br>\n"
        baseHtml += "<br>\n"
        baseHtml += "<a>" + json.loads(licenseContent[1])["body"] + "</a>\n"
        baseHtml += "<br>\n"
        baseHtml += "<br>\n"
        baseHtml += "<br>\n"
    baseHtml += end
    return baseHtml.replace("F*ck", "Fuck")


embedLinks = getLinksForEmbedded("src/main/java/com/spotifyxp/deps")
embedReadable = getReadableForEmbedded(authenticateWithGitHub())
mavenLinks = getLinksForMaven()
mavenReadable = getReadableForMaven()

dependencyEntries: [] = []

dependencyEntries.extend(embedReadable)
dependencyEntries.extend(mavenReadable)

with open("src/main/resources/setup/thirdparty.html", "w") as file:
    file.write(generateHTML(dependencyEntries))
    file.close()
