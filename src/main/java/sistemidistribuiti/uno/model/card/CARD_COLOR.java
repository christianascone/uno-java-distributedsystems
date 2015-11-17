package sistemidistribuiti.uno.model.card;

import java.util.Arrays;
import java.util.List;

/**
 * Card background colors
 * 
 * @author christian
 *
 */
public enum CARD_COLOR {
	RED, BLUE, YELLOW, GREEN,
	// SPECIAL COLOR
	RAINBOW;
	
	public static List<CARD_COLOR> getValidColor(){
		return Arrays.asList(RED, BLUE, YELLOW, GREEN);	
	}
}
