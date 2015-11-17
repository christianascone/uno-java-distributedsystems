package sistemidistribuiti.uno.model.card;

import java.util.Arrays;
import java.util.List;

/**
 * Enum for special cards
 * 
 * @author christian
 *
 */
public enum SPECIAL_CARD_TYPE {
	SKIP, DRAW_TWO, REVERSE, WILD, WILD_DRAW_FOUR;

	public static List<SPECIAL_CARD_TYPE> getColorType(){
		return Arrays.asList(SKIP, DRAW_TWO, REVERSE);	
	}
	
	public static List<SPECIAL_CARD_TYPE> getRainbowType(){
		return Arrays.asList(WILD, WILD_DRAW_FOUR);	
	}
}
