package sistemidistribuiti.uno.workflow;

import java.util.List;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.model.player.Player;

public class SpecialCardManager {

	/**
	 * Draw two card
	 * @param game
	 * @throws NextPlayerNotFoundException 
	 */
	public static void drawTwoCard(Game game) throws NextPlayerNotFoundException {
		List<UnoCard> deckCards = game.getDeck().getCardList();
		
		// TODO gestire il caso di deck finito come avviene in gamemanager
		Player nextPlayer = game.getNextPlayer();
		nextPlayer.getCards().add(deckCards.remove(0));
		nextPlayer.getCards().add(deckCards.remove(0));
	}
	
	/**
	 * Reverse card management
	 * @param game
	 */
	public static void reverseCard(Game game) {
		game.reverseDirection();
	}

	/**
	 * Skip card
	 * @param game
	 * @throws NextPlayerNotFoundException 
	 */
	public static void skipCard(Game game) throws NextPlayerNotFoundException {
		//TODO Da verificare perché sembra non funzionare
		game.setCurrent(game.getNextPlayerWithSkip());
	}

	/**
	 * Draw four cards
	 * @param game
	 * @throws NextPlayerNotFoundException 
	 */
	public static void drawFour(Game game) throws NextPlayerNotFoundException {
		drawTwoCard(game);
		drawTwoCard(game);
	}
}
