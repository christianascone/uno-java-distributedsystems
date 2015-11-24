package sistemidistribuiti.uno.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.rmi.utils.ServerHelper;

/**
 * Remote client.
 * Currently it is just an example caller
 * 
 * @author christian
 *
 */
public class UnoRemoteClient {
	private final static Logger logger = Logger.getLogger(UnoRemoteClient.class.getName());
	
	private List<UnoRemoteGameInterface> hosts;

	public UnoRemoteClient(Game game, int myId) throws RemoteException, NotBoundException {
		hosts = new LinkedList<UnoRemoteGameInterface>();
		
		List<Player> players = game.getPlayers();
		
		for(Player player : players){
			if(player.getId() == myId){
				continue;
			}
			hosts.add(ServerHelper.setupClient(player.getHost(), player.getNickname()));
		}
	}
	
	public void broadcastGame(Game game) throws RemoteException{
		logger.log(Level.INFO, "Broadcast game");
		for(UnoRemoteGameInterface remote : hosts){
			remote.sendGame(game);
		}
	}
}
