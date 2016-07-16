package me.buildcarter8.FreedomOpMod.Commands;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.buildcarter8.FreedomOpMod.FOPM_SuperadminList;
import me.buildcarter8.FreedomOpMod.Main;

public class C_creative extends FOPM_Command
{

    private final Main plugin;

    public Command_creative(Main plugin)
    {
        super("creative", "creative [player]", "Sets gamemode to creative.", PERM_MESSAGE, Arrays.asList("gmc"));
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {

        if (args.length == 0)
        {
            Player sender_p = getSenderP(sender);

            msg(sender, ChatColor.GOLD + "You set your gamemode to " + ChatColor.GREEN + "CREATIVE");
            sender_p.setGameMode(GameMode.CREATIVE);
            msg(sender, "Your gamemode has been updated.");

            return true;
        }
        if (args.length == 1)
        {
            Player player = getPlayer(args[0]);
            Player sender_p = getSenderP(sender);

            if (player == null)
            {
                notFound(sender);
            }

            if (!FOPM_SuperadminList.isUserSuperadmin(sender))
            {
                msgNoPerms(sender);
                return true;
            }

            msg(sender, ChatColor.GOLD + sender.getName() + " set your gamemode to " + ChatColor.GREEN + "CREATIVE");
            player.setGameMode(GameMode.CREATIVE);
            msg(sender, "Your gamemode has been updated.");
        }

        return true;
    }
}
