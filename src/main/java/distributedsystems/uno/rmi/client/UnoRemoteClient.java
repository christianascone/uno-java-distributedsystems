package distributedsystems.uno.rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import distributedsystems.uno.exception.NextPlayerNotFoundException;
import distributedsystems.uno.model.game.Game;
import distributedsystems.uno.model.player.CurrentNode;
import distributedsystems.uno.model.player.PLAYER_STATE;
import distributedsystems.uno.model.player.Player;
import distributedsystems.uno.rmi.utils.ServerHelper;
import distributedsystems.uno.utils.Host;

/**
 * Remote client. Currently it is just an example caller
 * 
 * @author Christian Ascone
 *
 */
public class UnoRemoteClient {
	private final static Logger logger = Logger.getLogger(UnoRemoteClient.class
			.getName());

	private int myId;
	private List<Host> hosts;

	public UnoRemoteClient(Game game, int myId) throws RemoteException,
			NotBoundException {
		this.myId = myId;
		hosts = new LinkedList<Host>();

		List<Player> players = game.getPlayers();

		// Setup every player host
		for (Player player : players) {
			Host currHost = new Host(ServerHelper.setupClient(player.getHost(),
					player.getNickname()), player.getId(), player.getHost(),
					player.getNickname());
			hosts.add(currHost);
		}
	}

	/**
	 * Broadcast newly createdgame object to every connected player
	 * 
	 * @param game
	 *            Game object
	 * @throws RemoteException
	 *             in case the host is not found
	 * @throws NotBoundException
	 */
	public void broadcastNewGame(Game game) throws RemoteException,
			NotBoundException {
		for (Host remote : hosts) {
			remote.getServer().setupGame(game);
		}
	}

	/**
	 * Broadcast the updated game to every connected player
	 * 
	 * @param game
	 *            Updated Game object
	 * @throws RemoteException
	 *             in case the host is not found
	 * @throws NotBoundException
	 * @throws NextPlayerNotFoundException
	 *             in case the player is not found
	 */
	public void broadcastUpdatedGame(Game game) throws RemoteException,
			NotBoundException, NextPlayerNotFoundException {

		Boolean booError = false;
		// Send game to every host
		for (Host remote : hosts) {
			try {
				remote.getServer().sendGame(game);
			} catch (Exception e) {
				logger.log(Level.SEVERE, String.format("Host %s (Player %s) is crashed.", remote.getHost(), remote.getNickname()));
				// Update host's player state as crashed
				// and remove it from hosts list
				for (Player player : game.getPlayers()) {
					if (player.getId() == remote.getId()) {
						game.getPlayers().remove(player);
						game.setPlayerState(player.getId(), PLAYER_STATE.CRASH);
						break;
					}
				}
				hosts.remove(remote);

				// Refresh next player
				Player newCurrent = game.getNextPlayer(CurrentNode
						.getInstance().getId());
				game.setCurrent(newCurrent);

				booError = true;
				break;

			}
		}

		// Retry to broadcast gmae in case of errors
		if (booError) {
			logger.log(Level.INFO, "Retry broadcast updated game");
			broadcastUpdatedGame(game);
		}
	}
}
