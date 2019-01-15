package realzork;

import java.util.*;

/**
 *
 * @author Benjamin.Korobkin
 */
public class Character {
    // may become a HashMap, we'll see.
    public ArrayList<Item> items;
    public Room room;
    public int storage;
    
    public Character(Room r) {
        items = new ArrayList<Item>();
        room = r;
       
    }
    
    public void addItem(Item i) {
        items.add(i);
        System.out.println("The " + i.getName() + " was added to your inventory.");
        
        if (i.getName().equals("torch")) {
            System.out.println("The ember of light brightens your way!\n"
                    + "You can see clearly now!");
        }
    }
    
    public boolean hasItem(Item i) {
        return items.contains(i);
    }
    
    public Item playerItem(String item) {
        ArrayList<Item> playerItems = items;
        for (Item i : playerItems)
        {
            if (i.getName().equals(item))
            {
                return i;
            }
        }
        return null;
    }
    
}
