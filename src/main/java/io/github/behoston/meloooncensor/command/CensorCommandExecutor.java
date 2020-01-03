package io.github.behoston.meloooncensor.command;

import io.github.behoston.meloooncensor.config.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CensorCommandExecutor implements CommandExecutor {

    Configuration config;

    public CensorCommandExecutor (Configuration config) {
        this.config = config;
    }

    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        String cmd = "";
        String word = "";
        String type = "";

        if (args.length >= 1) {
            cmd = args[0].toLowerCase();
        }

        if (args.length >= 2) {
            type = args[1].toLowerCase();
        }

        if (args.length >= 3) {
            word = args[2];
        }

        try {
            switch (cmd) {
                case "reload":
                    assertPermission(sender, "meloooncensor.reload");

                    config.reload();
                    sender.sendMessage(ChatColor.GREEN + config.getTranslation().getText("censor.config.reloaded"));
                    break;
                case "enable":
                case "e":
                    assertPermission(sender, "meloooncensor.enable");

                    if ( ! config.isEnabled()) {
                        config.setEnabled(true);
                        config.save();
                        sender.sendMessage(ChatColor.GREEN + config.getTranslation().getText("censor.enabled"));
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + config.getTranslation().getText("censor.already-enabled"));
                    }
                    break;
                case "disable":
                case "d":
                    assertPermission(sender, "meloooncensor.disable");

                    if (config.isEnabled()) {
                        config.setEnabled(false);
                        config.save();
                        sender.sendMessage(ChatColor.GREEN + config.getTranslation().getText("censor.disabled"));
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + config.getTranslation().getText("censor.already-disabled"));
                    }
                    break;
                case "add":
                case "a":
                    switch (type) {
                        case "censor":
                        case "censored":
                        case "c":
                            assertPermission(sender, "meloooncensor.add.censor");

                            if (config.addCensor(word)) {
                                config.save();
                                sender.sendMessage(ChatColor.GREEN + config.getTranslation().getText("censor.word-added"));
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + config.getTranslation().getText("censor.word-already-added"));
                            }
                            break;
                        case "ignore":
                        case "ignored":
                        case "i":
                            assertPermission(sender, "meloooncensor.add.ignore");

                            if (config.addIgnore(word)) {
                                config.save();
                                sender.sendMessage(ChatColor.GREEN + config.getTranslation().getText("censor.ignore-word-added"));
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + config.getTranslation().getText("censor.ignore-word-already-added"));
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + config.getTranslation().getText("censor.command.add.help"));
                            break;
                    }
                    break;
                case "remove":
                case "r":
                    switch (type) {
                        case "censor":
                        case "censored":
                        case "c":
                            assertPermission(sender, "meloooncensor.remove.censor");

                            if (config.removeCensor(word)) {
                                config.save();
                                sender.sendMessage(ChatColor.GREEN + config.getTranslation().getText("censor.word-removed"));
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + config.getTranslation().getText("censor.word-not-censored"));
                            }
                            break;
                        case "ignore":
                        case "ignored":
                        case "i":
                            assertPermission(sender, "meloooncensor.remove.ignore");

                            if (config.removeIgnore(word)) {
                                config.save();
                                sender.sendMessage(ChatColor.GREEN + config.getTranslation().getText("censor.ignore-word-removed"));
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + config.getTranslation().getText("censor.ignore-word-not-ignored"));
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + config.getTranslation().getText("censor.command.remove.help"));
                            break;
                    }
                    break;
                case "list":
                case "l":
                    switch (type) {
                        case "censor":
                        case "censored":
                        case "c":
                            assertPermission(sender, "meloooncensor.list.censor");

                            sender.sendMessage(toStringArrayPretty(config.getCensor()));
                            break;
                        case "ignore":
                        case "ignored":
                        case "i":
                            assertPermission(sender, "meloooncensor.list.ignore");

                            sender.sendMessage(toStringArrayPretty(config.getIgnore()));
                            break;
                        default:
                            assertPermission(sender, "meloooncensor.list.censor");
                            assertPermission(sender, "meloooncensor.list.ignore");

                            List<String> all = new ArrayList<>();
                            all.addAll(config.getCensor());
                            all.addAll(config.getIgnore());
                            sender.sendMessage(toStringArrayPretty(all));
                            break;
                    }
                    break;
                default:
                    return false;
            }
        } catch (NoPermissionException err) {
            sender.sendMessage(ChatColor.RED + config.getTranslation().getText("censor.command.no-permission"));
        }

        return true;
    }

    private String toStringArrayPretty (List<String> strings) {
        StringBuilder pretty = new StringBuilder();

        for (int index = 0; index < strings.size(); index++) {
            if (index != 0) pretty.append(", ");
            pretty.append(strings.get(index));
        }

        if (pretty.length() != 0) {
            return pretty.toString();
        } else {
            return "*Empty*";
        }
    }

    private void assertPermission (CommandSender sender, String permission) throws NoPermissionException {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if ( ! player.isOp() || ! player.hasPermission(permission)) {
                throw new NoPermissionException();
            }
        }
    }

}
