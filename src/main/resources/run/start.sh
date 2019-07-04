#!/bin/sh
baseDir=$(cd "$(dirname "$0")"; pwd)
cp=.
for file in $baseDir/libs/*.jar
do
   cp=$cp:$file
done
java -server -Xms2g -Xmx4g -XX:NewSize=512m -XX:SurvivorRatio=6 \
-XX:+AlwaysPreTouch -XX:+UseG1GC -XX:MaxGCPauseMillis=2000 \
-XX:GCTimeRatio=4 -XX:InitiatingHeapOccupancyPercent=30 -XX:G1HeapRegionSize=8M \
-XX:ParallelGCThreads=16 -XX:ConcGCThreads=16 -XX:G1HeapWastePercent=10 \
-XX:+UseTLAB -XX:+ScavengeBeforeFullGC -XX:+DisableExplicitGC \
-cp $cp com.zhishi.wechat.WechatApplication>> $baseDir/stdout.out 2>&1 &
