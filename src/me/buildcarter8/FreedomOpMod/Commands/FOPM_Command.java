package me.buildcarter8.FreedomOpMod.Commands;

import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.buildcarter8.FreedomOpMod.Main;

public abstract class FOPM_Command
{
    protected Main plugin;
    protected static final Logger log = Logger.getLogger("Minecraft-Server");

    public FOPM_Command()
    {
    }

    abstract public boolean run(final CommandSender sender, final Player sender_p, final Command cmd, final String commandLabel, final String[] args, final boolean senderIsConsole);

    public void setPlugin(Main plugin)
    {
        this.plugin = plugin;
    }

    public Player getPlayer(String partialname) throws CantFindPlayerException
    {
        List<Player> matches = Bukkit.matchPlayer(partialname);
        if (matches.isEmpty())
        {
            throw new CantFindPlayerException(partialname);
        }
        else
        {
            return matches.get(0);
        }
    }
}
