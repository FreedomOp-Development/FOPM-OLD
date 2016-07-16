package me.buildcarter8.FreedomOpMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import me.buildcarter8.FreedomOpMod.Commands.FOPM_Command;
import me.buildcarter8.FreedomOpMod.Listeners.*;

public class Main extends JavaPlugin
{
    //
    public static String VERSION = "0.1";
    public static String plugin_file = "plugin.yml";
    public static Main plugin;
    public static final String SUPERADMIN_FILE = "superadmin.yml";
    public static final String CONFIG_FILE = "config.yml";
    public static String NOPE = ChatColor.RED + "You do not have the correct rank to use this command";
    public static final String COMMAND_PATH = "me.buildcarter8.FreedomOpMod.Commands";
    public static final String COMMAND_PREFIX = "Command_";
    private static List<String> seniorAdminNames = new ArrayList<String>();
    //
    private FOPM_PluginLog log;

    @Override
    public void onLoad()
    {
        Main.plugin = this;
        this.log = new FOPM_PluginLog(plugin);
    }

    @Override
    public void onEnable()
    {
        log.minfo("FOPM Version " + VERSION + " has been enabled");
        log.minfo("FOPM created by buildcarter8");
        final PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new FOPM_PlayerHandler(), plugin);

    }

    @Override
    public void onDisable()
    {
        log.minfo("FOPM has been disabled");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        try
        {
            Player sender_p = null;
            boolean senderIsConsole = false;
            if (sender instanceof Player)
            {
                sender_p = (Player) sender;
                log.minfo(String.format("[PLAYER_COMMAND] %s(%s): /%s %s", sender_p.getName(), ChatColor.stripColor(sender_p.getDisplayName()), commandLabel,
                        FOPM_Util.implodeStringList(" ", Arrays.asList(args))));
            }
            else
            {
                senderIsConsole = true;
                log.minfo(String.format("[CONSOLE_COMMAND] %s: /%s %s", sender.getName(), commandLabel, FOPM_Util.implodeStringList(" ", Arrays.asList(args))));
            }

            FOPM_Command dispatcher;
            try
            {
                ClassLoader classLoader = Main.class.getClassLoader();
                dispatcher = (FOPM_Command) classLoader.loadClass(String.format("%s.%s%s", COMMAND_PATH, COMMAND_PREFIX, cmd.getName().toLowerCase())).newInstance();
                dispatcher.setPlugin(plugin);
            }
            catch (Throwable ex)
            {
                log.msevere("[" + getDescription().getName() + "] Command not loaded: " + cmd.getName() + "\n" + ex);
                sender.sendMessage(ChatColor.RED + "Command Error: Command not loaded: " + cmd.getName());
                return true;
            }

            try
            {
                return dispatcher.run(sender, sender_p, cmd, commandLabel, args, senderIsConsole);
            }
            catch (Throwable ex)
            {
                sender.sendMessage(ChatColor.RED + "Command Error: " + ex.getMessage());
            }

            dispatcher = null;
        }
        catch (Throwable ex)
        {
            log.msevere("[" + getDescription().getName() + "] Command Error: " + commandLabel + "\n" + ex);
            sender.sendMessage(ChatColor.RED + "Unknown Command Error.");
        }

        return true;
    }

    public void loadMainConfig()
    {

    }

    public static List<String> superadmins = new ArrayList<String>();
    public static List<String> superadmin_ips = new ArrayList<String>();

    public void loadSuperadminConfig()
    {

        try
        {
            FOPM_SuperadminList.backupSavedList();
            FOPM_SuperadminList.loadSuperadminList();

            superadmins = FOPM_SuperadminList.getSuperadminNames();
            superadmin_ips = FOPM_SuperadminList.getSuperadminIPs();
        }
        catch (Exception ex)
        {
            log.msevere("Error loading superadmin list: " + ex.getMessage());
        }
    }

}
