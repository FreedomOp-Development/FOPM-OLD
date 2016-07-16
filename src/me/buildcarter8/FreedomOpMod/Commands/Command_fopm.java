package me.buildcarter8.FreedomOpMod.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.buildcarter8.FreedomOpMod.Main;

public class Command_fopm extends FOPM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        sender.sendMessage(ChatColor.AQUA + "======" + ChatColor.GREEN + "FreedomOpMod" + ChatColor.AQUA + "======");
        sender.sendMessage(ChatColor.GREEN + "Version " + Main.VERSION);
        sender.sendMessage(ChatColor.GREEN + "Created by buildcarter8 - Chief Dev FreedomOP");
        sender.sendMessage(ChatColor.AQUA + "========================================================");

        return true;
    }

}
