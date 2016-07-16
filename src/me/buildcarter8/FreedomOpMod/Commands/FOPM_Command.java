package me.buildcarter8.FreedomOpMod.Commands;

/*
 * The MIT License
 *
 * Copyright 2016 buildcarter8.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.buildcarter8.FreedomOpMod.FOPM_PluginLog;

/*
* Some of this class has been modified to fix stuff
*/

public abstract class FOPM_Command implements CommandExecutor, TabExecutor
{

    public static String PERM_MESSAGE = ChatColor.RED + "You do not have permission to perform this command.";
    public static String U_R_OP = ChatColor.YELLOW + "You are now op!";
    public static String U_R_NOT_OP = ChatColor.RED + "You are no longer op!";

    protected final String command;
    protected final String description;
    protected final List<String> alias;
    protected final String usage;
    protected final String permMessage;

    protected static CommandMap cmap;

    public FOPM_Command(String command)
    {
        this(command, null, null, null, null);
    }

    public FOPM_Command(String command, String usage)
    {
        this(command, usage, null, null, null);
    }

    public FOPM_Command(String command, String usage, String description)
    {
        this(command, usage, description, null, null);
    }

    public FOPM_Command(String command, String usage, String description, String permissionMessage)
    {
        this(command, usage, description, permissionMessage, null);
    }

    public FOPM_Command(String command, String usage, String description, List<String> aliases)
    {
        this(command, usage, description, null, aliases);
    }

    public FOPM_Command(String command, String usage, String description, String permissionMessage, List<String> aliases)
    {
        this.command = command.toLowerCase();
        this.usage = usage;
        this.description = description;
        this.permMessage = permissionMessage;
        this.alias = aliases;
    }

    public void register()
    {
        ReflectCommand cmd = new ReflectCommand(this.command);
        if (this.alias != null)
        {
            cmd.setAliases(this.alias);
        }
        if (this.description != null)
        {
            cmd.setDescription(this.description);
        }
        if (this.usage != null)
        {
            cmd.setUsage(this.usage);
        }
        if (this.permMessage != null)
        {
            cmd.setPermissionMessage(this.permMessage);
        }
        getCommandMap().register("", cmd);
        cmd.setExecutor(this);
    }

    // Gets command to unregister through bukkit
    public void unregister()
    {
        // Debugger
        FOPM_PluginLog.info("Unregistering command " + this.command);
        Bukkit.getPluginCommand(this.command).unregister(getCommandMap());
    }

    final CommandMap getCommandMap()
    {
        if (cmap == null)
        {
            try
            {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                cmap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            }
            catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e)
            {
                FOPM_PluginLog.severe(e);
            }
        }
        else if (cmap != null)
        {
            return cmap;
        }
        return getCommandMap();
    }

    private final class ReflectCommand extends Command
    {

        private FOPM_Command exe = null;

        protected ReflectCommand(String command)
        {
            super(command);
        }

        public void setExecutor(FOPM_Command exe)
        {
            this.exe = exe;
        }

        @Override
        public boolean execute(CommandSender sender, String commandLabel, String[] args)
        {
            if (exe != null)
            {
                return exe.onCommand(sender, this, commandLabel, args);
            }
            return false;
        }

        @Override
        public List<String> tabComplete(CommandSender sender, String alais, String[] args)
        {
            if (exe != null)
            {
                return exe.onTabComplete(sender, this, alais, args);
            }
            return null;
        }
    }

    @SuppressWarnings("override")
    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

    @SuppressWarnings("override")
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args)
    {
        return null;
    }

    public void msg(Player player, String msg)
    {
        player.sendMessage(msg);
    }

    public void msg(CommandSender sender, String msg)
    {
        sender.sendMessage(msg);
    }

    public Player getPlayer(String play)
    {
        return Bukkit.getPlayer(play);
    }

    public Player getSenderP(CommandSender sender)
    {
        return (Player) sender;
    }

    public void notFound(Player player)
    {
        player.sendMessage(ChatColor.RED + "Player not found!");
    }

    public void notFound(CommandSender sender)
    {
        sender.sendMessage(ChatColor.RED + "Player not found!");
    }

    public void msgNoPerms(CommandSender sender)
    {
        sender.sendMessage(PERM_MESSAGE);
    }

    public void adminAnnounce(Player player_p, String msg)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.sendMessage(ChatColor.RED + player_p.getName() + " - " + msg);
        }
    }

    public void adminAnnounce(Player player_p, ChatColor color, String msg)
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            player.sendMessage(color + player_p.getName() + " - " + msg);
        }
    }

    public boolean isConsoleSender(CommandSender sender)
    {
        if (sender.getName() == Bukkit.getConsoleSender().getName())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
