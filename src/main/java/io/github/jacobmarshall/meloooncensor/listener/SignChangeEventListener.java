package io.github.jacobmarshall.meloooncensor.listener;

import io.github.jacobmarshall.meloooncensor.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignChangeEventListener implements Listener {

    Configuration config;

    public SignChangeEventListener (Configuration config) {
        this.config = config;
    }

    @EventHandler
    public void onSignChange (SignChangeEvent event) {
        if (config.isEnabled()) {
            Player player = event.getPlayer();

            if ( ! config.allowBypass(player)) {
                String[] lines = event.getLines();

                for (int index = 0; index < lines.length; index++) {
                    String line = lines[index];

                    if (config.getFilter().violatesPolicy(line)) {
                        line = config.getFilter().censorMessage(line);
                        event.setLine(index, line);
                    }
                }
            }
        }
    }

}
