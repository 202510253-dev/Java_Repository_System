@echo off
cd "C:\Users\Opriasa\Desktop\Java_Repository_System\Repository System"
javac -cp "lib\*" -d bin src\DatabaseConnection.java src\Frame.java src\login.java src\Repository.java src\AddWindow.java src\EditWindow.java
java -cp "bin;lib\*" login