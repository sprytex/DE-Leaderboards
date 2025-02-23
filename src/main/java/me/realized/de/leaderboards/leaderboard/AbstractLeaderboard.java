package me.realized.de.leaderboards.leaderboard;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import me.realized.de.leaderboards.Leaderboards;
import me.realized.de.leaderboards.config.Config;
import me.realized.duels.api.Duels;
import me.realized.duels.api.user.UserManager.TopEntry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class AbstractLeaderboard implements Leaderboard {

    protected final Leaderboards extension;
    protected final Duels api;
    protected final Config config;
    protected final String name;

    private final File file;
    private final LeaderboardType type;
    private Location location;
    private boolean changed;
    private TopEntry cached;
    private final FileConfiguration configuration;
    protected final String dataType;

    public AbstractLeaderboard(final Leaderboards extension, final Duels api, final LeaderboardType type, final String name, final String dataType, final Location location) {
        this.extension = extension;
        this.api = api;
        this.config = extension.getConfiguration();
        this.type = type;
        this.name = name;
        this.dataType = dataType;
        this.location = location;
        this.file = new File(extension.getLeaderboardManager().getFolder(), type + "-" + name + ".yml");
        this.configuration = YamlConfiguration.loadConfiguration(file);
    }

    public AbstractLeaderboard(final Leaderboards extension, final Duels api, final File file, final LeaderboardType type, final String name) {
        this.extension = extension;
        this.api = api;
        this.config = extension.getConfiguration();
        this.file = file;
        this.type = type;
        this.name = name;
        this.configuration = YamlConfiguration.loadConfiguration(file);

        final String dataType = configuration.getString("data-type");
        Objects.requireNonNull(dataType, "data-type is null");

        final ConfigurationSection locationSection = configuration.getConfigurationSection("location");
        Objects.requireNonNull(locationSection, "location is null");

        final String worldName = locationSection.getString("world");
        final World world;

        if (worldName == null || (world = Bukkit.getWorld(worldName)) == null) {
            throw new NullPointerException("worldName or world is null");
        }

        this.location = new Location(world, locationSection.getDouble("x"), locationSection.getDouble("y"), locationSection.getDouble("z"));

        this.dataType = dataType;
    }

    protected abstract void onUpdate(final TopEntry entry);

    void update(final TopEntry entry) {
        if (entry == null) {
            return;
        }
        
        if (changed || cached == null || !cached.equals(entry)) {
            cached = entry;
            changed = false;
            onUpdate(entry);
        }
    }

    protected void onRemove() {}

    @Override
    public void teleport(final Location location) {}

    @Override
    public void remove() {
        file.delete();  
        onRemove();     
    }

    @Override
    public void save() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            configuration.set("type", type.name());
            configuration.set("name", name);
            configuration.set("data-type", dataType);
            configuration.set("location.world", location.getWorld().getName());
            configuration.set("location.x", location.getX());
            configuration.set("location.y", location.getY());
            configuration.set("location.z", location.getZ());
            configuration.save(file);
        } catch (IOException ex) {
            extension.error("Failed to save leaderboard '" + name + "' (type " + type.name() + ")!", ex);
        }
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    public FileConfiguration getConfiguration() {
        return this.configuration;
    }

    public LeaderboardType getType() {
        return this.type;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isChanged() {
        return this.changed;
    }
    @Override
    public String getDataType() {
        return this.dataType;
    }
    public String getName() {
        return this.name;
    }

}
