package plugin.ltarla.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import plugin.ltarla.LTarla;

public class ConfigManager {
    private final LTarla plugin;
    private FileConfiguration config;

    public ConfigManager(LTarla plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public boolean isEventActive() {
        return config.getBoolean("etkinlik_aktif", true);
    }

    public double getEventMultiplier() {
        return config.getDouble("etkinlik_carpan", 2.0);
    }

    public int getEventDuration() {
        return config.getInt("etkinlik_suresi", 600);
    }

    public String getPrefix() {
        return ChatColor.translateAlternateColorCodes('&', config.getString("prefix", "&8[&eLTarla&8] "));
    }

    public String[] getEventStartMessage() {
        String msg = config.getString("etkinlik_basladi_mesaj", "&e&lTARLA ETKINLIGI BASLADI!|&aTum tarlalarda esya dusme sansi &c2 Katina &acikti!");
        return msg.split("\\|");
    }

    public String[] getEventEndMessage() {
        String msg = config.getString("etkinlik_bitti_mesaj", "&c&lTARLA ETKINLIGI BITTI!|&eSanslar normale dondu.");
        return msg.split("\\|");
    }
}
