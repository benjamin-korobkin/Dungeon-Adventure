/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realzork;

import java.util.*;
/**
 *
 * @author Benjamin.Korobkin
 */

class CommandWords
{
    // a constant array that holds all valid command words
    //private static ArrayList<String> valid_commands;
    private static final String[] ALL_COMMANDS = {
       "go", "look", "take", "drop", "back", "help", "quit"
    };

    /**
     * Constructor - initialize the command words.
     */
    public CommandWords()
    {
        /*
        valid_commands = new ArrayList<>();
        for(String s: ALL_COMMANDS) {
            valid_commands.add(s);
        }
        */
    }
    
    public void showCommands() 
    { 
        for(int i = 0; i < ALL_COMMANDS.length;i++)
        {
            System.out.print(ALL_COMMANDS[i] + " ");
        }
        System.out.println();
    }
}
