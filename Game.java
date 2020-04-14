package realzork;

import java.io.PrintStream;
import java.util.*;

class Game {
    PrintStream out = System.out;
    Scanner in = new Scanner(System.in);
    //private final Parser parser;
    private int coinGoal, coins = 0;
    private Room plyrRoom, prevRoom, monstRoom;
    private Room entrance, torchRoom, keyRoom, west0, boulderRoom, firstG,
            north0, east0, creeperRoom, shieldRoom;
    private Room sanct0, sanct1, sanct2, sanct3, sanct4, sanct5, sanct6, 
            sanct7, sanct8;
    private Item torch, key, gold, shield; //satchel
    private final Character PLAYER;
    private final Messages msg;
    private Character monster;
    boolean moved = false;
    boolean won = false;
    List<Room> sanctRooms = new ArrayList<>();
    //Random number generator for coins in rooms (monster later)
    Random rng = new Random();

    /**
     * Create the game and its internal map.
     */
    public Game() {
        createRooms();
        coinGoal = 4;
        createItems();
        disperseGold(coinGoal); // scatter coins around sanctum
        PLAYER = new Character(entrance); // start PLAYER at entrance
        plyrRoom = PLAYER.room;
        prevRoom = PLAYER.prevRoom;
        //parser = new Parser();
        createMonster();
        msg = new Messages();
        
      //For debugging, we grant 
      //PLAYER.addItem(torch);
      //boulderRoom.unlock();
      //creeperRoom.unlock();
      //plyrRoom = creeperRoom;
        
    }

    /**
     * Create all the rooms with items and link their exits together.
     */
    private void createRooms() {
        // create the rooms with standard descriptions. These are constant
        // Iff having one parameter causes errors, change the constructor
        // in the Room class.
        entrance = new Room("the entrance to the dungeon.", "Entrance");
        torchRoom = new Room("the southwestern corner of the dungeon.", "Torch Room");
        keyRoom = new Room("the most eastern part you can go.", "Key Room");
        west0 = new Room("a hallway scattered with droppings.", "West0");
        boulderRoom = new Room("a cold, damp area.", true, "Boulder Room");
        firstG = new Room("a small corner space.", "First Gold");
        north0 = new Room("a room strewn with cobwebs.", "North0");
        east0 = new Room("a room with puddles of water all around.", "East0");
        creeperRoom = new Room("an open space with a strange aura.", true, "Creeper Room");
        shieldRoom = new Room("an empty room save for some hooks on the wall.", "Shield Room");
        sanct0 = new Room("the entrance to the sanctum.", true, "Sanct0");
        sanct1 = new Room("Sanct1");
        sanct2 = new Room("Sanct2");
        sanct3 = new Room("Sanct3");
        sanct4 = new Room("Sanct4");
        sanct5 = new Room("Sanct5");
        sanct6 = new Room("Sanct6");
        sanct7 = new Room("Sanct7");
        sanct8 = new Room("Sanct8");  
        // Initialise room exits
        entrance.setExits(boulderRoom, keyRoom, null, west0);
        west0.setExits(null, entrance, torchRoom, null);
        torchRoom.setExits(west0, null, null, null);
        keyRoom.setExits(null, null, null, entrance);
        boulderRoom.setExits(north0, east0, null, creeperRoom);
        north0.setExits(null, firstG, boulderRoom, null);
        east0.setExits(firstG, null, null, boulderRoom);
        firstG.setExits(null, null, east0, north0);
        creeperRoom.setExits(shieldRoom, boulderRoom, null, null);
        shieldRoom.setExits(null, null, creeperRoom, null);
        sanct0.setExits(sanct1, null, sanct2, sanct3);
        sanct1.setExits(null, null, sanct0, sanct4);
        sanct2.setExits(sanct0, null, null, sanct5);
        sanct3.setExits(sanct4, sanct0, sanct5, sanct7);
        sanct4.setExits(null, sanct1, sanct3, sanct6);
        sanct5.setExits(sanct3, sanct2, null, sanct8);
        sanct6.setExits(null, sanct4, sanct7, null);
        sanct7.setExits(sanct6, sanct3, sanct8, null);
        sanct8.setExits(sanct7, sanct5, null, null);
        
        // Initialize specific descriptions for room. These can vary.
        entrance.setSpecDesc("It's cold and dark in the dungeon.\n"
                + "You can barely see your hand in front of your face.");
        west0.setSpecDesc("Though still blind, you can hear rats padding"
                + "\naround the stone floor.");
        torchRoom.setSpecDesc("Amidst the dark, you notice a small burning "
                + "torch in the corner.");
        keyRoom.setSpecDesc("You shuffle east in the dark till you reach\n"
                + "a dead end. May as well head back.\n[Type 'back' to go "
                + "to the previous room]");
        firstG.setSpecDesc("Wait, is that gold on the floor?");
        boulderRoom.setSpecDesc("A large stack of boulders blocks the "
                + "way westward.\n"
                + "Although, you can barely make out a figure on "
                + "the other side.\nIs someone there?");
        creeperRoom.setSpecDesc("The figure from before is nowhere to be seen.");
        shieldRoom.setSpecDesc("A wooden shield hangs on one of the hooks.");
        sanct0.setSpecDesc("You need to find the coins.");
        //Fill the sanctum arraylist with rooms
        sanctRooms.add(0, sanct0);
        sanctRooms.add(1, sanct1);
        sanctRooms.add(2, sanct2);
        sanctRooms.add(3, sanct3);
        sanctRooms.add(4, sanct4);
        sanctRooms.add(5, sanct5);
        sanctRooms.add(6, sanct6);
        sanctRooms.add(7, sanct7);
        sanctRooms.add(8, sanct8);
        
    }
    
    private void createItems() {
        torch = new Item("torch", "Its bright light shines up the room.");
        key = new Item("key", "Looks pretty rusted. Hopefully it still works.");
        gold = new Item("gold", "Ooooohhh, shiny!!!");
        shield = new Item("shield", "It hasn't aged very well, but looks strong"
                + "\nenough to provide a bit of protection.");
        // Put items in rooms
        torchRoom.addItem(torch);
        keyRoom.addItem(key);
        firstG.addItem(gold);
        shieldRoom.addItem(shield);
    }
    
    private void createMonster() {
        monster = new Character(sanct0); // at start, place monster at entrance.
        monstRoom = monster.room;
        moveMonster(); // move monster inside random sanctRoom (not sanct0-3)
        // We'll have to experiment to see if we want this at the beginning
        // or end of the room description. For now, at the end.
    }
    
    // TODO: rework the system so only rooms without coins will get a new one
    private void disperseGold(int coinsMissing) {
        //Add gold to random rooms (3-8). Be sure the rooms are unique via set.
        //Set<Integer> randIntSet = new HashSet<>();
        // make sure we get three unique ints...
        while (coinsMissing > 0) 
        {   // keep in range [1-8]. no coin at sanct0
            int randInt = rng.nextInt(8) + 1; 
            Room r = sanctRooms.get(randInt);
            if (!r.hasItem(gold)){
                r.addItem(gold);
                coinsMissing--;
            }
        }
        // Now, no sanctum room has more than 1 coin.
        // Change description of rooms with gold.
        for(Room r: sanctRooms) {
          if(r.hasItem(gold)) 
              r.setSpecDesc("You see a piece of gold on the floor.");
        } 
      /*  // ... and place the gold in the rooms corresponding to the randInts
        for (Integer i : randIntSet) 
        {
            Room r = sanctRooms.get(i);
            r.addItem(gold);
            out.println(r.toString());
        } 
        */
        
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
            //Command command = parser.getCommand();
            out.print("> ");
            String fullCommand = in.nextLine();
            fullCommand = fullCommand.toLowerCase();
            String[] commands = fullCommand.split(" ");
            finished = processCommand(commands);
        }
        //out.println("GG");
    }

     /**
     * Print out the opening message for the PLAYER.
     * /
    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(String[] commands)
    {
        if(commands.length > 2) {
            out.println("Commands may only be 1 or 2 words.");
            return false;
        }
       {
           String commandWord = commands[0];
            switch (commandWord) {
                case "help":
                    printHelp();
                    break;
                case "go":
                    if (commands.length > 1)
                    return goDir(commands[1]);
                    else out.println("Go where?");
                    break;
                case "north":
                    return goDir("north");
                    //break;
                case "east":
                   return goDir("east");
                    //break;
                case "south":
                   return goDir("south");
                   // break;
                case "west":
                   return goDir("west");
                   // break;
                case "coins":
                    out.println("Gold coins: " + coins + "/" + coinGoal);
                    break;
                case "look":
                    look(commands);
                    break;
                case "take":
                    if (take(commands)) return true;
                    break;
                case "gimmetorch":
                    PLAYER.addItem(torch);
                    gotTorch();
                    break;
                case "back":
                    back(prevRoom);
                    break;
                case "quit":
                    if(commands.length > 1)
                        out.println("If you want to quit, just type 'quit'.");
                    else
                        return true;  // signal that we want to quit
                    break;
                default:
                    out.println(msg.badInput);
                    return false;
                    /* case "drop":
                    //drop(command);
                    break;
                case "inv":
                    //inv();
                    break; */
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
        //parser.showCommands();
        out.println(plyrRoom.exitString());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private boolean goDir(String direction)
    {
        Room nextRoom = plyrRoom.nextRoom(direction);
            if (direction == null) out.println("Go where?");
            else if (direction.equals("back")) back(prevRoom);
            else if (nextRoom == null)
                out.println("You can only go to your exits.");
            else {
                if (nextRoom.locked && !PLAYER.hasItem(torch)) {
                    out.println(msg.blindLock);
                }
                else if (nextRoom.locked) 
                    out.println("You attempt to go " + direction + 
                            " but the way is blocked.");
                else {
                    prevRoom = plyrRoom;
                    plyrRoom = nextRoom;
                    if (plyrRoom.equals(boulderRoom) && 
                            prevRoom.equals(entrance))
                    {
                        out.println(msg.unlockDoor);
                        PLAYER.removeItem(key);
                    }
                    
                    if (plyrRoom.equals(monstRoom)) return faceMonster();
                    checkTorch();
                    checkMonster();
                }   
            }
            return false;
    }
    
    private void back(Room previous) {
        if (prevRoom == null) {
        out.println("There's no going back now...");
        }
        else if (currentRoom.equals(boulderRoom) && prevRoom.equals(entrance)) {
            out.println("Sorry, you can't go back!");
        }
        else {
            out.print("You head back to the previous room.\n");
            prevRoom = plyrRoom;
            plyrRoom = previous;
            if (!PLAYER.hasItem(torch) && !plyrRoom.equals(torchRoom)) {
                out.println(plyrRoom.blindDesc());
            }
            else {
                out.println(plyrRoom.description());
            }
        }
    }

    //private void look(Command command) {
    private void look (String[] command) {
        if((command.length < 2) || command[1].equals("room")) {
            // just "look" or "look room"? So try to look at the room.
            if (!PLAYER.hasItem(torch) && !plyrRoom.equals(torchRoom)) {
               out.println(plyrRoom.blindLook());
               return;
            }
            else { 
            out.println(plyrRoom.description());
            return;
            }
        }
        if (!PLAYER.hasItem(torch) && !plyrRoom.equals(torchRoom)) {
            out.println(plyrRoom.blindLook());
            //out.println("How can you look at something you can't see?");
        }
        else {
            String item = command[1];
            // If the item is in the room, return its description.
            Item roomItem = plyrRoom.roomItem(item);
            Item plyrItem = PLAYER.plyrItem(item);
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
                out.println("Trust me, you don't need to look at that."); 
                       // + command[1] + "..."); 
            }
        }
    }

    private boolean take(String[] command) {
        if(command.length < 2) {
            // if there is no second word, we don't know what to take...
            out.println("Take what?");
            return false;
        }
        // if there's no torch around, you can't take anything.
        if (!PLAYER.hasItem(torch) && !plyrRoom.equals(torchRoom)) {
            out.println("How do you take something you can't see?");
        }
        // otherwise, attempt to take the specified item.
        else {
            // If the item is in the room, try to add it to your inventory.
            boolean exists = false; Item item = null;
            for (Item i : plyrRoom.items) {
                if (i.getName().equals(command[1])) 
                    {exists = true; item = i;}
            }
            if (exists) {
                plyrRoom.removeItem(item);
                PLAYER.addItem(item);
                // if you get the torch, room descriptions change
                if(item.getName().equals("torch")) gotTorch();
                // if you get a key, a door unlocks
                if(item.getName().equals("key")) gotKey();
                if(item.getName().equals("gold")) return gotGold();
                if(item.getName().equals("shield")) gotShield();
            }
            else{out.println("Leave the " + command[1] + " alone.");}
        }
        return false;
    }
    
    private void gotTorch() {
       //hasItem(torch);
        out.println(msg.gotTorch);
        torchRoom.setSpecDesc("With all the light, it's even more"
                + " clearly a dead end.\n"
                + "[Type 'back' to return to the previous room]");
        keyRoom.setSpecDesc("You notice a small copper key on the floor.");
        west0.setSpecDesc("A small family of rats are huddled in the wall,\n"
                + "disturbed by the light from your torch.");
        entrance.setSpecDesc("You can see the door blocking the north exit.");
    }

    private void gotKey() { 
        boulderRoom.unlock(); 
        keyRoom.setSpecDesc("Nothing left here. It's just empty space."
                + "\nYou can chill here for a while if you want.");
    }
    
    private void checkTorch() {
        if (PLAYER.hasItem(torch) || plyrRoom.equals(torchRoom)) {
            out.println(plyrRoom.description());
        }
        else {
            out.println(plyrRoom.blindDesc());
        }    
    }  
    
    private boolean gotGold() {
        coins++;
        if (plyrRoom.equals(firstG)) {
            out.println("You hear something crash in another room.\n"
                    + "What was that?");
            firstG.setSpecDesc("Should probably investigate what that "
                + "noise was.");
            creeperRoom.unlock();
            boulderRoom.setSpecDesc("The stack of boulders has "
                + "mysteriously disappeared.");
        }
        else {
            if (coins >= coinGoal)
            {
                out.println(msg.win);
                return true;
            }
            out.println("You need " + (coinGoal - coins) + " more.");
            plyrRoom.setSpecDesc("Nothing interesting here.");
        }
        return false;
    }

    private void gotShield() {
        //sanct0.unlock();
        out.println(msg.goblin(coinGoal));
        plyrRoom = sanct0;
        prevRoom = null;
        msg.help1();
        sanct0.unlock();
        out.println(plyrRoom.description());
    }

    private boolean faceMonster() {
        out.println(msg.monsterEncounter);
        boolean blocked = false;
        if (PLAYER.hasItem(shield))
        {
           int blockChance = rng.nextInt(50); 
           if (blockChance >= 25) blocked = true;
           
           if (blocked) {
            out.println(msg.shieldBlock);
            moveMonster();
            return false;
            }
            else {
                out.println(msg.shieldMiss);
                if (coins > 0)
                {
                    out.println(msg.missWithCoin);
                    disperseGold(coins);
                    coins = 0;
                    moveMonster();
                    return false;
                }
                else {
                    out.println(msg.missWithoutCoin);
                    return true;
                }
            }
        }
        else {
            out.println(msg.noDefense);
            return true;
        }
    }
    
    public void moveMonster() {
        //TODO: change the spec desc of the adjacent rooms to remove the
        // monster thumping. (in progress)
        // place monster in a different room that is not the current room
        // and also not adjacent.
        Set<Room> exitRooms = monstRoom.exitRooms(); 
        // at game start, exit rooms are for sanct0
        Room randRoom = entrance; //placeholder
        do {
            int randInt = rng.nextInt(9);
            randRoom = sanctRooms.get(randInt);
        } while (exitRooms.contains(randRoom) || monstRoom.equals(randRoom));
        monstRoom = randRoom; 
    }

    private void checkMonster() {
        Set<Room> exitRooms = plyrRoom.exitRooms();
        if (exitRooms.contains(monstRoom))
        {
            out.println(msg.monsterNear);
        }
    }
}
   /* private void inv() {
        int itemAmt = PLAYER.items.size();
        
        String inv = "Inventory: ";
        for(Item i : PLAYER.items)
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
        out.println("Storage used: " + PLAYER.itemsWeight()+"/"+PLAYER.storage);
    } */

/*    private void gotSatchel() {
        PLAYER.storage += 5;
        satchelRoom.setSpecDesc("Looks like a great place to hang a satchel!\n"
                + "Wait...");
        out.println("Your storage capacity has been increased by 5!");
    } */

/* private void drop(Command command) {
        if(!command.hasSecondWord()) {
            out.println("Drop what?");
        }
        else
        {
            boolean exists = false; Item item = null;
            for (Item i : PLAYER.items) {
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
                PLAYER.removeItem(item);
                plyrRoom.addItem(item);
            }
            out.println();         
        }
    } */

/* private void goRoom(Command command) 
    {        
        if(!command.hasSecondWord())
        {
            // if there is no second word, we don't know where to go...
            out.println("Go where?");
            //return;
        }
        else if (command.getSecondWord().equals("back")) {
            back(prevRoom);
        }
        else {
           String direction = command.getSecondWord();
            // Try to leave current room.
            Room nextRoom = plyrRoom.nextRoom(direction);
            if (nextRoom == null)
                out.println("You can't go that way!");
            else {
                if (nextRoom.locked && !PLAYER.hasItem(torch)) {
                    out.println(msg.blindLock);
                }
                else if (nextRoom.locked) 
                    out.println("You attempt to go " + direction + 
                            " but the way is blocked.");
                else {
                    prevRoom = plyrRoom;
                    plyrRoom = nextRoom;
                    if (plyrRoom.equals(boulderRoom) && 
                            prevRoom.equals(entrance))
                    {
                        out.println(msg.unlockDoor);
                        PLAYER.removeItem(key);
                    }
                    //checkTorch();
                    if (sanctRooms.contains(plyrRoom)) checkMonster();
                    else checkTorch();
                }   
            }
        }
    }*/
