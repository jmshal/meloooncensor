package io.github.jacobmarshall.meloooncensor;

import io.github.jacobmarshall.meloooncensor.filter.ClassicFilter;
import io.github.jacobmarshall.meloooncensor.filter.Filter;
import io.github.jacobmarshall.meloooncensor.filter.StrictFilter;
import io.github.jacobmarshall.meloooncensor.listeners.ChatEventListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;

import java.io.IOException;
import java.util.List;

public class MelooonCensor extends JavaPlugin {

    static final String DEFAULT_TYPE = "classic";
    static final char DEFAULT_CHAR = '*';
    static final String[] DEFAULT_LIST = new String[] {"fuck", "shit", "piss", "bitch"};
    static final String[] DEFAULT_IGNORE = new String[] {"shitsu"};
    static final String DEFAULT_MESSAGE = "Please don't use that kind of language on this server.";

    FileConfiguration config = getConfig();
    Filter filter;
    String warning;

    void setupConfig () {
        config.options().header("MelooonCensor Configuration");
        config.addDefault("censor.type", DEFAULT_TYPE);
        config.addDefault("censor.char", DEFAULT_CHAR);
        config.addDefault("censor.list", DEFAULT_LIST);
        config.addDefault("censor.ignore", DEFAULT_IGNORE);
        config.addDefault("censor.message", DEFAULT_MESSAGE);
        config.options().copyDefaults(true);
        saveConfig();
        reloadConfig();
    }

    void loadConfig () {
        config = getConfig();
        filter = getFilter();
        warning = config.getString("censor.message");
    }

    void startMetrics () {
        try {
            Metrics metrics = new Metrics(this);
            metrics.start();
        } catch (IOException e) {
            getLogger().warning("Failed to start metrics.");
        }
    }

    void registerEvents () {
        getServer().getPluginManager().registerEvents(
            new ChatEventListener(filter, warning), this
        );
    }

    Filter getFilter () {
        Filter filter;

        char replace = config.getString("censor.char", "*").charAt(0);
        List<String> censor = config.getStringList("censor.list");
        List<String> ignore = config.getStringList("censor.ignore");

        String name = config.getString("censor.type", "classic");

        if (name.equalsIgnoreCase("strict")) {
            // StrictFilter extends from ClassicFilter's detection technique, but doesn't allow any messages
            // that violate the policy from sending out publicly.
            filter = new StrictFilter(replace, censor, ignore);
        } else {
            // ClassicFilter takes any words that contain/are censored words, which aren't ignored words,
            // and censors the whole word.
            filter = new ClassicFilter(replace, censor, ignore);
        }

        return filter;
    }

    @Override
    public void onEnable () {
        setupConfig();
        loadConfig();
        startMetrics();
        registerEvents();
    }

}
