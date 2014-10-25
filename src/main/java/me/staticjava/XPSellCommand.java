package me.staticjava;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by Jains on 4/4/14.
 */
public class XPSellCommand implements CommandExecutor {

    private XPSell plugin;
    private int perOrb;
    private int perLevel;

    public XPSellCommand(XPSell plugin, int perOrb, int perLevel) {

        this.plugin = plugin;
        this.perOrb = perOrb;
        this.perLevel = perLevel;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " Only Players can use this command.");
            return true;
        } else if (args.length == 1) {
            Player player = (Player) sender;
            String arg1 = args[0]; //The argument.
            String type = Character.toString(arg1.charAt(arg1.length() - 1)); //The last char in arg1.
            if (type.equalsIgnoreCase("o") || type.equalsIgnoreCase("l")) { //If they want to redeem XP/levels...
                if (player.hasPermission("xpsell.redeem")) {
                    if (type.equalsIgnoreCase("o")) {
                        String orbString = arg1.substring(0, arg1.length() - 1); //The num. of orbs user requested.
                        if (isInteger(orbString)) {
                            int orbs = Integer.parseInt(orbString); //The int value.
                            int playerXP = player.getTotalExperience(); //Player's current num. of orbs.

                            if (playerXP >= orbs) {
                                Economy econ = plugin.economy;
                                EconomyResponse r = econ.depositPlayer(player.getName(), perOrb * orbs);

                                if (r.transactionSuccess()) {
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.GREEN + " You have successfully redeemed " + orbs + " orb(s) and have received $" + perOrb * orbs + "!");
                                    int newPlayerXP = playerXP - orbs; //The amount of XP the player will have; their original XP - the amount they redeemed.
                                    player.setTotalExperience(0);
                                    player.setLevel(0);
                                    player.setExp(0);
                                    player.giveExp(newPlayerXP);
                                    return true;
                                } else {
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + "There was an error redeeming your orb(s)! Please report this problem to a Staff Member.");
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " You don't have enough orbs! Type /xpsell help for help.");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " Invalid command usage! Type /xpsell help for help.");
                            return true;
                        }
                    } else if (type.equalsIgnoreCase("l")) {
                        String levelString = arg1.substring(0, arg1.length() - 1); //The num. of levels user requested.
                        if (isInteger(levelString)) {
                            int levels = Integer.parseInt(levelString); //The int value.
                            int playerLevels = player.getLevel(); //Player's current num. of levels.

                            if (playerLevels >= levels) {
                                Economy econ = plugin.economy;
                                EconomyResponse r = econ.depositPlayer(player.getName(), perLevel * levels);

                                if (r.transactionSuccess()) {
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.GREEN + " You have successfully redeemed " + levels + " level(s) and have received $" + perLevel * levels + "!");
                                    int newPlayerLevel = playerLevels - levels; //The amount of XP the player will have; their original XP - the amount they redeemed.
                                    player.setLevel(newPlayerLevel);
                                    return true;
                                } else {
                                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + "There was an error redeeming your level(s)! Please report this problem to a Staff Member.");
                                    return true;
                                }
                            } else {
                                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " You don't have enough levels! Type /xpsell help for help.");
                                return true;
                            }
                        } else {
                            player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " Invalid command usage! Type /xpsell help for help.");
                            return true;
                        }

                    }
                } else {
                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " You don't have permission to execute this command!");
                }
            } else if (args[0].equalsIgnoreCase("help")) {
                player.sendMessage(ChatColor.BLUE + "------XPSell Help------");
                player.sendMessage(ChatColor.GOLD + "/xpsell help ->" + ChatColor.BLUE + " View the help menu.");
                player.sendMessage(ChatColor.GOLD + "/xpsell reload ->" + ChatColor.BLUE + " Reload the XPSell configuration.");
                player.sendMessage("");
                player.sendMessage(ChatColor.GOLD + "/xpsell 10o ->" + ChatColor.BLUE + " Redeem 10 orbs for money. Note that you can choose any number.");
                player.sendMessage(ChatColor.GOLD + "/xpsell 10l ->" + ChatColor.BLUE + " Redeem 10 levels for money. Note that you can choose any number.");
                return true;
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (player.hasPermission("xpsell.reload")) {
                    plugin.reloadConfig();
                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " Successfully reloaded the config!");
                    return true;
                } else {
                    player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " You don't have permission to execute this command!");
                    return true;
                }
            } else {
                player.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " Invalid command usage! Type /xpsell help for help.");
                return true;
            }
        } else {
            sender.sendMessage(ChatColor.GOLD + "[" + ChatColor.BLUE + "XPSell" + ChatColor.GOLD + "]" + ChatColor.RED + " Invalid command usage! Type /xpsell help for help.");
            return true;
        }
        return false;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
