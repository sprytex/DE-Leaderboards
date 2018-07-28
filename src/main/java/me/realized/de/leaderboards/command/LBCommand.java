package me.realized.de.leaderboards.command;

import lombok.Getter;
import me.realized.de.leaderboards.Leaderboards;
import me.realized.de.leaderboards.leaderboard.LeaderboardManager;
import org.bukkit.command.CommandSender;

public abstract class LBCommand {

    protected final Leaderboards extension;
    protected final LeaderboardManager leaderboardManager;

    @Getter
    private final String name;
    @Getter
    private final String usage;
    @Getter
    private final String description;
    @Getter
    private final int length;
    @Getter
    private final boolean playerOnly;

    public LBCommand(final Leaderboards extension, final String name, final String usage, final String description, final int length, final boolean playerOnly) {
        this.extension = extension;
        this.leaderboardManager = extension.getLeaderboardManager();
        this.name = name;
        this.usage = usage;
        this.description = description;
        this.length = length;
        this.playerOnly = playerOnly;
    }

    public abstract void execute(final CommandSender sender, final String label, final String[] args);
}
