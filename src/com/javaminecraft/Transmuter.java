// Transmuter
// Aliya Petranik 2015
// Changes one block type into another. 

package com.javaminecraft;

import java.util.Random;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.*;

public class Transmuter extends JavaPlugin {

    public static final Logger log = Logger.getLogger("Minecraft");
    Player me;
    Material input;
    Material output;

    @Override
    public void onEnable() {
        this.log.info(this.getDescription().getName() +" Has been enabled!");
    }

    @Override 
    public void onDisable() {
        this.log.info(this.getDescription().getName() + " Has been disabled!");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        me = (Player) sender;

        if (sender instanceof Player) {

            // if they want to change two materials
            if (label.equalsIgnoreCase("change")) {
                executeCommand(arguments);
                return true;

            } // if they want to undo their action
            else if (label.equalsIgnoreCase("undo")) {
                executeUndo(arguments);
                return true;

            } // if they don't type in anything real
            else {
                return false;
            }
        }
        return false;
    }

    public void executeCommand(String[] arguments) {
        if (arguments.length != 2) {
            me.sendMessage("Please enter the correct number of arguments");
            return;
        }

        input = Material.getMaterial(arguments[0]);
        output = Material.getMaterial(arguments[1]);

        // checks input is valid
        if (input == null || output == null) {
            if (input == null) {
                me.sendMessage(arguments[0] + " is not a known material");
                return;
            } else if (output == null) {
                me.sendMessage(arguments[1] + " is not a known material");
                return;
            }

        }

        //get players current inventory
        PlayerInventory stuff = me.getInventory();

        for (int i = 0; i < stuff.getSize(); i++) {

            //examine each stack of items
            ItemStack items = stuff.getItem(i);

            if (items == null) {
                // this inventory slot is empty
                continue;

            }

            if (items.getType() == input) {
                //this slot matches input material

                items.setType(output);
                //fill the slot with maximum amount possible
                items.setAmount(output.getMaxStackSize());
            }
        }
        me.sendMessage(randomColor() + input.name() + ChatColor.WHITE + " transmuted into " + randomColor() + output.name());
    }

    public void executeUndo(String[] arguments) {
        PlayerInventory stuff = me.getInventory();
        for (int i = 0; i < stuff.getSize(); i++) {

            //examine each stack of items
            ItemStack items = stuff.getItem(i);

            if (items == null) {
                // this inventory slot is empty
                continue;

            }

            if (items.getType() == output) {
                //this slot matches output material

                items.setType(input);

                //fill the slot with maximum amount possible
                items.setAmount(input.getMaxStackSize());
            }
        }
        me.sendMessage("Undo complete");
    }
    
    // returns a random chat colors -- for fun
    public ChatColor randomColor() {
        // Creates an array of all the chat colors in minecraft
        ChatColor[] colors = {ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA, 
            ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE, 
            ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN, ChatColor.LIGHT_PURPLE, 
            ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
        
        // returns a random chat color
        Random rand = new Random();
        int index = rand.nextInt(16);
        return colors[index];
    }
}
