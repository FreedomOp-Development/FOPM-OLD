package me.buildcarter8.FreedomOpMod;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

public class FOPM_SuperadminList 
{


	  private static Map<String, FOPM_Superadmin> superadminList = new HashMap<String, FOPM_Superadmin>();
	    private static List<String> superadminNames = new ArrayList<String>();
	    private static List<String> superadminIPs = new ArrayList<String>();
	    private static List<String> seniorAdminNames = new ArrayList<String>();
	    private static int clean_threshold_hours = 24 * 7; // 1 Week
	    public final static Logger log = Logger.getLogger("");

	    private FOPM_SuperadminList()
	    {
	        throw new AssertionError();
	    }

	    public static List<String> getSuperadminIPs()
	    {
	        return superadminIPs;
	    }

	    public static List<String> getSuperadminNames()
	    {
	        return superadminNames;
	    }

	    public static void loadSuperadminList()
	    {
	        try
	        {
	            superadminList.clear();

	            FOPM_Util.createDefaultConfiguration(Main.SUPERADMIN_FILE, Main.plugin_file);
	            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.plugin.getDataFolder(), Main.SUPERADMIN_FILE));

	            clean_threshold_hours = config.getInt("clean_threshold_hours", clean_threshold_hours);

	            if (config.isConfigurationSection("superadmins"))
	            {
	                ConfigurationSection section = config.getConfigurationSection("superadmins");

	                for (String admin_name : section.getKeys(false))
	                {
	                    FOPM_Superadmin superadmin = new FOPM_Superadmin(admin_name, section.getConfigurationSection(admin_name));
	                    superadminList.put(admin_name.toLowerCase(), superadmin);
	                }
	            }
	            else
	            {
	                log.warning("Missing superadmins section in superadmin.yml.");
	            }

	            updateIndexLists();
	        }
	        catch (Exception ex)
	        {
	            log.severe(ex.toString());
	        }
	    }

	    public static void backupSavedList()
	    {
	        File a = new File(Main.plugin.getDataFolder(), Main.SUPERADMIN_FILE);
	        File b = new File(Main.plugin.getDataFolder(), Main.SUPERADMIN_FILE + ".bak");
	        FileUtil.copy(a, b);
	    }

	    public static void updateIndexLists()
	    {
	        superadminNames.clear();
	        superadminIPs.clear();

	        Iterator<Entry<String, FOPM_Superadmin>> it = superadminList.entrySet().iterator();
	        while (it.hasNext())
	        {
	            Entry<String, FOPM_Superadmin> pair = it.next();

	            String admin_name = pair.getKey().toLowerCase();
	            FOPM_Superadmin superadmin = pair.getValue();

	            if (superadmin.isActivated())
	            {
	                superadminNames.add(admin_name);

	                for (String ip : superadmin.getIps())
	                {
	                    superadminIPs.add(ip);
	                }

	                if (superadmin.isSeniorAdmin())
	                {
	                    seniorAdminNames.add(admin_name);

	                    for (String console_alias : superadmin.getConsoleAliases())
	                    {
	                        seniorAdminNames.add(console_alias.toLowerCase());
	                    }
	                }
	            }
	        }
	    }

	    public static void saveSuperadminList()
	    {
	        try
	        {
	            updateIndexLists();

	            YamlConfiguration config = new YamlConfiguration();

	            config.set("clean_threshold_hours", clean_threshold_hours);

	            Iterator<Entry<String, FOPM_Superadmin>> it = superadminList.entrySet().iterator();
	            while (it.hasNext())
	            {
	                Entry<String, FOPM_Superadmin> pair = it.next();

	                String admin_name = pair.getKey().toLowerCase();
	                FOPM_Superadmin superadmin = pair.getValue();

	                config.set("superadmins." + admin_name + ".ips", superadmin.getIps());
	                config.set("superadmins." + admin_name + ".last_login", FOPM_Util.dateToString(superadmin.getLastLogin()));
	                config.set("superadmins." + admin_name + ".custom_login_message", superadmin.getCustomLoginMessage());
	                config.set("superadmins." + admin_name + ".is_senior_admin", superadmin.isSeniorAdmin());
	                config.set("superadmins." + admin_name + ".console_aliases", superadmin.getConsoleAliases());
	                config.set("superadmins." + admin_name + ".is_activated", superadmin.isActivated());
	            }

	            config.save(new File(Main.plugin.getDataFolder(), Main.SUPERADMIN_FILE));
	        }
	        catch (Exception ex)
	        {
	            log.severe(ex.toString());
	        }
	    }

	    public static FOPM_Superadmin getAdminEntry(String admin_name)
	    {
	        admin_name = admin_name.toLowerCase();

	        if (superadminList.containsKey(admin_name))
	        {
	            return superadminList.get(admin_name);
	        }
	        else
	        {
	            return null;
	        }
	    }

	    public static FOPM_Superadmin getAdminEntry(Player p)
	    {
	        return getAdminEntry(p.getName().toLowerCase());
	    }

	    public static FOPM_Superadmin getAdminEntryByIP(String ip)
	    {
	        Iterator<Entry<String, FOPM_Superadmin>> it = superadminList.entrySet().iterator();
	        while (it.hasNext())
	        {
	            Entry<String, FOPM_Superadmin> pair = it.next();
	            FOPM_Superadmin superadmin = pair.getValue();
	            if (superadmin.getIps().contains(ip))
	            {
	                return superadmin;
	            }
	        }
	        return null;
	    }

	    public static void updateLastLogin(Player p)
	    {
	        FOPM_Superadmin admin_entry = getAdminEntry(p);
	        if (admin_entry != null)
	        {
	            admin_entry.setLastLogin(new Date());
	            saveSuperadminList();
	        }
	    }

	    public static boolean isSeniorAdmin(CommandSender user)
	    {
	        String user_name = user.getName().toLowerCase();

	        if (!(user instanceof Player))
	        {
	            return seniorAdminNames.contains(user_name);
	        }

	        FOPM_Superadmin admin_entry = getAdminEntry((Player) user);
	        if (admin_entry != null)
	        {
	            return admin_entry.isSeniorAdmin();
	        }

	        return false;
	    }

	    public static boolean isUserSuperadmin(CommandSender user)
	    {
	        if (!(user instanceof Player))
	        {
	            return true;
	        }

	        if (Bukkit.getOnlineMode())
	        {
	            if (superadminNames.contains(user.getName().toLowerCase()))
	            {
	                return true;
	            }
	        }

	        try
	        {
	            String user_ip = ((Player) user).getAddress().getAddress().getHostAddress();
	            if (user_ip != null && !user_ip.isEmpty())
	            {
	                if (superadminIPs.contains(user_ip))
	                {
	                    return true;
	                }
	            }
	        }
	        catch (Exception ex)
	        {
	            return false;
	        }

	        return false;
	    }

	    public static boolean checkPartialSuperadminIP(String user_ip)
	    {
	        try
	        {
	            user_ip = user_ip.trim();

	            if (superadminIPs.contains(user_ip))
	            {
	                return true;
	            }
	            else
	            {
	                String[] user_octets = user_ip.split("\\.");
	                if (user_octets.length != 4)
	                {
	                    return false;
	                }

	                String match_ip = null;
	                for (String test_ip : getSuperadminIPs())
	                {
	                    String[] test_octets = test_ip.split("\\.");
	                    if (test_octets.length == 4)
	                    {
	                        if (user_octets[0].equals(test_octets[0]) && user_octets[1].equals(test_octets[1]) && user_octets[2].equals(test_octets[2]))
	                        {
	                            match_ip = test_ip;
	                            break;
	                        }
	                    }
	                }

	                if (match_ip != null)
	                {
	                    FOPM_Superadmin admin_entry = getAdminEntryByIP(match_ip);

	                    if (admin_entry != null)
	                    {
	                        List<String> ips = admin_entry.getIps();
	                        ips.add(user_ip);
	                        admin_entry.setIps(ips);
	                        saveSuperadminList();
	                    }

	                    return true;
	                }
	            }
	        }
	        catch (Exception ex)
	        {
	            log.severe(ex.toString());
	        }

	        return false;
	    }

	    public static boolean isSuperadminImpostor(CommandSender user)
	    {
	        if (!(user instanceof Player))
	        {
	            return false;
	        }

	        Player p = (Player) user;

	        if (superadminNames.contains(p.getName().toLowerCase()))
	        {
	            return !isUserSuperadmin(p);
	        }

	        return false;
	    }

	    public static void addSuperadmin(String admin_name, List<String> ips)
	    {
	        admin_name = admin_name.toLowerCase();

	        if (superadminList.containsKey(admin_name))
	        {
	            FOPM_Superadmin superadmin = superadminList.get(admin_name);
	            superadmin.setActivated(true);
	        }
	        else
	        {
	            Date last_login = new Date();
	            String custom_login_message = "";
	            boolean is_senior_admin = false;
	            List<String> console_aliases = new ArrayList<String>();

	            FOPM_Superadmin superadmin = new FOPM_Superadmin(admin_name, ips, last_login, custom_login_message, is_senior_admin, console_aliases, true);
	            superadminList.put(admin_name.toLowerCase(), superadmin);
	        }

	        saveSuperadminList();
	    }

	    public static void addSuperadmin(Player p)
	    {
	        String admin_name = p.getName().toLowerCase();
	        List<String> ips = Arrays.asList(p.getAddress().getAddress().getHostAddress());

	        addSuperadmin(admin_name, ips);
	    }

	    public static void addSuperadmin(String admin_name)
	    {
	        addSuperadmin(admin_name, new ArrayList<String>());
	    }

	    public static void removeSuperadmin(String admin_name)
	    {
	        admin_name = admin_name.toLowerCase();

	        if (superadminList.containsKey(admin_name))
	        {
	            FOPM_Superadmin superadmin = superadminList.get(admin_name);
	            superadmin.setActivated(false);
	            saveSuperadminList();
	        }
	    }

	    public static void removeSuperadmin(Player p)
	    {
	        removeSuperadmin(p.getName());
	    }

	    public static void cleanSuperadminList(boolean verbose)
	    {
	        try
	        {
	            Iterator<Entry<String, FOPM_Superadmin>> it = superadminList.entrySet().iterator();
	            while (it.hasNext())
	            {
	                Entry<String, FOPM_Superadmin> pair = it.next();
	                FOPM_Superadmin superadmin = pair.getValue();
	                if (superadmin.isActivated() && !superadmin.isSeniorAdmin())
	                {
	                    Date last_login = superadmin.getLastLogin();

	                    long hours_since_login = TimeUnit.HOURS.convert(new Date().getTime() - last_login.getTime(), TimeUnit.MILLISECONDS);

	                    if (hours_since_login > clean_threshold_hours)
	                    {
	                        if (verbose)
	                        {
	                            FOPM_Util.adminAction("FOPM", "Deactivating superadmin \"" + superadmin.getName() + "\", inactive for " + hours_since_login + " hours.", true);
	                        }

	                        superadmin.setActivated(false);
	                    }
	                }
	            }
	            saveSuperadminList();
	        }
	        catch (Exception ex)
	        {
	            log.severe(ex.toString());
	        }
}
}