package me.buildcarter8.FreedomOpMod;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.List;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
public class FOPM_Util 
{
	 private static final Logger log = Logger.getLogger("Minecraft-Server");
	public static String implodeStringList(String glue, List<String> pieces)
	   {
	       StringBuilder output = new StringBuilder();
	       for (int i = 0; i < pieces.size(); i++)
	       {
	           if (i != 0)
	           {
	               output.append(glue);
	           }
	           output.append(pieces.get(i));
	       }
	       return output.toString();
	   }
	
	   public static void createDefaultConfiguration(String name, Main bf2, File plugin_file)
	    {
	        File actual = new File(bf2.getDataFolder(), name);
	        if (!actual.exists())
	        {
	            log.info("[" + bf2.getDescription().getName() + "]: Installing default configuration file template: " + actual.getPath());
	            InputStream input = null;
	            try
	            {
	                JarFile file = new JarFile(plugin_file);
	                ZipEntry copy = file.getEntry(name);
	                if (copy == null)
	                {
	                    log.severe("[" + bf2.getDescription().getName() + "]: Unable to read default configuration: " + actual.getPath());
	                    return;
	                }
	                input = file.getInputStream(copy);
	            }
	            catch (IOException ioex)
	            {
	                log.severe("[" + bf2.getDescription().getName() + "]: Unable to read default configuration: " + actual.getPath());
	            }
	            if (input != null)
	            {
	                FileOutputStream output = null;

	                try
	                {
	                    bf2.getDataFolder().mkdirs();
	                    output = new FileOutputStream(actual);
	                    byte[] buf = new byte[8192];
	                    int length = 0;
	                    while ((length = input.read(buf)) > 0)
	                    {
	                        output.write(buf, 0, length);
	                    }

	                    log.info("[" + bf2.getDescription().getName() + "]: Default configuration file written: " + actual.getPath());
	                }
	                catch (IOException ioex)
	                {
	                    log.log(Level.SEVERE, "[" + bf2.getDescription().getName() + "]: Unable to write default configuration: " + actual.getPath(), ioex);
	                }
	                finally
	                {
	                    try
	                    {
	                        if (input != null)
	                        {
	                            input.close();
	                        }
	                    }
	                    catch (IOException ioex)
	                    {
	                    }

	                    try
	                    {
	                        if (output != null)
	                        {
	                            output.close();
	                        }
	                    }
	                    catch (IOException ioex)
	                    {
	                    }
	                }
	            }
	        }
	    }


	   
	       public static boolean isUserSuperadmin(CommandSender user, boolean IsUserSuperadmin)
	    {
	        try
	        {
	            if (!(user instanceof Player))
	            {
	                return true;
	            }

	            if (Bukkit.getOnlineMode())
	            {
	                if (Main.superadmins.contains(user.getName().toLowerCase()))
	                {
	                    return true;
	                }
	            }

	            Player p = (Player) user;
	            if (p != null)
	            {
	                InetSocketAddress ip_address_obj = p.getAddress();
	                if (ip_address_obj != null)
	                {
	                    String user_ip = ip_address_obj.getAddress().toString().replaceAll("/", "").trim();
	                    if (user_ip != null && !user_ip.isEmpty())
	                    {
	                        if (Main.superadmin_ips.contains(user_ip))
	                        {
	                            return true;
	                        }
	                    }
	                }
	            }
	        }
	        catch (Exception ex)
	        {
	            log.severe("Exception in FOPM_Util.isUserSuperadmin: " + ex.getMessage());
	        }

	        return false;
	    }

	    public static boolean checkPartialSuperadminIP(String user_ip, Main bf2)
	    {
	        user_ip = user_ip.trim();

	        if (Main.superadmin_ips.contains(user_ip))
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
	            for (String test_ip : Main.superadmin_ips)
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
	                Main.superadmin_ips.add(user_ip);

	                FileConfiguration config = YamlConfiguration.loadConfiguration(new File(bf2.getDataFolder(), Main.SUPERADMIN_FILE));

	                fileloop:
	                for (String user : config.getKeys(false))
	                {
	                    List<String> user_ips = (List<String>) config.getStringList(user);
	                    for (String ip : user_ips)
	                    {
	                        ip = ip.toLowerCase().trim();
	                        if (ip.equals(match_ip))
	                        {
	                            log.info("New IP '" + user_ip + "' matches old IP '" + match_ip + "' via partial match, adding it to superadmin list.");
	                            user_ips.add(user_ip);
	                            config.set(user, user_ips);
	                            break fileloop;
	                        }
	                    }
	                }

	                try
	                {
	                    config.save(new File(bf2.getDataFolder(), Main.SUPERADMIN_FILE));
	                }
	                catch (IOException ex)
	                {
	                    log.log(Level.SEVERE, null, ex);
	                }
	            }

	            return match_ip != null;
	        }
	    }
	    
	        public static boolean deleteFolder(File file)
	    {
	        if (file.exists())
	        {
	            if (file.isDirectory())
	            {
	                for (File f : file.listFiles())
	                {
	                    if (!FOPM_Util.deleteFolder(f))
	                    {
	                        return false;
	                    }
	                }
	            }
	            file.delete();
	            return !file.exists();
	        }
	        else
	        {
	            return false;
	        }
	    }

			public static String dateToString(Date date) {
				// TODO Auto-generated method stub
				return null;
			}

			public static Date stringToDate(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			public static void createDefaultConfiguration(String superadminFile, String plugin_file) {
				// TODO Auto-generated method stub
				
			}

			public static void adminAction(String string, String string2, boolean b) {
				// TODO Auto-generated method stub
				
			}

			 public static String getRank(CommandSender sender)
			    {
			        if (FOPM_SuperadminList.isSuperadminImpostor(sender))
			        {
			            return "an " + ChatColor.YELLOW + ChatColor.UNDERLINE + "impostor" + ChatColor.RESET + ChatColor.AQUA + "!";
			        }

			        FOPM_Superadmin admin_entry = FOPM_SuperadminList.getAdminEntry(sender.getName());

			        if (admin_entry != null)
			        {
			            if (admin_entry.isActivated())
			            {
			                String custom_login_message = admin_entry.getCustomLoginMessage();

			                if (custom_login_message != null)
			                {
			                    if (!custom_login_message.isEmpty())
			                    {
			                        return ChatColor.translateAlternateColorCodes('&', custom_login_message);
			                    }
			                }

			                if (admin_entry.isSeniorAdmin())
			                {
			                    return "a " + ChatColor.GOLD + "senior admin" + ChatColor.AQUA + ".";
			                }
			                else
			                {
			                    return "an " + ChatColor.RED + "admin" + ChatColor.AQUA + ".";
			                }
			            }
			        }
					return null;

			}

			public static void bcastMsg(String string, ChatColor color) {
				// TODO Auto-generated method stub
			}
				 public static String colorise(String string)
				    {
				        return ChatColor.translateAlternateColorCodes('&', string);
				    }
				
			}
