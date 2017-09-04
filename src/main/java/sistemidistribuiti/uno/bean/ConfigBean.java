package sistemidistribuiti.uno.bean;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.player.Player;

public class ConfigBean {
	private static final Logger logger = Logger.getLogger(ConfigBean.class
			.getName());

	public static final int UNKNOWN_LEADER = -1000;

	// Json Keys
	private final String UNO_CONFIG_KEY = "UnoConfig";
	private final String NODES_KEY = "nodes";
	private final String ID_KEY = "id";
	private final String NAME_KEY = "name";
	private final String HOST_KEY = "host";
	private final String LEADER_KEY = "leader";

	private List<Player> players;
	private int leaderId = UNKNOWN_LEADER; // Starting value for leader

	public ConfigBean(String json) {
		List<Player> players = parsePlayersJson(json);

		this.players = players;
	}

	/**
	 * Parse the given json string into a list of Players
	 * 
	 * @param json
	 *            String representation of Players json
	 * @return List of Player object
	 */
	private List<Player> parsePlayersJson(String json) {
		List<Player> players = new LinkedList<Player>();

		// Parse json
		JSONObject jsonObject = new JSONObject(json);
		JSONObject unoConfigJson = jsonObject.getJSONObject(UNO_CONFIG_KEY);
		JSONArray nodesJson = unoConfigJson.getJSONArray(NODES_KEY);

		for (int i = 0; i < nodesJson.length(); i++) {
			JSONObject node = (JSONObject) nodesJson.get(i);
			Player player = parsePlayer(node);
			players.add(player);
		}
		return players;
	}

	/**
	 * Parse the given JSONObject into a Player object
	 * 
	 * @param playerJsonObject
	 *            JSONObject with player data
	 * @return The parsed Player object
	 */
	private Player parsePlayer(JSONObject playerJsonObject) {

		int id = playerJsonObject.getInt(ID_KEY);
		String name = playerJsonObject.getString(NAME_KEY);
		String host = playerJsonObject.getString(HOST_KEY);

		try {
			boolean leader = playerJsonObject.getBoolean(LEADER_KEY);
			if (leader)
				this.leaderId = id;
		} catch (JSONException e) {
			logger.log(Level.INFO,
					String.format("Player %d is not the leader", id));
		}

		Player player = new Player(id, name, host, new LinkedList<UnoCard>());
		return player;
	}

	/**
	 * @return List of players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * Integer ID of leader player
	 * 
	 * @return
	 */
	public int getLeaderId() {
		return leaderId;
	}
}
