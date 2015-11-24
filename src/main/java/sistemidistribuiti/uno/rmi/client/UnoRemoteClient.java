package sistemidistribuiti.uno.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

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
	private List<UnoRemoteGameInterface> hosts;

	public UnoRemoteClient(Game game) throws RemoteException, NotBoundException {
		hosts = new LinkedList<UnoRemoteGameInterface>();
		
		List<Player> players = game.getPlayers();
		
		for(Player player : players){
			hosts.add(ServerHelper.setupClient(player.getHost(), player.getNickname()));
		}
	}    
}
