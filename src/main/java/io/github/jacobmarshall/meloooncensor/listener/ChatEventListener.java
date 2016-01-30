package io.github.jacobmarshall.meloooncensor.listener;

import io.github.jacobmarshall.meloooncensor.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventListener implements Listener {

    Configuration config;

    public ChatEventListener (Configuration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (config.getFilter().violatesPolicy(message)) {
            String censoredMessage = config.getFilter().censorMessage(message);

            if (censoredMessage == null) {
                event.setCancelled(true);
            } else {
                event.setMessage(censoredMessage);
            }

            player.sendMessage(ChatColor.GRAY + config.getMessage());
        }
    }

}
