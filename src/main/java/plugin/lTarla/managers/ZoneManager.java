package plugin.ltarla.managers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import plugin.ltarla.LTarla;
import plugin.ltarla.models.CustomDrop;
import plugin.ltarla.models.Zone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZoneManager {

    private final LTarla plugin;
    private final Map<String, Zone> zones;
    private final File zonesFolder;

    public ZoneManager(LTarla plugin) {
        this.plugin = plugin;
        this.zones = new HashMap<>();
        this.zonesFolder = new File(plugin.getDataFolder(), "zones");
        if (!zonesFolder.exists()) {
            zonesFolder.mkdirs();
        }
    }

    public void loadZones() {
        zones.clear();
        File[] files = zonesFolder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            String name = file.getName().replace(".yml", "");
            
            try {
                String worldName = config.getString("world");
                double minX = config.getDouble("minX");
                double minY = config.getDouble("minY");
                double minZ = config.getDouble("minZ");
                double maxX = config.getDouble("maxX");
                double maxY = config.getDouble("maxY");
                double maxZ = config.getDouble("maxZ");
                int respawnTime = config.getInt("respawn-time", 60);

                List<CustomDrop> customDrops = new ArrayList<>();
                ConfigurationSection dropsSection = config.getConfigurationSection("drops");
                if (dropsSection != null) {
                    for (String key : dropsSection.getKeys(false)) {
                        ConfigurationSection dropConfig = dropsSection.getConfigurationSection(key);
                        if (dropConfig != null) {
                            double chance = dropConfig.getDouble("chance");
                            boolean isCommand = dropConfig.getBoolean("is-command", false);
                            List<String> commands = dropConfig.getStringList("commands");
                            String material = dropConfig.getString("item.material", "STONE");
                            String itemName = dropConfig.getString("item.name", "");
                            List<String> lore = dropConfig.getStringList("item.lore");

                            customDrops.add(new CustomDrop(chance, isCommand, commands, material, itemName, lore));
                        }
                    }
                }

                Zone zone = new Zone(name, worldName, minX, minY, minZ, maxX, maxY, maxZ, respawnTime, customDrops);
                zones.put(name.toLowerCase(), zone);
                
            } catch (Exception e) {
                plugin.getLogger().warning("Tarla dosyasi yuklenemedi: " + file.getName());
                e.printStackTrace();
            }
        }
        plugin.getLogger().info(zones.size() + " adet tarla yuklendi.");
    }

    public void saveZone(Zone zone) {
        File file = new File(zonesFolder, zone.getName() + ".yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        config.set("world", zone.getWorldName());
        config.set("minX", zone.getMinX());
        config.set("minY", zone.getMinY());
        config.set("minZ", zone.getMinZ());
        config.set("maxX", zone.getMaxX());
        config.set("maxY", zone.getMaxY());
        config.set("maxZ", zone.getMaxZ());
        config.set("respawn-time", zone.getRespawnTimeSeconds());

        // Varsayılan drop örneklerini sadece baştan yaratırken koyalım, sonradan silerse tekrar eklemesin
        if (!file.exists()) {
            config.set("drops.example_money.chance", 1.0);
            config.set("drops.example_money.is-command", true);
            config.set("drops.example_money.commands", java.util.Arrays.asList("eco give %player% 100", "msg %player% &a100 TL kazandin!"));

            config.set("drops.example_item.chance", 2.0);
            config.set("drops.example_item.is-command", false);
            config.set("drops.example_item.item.material", "GOLD_NUGGET");
            config.set("drops.example_item.item.name", "&6&lTarla Coin");
            config.set("drops.example_item.item.lore", java.util.Arrays.asList("&7Ozel tarladan dusen", "&7degerli bir para birimi."));
        }

        try {
            config.save(file);
            zones.put(zone.getName().toLowerCase(), zone);
        } catch (IOException e) {
            plugin.getLogger().severe("Tarla kaydedilemedi: " + zone.getName());
            e.printStackTrace();
        }
    }

    public void saveAllZones() {
        for (Zone zone : zones.values()) {
            saveZone(zone);
        }
    }

    public Zone getZone(String name) {
        return zones.get(name.toLowerCase());
    }

    public Zone getZoneAt(org.bukkit.Location location) {
        for (Zone zone : zones.values()) {
            if (zone.contains(location)) {
                return zone;
            }
        }
        return null;
    }

    public boolean zoneExists(String name) {
        return zones.containsKey(name.toLowerCase());
    }

    public Map<String, Zone> getZones() {
        return zones;
    }
}
