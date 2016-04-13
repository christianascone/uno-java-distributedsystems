package sistemidistribuiti.uno.workflow;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.SpecialCard;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.CurrentNode;
import sistemidistribuiti.uno.model.player.PLAYER_STATE;
import sistemidistribuiti.uno.model.player.Player;
import sistemidistribuiti.uno.rmi.client.UnoRemoteClient;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.rmi.server.TimerCallback;
import sistemidistribuiti.uno.rmi.server.UNOTimer;
import sistemidistribuiti.uno.view.listener.GameGUIListener;

/**
 * Data receiver class which manage the callback when RMI methods are sent to a
 * class
 * 
 * @author christian
 *
 */
public class GameManager implements DataReceiverListener, TimerCallback {
	private static final Logger logger = Logger.getLogger(GameManager.class.getName());

	private int id;
	private String name;
	private int port;

	private UnoRemoteGameInterface remoteServer;
	private UnoRemoteClient remoteClient;

	private UNOTimer timer;
	private UNOTimer timerForDraw;
	
	private GameGUIListener gameGUIListener;

	private Game game;
	private HashMap<Integer,PLAYER_STATE> playersAvailability = new HashMap<Integer,PLAYER_STATE>();

	public GameManager(GameGUIListener gameGuiListener) {
		this.gameGUIListener = gameGuiListener;
	}

	public Game getGame() {
		return game;
	}
	
	public void setItemStateList(int id, PLAYER_STATE state){
		playersAvailability.put(id, state);
	}
	
	public PLAYER_STATE getPlayerState(int id){
		return playersAvailability.get(id);
	}

	@Override
	public void setGame(Game game) throws RemoteException, NotBoundException {
		if (this.timer != null) this.timer.stop();
		this.timer = null;
		this.game = game;		
		// check if one of the player won
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
			logger.log(Level.INFO, "Turn Conditions passed");
			// start own timer for mandatory draw
			// create obj timer
			startTimerForDraw();
			enableGame();
		}else{
			startUnoTimer();
		}
	}


	/**
	 * Play my own turn
	 */
	public void playMyTurn() {
		Player newCurrent;
		try {
			// stop timer for draw
			this.timerForDraw.stop();
			this.timerForDraw = null;
			
			newCurrent = game.getNextPlayer();
			game.setCurrent(newCurrent);
			
			logger.log(Level.INFO, "Before broadcast - game struct:" + game.toString());			
			remoteClient.broadcastUpdatedGame(game);
			startUnoTimer();
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
	
	@Override
	public void setupRemoteClient(Game game) throws RemoteException, NotBoundException {
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
			UnoCard card = deckCards.get(0);
			
			while(card.getColor() == CARD_COLOR.RAINBOW){
				game.shuffleDeck();
				deckCards = game.getDeck().getCardList();
				card = deckCards.get(0);
			}
			
			card = deckCards.remove(0);
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
	
	public int getId() {
		return id;
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

	public void manageSpecialCard(SpecialCard specialCard) throws NextPlayerNotFoundException {
		switch (specialCard.getSpecialCardType()) {
		case DRAW_TWO:
			SpecialCardManager.drawTwoCard(getGame());
			break;
		case REVERSE:
			SpecialCardManager.reverseCard(getGame());
			break;
		case SKIP:
			SpecialCardManager.skipCard(getGame());
			break;
		case WILD_DRAW_FOUR:
			SpecialCardManager.drawFour(getGame());
			break;
		case WILD:
			break;
		}
	}
	
	public void timeUp(UNOTimer caller) throws NextPlayerNotFoundException, RemoteException, NotBoundException {
		if (caller == timer){
			Player player = game.getNextPlayer();
			// if I am the next node in the turn I ll:
			// - remove
			Player crashedPlayer = game.getCurrent();
			setItemStateList(crashedPlayer.getId(), PLAYER_STATE.CRASH);
			updateGameField();
			game.setCurrent(player);
			//remove crashed player			
			game.getPlayers().remove(crashedPlayer);
			
			if (player.getId() == CurrentNode.getInstance().getId()){
				// play
				setGame(game);
				
			}else{
				startUnoTimer();		// again
			}
		}else if(caller == timerForDraw){
			// mandatory draw
			drawCard();
			// pass the game
			gameGUIListener.refreshUILazyUser();
			playMyTurn();
		}else{
			logger.log(Level.INFO, "################## timers error ##################");
		}
	}
	
	public void startUnoTimer(){
		this.timer = new UNOTimer(this, 40);
		this.timer.start();
	}

	public void startTimerForDraw(){
		// 10 seconds of gap to send the game obj
		this.timerForDraw = new UNOTimer(this, 30);
		this.timerForDraw.start();
	}
}
	