package distributedsystems.uno.utils;

import java.util.ArrayList;
import java.util.List;

import distributedsystems.uno.model.card.CARD_COLOR;
import distributedsystems.uno.model.card.SPECIAL_CARD_TYPE;
import distributedsystems.uno.model.card.UnoCard;
import distributedsystems.uno.model.card.impl.NumberCard;
import distributedsystems.uno.model.card.impl.SpecialCard;

/**
 * Helper class for starting Deck building
 * 
 * @author Christian Ascone
 *
 */
public class DeckHelper {
	// Maximum number for NUMBER_CARD
	private static final int CARD_MAX_NUMBER = 9;

	/**
	 * Build up a new Deck with number cards of every color and every
	 * needed special card.
	 * 
	 * @return A list of UnoCard
	 */
	public static List<UnoCard> buildNewDeck() {

		List<UnoCard> deck = new ArrayList<UnoCard>();

		// Number Cards creation 1-9 (0 excluded)
		for (int i = 0; i <= 1; i++) {
			for (int j = 1; j <= CARD_MAX_NUMBER; j++) {
				for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
					deck.add(new NumberCard(color, j));
				}
			}
		}

		// Number Cards creation 0
		for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
			deck.add(new NumberCard(color, 0));
		}

		// Special Cards with colors creation
		for (int i = 0; i <= 1; i++) {
			for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
				for (SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getColorType()) {
					deck.add(new SpecialCard(color, type));
				}
			}
		}

		// Special rainbow Cards creation
		for (SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getRainbowType()) {
			for (int i = 0; i < CARD_COLOR.getValidColor().size(); i++) {
				deck.add(new SpecialCard(CARD_COLOR.RAINBOW, type));
			}
		}

		return deck;
	}

}
