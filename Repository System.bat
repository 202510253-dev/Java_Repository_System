@echo off
cd /d "C:\Users\Opriasa\Desktop\Java_Repository_System\Repository System"
javac -cp "lib\*" -d bin src\*.java
if %errorlevel% equ 0 (
    java -cp "bin;lib\*" login
    pause
) else (
    echo Compilation failed.
    pause
)