package me.buildcarter8.FreedomOpMod;

import me.buildcarter8.FreedomOpMod.Commands.Command_creative;
import me.buildcarter8.FreedomOpMod.Commands.Command_fopm;
import me.buildcarter8.FreedomOpMod.Commands.Command_gtfo;
import me.buildcarter8.FreedomOpMod.Commands.Command_opall;
import me.buildcarter8.FreedomOpMod.Commands.Command_saconfig;
import me.buildcarter8.FreedomOpMod.Commands.Command_survival;

public class FOPM_CommandLoader
{
    // grabs all command classes for the plugin to load them up in
    
    //
    //  THIS class is REQUIRED to have the command load properly
    //
    
    
    private final Main plugin;
    //
    private final Command_fopm fopm;
    private final Command_gtfo gtfo;
    private final Command_opall opall;
    private final Command_saconfig saconfig;
    private final Command_creative creative;
    private final Command_survival survival;
    
    public FOPM_CommandLoader(Main plugin) {
        this.plugin = plugin;
        this.fopm = new Command_fopm(this.plugin);
        this.gtfo = new Command_gtfo(this.plugin);
        this.opall = new Command_opall(this.plugin);
        this.saconfig = new Command_saconfig(this.plugin);
        this.creative = new Command_creative(this.plugin);
        this.survival = new Command_survival(this.plugin);
    }
    
    public void registerCmds() {
        fopm.register();
        gtfo.register();
        opall.register();
        saconfig.register();
        creative.register();
        survival.register();
    }
    
    public void unregisterCmds() {
        fopm.unregister();
        gtfo.unregister();
        opall.unregister();
        saconfig.unregister();
        creative.unregister();
        survival.unregister();
    }
}
