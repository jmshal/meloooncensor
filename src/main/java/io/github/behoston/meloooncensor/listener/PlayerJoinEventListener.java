package io.github.behoston.meloooncensor.listener;

import io.github.behoston.meloooncensor.MelooonCensor;
import io.github.behoston.meloooncensor.config.Configuration;
import io.github.behoston.meloooncensor.updater.CheckForUpdatesTask;
import io.github.behoston.meloooncensor.updater.Release;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    MelooonCensor plugin;
    Configuration config;
    CheckForUpdatesTask updater;

    public PlayerJoinEventListener (MelooonCensor plugin, Configuration config, CheckForUpdatesTask updater) {
        this.plugin = plugin;
        this.config = config;
        this.updater = updater;
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.isOp()) {
            if (updater.isUpdateAvailable()) {
                Release release = updater.getLatestRelease();
                player.sendMessage(ChatColor.GRAY + config.getTranslation().getText("censor.update-available-notice"));
                if (release.hasSummary()) player.sendMessage(ChatColor.GRAY + release.getSummary());
            } else if (updater.isRunningPreRelease()) {
                player.sendMessage(ChatColor.RED + config.getTranslation().getText("censor.pre-release-notice"));
            }
        }
    }

}
