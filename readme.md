# TDT4100 exercise feature extraction

Feature extraction from the programming process of students solving exercises in the course TDT4100
at NTNU.

## Usage

Download the latest standalone jar from
[releases](https://github.com/boyeborg/tdt4100-exercise-feature-extraction/releases).

Then run it:

```
java -jar tdt4100-exercise-feature-extraction-{version}-STANDALONE.jar path/to/exercises
```

If you want, you can create an alias for it:

```
sudo cp tdt4100-exercise-feature-extraction-{version}-STANDALONE.jar /opt/tefe.jar
sudo chmod +x /opt/tefe.jar
alias tefe="java -jar /opt/tefe.jar"
```

The `path/to/exercises` should be the path the a directory containing one folder for each student.
The name of that folder will be the id of that student. Each student folder might contain several
exercise files. Here's an example of a structure:

```
exercises/
|--001/
  |--Exercise1.ex
  |--Exercise3.ex
|--002/
  |--Exercise4.ex
|--003/
  |--Exercise1.ex
  |--Exercise2.ex
  |--Exercise5.ex
|--004/
  |--Exercise1.ex
...
```

The program will write the results to the terminal (stdout). If you want to write the results to the
results to a file: `tefe path/to/exercises > results.csv`

You can also copy the results direcly to your clipboard.

Linux: `tefe path/to/exercises | xclip -sel clip`  
Mac: `tefe path/to/exercises | pbcopy`



## Setup

First clone the repository:

```
git clone git@github.com:boyeborg/tdt4100-exercise-feature-extraction.git
```

Then install third party jars:

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
