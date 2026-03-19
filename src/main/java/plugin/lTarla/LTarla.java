package plugin.ltarla;

import org.bukkit.plugin.java.JavaPlugin;
import plugin.ltarla.commands.LTarlaCommand;
import plugin.ltarla.listeners.WandListener;
import plugin.ltarla.listeners.WheatListener;
import plugin.ltarla.managers.ConfigManager;
import plugin.ltarla.managers.EventManager;
import plugin.ltarla.managers.ZoneManager;

public final class LTarla extends JavaPlugin {

    private static LTarla instance;
    private ConfigManager configManager;
    private ZoneManager zoneManager;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;


        this.configManager = new ConfigManager(this);
        this.configManager.loadConfig();

        this.zoneManager = new ZoneManager(this);
        this.zoneManager.loadZones();

        this.eventManager = new EventManager(this);
        if (configManager.isEventActive()) {
            this.eventManager.startEventTask();
        }


        getCommand("ltarla").setExecutor(new LTarlaCommand(this));
        getCommand("ltarla").setTabCompleter(new LTarlaCommand(this));


        getServer().getPluginManager().registerEvents(new WandListener(this), this);
        getServer().getPluginManager().registerEvents(new WheatListener(this), this);

        getLogger().info("LTarla eklentisi basariyla aktif edildi!");
    }

    @Override
    public void onDisable() {
        if (eventManager != null) {
            eventManager.stopEventTask();
        }
        if (zoneManager != null) {
            zoneManager.saveAllZones();
        }
        getLogger().info("LTarla eklentisi devre disi birakildi.");
    }

    public static LTarla getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public ZoneManager getZoneManager() {
        return zoneManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}
