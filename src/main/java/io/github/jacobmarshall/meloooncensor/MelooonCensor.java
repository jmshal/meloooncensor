package io.github.jacobmarshall.meloooncensor;

import io.github.jacobmarshall.meloooncensor.config.Configuration;
import io.github.jacobmarshall.meloooncensor.listener.ChatEventListener;
import io.github.jacobmarshall.meloooncensor.command.CensorCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;

public class MelooonCensor extends JavaPlugin {

    private Configuration config;

    protected void startMetrics () {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().warning("Failed to start metrics.");
        }
    }

    protected void registerEvents () {
        getServer().getPluginManager().registerEvents(
            new ChatEventListener(this.config), this
        );

        getCommand("censor").setExecutor(
            new CensorCommandExecutor(this.config)
        );
    }

    protected void setupConfig () {
        config = new Configuration(this);
    }

    @Override
    public void onEnable () {
        setupConfig();
        startMetrics();
        registerEvents();
    }

}
