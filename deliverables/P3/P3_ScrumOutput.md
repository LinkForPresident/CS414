## Sprint 2 Review
[TODO]

## Sprint 2 Retrospective
### Went well
[TODO]

### Room for improvement
[TODO]

### What we plan to do
[TODO]

|MEETING SUMMARIES|
|---|
**Sprint Planning Ceremony: Saturday Nov. 16th**
Dave: Cleaned up KanBan board in preparation of Sprint 3. Create sprint planning ceremony document in wiki. discussed prioritizing epics, tasks, and goals. Took screenshot of kanban.
Tim: Focused on top priorities for epic completion and cleaning up previous sprint.
Brian: Discussed priorities for this sprint, started planning new test frameworks (Selenium, etc)
Alex: Discussed prioritizing epics, tasks and goals. Removed two epics (tournaments and AI) that we felt were not realistic goals to accomplish this sprint. Kanban board is starting from scratch, Dave removed everything from the In Progress column, and we'll make sure this sprint to only work on stories that are in the column.
**Standup Meeting 18: Wednesday Nov. 20th**
Dave: Discussed the possibility of implementing an Observer design pattern to update the client when the server changes.
Tim: Was forced to focus on other classes.
Brian: Added RESTful service tests for new APIs
Alex: Had to work on other classes. Real-world work also bogged me down, have a deadline approaching.
**Standup Meeting 19: Monday Dec. 2nd**
Dave: Added images to instructions, Client Registration complete, Client support for multiple games. We still need unregister implementation, and bug fixes.
Tim: Took Kanban screenshot, began work on fixing multiple game logic bugs.
Brian: Started Selenium proof of concept, getting WebDriver setup
Alex: Updated Terminal.java and Database.java with refactored code (split up methods, renamed variables, added additional error checking, moved methods from Database to Terminal). Started work on view match history API on server side.
**Standup Meeting 20: Wednesday Dec. 4th**
Dave:Identified logic bugs, and passed the task of fixing them to Tim while I continued working on front end.
Tim: Finished fixing game logic bugs, adjusted html code and removed placeholders. Began work on deliverables.
Brian: More Selenium and RESTful test work
Alex: Finished view game history on both server and client side. Match history now displays as a table in the History tab of the web app.
**Standup Meeting 21: Saturday Dec. 7th**
Dave: Added meaningful headers to the game tab with opponent names, and refactored User tab to have meaningful information, logout, and unregister buttons. Unregister still needs to be implemented however. Did pair programming with team.
Tim: Added some deliverables. Added forfeiting, a win message if the game is completed, replaced buttons with a compact dropdown menu, participated in pair programming.
Brian: Finished Selenium tests to test all aspects of ReactJS front-end 
Alex: Added unregistration of user, with propagation of game states (username=unregistered user, and other player wins by default). Helped with forfeiting game, and got cancel invites working on both the server and the client. Also added checks for registering a user, ensuring that the email and username are unique.
**Standup Meeting 22: Sunday Dec. 8th**
Dave: Worked on deliverables, Put some time into an invite bug.
Tim: Assisted in completing deliverables and ran through practice presentation.
Brian: Working on presentation and deliverables, as well as double checking that Selenium tests work on lab machines
Alex: Worked on presentation and deliverables; slides, traceability link matrix, wiki, merge conflicts. If have time, will try to implement invite refresh upon invite tab click.