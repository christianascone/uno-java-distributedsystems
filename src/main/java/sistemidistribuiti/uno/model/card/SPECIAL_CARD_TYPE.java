package sistemidistribuiti.uno.model.card;

import java.util.Arrays;
import java.util.List;

/**
 * Enum for special cards
 * 
 * @author Christian Ascone
 *
 */
public enum SPECIAL_CARD_TYPE {
	SKIP, DRAW_TWO, REVERSE, WILD, WILD_DRAW_FOUR;

	/**
	 * Get the list of Special card type which have a color
	 * 
	 * @return List of Special card type for colored cards
	 */
	public static List<SPECIAL_CARD_TYPE> getColorType(){
		return Arrays.asList(SKIP, DRAW_TWO, REVERSE);	
	}
	
	/**
	 * Get the list of Special card type which don't have a color
	 * 
	 * @return List of Special card type for not colored cards
	 */
	public static List<SPECIAL_CARD_TYPE> getRainbowType(){
		return Arrays.asList(WILD, WILD_DRAW_FOUR);	
	}
}
