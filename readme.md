# TDT4100 exercise feature extraction

Feature extraction from the programming process of students solving exercises in the course TDT4100
at NTNU.

## Setup

Install third party jars:

```
mvn install:install-file -Dfile=libs/no.hal.learning.exercise.jdt-0.1.0-SNAPSHOT.jar \
-DgroupId=no.hal.learning.exercise -DartifactId=jdt -Dversion=0.1.0-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=libs/no.hal.learning.exercise.junit-0.1.0-SNAPSHOT.jar \
-DgroupId=no.hal.learning.exercise -DartifactId=junit -Dversion=0.1.0-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=libs/no.hal.learning.exercise.model-0.1.0-SNAPSHOT.jar \
-DgroupId=no.hal.learning.exercise -DartifactId=model -Dversion=0.1.0-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=libs/no.hal.learning.exercise.workbench-0.1.0-SNAPSHOT.jar \
-DgroupId=no.hal.learning.exercise -DartifactId=workbench -Dversion=0.1.0-SNAPSHOT -Dpackaging=jar

mvn install:install-file -Dfile=libs/no.hal.learning.exercise.workspace-0.1.0-SNAPSHOT.jar \
-DgroupId=no.hal.learning.exercise -DartifactId=workspace -Dversion=0.1.0-SNAPSHOT -Dpackaging=jar
```
