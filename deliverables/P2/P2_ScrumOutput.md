## Sprint 2 Review
* Met with product owner and demonstrated the product.
* Game piece moving and invites were not working, partly due to the CSU sysadmins changing the cURL privileges such that https requests were not working.
* Promised that the move and invitation functionality would be available at the end of the sprint.
* Got good feedback on the UML diagrams of the packages and the traceability link matrix.
* Felt good about the progress of the team and the state of our product.

## Sprint 2 Retrospective
### Went well
* Met with product owner and prioritized user stories
* Refactored previous user stories and tasks to include Acceptance Criterias
* Focused on completing the deliverables as well as completing tasks
* Communicated between teammates more than previous sprint (via Slack)
* Participated in much more pair programming
* Took a more explicit focus about following scrum methodology
* Took inspiration from good code practices from class and applied it to code

### Room for improvement
* Lost a teammate, and had to adjust workload around this
* Did work before planning partly due to not having feedback
* Worked in spurts rather than a constant workflow

### What we plan to do
* Meet and plan before starting next sprint
* Continue good communication and pair programming
* Continue meeting with product owner to prioritize stories efficiently
* Aim for a constant work flow




|MEETING SUMMARIES|
|---|
**Standup Meeting 1: Friday the 13th**
Planning basic modules and approach we want to take for this project.  Identifying epic tasks, Jungle game logic, our implementation of said logic, DB design, all in nascent stages.
**Standup Meeting 2: Wednesday September 18th**
Brian: Will start checking out Git branches, getting local ENV setup, begin writing JUnit tests to test Parker's Server side code
Alex: Working on RESTful API Proof Of Concept, will eventually be used to receive / send the game requests from Client to Server
Parker: Contiuning Server side code development and JUnit tests
Dave: Working on UML diagrams, front end, checking out Git branches
Tim: Starting to checkout git branches and get local instances of code up and running, JUnit tests
**Standup Meeting 3: Monday September 23rd**
Brian: Getting familiar with Parker's game logic and adding a few JUnit tests to increase our test coverage.
Alex: Planned out implementation of abstract Action class, removed OPTIONS request handler.
Parker: Continued developing JUnit Testing for back end. Going to start planning for game logic and server integration
Dave: Explore using a front end framework, and migrate away from straight HTML.
Tim: Got Jest framework started, created sanity test for front-end
**Standup Meeting 4: Wednesday September 25th**
Brian: Continuing my familiarization of Parker's backend code, starting to learn ReactJS so that I can help with front-end, but there is a long way to go for me in this regard.   
Alex: Implemented abstract Action class and concrete implementations for each action type.
Parker: Worked on planning for game logic/server integration and data flow throughout project. Will work with Alex to start integrating the server and game logic.
Dave: Develop an attractive Front end css with pages handled by tabs.
Tim: Continued work on implementation of Jest framework, but dependencies are causing issues.
**Standup Meeting 5: Saturday September 28th**
Brian: Completed a GameApp stand-alone game client to display the Jungle game without the need for a front-end or RESTful server, in the case that our front-end is not ready in time for the Presentation day.
Alex: Used generics to remove a nasty switch statement in the server logic. 
Parker: Worked with Alex and Dave to integrate back-end game logic with back-end server code and to get messaging between the client and server.
Dave: Manage state, transfers between parent and child components, begin creating a board component.
Tim: Jest is fully set up, and tests for front-end are created. Issues reading css files properly. Provided some code for front-end development.
**Standup Meeting 6: Sunday September 29th**
Brian: Preparing Deliverables and Practicing our Presentation
Alex: Request/Response objects fleshed out/created.
Parker: Worked with Alex and Dave to integrate client/server communication to play the game. Worked on deliverables for project 1.
Dave: Make post requests from the server. Make sure integration goes smoothly.
Tim: Attempted to fix issue with css files.
**Standup Meeting 7 Planning: Wednesday October 2nd**
All: Begin planning user stories
Parker: Planned for sprint 2 requirements and meeting with professor. Will continue planning specifics for sprint 2
Dave: Consider front end user stories that will be required by the owner this sprint
Tim: Complete testing as we go and focus on front-end tasks connected to priority epics; look into schema
Brian: Organizing questions for product owner and postulating tools we can add to our development process.  Researching strategies for AI game integration and started the Proof-Of-Concept for testing of back-end RESTful services.
Alex: Ask Product owner about databases: get the database tables set up, we need a master SQL script for creating the tables.
**Standup Meeting 8: Monday Oct. 7th**
Parker: Planned for implementation of "locked game state" detection. Will continue developing it in to code.
Dave: Made front end board which successfully makes calls to the server. It's not pretty yet, but it works.
Tim: Worked on redundant json schemas. Shifted focus to Selenium.
Brian: Added more tests to RESTful JUnit tests and worked with Server backend code to further my understanding.
Alex: Created Sketch It! UML diagrams for GameServer package.
**Standup Meeting 9: Wednesday Oct. 9th**
Parker: Implemented locked-state detection to back end game logic. Will being refactoring back-end game logic to simplify design and to ease in client info parsing.
Dave: Perform a general cleanup of React after a bit of mad pressure coding to prepare a functional demo.
Tim: Nearly completed selenium framework; css still needs to be able to be read.
Brian: More RESTful server tests, but not too much else to update.
Alex: Adjusted the logic in request to be less messy.
**Standup Meeting 9: Monday Oct. 14th**
Parker: Refactored back end-game logic to maintain a more descriptive game state for easier client parsing. Will continue developing back-end tests.
Dave: Debugged a PR that broke the App
Tim: Created some tests for selenium. Began work on images necessary for playable game.
Brian: Finished RESTful JUnit tests for ViewGame, Login, Register User APIs
Alex: Refactoring server in progress, splitting Server into Server and Database classes.
**Standup Meeting 9: Wednesday Oct. 16th**
Parker: Adapted back-end tests for new game state structure.
Dave: Refactored the board into a "Square" component to isolate the logic of displaying each individual square, and hopefully is easier to read and maintain.
Tim: Images are completed and being put into the system.
Brian: Added tests for Move Piece API, modified Server code to work with Cookies (work in progress)
Alex: Refactoring server in progress, splitting into separate classes, including modifying the Abstract class Action and the individual concrete implementations of Action for each API call, and Request/Response classes.
**Standup Meeting 10: Saturday Oct. 19th**
Dave: I was out of town this Saturday, and did not participate in stand up.
Tim: Images have been put into the system. Selenium has broken, and troubleshooting is under way.
Brian: Helped Form deliverables and get user stories for our epic tasks, added a few tests to backend GameLogic
Alex: Got together as a group to work on the deliverables for this sprint, including tasks for user stories, CRC diagrams, kanban boards, UML diagrams, traceability matrix, and email to PO to set up meeting.
**Standup Meeting 11: Monday Oct. 21st**
Dave: Reviewed deliverables for sprint 2, and begin modifying code base, and documentation to better conform to what is expected in our deliverables.
Tim: Did some troubleshooting for Selenium, shifted focus to## Sprint 2 Retrospective
### Went well
* Met with product owner and prioritized user stories
* Refactored previous user stories and tasks to include Acceptance Criterias
* Focused on completing the deliverables as well as completing tasks
* Communicated between teammates more than previous sprint (via Slack)
* Participated in much more pair programming
* Took a more explicit focus about following scrum methodology
* Took inspiration from good code practices from class and applied it to code

### Room for improvement
* Lost a teammate, and had to adjust workload around this
* Did work before planning partly due to not having feedback
* Worked in spurts rather than a constant workflow

### What we plan to do
* Meet and plan before starting next sprint
* Continue good communication and pair programming
* Continue meeting with product owner to prioritize stories efficiently
* Aim for a constant work flowwards deliverables.
Brian: Added Extra http call support in RESTFul JUnit tests
Alex: Did not work much on the project, had other classes to work on.
**Standup Meeting 12: Wednesday Oct. 23rd**
Dave: Also no updates, focusing on other courses, and crunch time at work.
Tim: No updates; other projects took up all my time Monday/Tuesday.
Brian: Not too much to update, did some planning on our future project strategy
Alex: Did not work much on the project, had other classes to work on.
**Standup Meeting 13: Monday Oct. 28th**
Dave: Tried to resolve isses with MovePiece curl and response issues. Didn't make much progress
Tim: Worked on deliverables. Focusing on updating wiki, kanban board, and user stories.
Brian: Helped Debug some front-end issues and continued adding tests to JUnit RESTful tests
Alex: Out of town in Texas, visiting grandparents.
**Standup Meeting 14: Monday Nov. 1st**
Dave: Pair programmed with team. Focused with Alex on resolving request response consistency on invites MovePiece.
Tim: Did team programming with server-side and client-side bug fixes.
Brian: Added new View User Games API to Server backend and added RESTful JUnit tests to test this API
Alex: Pair programmed with Dave on invites, game responses and user authentication.
**Standup Meeting 15: Saturday Nov. 2nd**
Dave: Team Programming, persistent data handling for database integration
Tim: Did team programming with client-side bug fixes, worked on deliverables.
Brian: Added new functionality to Accept Invite API, so that after accepting an invite a new game is created.  Re-generated the UML diagrams and made changes to Presentation slides.
Alex: Pair programming with team, got database tables for Invites and Games up and running. Added a few new functions for parsing and formatting data, mostly related to the Database class. Added a diagram to the slides and a few other small things as well.
**Standup Meeting 16: Sunday Nov. 3rd**
Dave: Focus on deliverables, presentation, kanban board cleanup, finish or icebox any tasks in the backlog or in progress.
Tim: Focused on deliverables with team.
Brian: Focused on deliverables and Presentation Slides, dry run, re-generated the UML diagrams again.
Alex: Focused on Deliverables. Last night I got persistent games working in the database. Now the user can accept an invite, which creates a game. The game is stored as a JSON-like string file in the database for persistent storage. Made CRC cards for GameServer package and the skeleton for a traceability link matrix. Worked with Tim to finish the traceability link matrix, and with the rest of the team to do dry runs of our presentation.
