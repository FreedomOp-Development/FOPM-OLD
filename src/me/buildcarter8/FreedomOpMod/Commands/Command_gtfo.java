package me.buildcarter8.FreedomOpMod.Commands;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.buildcarter8.FreedomOpMod.FOPM_AdministratorList;
import me.buildcarter8.FreedomOpMod.Main;
import net.md_5.bungee.api.ChatColor;

public class Command_gtfo extends FOPM_Command
{
    @SuppressWarnings("unused")
    private final Main plugin;

    public Command_gtfo(Main plugin)
    {
        super("gtfo", "gtfo [player]", "SA Command - Make someone get the fuck out.", PERM_MESSAGE, Arrays.asList("ban"));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (!FOPM_AdministratorList.isUserAdmin(sender))
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
                
                // Get current date and add 48 hours to it
                Date now = new Date();
                Date newDate = DateUtils.addHours(now, 48);
                
                // Switch between ip and username bans
                BanList.Type bt1 = BanList.Type.NAME;
                BanList.Type bt2 = BanList.Type.IP;
                
                
                // Ban by username
                Bukkit.getBanList(bt1).addBan(p.getName(), "GTFO", newDate, sender.getName());
                
                // Ban by ip
                Bukkit.getBanList(bt2).addBan(p.getAddress().getAddress().getHostAddress(), "GTFO", newDate, sender.getName());

                // Kick player in the end
                p.kickPlayer(ChatColor.RED + "GTFO.");
            }
        }

        return true;
    }
}
