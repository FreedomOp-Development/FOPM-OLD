package me.buildcarter8.FreedomOpMod.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.buildcarter8.FreedomOpMod.Main;

public class Command_opall extends FOPM_Command
{
    private final Main plugin;

    public Command_opall(Main plugin)
    {
        super("opall", "opall", "Op all players on the server", PERM_MESSAGE);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!plugin.al.isUserAdmin(sender))
        {
            sender.sendMessage(Main.NOPE);
        }
        else
        {
            Bukkit.broadcastMessage(ChatColor.GOLD + sender.getName() + " Opping all online players!");
            for (Player all : Bukkit.getOnlinePlayers())
            {
                all.setOp(true);
                all.sendMessage(ChatColor.RED + "You are now op!");
                all.setGameMode(GameMode.CREATIVE);
            }
        }

        return true;
    }

}
