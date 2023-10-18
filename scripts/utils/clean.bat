REM Delete build directory
IF EXIST build/ RMDIR /S /Q build

REM Delete old build
IF EXIST target/ RMDIR /S /Q target

REM Delete modules directory
IF EXIST Modules/ RMDIR /S /Q Modules