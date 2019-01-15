package realzork;

/**
 * This class contains all the possible messages that the game
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
    public String welcome, quit, win, gotItem, 
                    doorStatus, nullPath, charStatus, blindLock;
    
 
    public Messages() {
        welcome = ("You are on a quest to find"
                + " and retrieve the scroll of truth!\nTo do so, you must"
                + " traverse the dangerous dungeon.\nYou enter, and the wall"
                + " behind you crumbles\nand covers the entrance, trapping you "
                + "in complete darkness!\nYou must find the scroll and escape!"
                + "\n[Type 'help' to see commands]\n");
        
        quit = "LOL! git gud noob.";
        win = "Congratulations! You Win!!!";
        blindLock = ("You blindly attempt to go north, but"
                            + " the way\nis blocked by a door. You grasp"
                            + "\nfor the handle and fidget with it"
                            + "\nuntil you realize it's locked."
                            + "\nGuess you're not going that way.");
    }
}
