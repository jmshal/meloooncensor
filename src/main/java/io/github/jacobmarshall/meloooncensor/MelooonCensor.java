package io.github.jacobmarshall.meloooncensor;

import com.bugsnag.Client;
import io.github.jacobmarshall.meloooncensor.config.Configuration;
import io.github.jacobmarshall.meloooncensor.listener.ChatEventListener;
import io.github.jacobmarshall.meloooncensor.command.CensorCommandExecutor;
import io.github.jacobmarshall.meloooncensor.listener.PlayerJoinEventListener;
import io.github.jacobmarshall.meloooncensor.updater.CheckForUpdatesTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;

public class MelooonCensor extends JavaPlugin {

    private Configuration config;
    private CheckForUpdatesTask updater;
    private Client bugsnag;

    protected void startBugsnag () {
        bugsnag = new Client("b5347687fe92ee7494d20cdf5a725fad");
        bugsnag.setProjectPackages("io.github.jacobmarshall.meloooncensor");
    }

    protected void startMetrics () {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().warning("Failed to start metrics.");
        }
    }

    protected void registerEvents () {
        getServer().getScheduler().runTaskTimerAsynchronously(
            this, updater = new CheckForUpdatesTask(this, bugsnag), 0L, 36000L
        );

        getServer().getPluginManager().registerEvents(
            new ChatEventListener(config), this
        );

        getServer().getPluginManager().registerEvents(
            new PlayerJoinEventListener(this, updater), this
        );

        getCommand("censor").setExecutor(
            new CensorCommandExecutor(config)
        );
    }

    protected void setupConfig () {
        config = new Configuration(this);
    }

    @Override
    public void onEnable () {
        startBugsnag();
        setupConfig();
        startMetrics();
        registerEvents();
    }

}
