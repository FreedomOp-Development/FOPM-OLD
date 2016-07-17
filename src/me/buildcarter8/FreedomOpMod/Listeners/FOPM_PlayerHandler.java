package me.buildcarter8.FreedomOpMod.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import me.buildcarter8.FreedomOpMod.FOPM_Administrator;
import me.buildcarter8.FreedomOpMod.FOPM_Util;
import me.buildcarter8.FreedomOpMod.Main;

public class FOPM_PlayerHandler implements Listener
{
    // This is meant to Handle Player login and player chat nothing else

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        // Main method used completely
        final Player p = event.getPlayer();
        if (FOPM_Administrator.isUserAdmin(p))
        {
            // Announce that player is super admin
            Bukkit.broadcastMessage(ChatColor.GOLD + p.getDisplayName() + " is a " + FOPM_Administrator.getRank(p));

        }
    }

}
