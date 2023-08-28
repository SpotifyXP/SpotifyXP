ZipFile=WScript.Arguments(0)
ExtractTo=WScript.Arguments(1)


Set fso = CreateObject("Scripting.FileSystemObject")

If NOT fso.FolderExists(ExtractTo) Then

 fso.CreateFolder(ExtractTo)

End If

set objShell = CreateObject("Shell.Application")

set FilesInZip=objShell.NameSpace(ZipFile).items

objShell.NameSpace(ExtractTo).CopyHere(FilesInZip)

Set fso = Nothing
Set objShell = Nothing