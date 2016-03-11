package sistemidistribuiti.uno.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.CurrentNode;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.rmi.utils.ServerHelper;
import sistemidistribuiti.uno.utils.Host;

/**
 * Remote client.
 * Currently it is just an example caller
 * 
 * @author christian
 *
 */
public class UnoRemoteClient {
	private final static Logger logger = Logger.getLogger(UnoRemoteClient.class.getName());
	
	private List<Host> hosts;

	public UnoRemoteClient(Game game, int myId) throws RemoteException, NotBoundException {
		hosts = new LinkedList<Host>();
		
		List<Player> players = game.getPlayers();
		
		for(Player player : players){
			if(player.getId() == myId){
				continue;
			}
			
			Host currHost = new Host(ServerHelper.setupClient(player.getHost(), player.getNickname()), player.getId(), player.getHost(),player.getNickname());
			hosts.add(currHost);
		}
	}
	
	public void broadcastNewGame(Game game) throws RemoteException, NotBoundException{
		for(Host remote : hosts){
			remote.getServer().setupGame(game);
		}
	}
	

	public void broadcastUpdatedGame(Game game) throws RemoteException, NotBoundException, NextPlayerNotFoundException{
		Boolean booError = false;
		for(Host remote : hosts){
			try{
				remote.getServer().sendGame(game);				
			}catch(Exception e){				
				for(Player player : game.getPlayers()){
					if (player.getId() == remote.getId()){
						game.getPlayers().remove(player);
						break;
					}
				}
				hosts.remove(remote);

				Player newCurrent = game.getNextPlayer(CurrentNode.getInstance().getId());
				game.setCurrent(newCurrent);
				
				booError = true;
				break;
				
			}
		}
		
		if (booError) {		
			broadcastUpdatedGame(game);
		}
	}
}
