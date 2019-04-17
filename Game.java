package realzork;

import java.io.PrintStream;
import java.util.*;

class Game {
    PrintStream out = System.out;
    private final Parser parser;
    //private final CommandWords commands;
    //private Command quit;
    private Room currentRoom, prevRoom;
    private Room entrance, torchRoom, keyRoom, west1, firstG, satchelRoom;
    private Room sanctum;
    private Item torch, key, satchel, gold;
    private final Character player; // TODO: goblin, monster;
    private final Messages msg;
    boolean moved = false;
    boolean won = false;

    /**
     * Create the game and its internal map.
     */
    public Game() {
        createRooms();
        parser = new Parser();
        //commands = new CommandWords();
        player = new Character(entrance, 3);
        msg = new Messages();
        //quit = new Command("quit", null, "");
    }

    /**
     * Create all the rooms with items and link their exits together.
     */
    private void createRooms() {
        // create the rooms with standard descriptions. These are constant
        entrance = new Room("the entrance to the dungeon.", false);
        torchRoom = new Room("the southwestern corner of the dungeon.", false);
        keyRoom = new Room("the most eastern part you can go.", false);
        west1 = new Room("a hallway scattered with droppings.", false);
        firstG = new Room("a thin corridor.", true);
        satchelRoom = new Room("an empty space, save for a row of hooks on the"
                + " wall.", false);
        sanctum = new Room("The inner sanctum.", true);
        // initialise room exits
        entrance.setExits(firstG, keyRoom, null, west1);
        west1.setExits(null, entrance, torchRoom, null);
        torchRoom.setExits(west1, null, null, null);
        keyRoom.setExits(null, null, null, entrance);
        firstG.setExits(null, satchelRoom, null, null);
        satchelRoom.setExits(null, null, null, firstG);
        
        // initialize specific descriptions for room. These can vary.
        entrance.setSpecDesc("It's cold and dark in the dungeon.\n"
                + "You can barely see your hand in front of your face.");
        west1.setSpecDesc("Though still blind, you can hear rats padding"
                + "\naround the stone floor.");
        torchRoom.setSpecDesc("Amidst the dark, you notice a small burning "
                + "torch in the corner.");
        keyRoom.setSpecDesc("You shuffle east in the dark till you reach\n"
                + "a dead end. May as well head back.\n[Type 'back' to go "
                + "to the previous room]");
        firstG.setSpecDesc("Wait, is that gold on the floor?");
        satchelRoom.setSpecDesc("A satchel hangs on one of the hooks.");
        
// Create items
        torch = new Item("torch", 2, "Its bright light shines up the room.");
        key = new Item("key", 1, "Looks pretty rusted. Hopefully it still works.");
        satchel = new Item("satchel", 0, "A brown, leather bag with a shoulder "
                + "strap for easy portability.");
        gold = new Item("gold", 1, "Ooooohhh, shiny!!!");
        // Put items in rooms
        torchRoom.setItems(torch);
        keyRoom.setItems(key);
        satchelRoom.setItems(satchel);
        firstG.setItems(gold);
        // start game at entrance
        currentRoom = entrance;  
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
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        if (!won) {
        out.println(msg.quit);
        }
        else out.println(msg.win);
    }

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
            out.println(msg.badInput);
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
                case "drop":
                    drop(command);
                    break;
                case "inv":
                    inv();
                    break;
                case "quit":
                    if(command.hasSecondWord())
                        out.println("If you want to quit, just type 'quit'.");
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
    private void printHelp() 
    {
        out.println(msg.help);
        parser.showCommands();
        out.println(currentRoom.exitString());
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
        if(!command.hasSecondWord() || command.getSecondWord().equals("room")) {
            // just "look" or "look room"? So look at the room.
            if (!player.hasItem(torch) && !currentRoom.equals(torchRoom)) {
               out.println(currentRoom.blindLook());
               return;
            }
            else { 
            out.println(currentRoom.description());
            return;
            }
        }
        if (!player.hasItem(torch) && !currentRoom.equals(torchRoom)) {
            out.println("How can you look at something you can't see?");
        }
        else {
            String item = command.getSecondWord();
            // If the item is in the room, return its description.
            Item roomItem = currentRoom.roomItem(item);
            Item plyrItem = player.plyrItem(item);
            boolean found = (roomItem != null || plyrItem != null);
            if (found) {
                if (plyrItem == null) {
                    out.println(roomItem.getDescription());
                }
                else {
                    out.println(plyrItem.getDescription());
                }
            } 
            else {
                out.println("Please stop trying to look at the " 
                        + command.getSecondWord()+ "..."); 
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
            // If the item is in the room, try to add it to your inventory.
            boolean exists = false; Item item = null;
            for (Item i : currentRoom.items) {
                if (i.getName().equals(command.getSecondWord())) 
                    {exists = true; item = i;}
            }
            if (exists) {
                // Check if you can carry the item before picking it up.
                if (player.itemsWeight() + item.getWeight() <= player.storage)
                {
                    currentRoom.removeItem(item);
                    player.addItem(item);
                    // if you get the torch, room descriptions change
                    if(item.getName().equals("torch")) gotTorch();
                    // if you get a key, a door unlocks
                    if(item.getName().equals("key")) gotKey1();
                    // if you get the satchel, your storage expands.
                    if(item.getName().equals("satchel")) {
                        gotSatchel(); player.removeItem(item);
                    }   // Remove the satchel because it's not an inv item.
                        // It's just adding extra storage capacity.
                    if(item.getName().equals("gold")) {
                        gotGold();
                        if (currentRoom.equals(firstG))
                        {
                            player.removeItem(item);
                            // If it's the first gold, it's not really gold.
                        }
                    }
                     
                }
                else {
                    out.println("You don't have enough space to take that!\n"
                             + "[type 'inv' to see storage capacity]");
                }
            }
            else {
                out.println("You can't take that!");
            }
        }
    }

    private void drop(Command command) {
        if(!command.hasSecondWord()) {
            out.println("Drop what?");
        }
        else
        {
            boolean exists = false; Item item = null;
            for (Item i : player.items) {
                if (i.getName().equals(command.getSecondWord())) 
                    {exists = true; item = i;}
            }
            // Can't drop something that you don't have
            if (!exists) {
                out.print("Sorry, you can't drop the " + 
                    command.getSecondWord() + ".");
                if (command.getSecondWord().equals("beat") || 
                    command.getSecondWord().equals("bass")) 
                    out.print(" uhn tiss, uhn tiss, uhn tiss...");
            }
            // Can't drop the torch or satchel
            else if (command.getSecondWord().equals("torch") ||
                    command.getSecondWord().equals("satchel") ||
                    command.getSecondWord().equals("key"))
                out.print("You'll need that...");
            // Drop it
            else {
                //String itemName = item.getName();
                player.removeItem(item);
                currentRoom.addItem(item);
            }
            out.println();         
        }
        
        
    }
    
    private void gotTorch() {
       //hasItem(torch);
        torchRoom.setSpecDesc("With all the light, it's even more"
                + " clearly a dead end.");
        keyRoom.setSpecDesc("You notice a small copper key on the floor.");
        west1.setSpecDesc("A small family of rats are huddled in the wall,\n"
                + "disturbed by the light from your torch.");
        entrance.setSpecDesc("You can see the door blocking the north exit.");
        
    }

    private void gotKey1() { 
        firstG.unlock(); 
        keyRoom.setSpecDesc("Nothing left here. It's just empty space. "
                + "\nYou can chill here for a while if you want.");
    }
    
    private void checkTorch() {
        if (player.hasItem(torch) || currentRoom.equals(torchRoom)) {
            out.println(currentRoom.description());
        }
        else {
            out.println(currentRoom.blindDesc());
        }    
    }  

    private void inv() {
        int itemAmt = player.items.size();
        
        String inv = "Inventory: ";
        for(Item i : player.items)
        {
            inv += i.getName() + "(" + i.getWeight() + "), ";
        }
        if (itemAmt == 0)
        {
            out.println("Inventory:[empty]");
        }
        else
        {
            out.println(inv.substring(0, inv.length()-2));
        }
        out.println("Storage used: " + player.itemsWeight()+"/"+player.storage);
    }

    private void gotSatchel() {
        player.storage += 5;
        satchelRoom.setSpecDesc("Looks like a great place to hang a satchel!\n"
                + "Wait...");
        out.println("Your storage capacity has been increased by 5!");
    }

    private void gotGold() {
        if (currentRoom.equals(firstG))
        {
            out.println("Your hands reach for the gold but it vanishes...\n"
                    + "It was just an illusion!\n"
                    + "You hear stones crash to the gound in another room.\n"
                    + "What was that?");
        }
        firstG.setSpecDesc("Should probably investigate what that"
                + "noise was.");
    }
}