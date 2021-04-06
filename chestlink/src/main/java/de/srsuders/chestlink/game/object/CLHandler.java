package de.srsuders.chestlink.game.object;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.srsuders.chestlink.game.CLPlayer;

/**
 * Author: SrSuders aka. Mario-Angelo Date: 06.04.2021 Project: chestlink
 */
public class CLHandler {

	private static final Map<UUID, CLPlayer> players = new HashMap<>();

	public static CLPlayer getCLPlayer(final UUID uuid) {
		if (!players.containsKey(uuid)) {
			final CLPlayer clp = new CLPlayer(uuid);
			players.put(uuid, clp);
		}
		return players.get(uuid);
	}
}
