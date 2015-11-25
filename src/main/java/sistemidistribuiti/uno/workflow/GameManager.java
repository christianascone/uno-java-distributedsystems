package sistemidistribuiti.uno.workflow;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;

/**
 * Data receiver class which manage the callback when RMI methods are sent to 
 * a class
 * @author christian
 *
 */
public class GameManager implements DataReceiverListener{
	private static final Logger logger = Logger.getLogger(GameManager.class.getName());
	
	private int id;
	private String name;
	private int port;
	
	private UnoRemoteGameInterface remoteServer;
	private UnoRemoteClient remoteClient;
	
	private Game game;
	
	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) throws RemoteException, NotBoundException {
		this.game = game;
		if(game.getCurrent().getId() == id){
			logger.log(Level.INFO, String.format("Node %d has the token", id));
		}
		this.remoteClient = new UnoRemoteClient(game, id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setRemoteServer(UnoRemoteGameInterface remoteServer) {
		this.remoteServer = remoteServer;
	}

	public void setRemoteClient(UnoRemoteClient remoteClient) {
		this.remoteClient = remoteClient;
	}

	public UnoRemoteGameInterface getRemoteServer() {
		return remoteServer;
	}

	public UnoRemoteClient getRemoteClient() {
		return remoteClient;
	}
}
