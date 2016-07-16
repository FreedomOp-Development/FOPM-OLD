package me.buildcarter8.FreedomOpMod.Commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.buildcarter8.FreedomOpMod.FOPM_SuperadminList;
import me.buildcarter8.FreedomOpMod.FOPM_Util;
import me.buildcarter8.FreedomOpMod.Main;
import net.md_5.bungee.api.ChatColor;

public class C_gtfo extends FOPM_Command
{

    private final Main plugin;

    public Command_gtfo(Main plugin)
    {
        super("gtfo", "gtfo [player]", "SA Command - Make someone get the fuck out.", PERM_MESSAGE, Arrays.asList("ban"));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!FOPM_SuperadminList.isUserSuperadmin(sender))
        {
            sender.sendMessage(Main.NOPE);
        }
        else
        {
            if (args.length == 0)
            {
                return false;
            }
            if (args.length == 1)
            {
                Player p = Bukkit.getServer().getPlayer(args[0]);
                p.setOp(false);
                Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + " - " + "Banning " + p.getName());
                p.setBanned(true);
                p.kickPlayer(ChatColor.RED + "GTFO.");
                Bukkit.getServer().banIP(p.getAddress().getAddress().getHostAddress());

            }
        }

        return true;
    }
}
