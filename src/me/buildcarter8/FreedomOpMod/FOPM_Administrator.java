package me.buildcarter8.FreedomOpMod;

import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.configuration.ConfigurationSection;

public class FOPM_Administrator
{
    private final String name;
    private final String loginMessage;
    private final boolean isSeniorAdmin;
    private final List<String> consoleAliases;
    private final List<String> ips;
    private Date lastLogin;
    private boolean isActivated;

    public FOPM_Administrator(String name, List<String> ips, Date lastLogin, String loginMessage, boolean isSeniorAdmin, List<String> consoleAliases, boolean isActivated)
    {
        this.name = name.toLowerCase();
        this.ips = ips;
        this.lastLogin = lastLogin;
        this.loginMessage = loginMessage;
        this.isSeniorAdmin = isSeniorAdmin;
        this.consoleAliases = consoleAliases;
        this.isActivated = isActivated;
    }

    public FOPM_Administrator(String name, ConfigurationSection section)
    {
        this.name = name.toLowerCase();
        this.ips = section.getStringList("ips");
        this.lastLogin = FOPM_Util.stringToDate(section.getString("last_login", FOPM_Util.dateToString(new Date(0L))));
        this.loginMessage = section.getString("custom_login_message", "");
        this.isSeniorAdmin = section.getBoolean("is_senior_admin", false);
        this.consoleAliases = section.getStringList("console_aliases");
        this.isActivated = section.getBoolean("is_activated", true);
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();

        try
        {
            output.append("Name: ").append(this.name).append("\n");
            output.append("- IPs: ").append(StringUtils.join(this.ips, ", ")).append("\n");
            output.append("- Last Login: ").append(FOPM_Util.dateToString(this.lastLogin)).append("\n");
            output.append("- Custom Login Message: ").append(this.loginMessage).append("\n");
            output.append("- Is Senior Admin: ").append(this.isSeniorAdmin).append("\n");
            output.append("- Console Aliases: ").append(StringUtils.join(this.consoleAliases, ", ")).append("\n");
            output.append("- Is Activated: ").append(this.isActivated);
        }
        catch (Exception ex)
        {
            FOPM_PluginLog.severe(ex);
        }

        return output.toString();
    }

    public String getName()
    {
        return name;
    }

    public List<String> getIps()
    {
        return ips;
    }

    public Date getLastLogin()
    {
        return lastLogin;
    }

    public String getCustomLoginMessage()
    {
        return loginMessage;
    }

    public boolean isSeniorAdmin()
    {
        return isSeniorAdmin;
    }

    public List<String> getConsoleAliases()
    {
        return consoleAliases;
    }

    public void setLastLogin(Date lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public boolean isActivated()
    {
        return isActivated;
    }

    public void setActivated(boolean isActivated)
    {
        this.isActivated = isActivated;
    }
}