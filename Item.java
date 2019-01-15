package realzork;

public class Item {
    private String name;
    private int weight;
    private String description;
    public boolean taken;
    
    public Item(String name, int weight, String desc)
    {
        this.name = name;
        this.weight = weight;
        this.description = desc;
        taken = false;
    }
    
    public Item(String name, String desc)
    {
        this(name, 0, desc);
        
    }
    
    public String toString(Item item) {
        return item.getName();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the weight
     */
    public int getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}