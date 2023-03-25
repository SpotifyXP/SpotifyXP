@echo off


echo Building SpotifyXP

REM Build with Maven
call mvn clean package

REM check if file has been build
IF NOT EXIST target/SpotifyXP.jar GOTO BUILDNOTEXISTING

REM Check if build folder exists
IF EXIST build/ GOTO BUILDEXISTING

REM Create build directory
mkdir build

REM Goto BUILDEXISTING because build directory has been made
GOTO BUILDEXISTING

:BUILDNOTEXISTING
REM Set color to Red
color 04
echo Build failed


:BUILDEXISTING

REM copy file
cp target/SpotifyXP.jar build

REM check if file has ben copied
IF NOT EXIST build/SpotifyXP.jar GOTO BUILDNOTEXISTING

REM Set color to Green
color 0A
echo Build Successfull! You can find it in %cd%/build/SpotifyXP.jar