package sistemidistribuiti.uno.workflow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import sistemidistribuiti.uno.bean.ConfigBean;
import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.Deck;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;
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
	private static UnoRemoteGameInterface remoteServer;
	private static UnoRemoteClient remoteClient;
	
	private static DataReceiverListener dataReceiverListener;
	private static Game game;
	
	private static final int START_CARDS_COUNT = 7;
	
	private static boolean leader = false;
	
	public static void main(String[] args) throws IOException {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		URL url = UnoRemoteServer.class.getClassLoader().getResource(
				"java.policy");

		System.setProperty("java.security.policy", url.getPath());

		File configFile = new File("config.json");
		if(configFile.exists()){
			leader = true;
		}
		
		try {
			dataReceiverListener = new DataReceiver();
			Scanner scan = new Scanner(System.in);

			System.out.println("Insert server name:");
			String name = scan.nextLine();

			System.out.println("Insert server port:");
			int port = scan.nextInt();
			scan.close();

			remoteServer = new UnoRemoteServer(dataReceiverListener);
			ServerHelper.setupServer(remoteServer, name, port);
			System.out.println("ComputeEngine bound");
		} catch (Exception e) {
			System.err.println("ComputeEngine exception:");
			e.printStackTrace();
		}
		
		if(leader){
			setupGame(configFile);
		}
	}

	/**
	 * Parse config json and setup game
	 * @param configFile
	 * @throws IOException 
	 */
	private static void setupGame(File configFile) throws IOException {
		List<UnoCard> cards = DeckHelper.getDeck(); 
		Deck newDeck = new Deck(cards);
		
		String jsonString = FileUtils.readFileToString(configFile);
		ConfigBean configBean = new ConfigBean(jsonString);
		
		for(Player player : configBean.getPlayers()){
			for(int i = 0; i < START_CARDS_COUNT; i++){
				UnoCard draw = newDeck.getCardList().remove(0);
				player.addCard(draw);
			}
		}
		
		game = new Game(configBean.getPlayers(), newDeck);
	}

}
