package io.github.behoston.meloooncensor.log;

import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.logging.*;

public class ViolationLogger {

    Logger logger;
    FileHandler handler;

    public ViolationLogger (File folder, String name) throws IOException {
        this.logger = Logger.getLogger(name);

        this.handler = new FileHandler(folder + "/" + name + ".log");
        this.logger.addHandler(this.handler);

        Formatter simpleFormatter = new SimpleFormatter();
        this.handler.setFormatter(simpleFormatter);
    }

    public void log (Player player, String message) {
        this.log(player, message, null);
    }

    public void log (Player player, String message, String meta) {
        this.logger.log(Level.WARNING, "<" + player.getName() + ":" + player.getUniqueId() + "> " + (meta != null ? meta + " " : "") + message);
    }

}
