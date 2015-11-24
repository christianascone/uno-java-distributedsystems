package sistemidistribuiti.uno.bean;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.player.Player;

public class ConfigBean {
	private final String UNO_CONFIG_KEY = "UnoConfig";
	private final String NODES_KEY = "nodes";
	private final String NAME_KEY = "name";
	private final String HOST_KEY = "host";
	
	private List<Player> players;
	
	public ConfigBean(String json){
		// Parse json
		JSONObject jsonObject = new JSONObject(json);
		JSONObject unoConfigJson = jsonObject.getJSONObject(UNO_CONFIG_KEY);
		JSONArray nodesJson = unoConfigJson.getJSONArray(NODES_KEY);
		
		for(int i = 0; i < nodesJson.length(); i++){
			JSONObject node = (JSONObject) nodesJson.get(i);
			
			String name = node.getString(NAME_KEY);
			String host = node.getString(HOST_KEY);
			
			Player player = new Player(name, host, new LinkedList<UnoCard>());
			players.add(player);
		}
	}

	public List<Player> getPlayers() {
		return players;
	}
}
