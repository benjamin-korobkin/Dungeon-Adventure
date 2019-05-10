/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realzork;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

class Parser 
{
    
    public final ArrayList<String> ALL_COMMANDS;  // holds all valid command words
    public final ArrayList<String> DIRECTIONS;
    
    public Parser()
    {
        this.ALL_COMMANDS = new ArrayList<>
        //this.commands = new ArrayList<>
          (Arrays.asList("go", "look", "take", "back", 
                  "help", "quit"));
                                  
        this.DIRECTIONS = new ArrayList<>(Arrays.asList
        ("north", "east", "south", "west"));
    }

    public void showCommands() 
    { 
        /**for(int i = 0; i < ALL_COMMANDS.length;i++)
        {
        System.out.print(ALL_COMMANDS[i] + " ");
        }**/
        ALL_COMMANDS.forEach((str) -> {
            System.out.print(str + " ");
        });
        System.out.println();
    }
    
    public Command getCommand() 
    {
        String inputLine = "";   // will hold the full input line
        String word1;
        String word2;
        String word3;

        System.out.print("> ");     // print prompt

        BufferedReader reader = 
            new BufferedReader(new InputStreamReader(System.in));
        try {
            inputLine = reader.readLine();
        }
        catch(java.io.IOException exc) {
            System.out.println ("There was an error during reading: "
                                + exc.getMessage());
        }

        StringTokenizer tokenizer = new StringTokenizer(inputLine);

        if(tokenizer.hasMoreTokens())
            word1 = tokenizer.nextToken().toLowerCase();      // get first word
        else
            word1 = null;
        if(tokenizer.hasMoreTokens())
            word2 = tokenizer.nextToken().toLowerCase();      // get second word
        else
            word2 = null;
        if(tokenizer.hasMoreTokens())
            word3 = tokenizer.nextToken().toLowerCase();      // get third word
        else
            word3 = null;

        // note: we just ignore the rest of the input line.
        // Now check whether this word is known. If so, create a command
        // with it. If not, create a "nil" command (for unknown command).

        if(DIRECTIONS.contains(word1) || ALL_COMMANDS.contains(word1))/*
            */return new Command(word1, word2, word3);
        else
            return new Command(null, word2, word3);
    }

    /**
     * Print out a list of valid command words.
     */
}