package me.buildcarter8.FreedomOpMod.Commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.buildcarter8.FreedomOpMod.FOPM_SuperadminList;
import me.buildcarter8.FreedomOpMod.Main;
import net.md_5.bungee.api.ChatColor;

public class Command_opall extends FOPM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        if (!FOPM_SuperadminList.isUserSuperadmin(sender))
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
