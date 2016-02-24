package io.github.jacobmarshall.meloooncensor.listener;

import io.github.jacobmarshall.meloooncensor.MelooonCensor;
import io.github.jacobmarshall.meloooncensor.updater.CheckForUpdatesTask;
import io.github.jacobmarshall.meloooncensor.updater.Release;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    CheckForUpdatesTask updater;
    MelooonCensor plugin;

    public PlayerJoinEventListener (MelooonCensor plugin, CheckForUpdatesTask updater) {
        this.plugin = plugin;
        this.updater = updater;
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            if (updater.isUpdateAvailable()) {
                Release release = updater.getLatestRelease();
                player.sendMessage(ChatColor.GRAY + "You're currently running MelooonCensor v" + plugin.getDescription().getVersion() + ", which is currently not the latest version.");
                if (release.hasSummary()) player.sendMessage(ChatColor.GRAY + release.getSummary());
            } else if (updater.isRunningPreRelease()) {
                player.sendMessage(ChatColor.RED +
                        "You're currently running a pre-release version of MelooonCensor." +
                        "If you encounter any bugs or issues, please report them. " +
                        "This pre-release has been compiled for development/evaluation purposes ONLY.");
            }
        }
    }

}
