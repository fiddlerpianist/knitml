@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM KnitML Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM KNITML_HOME - location of KnitML's installed home dir
@REM KNITML_OPTS - parameters passed to the Java VM when running KnitML
@REM     e.g. to debug KnitML itself, use
@REM set KNITML_OPTS=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM ----------------------------------------------------------------------------

@REM Begin all REM lines with '@' in case KNITML_BATCH_ECHO is 'on'
@echo off

@REM set %HOME% to equivalent of $HOME
if "%HOME%" == "" (set HOME=%HOMEDRIVE%%HOMEPATH%)

@REM Execute a user defined script before this one
@REM if exist "%HOME%\mavenrc_pre.bat" call "%HOME%\mavenrc_pre.bat"

set ERROR_CODE=0

@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" @setlocal

@REM ==== START VALIDATION ====
if not "%JAVA_HOME%" == "" goto OkJHome

echo.
echo ERROR: JAVA_HOME not found in your environment.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:OkJHome
if exist "%JAVA_HOME%\bin\java.exe" goto chkKHome

echo.
echo ERROR: JAVA_HOME is set to an invalid directory.
echo JAVA_HOME = %JAVA_HOME%
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation
echo.
goto error

:chkKHome
if not "%KNITML_HOME%"=="" goto valKHome

if "%OS%"=="Windows_NT" SET KNITML_HOME=%~dp0\..
echo KnitML Home is %KNITML_HOME%
if not "%KNITML_HOME%"=="" goto valKHome

echo.
echo ERROR: KNITML_HOME not found in your environment.
echo Please set the KNITML_HOME variable in your environment to match the
echo location of the KnitML installation
echo.
goto error

:valKHome
if exist "%KNITML_HOME%\bin\knitml.bat" goto init

echo.
echo ERROR: KNITML_HOME is set to an invalid directory.
echo KNITML_HOME = %KNITML_HOME%
echo Please set the KNITML_HOME variable in your environment to match the
echo location of the KnitML installation
echo.
goto error
@REM ==== END VALIDATION ====

:init
@REM Decide how to startup depending on the version of windows

@REM -- Win98ME
if NOT "%OS%"=="Windows_NT" goto Win9xArg

@REM -- 4NT shell
if "%@eval[2+2]" == "4" goto 4NTArgs

@REM -- Regular WinNT shell
set KNITML_CMD_LINE_ARGS=%*
goto endInit

@REM The 4NT Shell from jp software
:4NTArgs
set KNITML_CMD_LINE_ARGS=%$
goto endInit

:Win9xArg
@REM Slurp the command line arguments.  This loop allows for an unlimited number
@REM of agruments (up to the command line limit, anyway).
set KNITML_CMD_LINE_ARGS=
:Win9xApp
if %1a==a goto endInit
set KNITML_CMD_LINE_ARGS=%KNITML_CMD_LINE_ARGS% %1
shift
goto Win9xApp

@REM Reaching here means variables are defined and arguments have been captured
:endInit
SET KNITML_JAVA_EXE="%JAVA_HOME%\bin\java.exe"


@REM set all JARs in the KnitML lib directory to the classpath to use

set KNITML_JARS=


for /f "delims=" %%a in ('dir %KNITML_HOME%\lib\*.jar /b /a-d') do call :knitmljarsLoop %%a
goto :setknitmlCP

:knitmljarsLoop
if "%KNITML_JARS%"=="" set KNITML_JARS=%KNITML_HOME%\lib\%1& goto :end
set KNITML_JARS=%KNITML_JARS%;%KNITML_HOME%\lib\%1
goto :end

:setknitmlCP

SET KNITML_CLASSPATH=.;%KNITML_HOME%/conf;%KNITML_JARS%

@REM Start KNITML
:runknitml
%KNITML_JAVA_EXE% %KNITML_OPTS% -classpath "%KNITML_CLASSPATH%" com.knitml.tools.runner.KnitML %KNITML_CMD_LINE_ARGS%
if ERRORLEVEL 1 goto error
goto end

:error
if "%OS%"=="Windows_NT" @endlocal
set ERROR_CODE=1

:end
@REM set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" goto endNT

@REM For old DOS remove the set variables from ENV - we assume they were not set
@REM before we started - at least we don't leave any baggage around
set KNITML_JAVA_EXE=
set KNITML_CMD_LINE_ARGS=
set KNITML_CLASSPATH=
goto postExec

:endNT
@endlocal

:postExec

exit /B %ERROR_CODE%

