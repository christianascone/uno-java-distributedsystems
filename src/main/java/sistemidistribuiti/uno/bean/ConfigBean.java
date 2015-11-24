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
	private static final Logger logger = Logger.getLogger(ConfigBean.class.getName());
	
	public static final int UNKNOWN_LEADER = -1000;
	
	private final String UNO_CONFIG_KEY = "UnoConfig";
	private final String NODES_KEY = "nodes";
	private final String ID_KEY = "id";
	private final String NAME_KEY = "name";
	private final String HOST_KEY = "host";
	private final String LEADER_KEY = "leader";
	
	private List<Player> players;
	private int leaderId = UNKNOWN_LEADER;
	
	public ConfigBean(String json){
		players = new LinkedList<Player>();
		
		// Parse json
		JSONObject jsonObject = new JSONObject(json);
		JSONObject unoConfigJson = jsonObject.getJSONObject(UNO_CONFIG_KEY);
		JSONArray nodesJson = unoConfigJson.getJSONArray(NODES_KEY);
		
		for(int i = 0; i < nodesJson.length(); i++){
			JSONObject node = (JSONObject) nodesJson.get(i);
			
			int id = node.getInt(ID_KEY);
			String name = node.getString(NAME_KEY);
			String host = node.getString(HOST_KEY);
			
			try{
				boolean leader = node.getBoolean(LEADER_KEY);
				if(leader)
					this.leaderId = id; 
			}catch(JSONException e){
				logger.log(Level.INFO, String.format("Player %d is not the leader", id));
			}
			
			Player player = new Player(id, name, host, new LinkedList<UnoCard>());
			players.add(player);
		}
	}

	public List<Player> getPlayers() {
		return players;
	}

	public int getLeaderId() {
		return leaderId;
	}
}
