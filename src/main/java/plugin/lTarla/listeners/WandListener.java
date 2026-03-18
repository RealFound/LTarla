package plugin.ltarla.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import plugin.ltarla.LTarla;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WandListener implements Listener {

    private final LTarla plugin;
    public static final Map<UUID, Location[]> selections = new HashMap<>();

    public WandListener(LTarla plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPermission("ltarla.admin")) return;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() != Material.DIAMOND_AXE || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName()) return;
        if (!item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + "LTarla Asasi")) return;

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            Location loc = event.getClickedBlock().getLocation();
            Location[] sel = selections.getOrDefault(player.getUniqueId(), new Location[2]);
            sel[0] = loc;
            selections.put(player.getUniqueId(), sel);
            player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.GREEN + "Birinci nokta secildi: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            Location loc = event.getClickedBlock().getLocation();
            Location[] sel = selections.getOrDefault(player.getUniqueId(), new Location[2]);
            sel[1] = loc;
            selections.put(player.getUniqueId(), sel);
            player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.GREEN + "Ikinci nokta secildi: " + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ());
        }
    }
}
