package me.buildcarter8.FreedomOpMod.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import me.buildcarter8.FreedomOpMod.FOPM_AdministratorList;
import me.buildcarter8.FreedomOpMod.FOPM_Util;

public class FOPM_PlayerHandler implements Listener
{
    // This is ment to Handle Player login and player chat nothing else

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        final Player p = event.getPlayer();
        if (FOPM_AdministratorList.isUserAdmin(p))
        {
            // Announce that player is super admin
            Bukkit.broadcastMessage(ChatColor.GOLD + p.getDisplayName() + " is a " + FOPM_Util.getRank(p));

        }

    }

}
