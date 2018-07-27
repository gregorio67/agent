SET JAVA_HOME="C:\STIS\bin\jdk1.7.0_80"
SET JAVA_LIB_PATH="D:/temp/sigar-bin/lib"

%JAVA_HOME%\bin\java -Djava.library.path=%JAVA_LIB_PATH% -jar BatchAgent-1.0.0.jar
