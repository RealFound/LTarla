package plugin.ltarla.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import plugin.ltarla.LTarla;
import plugin.ltarla.models.CustomDrop;
import plugin.ltarla.models.Zone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WheatListener implements Listener {

    private final LTarla plugin;
    private final Random random;

    public WheatListener(LTarla plugin) {
        this.plugin = plugin;
        this.random = new Random();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        
        // Sadece bugdaylari kontrol et
        if (block.getType() != Material.WHEAT) return;
        
        Player player = event.getPlayer();
        Zone zone = plugin.getZoneManager().getZoneAt(block.getLocation());
        
        // Bir tarla icinde degilse karisma
        if (zone == null) return;
        
        BlockData data = block.getBlockData();
        if (data instanceof Ageable) {
            Ageable ageable = (Ageable) data;
            // Fully grown degilse kirmasina izin verme
            if (ageable.getAge() != ageable.getMaximumAge()) {
                event.setCancelled(true);
                return;
            }
        }

        // Event'i iptal et (normal droplari engelle ve blogu baska biri kirmasin diye kendimiz silelim)
        event.setCancelled(true);
        
        // Odulleri ver (Sansa bagli)
        giveRewards(player, zone);
        
        // Blogu hava yap
        block.setType(Material.AIR);
        Location loc = block.getLocation();
        
        // Particle & ses efekti (Kırma)
        loc.getWorld().playSound(loc, Sound.BLOCK_CROP_BREAK, 1.0f, 1.0f);
        loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc.clone().add(0.5, 0.5, 0.5), 15, 0.3, 0.3, 0.3, Bukkit.createBlockData(Material.WHEAT));

        // Kırıldıktan sonra bekleme süresince hava (AIR) olarak kalacak.
        loc.getBlock().setType(Material.AIR);

        // Toplam bekleme suresi (Tick cinsinden)
        long respawnTicks = zone.getRespawnTimeSeconds() * 20L;

        new org.bukkit.scheduler.BukkitRunnable() {
            int ticksElapsed = 0;
            
            @Override
            public void run() {
                Block b = loc.getBlock();
                // Eger baska bir blok koyulmussa engellememek icin iptal et
                if (b.getType() != Material.AIR) {
                    this.cancel();
                    return;
                }
                
                ticksElapsed += 10;
                
                // Gonderdiginiz fotograftaki Mor Cadi (Witch Magic) yerine gercekci yesil (Koylu takas) parcaciklarini bekleme suresince cikar
                double offsetX = (random.nextDouble() - 0.5) * 0.6;
                double offsetZ = (random.nextDouble() - 0.5) * 0.6;
                double offsetY = random.nextDouble() * 0.4 + 0.1; // Topragin hafif ustunden (gercekci)
                
                // VILLAGER_HAPPY (Yesil yaprak/yildiz) efekti
                loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0.5 + offsetX, offsetY, 0.5 + offsetZ), 1, 0, 0, 0, 0);

                if (ticksElapsed >= respawnTicks) {
                    // Sure doldugunda tek seferde Age 7 (Tam buyumus) yap
                    b.setType(Material.WHEAT);
                    BlockData bData = b.getBlockData();
                    if (bData instanceof Ageable) {
                        Ageable bAge = (Ageable) bData;
                        bAge.setAge(bAge.getMaximumAge());
                        b.setBlockData(bAge);
                        
                        // Tamamlandigini belirten buyuk patlama (cok sayida yesil efekt)
                        loc.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.clone().add(0.5, 0.4, 0.5), 20, 0.4, 0.4, 0.4, 0);
                        loc.getWorld().playSound(loc, Sound.BLOCK_CROP_BREAK, 1.0f, 1.2f);
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 10L, 10L);
    }

    private void giveRewards(Player player, Zone zone) {
        double multiplier = 1.0;
        if (plugin.getEventManager().isEventActiveNow()) {
            multiplier = plugin.getConfigManager().getEventMultiplier();
        }

        // Standart bugday drobi (Ornek: 1 bugday, 0-2 tohum)
        player.getInventory().addItem(new ItemStack(Material.WHEAT, 1));
        int seeds = random.nextInt(3);
        if (seeds > 0) {
            player.getInventory().addItem(new ItemStack(Material.WHEAT_SEEDS, seeds));
        }

        // Ozel droplar
        for (CustomDrop drop : zone.getCustomDrops()) {
            double effectiveChance = drop.getChance() * multiplier;
            if (random.nextDouble() * 100.0 <= effectiveChance) {
                if (drop.isCommand()) {
                    for (String cmd : drop.getCommands()) {
                        String finalCmd = cmd.replace("%player%", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCmd);
                    }
                } else {
                    Material mat = Material.matchMaterial(drop.getMaterial());
                    if (mat != null) {
                        ItemStack item = new ItemStack(mat);
                        ItemMeta meta = item.getItemMeta();
                        if (meta != null) {
                            if (drop.getName() != null && !drop.getName().isEmpty()) {
                                meta.setDisplayName(org.bukkit.ChatColor.translateAlternateColorCodes('&', drop.getName()));
                            }
                            if (drop.getLore() != null && !drop.getLore().isEmpty()) {
                                List<String> coloredLore = new ArrayList<>();
                                for (String line : drop.getLore()) {
                                    coloredLore.add(org.bukkit.ChatColor.translateAlternateColorCodes('&', line));
                                }
                                meta.setLore(coloredLore);
                            }
                            item.setItemMeta(meta);
                        }
                        player.getInventory().addItem(item);
                    }
                }
            }
        }
    }
}
