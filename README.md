# Dungeon-Adventure
A text-based dungeon adventure game, currently in development.

Commands are entered as a sequence of 1-2 words.

Commands: go, look, take, back, help, quit

Grammar:     
[] - denotes mandatory field  
() - denotes optional field  
| - denotes binary option  
"" - denotes literal text  

go "back"|[direction]- Takes you to another room, if available. For example, to go east, simply type "go east".
  -Alternatively, type the direction only
look ("room"|[item]) - gives description of the current room or an item in the room, if description is available.  
take [item] - Adds item to inventory, if available in current room.  
back - Takes you to previous room. 
  -Alternatively, "go back"
help - Provides you with objective, command words, and current exits.  
quit - ends the game  
