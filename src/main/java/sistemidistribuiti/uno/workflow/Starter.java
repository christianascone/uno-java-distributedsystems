package sistemidistribuiti.uno.workflow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import sistemidistribuiti.uno.bean.ConfigBean;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.Deck;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.rmi.server.UnoRemoteServer;
import sistemidistribuiti.uno.rmi.utils.ServerHelper;
import sistemidistribuiti.uno.utils.DeckHelper;

/**
 * Starter class for every server/client
 * 
 * @author christian
 *
 */
public class Starter {
	private static int id;

	private static GameManager gameManager;
	private static Game game;

	private static final int START_CARDS_COUNT = 7;

	public static void main(String[] args) throws IOException, NotBoundException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		URL url = UnoRemoteServer.class.getClassLoader().getResource(
				"java.policy");

		System.setProperty("java.security.policy", url.getPath());

		serverConfiguration();

		File configFile = new File("config.json");
		boolean leader = false;
		if (configFile.exists()) {
			leader = setupGame(configFile);
		}

		if (leader) {
			gameManager.setGame(game);
			gameManager.getRemoteClient().broadcastGame(game);
		}
		
	}

	/**
	 * Configures the server with data from input
	 * @throws RemoteException
	 * @throws AccessException
	 */
	private static void serverConfiguration() throws RemoteException,
			AccessException {
		gameManager = new GameManager();
		Scanner scan = new Scanner(System.in);

		System.out.println("Insert server name:");
		String name = scan.nextLine();
		
		System.out.println("Insert server id:");
		id = scan.nextInt();		

		System.out.println("Insert server port:");
		int port = scan.nextInt();
		scan.close();

		UnoRemoteGameInterface remoteServer = new UnoRemoteServer(gameManager);
		ServerHelper.setupServer(remoteServer, name, port);
		System.out.println("ComputeEngine bound");
		
		gameManager.setRemoteServer(remoteServer);
		gameManager.setId(id);
		gameManager.setName(name);
		gameManager.setPort(port);
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

		game = new Game(configBean.getPlayers(), newDeck);
		return true;
	}

}
