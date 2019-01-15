package realzork;

import java.io.PrintStream;
import java.util.*;

class Game {
    PrintStream out = System.out;
    private Parser parser;
    private CommandWords commands;
    private Command quit;
    private Room currentRoom, prevRoom;
    private Room entrance, torchRoom, keyRoom, west1, sanctum;
    private Item torch, key;
    private final Character player;
    private final Messages msg;
    boolean moved = false;
    boolean won = false;

    /**
     * Create the game and its internal map.
     */
    public Game() {
        createRooms();
        parser = new Parser();
        commands = new CommandWords();
        player = new Character(entrance);
        msg = new Messages();
        quit = new Command("quit",null,"");
    }

    /**
     * Create all the rooms with items and link their exits together.
     */
    private void createRooms() {
        // create the rooms with standard descriptions
        entrance = new Room("the entrance to the dungeon.", false);
        torchRoom = new Room("the southwestern corner of the dungeon.", false);
        keyRoom = new Room("the most eastern part you can go.", false);
        west1 = new Room("a thin corridor scattered with droppings.", false);
        sanctum = new Room("the inner sanctum. Heaven only knows "
                + "what lies ahead...", true);
        // initialise room exits
        entrance.setExits(sanctum, keyRoom, null, west1);
        west1.setExits(null, entrance, torchRoom, null);
        torchRoom.setExits(west1, null, null, null);
        keyRoom.setExits(null, null, null, entrance);
        sanctum.setExits(null, null, null, null);
        
        // initialize specific descriptions for room.
        entrance.setSpecDesc("It's cold and dark in the dungeon.\n"
                + "You can barely see your hand in front of your face.");
        west1.setSpecDesc("Though still blind, you can hear rats padding"
                + "\naround the stone floor.");
        torchRoom.setSpecDesc("Amidst the dark, you notice a small burning "
                + "torch in the corner");
        keyRoom.setSpecDesc("You shuffle east in the dark till you reach\n"
                + "a dead end. May as well head back.\n[Type 'back' to go "
                + "to the previous room]");
        sanctum.setSpecDesc("");
        
        torch = new Item("torch", 2, "Its bright light shines up the room.");
        key = new Item("key", 1, "It looks pretty rusted, hopefully it works.");
        
        torchRoom.setItems(torch);
        keyRoom.setItems(key);
        
        currentRoom = entrance;  // start game at entrance
        prevRoom = null;
    }
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() {            
        out.println(msg.welcome + entrance.exitString());
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        if (!won) {
        out.println(msg.quit);
        }
        else out.println(msg.win);
    }

    /**
     * private void printWelcome()
     * {
     *       out.println(entrance.exitString());
     *  }

    /**
     * Print out the opening message for the player.
     * /

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        if(command.isUnknown()) {
            out.println("try doing something you can actually do!");
            return false;
        } else {
            String commandWord = command.getCommandWord();
            switch (commandWord) {
                case "help":
                    printHelp();
                    break;
                case "go":
                    goRoom(command);
                    break;
                case "look":
                    look(command);
                    break;
                case "take":
                    take(command);
                    break;
                case "back":
                    back(prevRoom);
                    break;
                case "quit":
                    if(command.hasSecondWord())
                        out.println("Quit what?");
                    else
                        return true;  // signal that we want to quit
                    break;
                default:
                    break;
            }
        }
        return false;
    }
    // implementations of user commands:
    /**
     * Print out some help information.
     * Here we print some message and a list of the 
     * command words.
     */
    private void printHelp() {
        out.println("You must retrieve the scroll and escape!");
        out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord())
        {
            // if there is no second word, we don't know where to go...
            out.println("Go where?");
            return;
        }
        else if (command.getSecondWord().equals("back")) {
            back(prevRoom);
        }
        else {
            String direction = command.getSecondWord();
            // Try to leave current room.
            Room nextRoom = currentRoom.nextRoom(direction);
        
            if (nextRoom == null)
                out.println("You can't go that way!");
            else {
                if (nextRoom.locked && !player.hasItem(torch)) {
                    out.println(msg.blindLock);
                }
                else if (nextRoom.locked) 
                    out.println("You attempt to go " + direction + 
                            " but the door is locked.");
                else {
                    prevRoom = currentRoom;
                    currentRoom = nextRoom;
                    checkTorch();
                    if (currentRoom.equals(sanctum)) {
                        won = true;
                        out.println("The current version of the game ends here."
                                + " Type 'quit' to exit.");
                        
                    }
                }   
            }
        }
    }
    
    private void back(Room previous) {
        if (prevRoom == null) {
        out.println("And how do you propose to do that?");
        }
        else {
            out.print("You head back to the previous room.\n");
            prevRoom = currentRoom;
            currentRoom = previous;
            if (!player.hasItem(torch) && !currentRoom.equals(torchRoom)) {
                out.println(currentRoom.blindDesc());
            }
            else {
                out.println(currentRoom.description());
            }
        }
    }

    private void look(Command command) {
        if(!command.hasSecondWord()) {
            // just "look"? So look at the room.
            if (!player.hasItem(torch) && !currentRoom.equals(torchRoom)) {
               out.println(currentRoom.blindLook());
            return; 
            }
            out.println(currentRoom.description());
            return;
        }
        if (!player.hasItem(torch) && !currentRoom.equals(torchRoom)) {
            out.println("How can you look at something you can't see?");
        }
        else {
            String item = command.getSecondWord();
            // If the item is in the room, return its description.
            Item roomItem = currentRoom.roomItem(item);
            Item playerItem = player.playerItem(item);
            boolean found = (roomItem != null || playerItem != null);
            if (found) {
                if (playerItem == null) {
                    out.println(roomItem.getDescription());
                }
                else {
                    out.println(playerItem.getDescription());
                }
            }
            else {
                out.println("Stop trying to look at the " + command.getSecondWord()
                + "!");
            }
        }
    }

    private void take(Command command) {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            out.println("Take what?");
            return;
        }
        // if there's no torch around, you can't take anything.
        if (!player.hasItem(torch) && !currentRoom.equals(torchRoom)) {
            out.println("How do you take something you can't see?");
        }
        // otherwise, attempt to take the specified item.
        else {
            ArrayList<Item> roomItems = currentRoom.items;
            // If the item is in the room, add it to your inventory.
            boolean exists = false; Item item = null;
            for (Item i : roomItems) {
                if (i.getName().equals(command.getSecondWord())) {
                    exists = true; item = i;
                }
            }
            if (exists) {
                player.addItem(item);
                currentRoom.removeItem(item);
                // if you get the torch, room descriptions change
                if(item.getName().equals("torch")) gotTorch();
                // if you get a key, a door unlocks
                if(item.getName().equals("key")) gotKey();
            }
            else {
                out.println("You can't take that!");
            }
        }
    }

    private void gotTorch() {
       //hasItem(torch);
        torchRoom.setSpecDesc("With all the light, it's even more"
                + " clearly a dead end");
        keyRoom.setSpecDesc("You notice a small copper key on the floor.");
        west1.setSpecDesc("A small family of rats are huddled in the wall,\n"
                + "disturbed by the light from your torch.");
        entrance.setSpecDesc("You can see the door blocking the north exit.");
    }

    private void gotKey() {
        sanctum.unlock();
    }
    
    private void checkTorch() {
        if (player.hasItem(torch) || currentRoom.equals(torchRoom)) {
            out.println(currentRoom.description());
        }
        else {
            out.println(currentRoom.blindDesc());
        }    
    }  
}