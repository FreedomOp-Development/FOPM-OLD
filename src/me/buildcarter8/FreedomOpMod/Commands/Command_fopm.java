package me.buildcarter8.FreedomOpMod.Commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import me.buildcarter8.FreedomOpMod.FOPM_PluginLog;
import me.buildcarter8.FreedomOpMod.Main;

public class Command_fopm extends FOPM_Command
{
    private final Main plugin;

    public Command_fopm(Main plugin)
    {
        super("fopm", "fopm", "The main FOPM command.", PERM_MESSAGE, Arrays.asList("fom"));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length == 0)
        {
            sender.sendMessage(ChatColor.AQUA + "======" + ChatColor.GREEN + "FreedomOpMod" + ChatColor.AQUA + "======");
            sender.sendMessage(ChatColor.GREEN + "Version " + Main.VERSION);
            sender.sendMessage(ChatColor.GREEN + "Created by buildcarter8 (Chief of Development Operations)");
            sender.sendMessage(ChatColor.AQUA + "=====================================================");
            return true;
        }
        else if (args.length == 1)
        {
            if (args[0].equalsIgnoreCase("reload"))
            {
                if (!plugin.al.isUserAdmin(sender))
                {
                    msgNoPerms(sender);
                    return true;
                }

                sender.sendMessage(ChatColor.GRAY + "Reloading FOPM");
                try {
                    Bukkit.getPluginManager().disablePlugin(plugin);
                    Bukkit.getPluginManager().enablePlugin(plugin);
                }
                catch (Exception ex)
                {
                    FOPM_PluginLog.severe(ex);
                }
                sender.sendMessage(ChatColor.GRAY + "FOPM reloaded. Check logs to see if there are any issues.");
                return true;
            }
        }
        return true;
    }

}
