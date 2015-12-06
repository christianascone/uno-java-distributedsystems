package sistemidistribuiti.uno.workflow;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.game.Direction;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.PLAYER_STATE;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.view.listener.GameGUIListener;

/**
 * Data receiver class which manage the callback when RMI methods are sent to a
 * class
 * 
 * @author christian
 *
 */
public class GameManager implements DataReceiverListener {
	private static final Logger logger = Logger.getLogger(GameManager.class
			.getName());

	private int id;
	private String name;
	private int port;

	private UnoRemoteGameInterface remoteServer;
	private UnoRemoteClient remoteClient;

	private GameGUIListener gameGUIListener;

	private Game game;

	public GameManager(GameGUIListener gameGuiListener) {
		this.gameGUIListener = gameGuiListener;
	}

	public Game getGame() {
		return game;
	}

	@Override
	public void setGame(Game game) throws RemoteException, NotBoundException {
		this.game = game;
		
		if(game.playerWon()){
			List<Player> players = game.getPlayers();
			for(Player player : players){
				if(player.getState() == PLAYER_STATE.WINNER){
					gameGUIListener.showWinnerAlert(player);
				}
			}
			
			return;
		}
		
		if(game != null){
			updateGameField();			
		}
		
		if (game != null && game.getCurrent() != null && isMyTurn(game)) {
			logger.log(Level.INFO, String.format("Node %d has the token", id));
			enableGame();
		}
	}

	/**
	 * Play my own turn
	 */
	public void playMyTurn() {
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

	private void enableGame() {
		gameGUIListener.playMyTurn();
	}

	private void updateGameField() {
		gameGUIListener.updateGameField();	
	}
	
	private Player getNextPlayer() throws NextPlayerNotFoundException {
		List<Player> players = game.getPlayers();
		
		Direction direction = game.getGameDirection();
		
		int directionValue = 0;
		
		switch(direction){
		case BACKWARD:
			directionValue = -1;
			break;
		case FORWARD:
			directionValue = 1;
			break;
		}
		
		for (int i = 0; i < players.size(); i++) {
			Player iteratePlayer = players.get(i);
			if (iteratePlayer.getId() == game.getCurrent().getId()) {
				i = (i + directionValue) % players.size();
				if(i < 0){
					i = players.size() - 1;
				}
				Player newCurrent = players.get(i);
				return newCurrent;
			}
		}

		throw new NextPlayerNotFoundException();
	}

	@Override
	public void setupRemoteClient(Game game) throws RemoteException,
			NotBoundException {
		this.remoteClient = new UnoRemoteClient(game, id);
		setGame(game);
	}
	
	/**
	 * Gets the cards of the current player
	 * @return
	 */
	public List<UnoCard> getMyCards(){
		List<Player> players = this.game.getPlayers();
		for(Player player : players){
			if(player.getId() == id){
				return player.getCards();
			}
		}
		
		return new LinkedList<UnoCard>();
	}
	
	public UnoCard getLastPlayedCard() {
		List<UnoCard> discarded = game.getDiscarded().getCardList();
		if(discarded.isEmpty()){
			List<UnoCard> deckCards = game.getDeck().getCardList();
			UnoCard card = deckCards.remove(0);
			discarded.add(card);
		}
		return discarded.get(discarded.size()-1);
	}
	
	public UnoCard drawCard(){
		List<UnoCard> deckCards = game.getDeck().getCardList();
		if(!deckCards.isEmpty()){
			return deckCards.remove(0);
		}
		List<UnoCard> discardedCards = game.getDiscarded().getCardList();
		
		deckCards.addAll(discardedCards);
		discardedCards.clear();
		discardedCards.add(deckCards.get(deckCards.size()-1));
		
		game.shuffleDeck();
		
		return deckCards.remove(0);
	}
	
	/**
	 * Discard the given card
	 * @param discarded
	 */
	public void discardCard(UnoCard discarded){
		this.game.getDiscarded().getCardList().add(discarded);
	}

	/**
	 * Check whether the token is actually owned
	 * 
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

	public void winGame() {
		List<Player> players = game.getPlayers();
		for(Player player : players){
			if(player.getId() == id){
				player.setState(PLAYER_STATE.WINNER);
			}
		}
	}

}
