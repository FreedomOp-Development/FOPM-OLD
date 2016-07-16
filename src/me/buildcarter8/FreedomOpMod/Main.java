package me.buildcarter8.FreedomOpMod;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import me.buildcarter8.FreedomOpMod.Listeners.*;

public class Main extends JavaPlugin
{
    //
    public boolean debug = true;
    public static String VERSION = "0.1";
    public static String plugin_file = "plugin.yml";
    public static Main plugin;
    public static final String SUPERADMIN_FILE = "superadmin.yml";
    public static final String CONFIG_FILE = "config.yml";
    public static String NOPE = ChatColor.RED + "You do not have the correct rank to use this command";
    public static final List<String> DEVELOPERS = Arrays.asList("buildcarter8", "tylerhyperHD", "Cyro1999");
    //
    public FOPM_PluginLog log;
    public FOPM_CommandLoader cl;

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
        loadSuperadminConfig();
        FOPM_PluginLog.info("FOPM Version " + VERSION + " has been enabled");
        FOPM_PluginLog.info("FOPM created by buildcarter8 and the developement team");
        final PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new FOPM_PlayerHandler(), plugin);
        pm.registerEvents(new FOPM_PlayerListener(), plugin);

    }

    @Override
    public void onDisable()
    {
        FOPM_PluginLog.info("FOPM has been disabled");
    }

    public boolean areWeDebugging() {
        return debug;
    }
    
    public void loadMainConfig()
    {

    }

    
    // TODO: Fix super admin config! It doesn't work or detect anything!
    public void loadSuperadminConfig()
    {
        try
        {
            FOPM_AdministratorList.backupSavedList();
            FOPM_AdministratorList.loadSuperadminList();
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe("Error loading superadmin list: " + ex.getMessage());
        }
    }
}
