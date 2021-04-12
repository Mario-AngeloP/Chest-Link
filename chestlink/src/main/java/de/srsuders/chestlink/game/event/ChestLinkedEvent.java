package de.srsuders.chestlink.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.mongodb.lang.NonNull;

import de.srsuders.chestlink.game.object.LinkedChest;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 12.04.2021
* Project: chestlink
*/
public class ChestLinkedEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	private final LinkedChest lc;
	
	public ChestLinkedEvent( @NonNull final LinkedChest lc) {
		this.lc = lc;
	}
	
	/**
	 * Durch die eine LinkedChest, erhält man auch die andere
	 * @return
	 */
	public LinkedChest getLinkedChest() {
		return this.lc;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
}
