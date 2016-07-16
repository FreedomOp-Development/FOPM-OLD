package me.buildcarter8.FreedomOpMod.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

import me.buildcarter8.FreedomOpMod.FOPM_Messages;

public class FOPM_PlayerListener implements Listener
{
    // This is for all the stuff Tyler doesn't want in handler lel
    
    @EventHandler
    public void onVelocity(PlayerVelocityEvent event)
    {
        event.setCancelled(true);
        event.getPlayer().sendMessage(FOPM_Messages.MOD_TAG + "Knockback is currently disabled because of crash issues.");
    }
}
