package io.github.jacobmarshall.meloooncensor.listener;

import io.github.jacobmarshall.meloooncensor.config.Configuration;
import io.github.jacobmarshall.meloooncensor.updater.CheckForUpdatesTask;
import io.github.jacobmarshall.meloooncensor.updater.Release;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    CheckForUpdatesTask updater;

    public PlayerJoinEventListener (CheckForUpdatesTask updater) {
        this.updater = updater;
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp() && updater.isUpdateAvailable()) {
            Release latestRelease = updater.getLatestRelease();
            player.sendMessage(ChatColor.AQUA + "A new version of MelooonCensor is available, please visit " + latestRelease.getReleaseUrl() + ".");
        }
    }

}
