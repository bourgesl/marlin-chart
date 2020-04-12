# marlin-chart
Sample Swing apps showing jfreechart plots with the Marlin renderer

How to build ?
==============

Install libraries (first time):
```mvn process-resources```

Build:
```mvn verify```

Edit your JDK (zulu 8, openjdk8):
```vi env.sh```

Run:
- OpenJDK8:
```./run-ojdk8-marlin.sh```
- OpenJDK8 + Marlin release 0.9.4.3:
```./run-ojdk8-marlin.sh```
- Zulu8:
```./test-zulu8.sh```

Enjoy FOSS !
