package plugin.ltarla.models;

import org.bukkit.Location;
import java.util.List;

public class Zone {
    private final String name;
    private final String worldName;
    private final double minX, minY, minZ;
    private final double maxX, maxY, maxZ;
    private final int respawnTimeSeconds;
    private final List<CustomDrop> customDrops;

    public Zone(String name, String worldName, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, int respawnTimeSeconds, List<CustomDrop> customDrops) {
        this.name = name;
        this.worldName = worldName;
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
        this.respawnTimeSeconds = respawnTimeSeconds;
        this.customDrops = customDrops;
    }

    public boolean contains(Location loc) {
        if (loc.getWorld() == null || !loc.getWorld().getName().equals(worldName)) {
            return false;
        }
        double x = loc.getBlockX();
        double y = loc.getBlockY();
        double z = loc.getBlockZ();
        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public String getName() { return name; }
    public String getWorldName() { return worldName; }
    public double getMinX() { return minX; }
    public double getMinY() { return minY; }
    public double getMinZ() { return minZ; }
    public double getMaxX() { return maxX; }
    public double getMaxY() { return maxY; }
    public double getMaxZ() { return maxZ; }
    public int getRespawnTimeSeconds() { return respawnTimeSeconds; }
    public List<CustomDrop> getCustomDrops() { return customDrops; }
}
