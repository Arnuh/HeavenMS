@echo off
@title HeavenMS
set CLASSPATH=.;nbdist\*
C:\Progra~1\Java\jdk1.7.0_80\bin\java.exe -Xmx4G -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Dnet.sf.odinms.wzpath=wz/ net.server.Server
pause