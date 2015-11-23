package sistemidistribuiti.uno.utils;

import java.util.ArrayList;
import java.util.List;

import sistemidistribuiti.uno.model.card.CARD_COLOR;
import sistemidistribuiti.uno.model.card.SPECIAL_CARD_TYPE;
import sistemidistribuiti.uno.model.card.UnoCard;
import sistemidistribuiti.uno.model.card.impl.NumberCard;
import sistemidistribuiti.uno.model.card.impl.SpecialCard;

public class DeckHelper {

	/**
	 * TODO:	It is necessary to define the image for each card, 
	 * 			now the attribute @Image is null
	 * @return
	 */
	public static List<UnoCard> getDeck() {
		
		List<UnoCard> deck = new ArrayList<UnoCard>();
		
		//  Number Cards creation 1-9
		for(int i = 0; i<=1;i++){
			for(int j = 1; j<=9;j++){			
				for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
					deck.add(new NumberCard(color, null, j));
				}
			}
		}
		
		// Number Cards creation 0
		for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
			deck.add(new NumberCard(color, null, 0));
		}
		
		// Special Cards with colors creation 
		for(int i = 0; i<=1;i++){
			for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
				for (SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getColorType()) {
					deck.add(new SpecialCard(color, null, type));
				}
			}
		}
		
		// Special rainbow Cards creation 
		for (SPECIAL_CARD_TYPE type : SPECIAL_CARD_TYPE.getRainbowType()) {
			for (CARD_COLOR color : CARD_COLOR.getValidColor()) {
				deck.add(new SpecialCard(CARD_COLOR.RAINBOW, null, type));
			}
		}		
		
		return deck;
	}
	
	
	
	
	

	
	

}
