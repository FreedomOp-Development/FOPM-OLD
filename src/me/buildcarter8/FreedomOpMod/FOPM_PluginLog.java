package me.buildcarter8.FreedomOpMod;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FOPM_PluginLog
{
    // In this class, we remove the amount of errors and warnings

    private static Main plugin;

    public FOPM_PluginLog(Main plugin)
    {
        FOPM_PluginLog.plugin = plugin;
    }

    // Static methodology only
    public static void info(String args)
    {
        plugin.getLogger().info(args);
    }

    public static void warn(String args)
    {
        plugin.getLogger().warning(args);
    }

    public static void warn(Exception args)
    {
        plugin.getLogger().warning(args.getMessage());
    }

    public static void severe(String args)
    {
        plugin.getLogger().severe(args);
    }

    public static void severe(Exception args)
    {
        plugin.getLogger().severe(args.getMessage());
    }

    public static void log(Level lvl, String args)
    {
        plugin.getLogger().log(lvl, args);
    }

    public static void log(Level lvl, String args, Exception ex)
    {
        plugin.getLogger().log(lvl, args, ex);
    }

}
