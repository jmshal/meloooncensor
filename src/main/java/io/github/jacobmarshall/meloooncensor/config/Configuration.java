package io.github.jacobmarshall.meloooncensor.config;

import com.bugsnag.Client;
import io.github.jacobmarshall.meloooncensor.MelooonCensor;
import io.github.jacobmarshall.meloooncensor.filter.ClassicFilter;
import io.github.jacobmarshall.meloooncensor.filter.Filter;
import io.github.jacobmarshall.meloooncensor.filter.StrictFilter;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class Configuration {

    public static final String ENABLE = "censor.enable";
    public static final String BYPASS = "censor.bypass";
    public static final String TYPE = "censor.type";
    public static final String CHAR = "censor.char";
    public static final String CENSOR = "censor.list";
    public static final String IGNORE = "censor.ignore";
    public static final String MESSAGE = "censor.message";

    public static final boolean DEFAULT_ENABLE = true;
    public static final boolean DEFAULT_BYPASS = false;
    public static final String DEFAULT_TYPE = "classic";
    public static final char DEFAULT_CHAR = '*';
    public static final String[] DEFAULT_CENSOR = new String[] {"fuck", "shit", "piss", "bitch"};
    public static final String[] DEFAULT_IGNORE = new String[] {"shitsu"};
    public static final String DEFAULT_MESSAGE = "Please don't use that kind of language on this server.";

    MelooonCensor plugin;
    Client bugsnag;

    Filter filter;
    boolean enabled;
    boolean bypass;
    String type;
    char _char;
    List<String> censor;
    List<String> ignore;
    String message;

    public Configuration (MelooonCensor plugin, Client bugsnag) {
        this.plugin = plugin;
        this.bugsnag = bugsnag;
        load();
    }

    protected FileConfiguration getConfig () {
        return plugin.getConfig();
    }

    private void addDefaults () {
        getConfig().options().header("MelooonCensor Configuration");
        getConfig().addDefault(ENABLE, DEFAULT_ENABLE);
        getConfig().addDefault(BYPASS, DEFAULT_BYPASS);
        getConfig().addDefault(TYPE, DEFAULT_TYPE);
        getConfig().addDefault(CHAR, DEFAULT_CHAR);
        getConfig().addDefault(CENSOR, DEFAULT_CENSOR);
        getConfig().addDefault(IGNORE, DEFAULT_IGNORE);
        getConfig().addDefault(MESSAGE, DEFAULT_MESSAGE);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void loadConfig () {
        plugin.reloadConfig();
        setEnabled(getConfig().getBoolean(ENABLE));
        setBypass(getConfig().getBoolean(BYPASS));
        setType(getConfig().getString(TYPE));
        setCharString(getConfig().getString(CHAR));
        setCensor(getConfig().getStringList(CENSOR));
        setIgnore(getConfig().getStringList(IGNORE));
        setMessage(getConfig().getString(MESSAGE));
        updateFilter();
    }

    private void saveConfig () {
        plugin.saveConfig();
    }

    public void save () {
        getConfig().set(ENABLE, enabled);
        getConfig().set(BYPASS, bypass);
        getConfig().set(TYPE, type);
        getConfig().set(CHAR, _char);
        getConfig().set(CENSOR, censor);
        getConfig().set(IGNORE, ignore);
        getConfig().set(MESSAGE, message);
        saveConfig();
    }

    public void load () {
        addDefaults();
        loadConfig();
    }

    public void reload () {
        loadConfig();
    }

    public void setEnabled (boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled () {
        return enabled;
    }

    public void setBypass (boolean bypass) {
        this.bypass = bypass;
    }

    public boolean allowBypass () {
        return bypass;
    }

    public boolean allowBypass (Player player) {
        return allowBypass() && player.hasPermission("meloooncensor.bypass");
    }

    public void setType (String type) {
        this.type = type;
        updateFilter();
    }

    public String getType () {
        return type;
    }

    public Filter getFilter () {
        return filter;
    }

    private void updateFilter () {
        switch (getType()) {
            case "strict":
                // StrictFilter extends from ClassicFilter's detection technique, but doesn't allow any messages
                // that violate the policy from sending out publicly.
                filter = new StrictFilter(this);
                break;
            case "classic":
                // ClassicFilter takes any words that contain/are censored words, which aren't ignored words,
                // and censors the whole word.
                filter = new ClassicFilter(this);
                break;
            default:
                try {
                    Class<?> unknownClass = Class.forName(getType());

                    if (unknownClass.isAssignableFrom(Filter.class)) {
                        // The filter implementation should always accept a single argument in it's constructor
                        filter = (Filter) unknownClass.getDeclaredConstructor(Configuration.class).newInstance(this);
                    }
                } catch (Exception e) {
                    // Not that there is anything we can do at this point, as this error is only ever caused by
                    // human error (on the server admin's side), but it may help notifying 3rd parties that their
                    // filters are incorrectly being utilised (in the case of a recognisable class name).
                    bugsnag.notify(e);

                    // The best thing to do here is activate the strict filter, due to the fact it completely
                    // prevents censored messages from being visible to players (a safe fallback)
                    filter = new StrictFilter(this);
                }
                break;
        }
    }

    public void setCharString (String _char) {
        if (_char != null) {
            this._char = _char.charAt(0);
        }
    }

    public void setChar (char _char) {
        this._char = _char;
    }

    public String getCharString () {
        return String.valueOf(getChar());
    }

    public char getChar () {
        return _char;
    }

    public void setCensor (List<String> censor) {
        this.censor = censor;
    }

    public List<String> getCensor () {
        return censor;
    }

    public boolean addCensor (String word) {
        if ( ! censor.contains(word)) {
            censor.add(word);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeCensor (String word) {
        if (censor.contains(word)) {
            censor.remove(word);
            return true;
        } else {
            return false;
        }
    }

    public void clearCensor () {
        censor.clear();
    }

    public void setIgnore (List<String> ignore) {
        this.ignore = ignore;
    }

    public List<String> getIgnore () {
        return ignore;
    }

    public boolean addIgnore (String word) {
        if ( ! ignore.contains(word)) {
            ignore.add(word);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeIgnore (String word) {
        if (ignore.contains(word)) {
            ignore.remove(word);
            return true;
        } else {
            return false;
        }
    }

    public void clearIgnore () {
        ignore.clear();
    }

    public void setMessage (String message) {
        this.message = message;
    }

    public String getMessage () {
        return message;
    }

    public String getFormattedMessage () {
        return getFormattedMessage(null);
    }

    public String getFormattedMessage (HashMap<String, String> values) {
        // Translate the alt color codes first, in case of user input
        String message = ChatColor.translateAlternateColorCodes('&', getMessage());

        if (values != null && values.size() > 0) {
            for (String key : values.keySet()) {
                // {player} => jacoooooooooooob
                message = message.replaceAll("\\{" + key + "\\}", values.get(key));
            }
        }

        return message;
    }

}
