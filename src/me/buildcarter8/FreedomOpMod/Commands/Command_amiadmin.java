package me.buildcarter8.FreedomOpMod.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.buildcarter8.FreedomOpMod.FOPM_AdministratorList;
import me.buildcarter8.FreedomOpMod.Main;

public class Command_amiadmin extends FOPM_Command
{
    private final Main plugin;

    public Command_amiadmin(Main plugin)
    {
        super("amiadmin", "amiadmin", "Checks to see if you are actually admin.", PERM_MESSAGE);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {   
        if (FOPM_AdministratorList.isUserAdmin(sender)) {
            sender.sendMessage(ChatColor.GREEN + "You are an admin.");
        }
        else {
            sender.sendMessage(ChatColor.RED + "You are NOT an admin.");
        }
        
        return true;
    }
}
