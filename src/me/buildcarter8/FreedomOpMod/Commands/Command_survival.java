package me.buildcarter8.FreedomOpMod.Commands;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.buildcarter8.FreedomOpMod.Main;

public class Command_survival extends FOPM_Command
{
    private final Main plugin;

    public Command_survival(Main plugin)
    {
        super("survival", "survival [player]", "Sets gamemode to survival.", PERM_MESSAGE, Arrays.asList("gms"));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if (args.length == 0)
        {
            Player sender_p = getSenderP(sender);

            msg(sender, ChatColor.GOLD + "You set your gamemode to " + ChatColor.RED + "SURVIVAL");
            sender_p.setGameMode(GameMode.SURVIVAL);
            msg(sender, "Your gamemode has been updated.");

            return true;
        }
        if (args.length == 1)
        {
            Player player = getPlayer(args[0]);

            if (player == null)
            {
                notFound(sender);
            }

            if (!plugin.al.isUserAdmin(sender))
            {
                msgNoPerms(sender);
                return true;
            }

            msg(sender, ChatColor.GOLD + sender.getName() + " set your gamemode to " + ChatColor.RED + "SURVIVAL");
            player.setGameMode(GameMode.SURVIVAL);
            msg(sender, "Your gamemode has been updated.");
        }

        return true;
    }
}
