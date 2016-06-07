package sistemidistribuiti.uno.workflow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import sistemidistribuiti.uno.bean.ConfigBean;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.Deck;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.CurrentNode;
import sistemidistribuiti.uno.model.player.PLAYER_STATE;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.rmi.server.UnoRemoteServer;
import sistemidistribuiti.uno.rmi.utils.ServerHelper;
import sistemidistribuiti.uno.utils.DeckHelper;
import sistemidistribuiti.uno.view.listener.GameGUIListener;

/**
 * Starter class for every server/client
 * 
 * @author christian
 *
 */
public class Starter {
	private static final Logger logger = Logger.getLogger(Starter.class.getName());
	
	private static int id;

	private static GameManager gameManager;
	private static Game game;

	private static final int START_CARDS_COUNT = 7;

	public static void startGame(String[] args, GameGUIListener guiListener) throws IOException,
			NotBoundException {
		File file = new File("java.policy");
		logger.log(Level.INFO, String.format("Policy url -> %s", file.getAbsolutePath()));

		System.setProperty("java.security.policy", file.getAbsolutePath());
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		serverConfiguration(args, guiListener);

		File configFile = new File("config.json");
		boolean leader = false;
		if (configFile.exists()) {
			leader = setupGame(configFile);
		}

		if (leader) {
			gameManager.setRemoteClient(new UnoRemoteClient(game, id));
			gameManager.getRemoteClient().broadcastNewGame(game);
			gameManager.setGame(game);
		}

	}

	/**
	 * Configures the server with data from input
	 * @param gameGuiListener 
	 * 
	 * @throws RemoteException
	 * @throws AccessException
	 */
	private static void serverConfiguration(String[] args, GameGUIListener gameGuiListener)
			throws RemoteException, AccessException {
		gameManager = new GameManager(gameGuiListener);

		String name = "";
		id = 0;
		int port = 0;
		
		if (args.length == 0) {
			Scanner scan = new Scanner(System.in);

			System.out.println("Insert server name:");
			name = scan.nextLine();

			System.out.println("Insert server id:");
			id = scan.nextInt();

			System.out.println("Insert server port:");
			port = scan.nextInt();
			scan.close();
		} else {
			name = args[0];
			id = Integer.parseInt(args[1]);
			port = Integer.parseInt(args[2]);
		}
		
		gameGuiListener.setup(name);
		
		logger.log(Level.INFO, String.format("Server name: %s", name));
		logger.log(Level.INFO, String.format("Server id: %d", id));
		logger.log(Level.INFO, String.format("Server port: %d", port));

		CurrentNode.getInstance().setId(id); 
		CurrentNode.getInstance().setHost(name); 
		
		UnoRemoteGameInterface remoteServer = new UnoRemoteServer(gameManager);
		ServerHelper.setupServer(remoteServer, name, port);

		gameManager.setRemoteServer(remoteServer);
		gameManager.setId(id);
		gameManager.setName(name);
		gameManager.setPort(port);
		gameGuiListener.setGameManager(gameManager);
	}

	/**
	 * Parse config json and setup game
	 * 
	 * @param configFile
	 * @throws IOException
	 */
	private static boolean setupGame(File configFile) throws IOException {
		List<UnoCard> cards = DeckHelper.getDeck();
		Deck newDeck = new Deck(cards);

		String jsonString = FileUtils.readFileToString(configFile);
		ConfigBean configBean = new ConfigBean(jsonString);
		
		game = new Game(configBean.getPlayers(), newDeck);

		for (Player player : configBean.getPlayers()) {
			for (int i = 0; i < START_CARDS_COUNT; i++) {
				UnoCard draw = newDeck.getCardList().remove(0);
				player.addCard(draw);
			}
		}

		int leaderId = configBean.getLeaderId();
		if (leaderId != id) {
			return false;
		}
		
		return true;
	}

}
