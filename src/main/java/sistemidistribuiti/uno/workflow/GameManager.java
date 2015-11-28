package sistemidistribuiti.uno.workflow;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;

/**
 * Data receiver class which manage the callback when RMI methods are sent to 
 * a class
 * @author christian
 *
 */
public class GameManager implements DataReceiverListener, Runnable{
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
		if(isMyTurn(game)){
			logger.log(Level.INFO, String.format("Node %d has the token", id));
		}
	}

	/**
	 * Play my own turn
	 */
	private void playMyTurn() {
		Scanner scan = new Scanner(System.in);
		String played = "";
		while(true){
			try{
				played = scan.nextLine();		
				break;
			}catch(Exception e){
				played= "auto";
				break;
			}
		}
		logger.log(Level.INFO, String.format("Input: %s", played));
		scan.close();
				
		Player newCurrent;
		try {
			newCurrent = getNextPlayer();
			game.setCurrent(newCurrent);
			remoteClient.broadcastUpdatedGame(game);
		} catch (Exception e) {
			// TODO GESTIRE UN BEL PROBLEMONE
			e.printStackTrace();
		}
	}
	
	private Player getNextPlayer() throws NextPlayerNotFoundException{
		List<Player> players = game.getPlayers();
		for(int i = 0; i < players.size(); i++){
			Player iteratePlayer = players.get(i);
			if(iteratePlayer.getId() == game.getCurrent().getId()){
				Player newCurrent = players.get((i + 1) % players.size());
				return newCurrent;
			}
		}
		
		throw new NextPlayerNotFoundException();
	}

	@Override
	public void setupRemoteClient(Game game) throws RemoteException, NotBoundException{
		this.remoteClient = new UnoRemoteClient(game, id);
		setGame(game);
	}
	
	/**
	 * Check whether the token is actually owned
	 * @param game
	 * @return
	 */
	private boolean isMyTurn(Game game) {
		return game.getCurrent().getId() == id;
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

	@Override
	public void run() {
		while(true){
			if(game != null && game.getCurrent() != null && isMyTurn(game)){
				playMyTurn();
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
