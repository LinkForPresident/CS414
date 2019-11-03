Development Manual
==================================

How to set up workspace:

0) (DEPENDENCIES): This project is designed to run on Linux, specifically Ubuntu 18. Required packages are: java, git, curl, npm, junit.
1) Download and install IntelliJ idea from https://www.jetbrains.com/idea/download/#section=linux
2) Run IntelliJ and select the option to import a project using version control. Use this link to clone the repo https://github.com/DavidWellsTheDeveloper/cs414-f18-001-Team.name.git.
3) Once the project has been loaded, build the project. Now you're ready to run the different components


# Running The Server
##In order to compile and run the server:
* From the cs414-f18-001-Team.name root folder, execute 2 commands:

    * ```javac -cp .:GameServer/gson-2.8.5.jar GameServer/*.java ``` 
    
    * ```java -cp .:GameServer/mariadb-java-client-2.4.4.jar:GameServer/gson-2.8.5.jar GameServer.Server```

    * add the ```--debug``` flag at the end of the previous command to enable verbose debug mode.

### The server should print out
```GameServer listening.```

## Adding response support
    Add Workflow here...

# Running tests
To run game logic test: execute GameLogic/GameTest.java by right clicking the class and selecting "run".

GameLogic backend

#Compiling GameLogic java packages

javac GameLogic/*.java GameLogic/exception/*.java GameLogic/application/*.java -classpath lib/junit-4.12.jar 

#Running java tests for GameLogic backend

Run the GameTest.java in your IDE