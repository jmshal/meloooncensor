package io.github.jacobmarshall.meloooncensor;

import com.bugsnag.Client;
import io.github.jacobmarshall.meloooncensor.config.Configuration;
import io.github.jacobmarshall.meloooncensor.listener.ChatEventListener;
import io.github.jacobmarshall.meloooncensor.command.CensorCommandExecutor;
import io.github.jacobmarshall.meloooncensor.listener.PlayerJoinEventListener;
import io.github.jacobmarshall.meloooncensor.listener.SignChangeEventListener;
import io.github.jacobmarshall.meloooncensor.listener.UnhandledExceptionListener;
import io.github.jacobmarshall.meloooncensor.log.ViolationLogger;
import io.github.jacobmarshall.meloooncensor.updater.CheckForUpdatesTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;

public class MelooonCensor extends JavaPlugin {

    private Configuration config;
    private CheckForUpdatesTask updater;
    private Client bugsnag;
    private ViolationLogger chatLogger;
    private ViolationLogger signLogger;

    protected void startBugsnag () {
        bugsnag = new Client("b5347687fe92ee7494d20cdf5a725fad");
        bugsnag.setAppVersion(getDescription().getVersion());
        bugsnag.setProjectPackages("io.github.jacobmarshall.meloooncensor");
        bugsnag.addBeforeNotify(new UnhandledExceptionListener());
    }

    protected void startMetrics () {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().warning("Failed to start metrics.");
        }
    }

    protected void createViolationLoggers () {
        try {
            chatLogger = new ViolationLogger(getDataFolder(), "chat");
            signLogger = new ViolationLogger(getDataFolder(), "signs");
        } catch (IOException err) {
            bugsnag.notify(err);
        }
    }

    protected void registerEvents () {
        getServer().getScheduler().runTaskTimerAsynchronously(
            this, updater = new CheckForUpdatesTask(this, bugsnag), 0L, 36000L
        );

        getServer().getPluginManager().registerEvents(
            new ChatEventListener(config, chatLogger), this
        );

        getServer().getPluginManager().registerEvents(
            new SignChangeEventListener(config, signLogger), this
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
        startMetrics();
        setupConfig();
        createViolationLoggers();
        registerEvents();
    }

}
