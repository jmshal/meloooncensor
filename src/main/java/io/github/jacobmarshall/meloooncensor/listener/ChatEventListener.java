package io.github.jacobmarshall.meloooncensor.listener;

import io.github.jacobmarshall.meloooncensor.config.Configuration;
import io.github.jacobmarshall.meloooncensor.log.ViolationLogger;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class ChatEventListener implements Listener {

    Configuration config;
    ViolationLogger logger;

    public ChatEventListener (Configuration config, ViolationLogger logger) {
        this.config = config;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event) {
        if (config.isEnabled()) {
            Player player = event.getPlayer();
            String message = event.getMessage();

            if ( ! config.allowBypass(player)) {
                if (config.getFilter().violatesPolicy(message)) {
                    String censoredMessage = config.getFilter().censorMessage(message);

                    if (censoredMessage == null) {
                        event.setCancelled(true);
                    } else {
                        event.setMessage(censoredMessage);
                    }

                    HashMap<String, String> values = new HashMap<>();
                    values.put("player", player.getName());

                    player.sendMessage(ChatColor.GRAY + config.getFormattedMessage(values));

                    if (logger != null) {
                        // The check above is in case the log file failed to create
                        logger.log(player, message);
                    }
                }
            }
        }
    }

}
