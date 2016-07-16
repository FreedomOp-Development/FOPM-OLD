package me.buildcarter8.FreedomOpMod;

import me.buildcarter8.FreedomOpMod.Commands.C_creative;
import me.buildcarter8.FreedomOpMod.Commands.C_fopm;
import me.buildcarter8.FreedomOpMod.Commands.C_gtfo;
import me.buildcarter8.FreedomOpMod.Commands.C_opall;
import me.buildcarter8.FreedomOpMod.Commands.C_saconfig;
import me.buildcarter8.FreedomOpMod.Commands.C_survival;

public class FOPM_CommandLoader
{
    // grabs all command classes for the plugin to load them up in

    //
    // THIS class is REQUIRED to have the command load properly
    //

    private final Main plugin;
    //
    private final C_fopm fopm;
    private final C_gtfo gtfo;
    private final C_opall opall;
    private final C_saconfig saconfig;
    private final C_creative creative;
    private final C_survival survival;

    public FOPM_CommandLoader(Main plugin)
    {
        this.plugin = plugin;
        this.fopm = new C_fopm(this.plugin);
        this.gtfo = new C_gtfo(this.plugin);
        this.opall = new C_opall(this.plugin);
        this.saconfig = new C_saconfig(this.plugin);
        this.creative = new C_creative(this.plugin);
        this.survival = new C_survival(this.plugin);
    }

    public void registerCmds()
    {
        fopm.register();
        gtfo.register();
        opall.register();
        saconfig.register();
        creative.register();
        survival.register();
    }

    public void unregisterCmds()
    {
        fopm.unregister();
        gtfo.unregister();
        opall.unregister();
        saconfig.unregister();
        creative.unregister();
        survival.unregister();
    }
}
