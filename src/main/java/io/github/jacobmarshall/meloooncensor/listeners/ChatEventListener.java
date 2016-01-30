package io.github.jacobmarshall.meloooncensor.listeners;

import io.github.jacobmarshall.meloooncensor.filter.Filter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEventListener implements Listener {

    Filter filter;
    String warning;

    public ChatEventListener (Filter filter, String warning) {
        this.filter = filter;
        this.warning = warning;
    }

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (filter.violatesPolicy(message)) {
            String censoredMessage = filter.censorMessage(message);

            if (censoredMessage == null) {
                event.setCancelled(true);
            } else {
                event.setMessage(censoredMessage);
            }

            player.sendMessage(ChatColor.GRAY + warning);
        }
    }

}
