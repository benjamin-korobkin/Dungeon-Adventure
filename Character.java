package realzork;

import java.util.*;

/**
 *
 * @author Benjamin.Korobkin
 */
public class Character {
    // may become a HashMap, we'll see.
    public ArrayList<Item> items;
    //public Room room;
    public int storage;
    
    public Character(Room r, int s) {
        items = new ArrayList<>();
        //room = r;
        storage = s;
    }
    
    public void addItem(Item i) {
        items.add(i);
        System.out.println("The " + i.getName() + " was added to your inventory.");
        
        if (i.getName().equals("torch")) {
            System.out.println("The ember of light brightens your way!\n"
                    + "You can see clearly now!");            
        }
        
    }
    
    public void removeItem(Item i) {
        items.remove(i);
    }
    
    public boolean hasItem(Item i) {
        return items.contains(i);
    }
    
    public int itemsWeight() {
        int weight = 0;
        for (Item i : items)
        {
            weight += i.getWeight();
        }
        return weight;
    }
    
    public Item plyrItem(String item) {
        //ArrayList<Item> plyrItems = items;
        for (Item i : items)
        {
            if (i.getName().equals(item))
            {
                return i;
            }
        }
        return null;
    }
    
}
