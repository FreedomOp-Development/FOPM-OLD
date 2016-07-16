package me.buildcarter8.FreedomOpMod;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
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
    private FOPM_CommandLoader cl;

    @Override
    public void onLoad()
    {
        Main.plugin = this;
        this.log = new FOPM_PluginLog(plugin);
        this.cl = new FOPM_CommandLoader(plugin);
    }

    @Override
    public void onEnable()
    {
        cl.registerCmds();
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
