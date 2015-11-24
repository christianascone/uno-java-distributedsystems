package sistemidistribuiti.uno.workflow;

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
	public void setGame(Game game) {
		this.game = game;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public UnoRemoteGameInterface getRemoteServer() {
		return remoteServer;
	}

	public void setRemoteServer(UnoRemoteGameInterface remoteServer) {
		this.remoteServer = remoteServer;
	}

	public UnoRemoteClient getRemoteClient() {
		return remoteClient;
	}

	public void setRemoteClient(UnoRemoteClient remoteClient) {
		this.remoteClient = remoteClient;
	}


}
