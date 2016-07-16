package me.buildcarter8.FreedomOpMod.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.buildcarter8.FreedomOpMod.FOPM_SuperadminList;
import me.buildcarter8.FreedomOpMod.Main;
import net.md_5.bungee.api.ChatColor;

public class Command_saconfig extends FOPM_Command
{

    @Override
    public boolean run(CommandSender sender, Player sender_p, Command cmd, String commandLabel, String[] args, boolean senderIsConsole)
    {
        {
            if (!senderIsConsole)
            {
                sender.sendMessage(Main.NOPE);
            }
            else
            {
                if (args[0].equalsIgnoreCase("add"))
                {
                    Player p;
                    try
                    {
                        p = getPlayer(args[1]);
                    }
                    catch (CantFindPlayerException ex)
                    {
                        sender.sendMessage(ex.getMessage());
                        return true;
                    }

                    String user_name = p.getName().toLowerCase().trim();
                    FOPM_SuperadminList.addSuperadmin(user_name);
                    Bukkit.broadcastMessage(ChatColor.RED + "Adding " + user_name + " to the super admin list");
                }

            }
            return true;
        }
    }
}
