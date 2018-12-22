@echo off
REM commented out for later
REM del /s /q "plugins\*.*"
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -jar spigot-1.13.2.jar