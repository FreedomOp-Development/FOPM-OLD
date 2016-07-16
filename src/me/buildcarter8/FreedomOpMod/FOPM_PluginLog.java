package me.buildcarter8.FreedomOpMod;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FOPM_PluginLog
{
    // In this class, we remove the amount of errors and warnings

    private static Main plugin;
    private static Logger log = plugin.getLogger();

    public FOPM_PluginLog(Main plugin)
    {
        FOPM_PluginLog.plugin = plugin;
    }

    // Not static for plugin usage only
    public void minfo(String args)
    {
        log.info(args);
    }

    public void mwarn(String args)
    {
        log.warning(args);
    }

    public void mwarn(Exception args)
    {
        log.warning(args.getMessage());
    }

    public void msevere(String args)
    {
        log.severe(args);
    }

    public void msevere(Exception args)
    {
        log.severe(args.getMessage());
    }

    public void mlog(Level lvl, String args)
    {
        log.log(lvl, args);
    }

    public void mlog(Level lvl, String args, Exception ex)
    {
        log.log(lvl, args, ex);
    }

    // Static methodology only
    public static void info(String args)
    {
        log.info(args);
    }

    public static void warn(String args)
    {
        log.warning(args);
    }

    public static void warn(Exception args)
    {
        log.warning(args.getMessage());
    }

    public static void severe(String args)
    {
        log.severe(args);
    }

    public static void severe(Exception args)
    {
        log.severe(args.getMessage());
    }

    public static void log(Level lvl, String args)
    {
        log.log(lvl, args);
    }

    public static void log(Level lvl, String args, Exception ex)
    {
        log.log(lvl, args, ex);
    }

}
