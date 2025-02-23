package me.realized.de.leaderboards.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import me.realized.de.leaderboards.Leaderboards;
import me.realized.de.leaderboards.util.StringUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {


    @Getter
    private final boolean hookHD;

    private final int changeCheckInterval;

    @Getter
    private final boolean prefixesEnabled;
    @Getter
    private final Map<String, String> prefixes;

    private String headLoading;
    private String headNoData;
    private List<String> headSignFormat;

    @Getter
    private final String hologramLoading;
    @Getter
    private final String hologramNoData;
    @Getter
    private final String hologramHeader;
    @Getter
    private final String hologramLineFormat;
    @Getter
    private final String hologramFooter;
    @Getter
    private final double spaceBetweenLines;

    @Getter
    private final String signLoading;
    @Getter
    private final String signNoData;
    @Getter
    private final String signHeader;
    @Getter
    private final boolean signSpaceBetween;
    @Getter
    private final String signLineFormat;

    private final String placeholderNoRank;
    private final String placeholderLoading;
    private final String placeholderNoData;


    public String getHeadLoading() {
        return headLoading;
    }

    public String getHeadNoData() {
        return headNoData;
    }

    public List<String> getHeadSignFormat() {
        return headSignFormat;
    }

    public Config(final Leaderboards extension) {
        FileConfiguration config = extension.getConfig();

        this.hookHD = config.getBoolean("hook-into-holographicdisplays", true);
        this.changeCheckInterval = config.getInt("change-check-interval", 60); // Стандартное значение 60

        this.prefixesEnabled = config.getBoolean("prefixes.enabled", true);
        this.prefixes = new HashMap<>();

        final ConfigurationSection prefixes = config.getConfigurationSection("prefixes.groups");

        if (prefixes != null) {
            for (final String group : prefixes.getKeys(false)) {
                this.prefixes.put(group.toLowerCase(), StringUtil.color(prefixes.getString(group)));
            }
        }
        
        this.headLoading = config.getString("types.HEAD.loading", "&cLoading...");
        this.headNoData = config.getString("types.HEAD.no-data", "&cNo data.");
        this.headSignFormat = config.getStringList("types.HEAD.sign-format");
        
        this.hologramLoading = config.getString("types.HOLOGRAM.loading", "&cLeaderboard is loading...");
        this.hologramNoData = config.getString("types.HOLOGRAM.no-data", "&cNo data available.");
        this.hologramHeader = config.getString("types.HOLOGRAM.header", "&9&m-------&r &7Top &f10 &7%type% &9&m-------&r");
        this.hologramLineFormat = config.getString("types.HOLOGRAM.line-format", "&e%rank%. &f%name% &7- &a%value% &7%identifier%");
        this.hologramFooter = config.getString("types.HOLOGRAM.footer", "&9&m-------&r &7Top &f10 &7%type% &9&m-------&r");
        this.spaceBetweenLines = config.getDouble("types.HOLOGRAM.space-between-lines", 0.05);
        
        this.signLoading = config.getString("types.SIGN.loading", "&cLoading...");
        this.signNoData = config.getString("types.SIGN.no-data", "&cNo data.");
        this.signHeader = config.getString("types.SIGN.header", "&fTop &910 &f%type%");
        this.signSpaceBetween = config.getBoolean("types.SIGN.space-between", true);
        this.signLineFormat = config.getString("types.SIGN.sign-line-format", "&e#%rank% %name%");
        
        this.placeholderNoRank = config.getString("placeholders.no-rank", "Unranked");
        this.placeholderLoading = config.getString("placeholders.loading", "Loading...");
        this.placeholderNoData = config.getString("placeholders.no-data", "No data.");
    }

    public int getChangeCheckInterval() {
        return changeCheckInterval;
    }

    public String getPlaceholderLoading() {
        return placeholderLoading;
    }

    public String getPlaceholderNoRank() {
        return placeholderNoRank;
    }

    public String getPlaceholderNoData() {
        return placeholderNoData;
    }

}
