package realzork;

/**
 * This class contains messages that the game
 * can display to the player.
 * 'welcome' - is printed at the start.
 * 'quit' - when the player quits before winning
 * 'win' - when the player wins the game
 * 'gotItem' - when the player acquires a new Item. Can vary heavily.
 * 'door' - when a door locks or unlocks.
 * 'status' - when a character status (e.g. blind, hurt, etc.) changes
 * @author Benjamin.Korobkin
 */
public class Messages {
    // The message printed at the start
    public String welcome, quit, win, gotItem, doorStatus, nullPath, 
            charStatus, blindLock, badInput, help, unlockDoor, goblin, gotTorch;
    
 
    public Messages() {
        welcome = ("You are on a quest to find"
                + " and retrieve the scroll of truth!\nTo do so, you must"
                + " traverse the dangerous dungeon.\nYou enter, and the wall"
                + " behind you crumbles\nand covers the entrance, trapping you "
                + "in complete darkness!\nYou must find the scroll and escape!"
                + "\n[Type 'help' to see commands]\n");
        help = "You must retrieve the scroll and escape!\n" 
                + "Your commands are: ";
        //quit = "LOL! git gud noob.";
        
        gotTorch = "The ember of light brightens your way!\n"
                    + "You can see clearly now!";
        
        blindLock = ("You blindly attempt to go north, but"
                            + " the way\nis blocked by a door. You grasp"
                            + "\nfor the handle and fidget with it"
                            + "\nuntil you realize it's locked."
                            + "\nGuess you're not going that way.");
        
        badInput = "I don't understand. Try using one of your command words.";
        
        unlockDoor = "You unlock the door. As you enter, the door slams shut"
                + " behind you.\nYou try to pry it open, but it won't budge.\n"
                + "You attempt to use the key, but it drops to the floor"
                + " and shatters.\nThere's no going back now...";
        
        goblin =  "\nOut of nowhere, a small creature leaps out of the shadows"
                + "\nand lunges itself towards you, its decrepit claws reaching"
                + "\nout to strike!\n\"GOLD! MY GOLD!\", it shrieks."
                + "\nYou instinctively lift up the shield to defend yourself."
                + "\nThe creature's claws swipe and bang hard on the wood,"
                + "\nknocking itself back.\nThe creature howls in pain."
                + "\n\"NYAAAAAHHHHH!!! You come and take my STUFF!!!\" it shouts."
                + "\nThe creature appers to be some kind of goblin."
                + "\nYou notice a rolled up parchment in its raised fist."
                + "\nIs that...?\n\"Ah, you like this?\" it snarls."
                + "\n\"You want it? Go get my gold back!\""
                + "\nIt points to the western wall. At the top,"
                + "\nyou notice a small opening, barely enough to crawl through."
                + "\nThe goblin looks threatening though. Should probably go"
                + "\neven just to get away. You hoist yourself up and "
                + "\nsqueeze through the gap."
                + "\nJust hope that the filthy goblin keeps his promise.";
        
        win = "\nSuddenly, the goblin appears!"
                + "\n\"NYAAAA!!! MY GOLD! NOW GET OUT!!!\""
                + "\nIt grabs the coins from you, throws the scroll at you,"
                + "\nand runs away. Surprisingly, he actually kept his"
                + "\npromise! You pick it up and unfurl it, anxious to see its"
                + "\ncontents. There's a strange inscription on it with mysterious"
                + "\nsymbols. They slowly start to light up, and everything in the"
                + "\nroom flashes with a bright glow! You look around and find yourself"
                + "\noutside the dungeon's entrance."
                + "\n...what just happened?"
                + "\n\n[TO BE CONTINUED]";
    }
    
    public void help1() {
        help = "You must retrieve the coins from the sanctum!\n"
                + "Your commands are:";
    }
}
