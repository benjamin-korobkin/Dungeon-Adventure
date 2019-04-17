/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realzork;


import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;

class Room 
{
    public String description, specDesc, blindLook;
    private HashMap exits;        // stores exits of this room.
    public ArrayList<Item> items;
    public boolean locked;

    /**
     * Create a room described "description". At first, it has no exits.
     * "description" is something like "a kitchen" or "an open court yard".
     */
    public Room(String description, boolean locked) 
    {
        this.description = description;
        exits = new HashMap();
        items = new ArrayList<Item>();
        blindLook = "It's pitch black. You can't see a thing!";
        this.locked = locked;
    }

    /**
     * Define the exits of this room.  Every direction either leads to
     * another room or is null (no exit there).
     */
    public void setExits(Room north, Room east, Room south, Room west) 
    {
        if(north != null)
            exits.put("north", north);
        if(east != null)
            exits.put("east", east);
        if(south != null)
            exits.put("south", south);
        if(west != null)
            exits.put("west", west);
    }

    public void setItems(Item ... i)
    {
        for (Item item : i)
        {
            items.add(item);
        }
    } 
    // Used to check if item exists in room
    public Item roomItem(String item)
    {
        ArrayList<Item> roomItems = items;
        for (Item i : roomItems)
        {
            if (i.getName().equals(item))
            {
                return i;
            }
        }
        return null;
    }
    
    public void removeItem(Item i) {
        items.remove(i);
    }
    
    public void addItem(Item i) {
        items.add(i);
    }

    /**
     * Return a long description of this room, on the form:
     *     You are in the kitchen.
     *     Exits: north west
     */
    
    public String description() {  
        return "You stand at " + description + "\n" + specDesc +"\n"+ exitString();
    }
    
    public String lookDescription() {
        return "It's " + description + specDesc + "\n"+exitString();
    }
    
    public String blindDesc() {
        return specDesc + "\n"+ exitString();
    }

    /**
     * Return a string describing the exits, for example
     * "Exits: north west ".
     */
    public String exitString()
    {
        String returnString = "Exits:";
		Set keys = exits.keySet();
        for(Iterator iter = keys.iterator(); iter.hasNext(); )
            returnString += " " + iter.next();
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     */
    public Room nextRoom(String direction) {
        return (Room)exits.get(direction);
    }

    public void setSpecDesc(String s) {
        specDesc = s;
    }
    
    public void unlock() {
        this.locked = false;
    }
    public void lock() {
        this.locked = true;
    }

    public String blindLook() {
        return blindLook;
    }
    /*public void setBlindDesc(String s) {
        blindLook = s;
    }
   */
  
    
}
