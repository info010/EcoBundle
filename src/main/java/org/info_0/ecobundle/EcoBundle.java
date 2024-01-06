package org.info_0.ecobundle;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.info_0.ecobundle.commands.Deposit;
import org.info_0.ecobundle.commands.Withdraw;

public final class EcoBundle extends JavaPlugin {
	private static Economy econ = null;
	private static EcoBundle instance;
	public static EcoBundle getInstance(){
		return instance;
	}

    @Override
    public void onEnable() {
		if (!setupEconomy()) {
			getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		instance = this;
		getCommand("deposit").setExecutor(new Deposit());
		getCommand("withdraw").setExecutor(new Withdraw());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return true;
	}

	public static Economy getEconomy() {
		return econ;
	}
}
