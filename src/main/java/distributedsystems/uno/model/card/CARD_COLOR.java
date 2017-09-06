package distributedsystems.uno.model.card;

import java.util.Arrays;
import java.util.List;

/**
 * Card background colors
 * 
 * @author Christian Ascone
 *
 */
public enum CARD_COLOR {
	RED, BLUE, YELLOW, GREEN,
	// SPECIAL COLOR
	RAINBOW;
	
	/**
	 * Gets a list of valid (not special) colors
	 * 
	 * @return List with every CARD_COLOR except Special one
	 */
	public static List<CARD_COLOR> getValidColor(){
		return Arrays.asList(RED, BLUE, YELLOW, GREEN);	
	}
}
