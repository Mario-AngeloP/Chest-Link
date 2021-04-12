package de.srsuders.chestlink.game.event;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.mongodb.lang.NonNull;

/**
* Author: SrSuders aka. Mario-Angelo
* Date: 12.04.2021
* Project: chestlink
*/
public class ChestLinkEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private final UUID uuid;
	private final Location loc1, loc2;
	private boolean cancelled = false;
	
	public ChestLinkEvent( @NonNull final Location loc1, @NonNull final Location loc2, @NonNull final UUID uuid) {
		this.uuid = uuid;
		this.loc1 = loc1;
		this.loc2 = loc2;
	}
	
	/**
	 * Gibt die UUID des Spielers wieder
	 * @return
	 */
	public UUID getUUID() {
		return uuid;
	}

	public Location getFirstLocation() {
		return loc1;
	}

	public Location getSecondLocation() {
		return loc2;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}
}
