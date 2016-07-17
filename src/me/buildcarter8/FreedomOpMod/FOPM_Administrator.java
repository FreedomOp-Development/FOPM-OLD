package me.buildcarter8.FreedomOpMod;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

// The admin list, done correctly the first time.
public class FOPM_Administrator
{
    private static Main plugin;
    private Map<UUID, List<UUID>> players;

    public FOPM_Administrator(Main plugin)
    {
        FOPM_Administrator.plugin = plugin;
        this.players = new HashMap<UUID, List<UUID>>();
    }

    public static String getRank(Player player)
    {
        if (Main.DEVELOPERS.contains(player.getName()))
        {
            return ChatColor.DARK_PURPLE + "Developer";
        }
        else if (getInstance().isUserAdmin(player))
        {
            return ChatColor.BLUE + "Super Admin";
        }
        else
        {
            return null;
        }
    }
    
    public void addAdmin(CommandSender sender)
    {
        Player sender_p = (Player) sender;
        UUID uuid = sender_p.getUniqueId();
        List<UUID> admins = players.get(uuid);
        
        admins.add(uuid);
    }

    public boolean isUserAdmin(CommandSender sender)
    {
        Player sender_p = (Player) sender;
        UUID uuid = sender_p.getUniqueId();

        List<UUID> admins = players.get(uuid);

        if(sender.getName() == Bukkit.getConsoleSender().getName())
        {
            return true;
        }
        
        if(admins.isEmpty())
        {
            return false;
        }
        else if (admins.contains(uuid))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    public void saveAll()
    {
        try {
            saveAdmins(players);
        }
        catch(Exception ex) {
            FOPM_PluginLog.severe("Failed to save the admin list!" + ex);
        }
    }
    
    public void saveAdmins(Map<UUID, List<UUID>> map)
    {
        try
        {
            File fileTwo = new File(plugin.getDataFolder(), File.separator + "admins.yml");
            FileOutputStream fos = new FileOutputStream(fileTwo);
            PrintWriter pw = new PrintWriter(fos);

            for (Map.Entry<UUID, List<UUID>> m : map.entrySet())
            {
                pw.println(m.getKey() + "=" + m.getValue());
            }

            pw.flush();
            pw.close();
            fos.close();
        }
        catch (Exception e)
        {
            FOPM_PluginLog.severe(e);
        }
    }

    public void loadAdmins()
    {
        try
        {
            File toRead = new File(plugin.getDataFolder(), File.separator + "admins.yml");
            if (!toRead.exists()) {
                toRead.createNewFile();
            }
            
            FileInputStream fis = new FileInputStream(toRead);

            Scanner sc = new Scanner(fis);
            Map<UUID, List<UUID>> mapInFile = new HashMap<UUID, List<UUID>>();

            // read data from file line by line:
            String currentLine;
            while (sc.hasNextLine())
            {
                currentLine = sc.nextLine();
                // now tokenize the currentLine:
                StringTokenizer st = new StringTokenizer(currentLine, "=", false);
                // put tokens ot currentLine in map
                UUID uuid = UUID.fromString(st.nextToken());
                List<UUID> list = Arrays.asList(UUID.fromString(st.nextToken()));
                mapInFile.put(uuid, list);
            }
            fis.close();

            // print All data in MAP
            for (Map.Entry<UUID, List<UUID>> m : mapInFile.entrySet())
            {
                players.clear();
                players.put(m.getKey(), m.getValue());
            }
        }
        catch (Exception e)
        {
            FOPM_PluginLog.severe(e);
        }
    }

    public static FOPM_Administrator getInstance()
    {
        return FOPM_AdministratorHolder.INSTANCE;
    }

    private static class FOPM_AdministratorHolder
    {
        private static final FOPM_Administrator INSTANCE = new FOPM_Administrator(plugin);
    }
}