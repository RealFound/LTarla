package plugin.ltarla.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugin.ltarla.LTarla;
import plugin.ltarla.listeners.WandListener;
import plugin.ltarla.models.Zone;

import java.util.ArrayList;
import java.util.List;

public class LTarlaCommand implements CommandExecutor, TabCompleter {

    private final LTarla plugin;

    public LTarlaCommand(LTarla plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("ltarla.admin")) {
            sender.sendMessage(ChatColor.RED + "Bunun icin yetkiniz yok.");
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        if (args[0].equalsIgnoreCase("wand")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Bu komut sadece oyuncular icindir.");
                return true;
            }
            Player player = (Player) sender;
            ItemStack wand = new ItemStack(Material.DIAMOND_AXE);
            ItemMeta meta = wand.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "LTarla Asasi");
            meta.setLore(List.of(ChatColor.GRAY + "Sol tik: 1. Nokta", ChatColor.GRAY + "Sag tik: 2. Nokta"));
            wand.setItemMeta(meta);
            player.getInventory().addItem(wand);
            player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.GREEN + "LTarla asasi verildi.");
            return true;
        }

        if (args[0].equalsIgnoreCase("create")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Bu komut sadece oyuncular icindir.");
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 2) {
                player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.RED + "Kullanim: /ltarla create <tarla_ismi>");
                return true;
            }
            String zoneName = args[1];
            
            Location[] sel = WandListener.selections.get(player.getUniqueId());
            if (sel == null || sel[0] == null || sel[1] == null) {
                player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.RED + "Once ltarla asasi ile iki nokta secmelisiniz.");
                return true;
            }
            
            if (!sel[0].getWorld().getName().equals(sel[1].getWorld().getName())) {
                player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.RED + "Iki nokta ayni dunyada olmalidir.");
                return true;
            }

            if (plugin.getZoneManager().zoneExists(zoneName)) {
                player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.RED + "Bu isimde bir tarla zaten var.");
                return true;
            }

            Zone newZone = new Zone(
                    zoneName,
                    sel[0].getWorld().getName(),
                    sel[0].getBlockX(), sel[0].getBlockY(), sel[0].getBlockZ(),
                    sel[1].getBlockX(), sel[1].getBlockY(), sel[1].getBlockZ(),
                    60, 
                    new ArrayList<>()
            );
            
            plugin.getZoneManager().saveZone(newZone);
            player.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.GREEN + "Tarla basariyla olusturuldu! Ayarlarini zones klasorunden yapabilirsiniz.");
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.getConfigManager().reloadConfig();
            plugin.getZoneManager().loadZones();
            sender.sendMessage(plugin.getConfigManager().getPrefix() + ChatColor.GREEN + "LTarla ayarlari ve tarlalar yenilendi.");
            return true;
        }

        sendHelp(sender);
        return true;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "--- LTarla ---");
        sender.sendMessage(ChatColor.YELLOW + "/ltarla wand" + ChatColor.GRAY + " - Tarla secim asasi alir.");
        sender.sendMessage(ChatColor.YELLOW + "/ltarla create <isim>" + ChatColor.GRAY + " - Secili alani tarla yapar.");
        sender.sendMessage(ChatColor.YELLOW + "/ltarla reload" + ChatColor.GRAY + " - Ayarlari yeniler.");
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("ltarla.admin")) {
                completions.add("wand");
                completions.add("create");
                completions.add("reload");
            }
        }
        return completions;
    }
}
