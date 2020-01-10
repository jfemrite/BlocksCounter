package com.lfn.blockscounter;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;






public class Main extends JavaPlugin implements Listener {
	
	private HashMap<Player, Long> timer = new HashMap<>();
	private HashMap<Player, Integer> blocks = new HashMap<>();
	private HashMap<Player, Integer> finalBlocks = new HashMap<>();
	
	private String title;
	private Actionbar actionbar;
	
	@Override
	public void onEnable() {
	
		if(setupActionbar()) {
			Bukkit.getPluginManager().registerEvents(this, this);
		} else {
			System.out.println("Server version not compatible with BlocksCounter!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		
		this.getConfig().options().copyDefaults();
		saveDefaultConfig();
		
		String rawTitle = this.getConfig().getString("BlocksCounterPlus.title");
		title = rawTitle.replaceAll("&", "§");	
		
	}
	
	public boolean setupActionbar() {
		
		String version;
		
		try {
			version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		} catch (ArrayIndexOutOfBoundsException x) {
			System.out.println("You are not using a compatible server version");
			return false;
		}
		
		if(version.equals("v1_8_R1")) {
			actionbar = new Actionbar_1_8_R1();
		} else if (version.equals("v1_8_R3")) {
			actionbar = new Actionbar_1_8_R3();
		} else if (version.equals("v1_11_R1")) {
			actionbar = new Actionbar_1_11_R1();
		} else if (version.equals("v1_12_R1")) {
			actionbar = new Actionbar_1_12_R1();
		} else if (version.equals("v1_14_R1")) {
			actionbar = new Actionbar_1_14_R1();
		} else if (version.equals("v1_15_R1")) {
			actionbar = new Actionbar_1_15_R1();
		}
		
		return actionbar != null;
		
		
	}
		
	@EventHandler
	public void onBlockUpdate(BlockCountUpdateEvent e) {
		String finalTitle = title.replace("{BPS}", finalBlocks.get(e.getPlayer()).toString());
		
		actionbar.sendActionbar(e.getPlayer(), finalTitle);
		
		finalBlocks.remove(e.getPlayer());
		
		Bukkit.getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				if(finalBlocks.get(e.getPlayer()) != null) {
					finalBlocks.put(e.getPlayer(), 0);
				}
			}
			
		}, 25L);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		
		Player player = e.getPlayer();
		long initialTime;
		long currentTime = System.currentTimeMillis();
		int totalBlocks;		
		
		if(timer.containsKey(player)) {
			initialTime = timer.get(player);
			
			if(currentTime >= initialTime + 1000L) {

				totalBlocks = blocks.get(player);
				this.finalBlocks.put(player, totalBlocks);

				Bukkit.getPluginManager().callEvent(new BlockCountUpdateEvent(player, this.finalBlocks.get(player)));

				timer.remove(player);
			} else {
				blocks.put(player, blocks.get(player) + 1);
				totalBlocks = blocks.get(player);
			}

		} else if(!timer.containsKey(player)) {
			timer.put(player, System.currentTimeMillis());
			blocks.put(player, 1);
			totalBlocks = 1;
			initialTime = timer.get(player);
		}		
	}
}
