package com.lfn.blockscounter;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BlockCountUpdateEvent extends Event {

	private final Player player;
	private final int totalBlocks;
	private static final HandlerList HANDLERS = new HandlerList();
	
	public BlockCountUpdateEvent(Player player, Integer totalBlocks) {
		this.player = player;
		this.totalBlocks = totalBlocks;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getFinalBlocks() {
		return totalBlocks;
	}
	
	public static HandlerList getHandlerList() {
		return HANDLERS;
	}
	
	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

}
