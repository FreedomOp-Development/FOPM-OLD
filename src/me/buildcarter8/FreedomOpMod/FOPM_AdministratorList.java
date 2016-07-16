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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

import net.md_5.bungee.api.ChatColor;

public class FOPM_AdministratorList
{
    private static final Map<String, FOPM_Administrator> superadminList = new HashMap<String, FOPM_Administrator>();
    private static List<String> superadminNames = new ArrayList<String>();
    private static List<String> senioradminNames = new ArrayList<String>();
    private static List<String> superadminIPs = new ArrayList<String>();
    private static int cleanThreshold = 24 * 7; // 1 Week in hours

    private FOPM_AdministratorList()
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

    public static List<String> getSenioradminNames()
    {
        return senioradminNames;
    }

    public static void loadSuperadminList()
    {
        try
        {
            superadminList.clear();

            FOPM_Util.createDefaultConfiguration(Main.SUPERADMIN_FILE);
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Main.plugin.getDataFolder(), Main.SUPERADMIN_FILE));

            cleanThreshold = config.getInt("clean_threshold_hours", cleanThreshold);

            if (config.isConfigurationSection("superadmins"))
            {
                ConfigurationSection section = config.getConfigurationSection("superadmins");

                for (String admin_name : section.getKeys(false))
                {
                    FOPM_Administrator superadmin = new FOPM_Administrator(admin_name, section.getConfigurationSection(admin_name));
                    superadminList.put(admin_name.toLowerCase(), superadmin);
                }
            }
            else
            {
                FOPM_PluginLog.warn("Missing superadmins section in superadmin.yml.");
            }

            updateIndexLists();
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe(ex);
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
        senioradminNames.clear();
        superadminIPs.clear();

        Iterator<Entry<String, FOPM_Administrator>> it = superadminList.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, FOPM_Administrator> pair = it.next();

            String name = pair.getKey().toLowerCase();
            FOPM_Administrator superadmin = pair.getValue();

            if (superadmin.isActivated())
            {
                superadminNames.add(name);

                for (String ip : superadmin.getIps())
                {
                    superadminIPs.add(ip);
                }

                if (superadmin.isSeniorAdmin())
                {
                    senioradminNames.add(name);

                    for (String console_alias : superadmin.getConsoleAliases())
                    {
                        senioradminNames.add(console_alias.toLowerCase());
                    }
                }
            }
        }

        superadminNames = FOPM_Util.removeDuplicates(superadminNames);
        senioradminNames = FOPM_Util.removeDuplicates(senioradminNames);
        superadminIPs = FOPM_Util.removeDuplicates(superadminIPs);
    }

    public static void saveSuperadminList()
    {
        try
        {
            updateIndexLists();

            YamlConfiguration config = new YamlConfiguration();

            config.set("clean_threshold_hours", cleanThreshold);

            Iterator<Entry<String, FOPM_Administrator>> it = superadminList.entrySet().iterator();
            while (it.hasNext())
            {
                Entry<String, FOPM_Administrator> pair = it.next();

                String admin_name = pair.getKey().toLowerCase();
                FOPM_Administrator superadmin = pair.getValue();

                config.set("superadmins." + admin_name + ".ips", FOPM_Util.removeDuplicates(superadmin.getIps()));
                config.set("superadmins." + admin_name + ".last_login", FOPM_Util.dateToString(superadmin.getLastLogin()));
                config.set("superadmins." + admin_name + ".custom_login_message", superadmin.getCustomLoginMessage());
                config.set("superadmins." + admin_name + ".is_senior_admin", superadmin.isSeniorAdmin());
                config.set("superadmins." + admin_name + ".console_aliases", FOPM_Util.removeDuplicates(superadmin.getConsoleAliases()));
                config.set("superadmins." + admin_name + ".is_activated", superadmin.isActivated());
            }

            config.save(new File(Main.plugin.getDataFolder(), Main.SUPERADMIN_FILE));
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe(ex);
        }
    }

    public static FOPM_Administrator getAdminEntry(Player player)
    {
        final String name = player.getName().toLowerCase();

        if (Bukkit.getOnlineMode())
        {
            if (superadminList.containsKey(name))
            {
                return superadminList.get(name);
            }
        }

        try
        {
            final String ip = player.getAddress().getAddress().getHostAddress().trim();
            if (ip != null && !ip.isEmpty())
            {
                return getAdminEntryByIP(ip);
            }
        }
        catch (Exception ex)
        {
            return null;
        }
        return null;
    }

    @Deprecated
    public static FOPM_Administrator getAdminEntry(String name)
    {
        name = name.toLowerCase();

        if (superadminList.containsKey(name))
        {
            return superadminList.get(name);
        }
        else
        {
            return null;
        }
    }

    public static FOPM_Administrator getAdminEntryByIP(String ip)
    {
        return getAdminEntryByIP(ip, false);
    }

    public static FOPM_Administrator getAdminEntryByIP(String needleIP, boolean fuzzy)
    {
        Iterator<Entry<String, FOPM_Administrator>> it = superadminList.entrySet().iterator();
        while (it.hasNext())
        {
            Entry<String, FOPM_Administrator> pair = it.next();
            FOPM_Administrator superadmin = pair.getValue();
            if (fuzzy)
            {
                for (String haystackIP : superadmin.getIps())
                {
                    if (FOPM_Util.fuzzyIpMatch(needleIP, haystackIP, 3))
                    {
                        return superadmin;
                    }
                }
            }
            else
            {
                if (superadmin.getIps().contains(needleIP))
                {
                    return superadmin;
                }
            }
        }
        return null;
    }

    public static void updateLastLogin(Player player)
    {
        FOPM_Administrator admin_entry = getAdminEntry(player);
        if (admin_entry != null)
        {
            admin_entry.setLastLogin(new Date());
            saveSuperadminList();
        }
    }

    public static boolean isSeniorAdmin(CommandSender user)
    {
        return isSeniorAdmin(user, false);
    }

    public static boolean isSeniorAdmin(CommandSender user, boolean verifySuperadmin)
    {
        if (verifySuperadmin)
        {
            if (!isUserAdmin(user))
            {
                return false;
            }
        }

        String username = user.getName().toLowerCase();

        if (!(user instanceof Player))
        {
            return senioradminNames.contains(username);
        }

        FOPM_Administrator entry = getAdminEntry((Player) user);
        if (entry != null)
        {
            return entry.isSeniorAdmin();
        }

        return false;
    }

    public static boolean isUserAdmin(CommandSender user)
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
            String ip = ((Player) user).getAddress().getAddress().getHostAddress();
            if (ip != null && !ip.isEmpty())
            {
                if (superadminIPs.contains(ip))
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

    public static boolean checkPartialSuperadminIP(String ip, String name)
    {
        try
        {
            ip = ip.trim();

            if (superadminIPs.contains(ip))
            {
                return true;
            }
            else
            {
                String matchIp = null;
                for (String testIp : getSuperadminIPs())
                {
                    if (FOPM_Util.fuzzyIpMatch(ip, testIp, 3))
                    {
                        matchIp = testIp;
                        break;
                    }
                }

                if (matchIp != null)
                {
                    FOPM_Administrator entry = getAdminEntryByIP(matchIp);

                    if (entry != null)
                    {
                        if (entry.getName().equalsIgnoreCase(name))
                        {
                            List<String> ips = entry.getIps();
                            ips.add(ip);
                            saveSuperadminList();
                        }
                    }

                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe(ex);
        }

        return false;
    }

    public static boolean isSuperadminImpostor(CommandSender user)
    {
        if (!(user instanceof Player))
        {
            return false;
        }

        Player player = (Player) user;

        if (superadminNames.contains(player.getName().toLowerCase()))
        {
            return !isUserAdmin(player);
        }

        return false;
    }

    public static void addSuperadmin(String username, List<String> ips)
    {
        try
        {
            username = username.toLowerCase();

            if (superadminList.containsKey(username))
            {
                FOPM_Administrator superadmin = superadminList.get(username);
                superadmin.setActivated(true);
                superadmin.getIps().addAll(ips);
                superadmin.setLastLogin(new Date());
            }
            else
            {
                FOPM_Administrator superadmin = new FOPM_Administrator(username, ips, new Date(), "", false, new ArrayList<String>(), true);
                superadminList.put(username.toLowerCase(), superadmin);
            }

            saveSuperadminList();
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe(ex);
        }
    }

    public static void addSuperadmin(Player player)
    {
        String username = player.getName().toLowerCase();
        List<String> ips = Arrays.asList(player.getAddress().getAddress().getHostAddress());

        addSuperadmin(username, ips);
    }

    public static void addSuperadmin(String adminName)
    {
        addSuperadmin(adminName, new ArrayList<String>());
    }

    public static void removeSuperadmin(String username)
    {
        try
        {
            username = username.toLowerCase();

            if (superadminList.containsKey(username))
            {
                FOPM_Administrator superadmin = superadminList.get(username);
                superadmin.setActivated(false);
                saveSuperadminList();
            }
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe(ex);
        }
    }

    public static void removeSuperadmin(Player player)
    {
        removeSuperadmin(player.getName());
    }

    public static void cleanSuperadminList(boolean verbose)
    {
        try
        {
            Iterator<Entry<String, FOPM_Administrator>> it = superadminList.entrySet().iterator();
            while (it.hasNext())
            {
                Entry<String, FOPM_Administrator> pair = it.next();
                FOPM_Administrator superadmin = pair.getValue();
                if (superadmin.isActivated() && !superadmin.isSeniorAdmin())
                {
                    Date lastLogin = superadmin.getLastLogin();

                    long lastLoginHours = TimeUnit.HOURS.convert(new Date().getTime() - lastLogin.getTime(), TimeUnit.MILLISECONDS);

                    if (lastLoginHours > cleanThreshold)
                    {
                        if (verbose)
                        {
                            Bukkit.broadcastMessage(ChatColor.RED + "[FOPM] Deactivating superadmin \"" + superadmin.getName() + "\", inactive for " + lastLoginHours + " hours.");
                        }

                        superadmin.setActivated(false);
                    }
                }
            }
            saveSuperadminList();
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe(ex);
        }
    }

    public static boolean verifyIdentity(String username, String ip) throws Exception
    {
        if (Bukkit.getOnlineMode())
        {
            return true;
        }

        FOPM_Administrator entry = getAdminEntry(username);
        if (entry != null)
        {
            return entry.getIps().contains(ip);
        }
        else
        {
            throw new Exception();
        }
    }
}