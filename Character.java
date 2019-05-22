package realzork;

import java.util.*;

/**
 *
 * @author Benjamin.Korobkin
 */
public class Character {
    // may become a HashMap, we'll see.
    public ArrayList<Item> items;
    public Room room, prevRoom;
    //public Room room;
    //public int storage;
    //private int coins;
    
    public Character(Room r) {
        items = new ArrayList<>();
        this.room = r;
        prevRoom = null;
        //coins = 0;
        //storage = s;        
    }
    
    public void addItem(Item i) {
        items.add(i);
        System.out.println("The " + i.getName() + " was added to your inventory.");        
    }
    
    public void removeItem(Item i) {
        items.remove(i);
    }
    
    public boolean hasItem(Item i) {
        return items.contains(i);
    }
    
   /* public int itemsWeight() {
        int weight = 0;
        for (Item i : items)
        {
            weight += i.getWeight();
        }
        return weight;
    }
    */
    
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
