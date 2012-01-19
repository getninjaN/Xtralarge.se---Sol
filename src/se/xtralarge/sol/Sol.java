package se.xtralarge.sol;

// Imports
import net.milkbowl.vault.economy.Economy;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Random;
import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

// Main class
public class Sol extends JavaPlugin {
	public static Economy economy = null;
	private int cost = 50;
	Logger log = Logger.getLogger("Minecraft"); // Sätt loggningsprylar
	
	// När plugin blir aktiverat
	public void onEnable(){
		log.info("Sol has been enabled!");
		
		setupEconomy();
	}
	
	// När plugin blir inaktiverat
	public void onDisable(){
		log.info("Sol has been disabled.");
	}
	
	// When player enters command
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Player player = null;
		
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			sender.sendMessage("Du måste vara en spelare.");
			return true;
		}
		
		if(cmd.getName().equalsIgnoreCase("sol")) {
			if(player.hasPermission("granis.sol")) {
				// Ekonomi grunks
				double balance = economy.getBalance(player.getName());
				
				if (balance < cost) {
				    player.sendMessage(ChatColor.RED + "- Du har inte råd att köpa sol... Det kostar "+ cost +"c!");;
				    return true;
				}
				
				if(player.getWorld().hasStorm()) {
					Random random = new Random();
					 
					player.getWorld().strikeLightningEffect(player.getLocation());
					player.getWorld().setStorm(false);
					player.getWorld().setThundering(false);
					player.getWorld().setWeatherDuration(random.nextInt(168000) + 12000);
					this.getServer().broadcastMessage(ChatColor.YELLOW +"- Varde ljus! "+ player.getName() + ChatColor.YELLOW +" torkade bort allt regn i "+ player.getWorld().getName() +"!");
					player.sendMessage("Du är just nu "+ cost +"c riksdaler fattigare, tackar!");
					
					// Mer ekonomigrunks
					economy.withdrawPlayer(player.getName(), cost);
					economy.depositPlayer("Corningstone", cost);
				} else {
					player.sendMessage(ChatColor.RED + "- Det regnar ju inte?");
				}
			} else {
				player.sendMessage(ChatColor.RED + "- Du har inte tillåtelse att använda sol-kommandot.");
			}
			
		}
		
		return true;
	}
	
	// Economy setup
	private Boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		
		return (economy != null);
	}
}