File Watcher System

A Java-based file monitoring project that uses a GUI to track file events by selected file extension.

Team member names:

Rohullah Babakarkhail, 
Kalsoom Babakarkhail

Final Iteration Summary:

During Iteration 6, we completed the final version of the File Watcher System. We tested real file monitoring, extension filtering, SQLite database saving, database queries, clear database behavior, exit save prompt, and GUI usability. We also added support for CREATE, CHANGE, DELETE, and RENAME event types.

Completed work:
- Final tested real file monitoring
- Finalized extension filtering
- Finalized SQLite database saving
- Finalized database query feature
- Finalized Clear Database feature
- Finalized exit save prompt
- Cleaned up GUI usability
- Completed final testing and bug fixes
- Added support for CREATE, CHANGE, DELETE, and RENAME events
- Improved final event display formatting
- Updated final README
- Prepared final screenshots and submission files

Issues encountered:
Java WatchService does not directly provide a rename event, so rename was handled by detecting a quick delete and create event.  
Some final testing was needed to make sure monitoring, saving, and querying worked together.
