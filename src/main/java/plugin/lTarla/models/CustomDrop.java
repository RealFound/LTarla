package plugin.ltarla.models;

import java.util.List;

public class CustomDrop {

    private final double chance;
    private final boolean isCommand;
    private final List<String> commands;
    private final String material;
    private final String name;
    private final List<String> lore;

    public CustomDrop(double chance, boolean isCommand, List<String> commands, String material, String name, List<String> lore) {
        this.chance = chance;
        this.isCommand = isCommand;
        this.commands = commands;
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    public double getChance() { return chance; }
    public boolean isCommand() { return isCommand; }
    public List<String> getCommands() { return commands; }
    public String getMaterial() { return material; }
    public String getName() { return name; }
    public List<String> getLore() { return lore; }
}
