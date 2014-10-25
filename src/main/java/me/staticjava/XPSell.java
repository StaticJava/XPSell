package me.staticjava;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Jains on 4/3/14.
 */
public class XPSell extends JavaPlugin {

    public static Economy economy = null;

    public int perOrb;
    public int perLevel;

    public void onEnable() {

        if (!setupEconomy()) {
            getLogger().severe("Plugin disabled due to no Vault dependency found!");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("XPSell has been enabled and will load all files.");
        getLogger().info("Plugin made by StaticJava. Check him out on SpigotMC!");

        this.saveDefaultConfig();

        this.perOrb = getConfig().getInt("amounts.orb");
        this.perLevel = getConfig().getInt("amounts.level");

        getCommand("xpsell").setExecutor(new XPSellCommand(this, this.perOrb, this.perLevel));
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    public void onDisable() {
        getLogger().info("XPSell has been disabled and will save all files.");
        getLogger().info("Have a good day. :)");

        this.saveConfig();
    }
}
