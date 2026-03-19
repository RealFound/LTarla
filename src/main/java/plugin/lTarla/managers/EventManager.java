package plugin.ltarla.managers;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import plugin.ltarla.LTarla;

import java.util.Random;

public class EventManager {

    private final LTarla plugin;
    private final Random random;
    private boolean isEventActiveNow;
    private BukkitRunnable eventTask;

    public EventManager(LTarla plugin) {
        this.plugin = plugin;
        this.random = new Random();
        this.isEventActiveNow = false;
    }

    public void startEventTask() {
        if (eventTask != null) return;
        
        eventTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isEventActiveNow && random.nextInt(100) < 10) {
                    startEvent();
                }
            }
        };
        eventTask.runTaskTimer(plugin, 20L * 60 * 30, 20L * 60 * 30);
    }

    public void stopEventTask() {
        if (eventTask != null) {
            eventTask.cancel();
            eventTask = null;
        }
    }

    public void startEvent() {
        isEventActiveNow = true;
        
        String[] startMsg = plugin.getConfigManager().getEventStartMessage();
        String title = startMsg.length > 0 ? org.bukkit.ChatColor.translateAlternateColorCodes('&', startMsg[0]) : "";
        String subTitle = startMsg.length > 1 ? org.bukkit.ChatColor.translateAlternateColorCodes('&', startMsg[1]) : "";
        
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(title, subTitle, 10, 70, 20);
            p.sendMessage(plugin.getConfigManager().getPrefix() + title + " " + subTitle);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                endEvent();
            }
        }.runTaskLater(plugin, 20L * plugin.getConfigManager().getEventDuration());
    }

    public void endEvent() {
        isEventActiveNow = false;
        
        String[] endMsg = plugin.getConfigManager().getEventEndMessage();
        String title = endMsg.length > 0 ? org.bukkit.ChatColor.translateAlternateColorCodes('&', endMsg[0]) : "";
        String subTitle = endMsg.length > 1 ? org.bukkit.ChatColor.translateAlternateColorCodes('&', endMsg[1]) : "";
        
        for (org.bukkit.entity.Player p : Bukkit.getOnlinePlayers()) {
            p.sendTitle(title, subTitle, 10, 70, 20);
            p.sendMessage(plugin.getConfigManager().getPrefix() + title + " " + subTitle);
        }
    }

    public boolean isEventActiveNow() {
        return isEventActiveNow;
    }
}
