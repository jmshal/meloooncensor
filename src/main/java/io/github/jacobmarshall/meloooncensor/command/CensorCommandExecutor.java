package io.github.jacobmarshall.meloooncensor.command;

import io.github.jacobmarshall.meloooncensor.config.Configuration;
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
                case "enable":
                case "e":
                    assertPermission(sender, "meloooncensor.enable");

                    if ( ! config.isEnabled()) {
                        config.setEnabled(true);
                        config.save();
                        sender.sendMessage(ChatColor.GREEN + "Enabled censor filter.");
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "Censor filter is already enabled.");
                    }
                    break;
                case "disable":
                case "d":
                    assertPermission(sender, "meloooncensor.disable");

                    if (config.isEnabled()) {
                        config.setEnabled(false);
                        config.save();
                        sender.sendMessage(ChatColor.GREEN + "Disabled censor filter.");
                    } else {
                        sender.sendMessage(ChatColor.YELLOW + "Censor filter is already disabled.");
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
                                sender.sendMessage(ChatColor.GREEN + "Added to censored words.");
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + "Already a censored word.");
                            }
                            break;
                        case "ignore":
                        case "ignored":
                        case "i":
                            assertPermission(sender, "meloooncensor.add.ignore");

                            if (config.addIgnore(word)) {
                                config.save();
                                sender.sendMessage(ChatColor.GREEN + "Added to ignored words.");
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + "Already an ignored word.");
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "Provide a list to add a word to (censor/ignore).");
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
                                sender.sendMessage(ChatColor.GREEN + "Removed from censored words.");
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + "Not a censored word.");
                            }
                            break;
                        case "ignore":
                        case "ignored":
                        case "i":
                            assertPermission(sender, "meloooncensor.remove.ignore");

                            if (config.removeIgnore(word)) {
                                config.save();
                                sender.sendMessage(ChatColor.GREEN + "Removed from ignored words.");
                            } else {
                                sender.sendMessage(ChatColor.YELLOW + "Not an ignored word.");
                            }
                            break;
                        default:
                            sender.sendMessage(ChatColor.RED + "Provide a list to remove a word from (censor/ignore).");
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
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform this action.");
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
