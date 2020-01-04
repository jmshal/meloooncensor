package io.github.behoston.meloooncensor;

import com.bugsnag.Client;
import io.github.behoston.meloooncensor.command.CensorCommandExecutor;
import io.github.behoston.meloooncensor.config.Configuration;
import io.github.behoston.meloooncensor.listener.ChatEventListener;
import io.github.behoston.meloooncensor.listener.PlayerJoinEventListener;
import io.github.behoston.meloooncensor.listener.SignChangeEventListener;
import io.github.behoston.meloooncensor.listener.UnhandledExceptionListener;
import io.github.behoston.meloooncensor.log.ViolationLogger;
import io.github.behoston.meloooncensor.updater.CheckForUpdatesTask;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class MelooonCensor extends JavaPlugin {

    private Configuration config;
    private CheckForUpdatesTask updater;
    private Client bugsnag;
    private ViolationLogger chatLogger;
    private ViolationLogger signLogger;
    private static Plugin plugin; // <-- Where you store an instance of your main class

    protected void startBugsnag() {
        bugsnag = new Client("b5347687fe92ee7494d20cdf5a725fad");
        bugsnag.setAppVersion(getDescription().getVersion());
        bugsnag.setProjectPackages("io.github.behoston.meloooncensor");
        bugsnag.addBeforeNotify(new UnhandledExceptionListener());
    }

    protected void createViolationLoggers() {
        try {
            chatLogger = new ViolationLogger(getDataFolder(), "chat");
            signLogger = new ViolationLogger(getDataFolder(), "signs");
        } catch (IOException err) {
            bugsnag.notify(err);
        }
    }

    protected void registerEvents() {
        getServer().getScheduler().runTaskTimerAsynchronously(
            this, updater = new CheckForUpdatesTask(this, bugsnag), 0L, 36000L
        );

        getServer().getPluginManager().registerEvents(
            new ChatEventListener(config, chatLogger, this), this
        );

        getServer().getPluginManager().registerEvents(
            new SignChangeEventListener(config, signLogger), this
        );

        getServer().getPluginManager().registerEvents(
            new PlayerJoinEventListener(this, config, updater), this
        );

        getCommand("censor").setExecutor(
            new CensorCommandExecutor(config)
        );
    }

    public static Plugin getPlugin() {
        return plugin;
    }

    protected void setupConfig() {
        config = new Configuration(this, bugsnag);
    }

    @Override
    public void onEnable() {
        plugin = this;
        setupConfig();
        createViolationLoggers();
        registerEvents();
    }

}
