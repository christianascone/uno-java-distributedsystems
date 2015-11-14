package sistemidistribuiti.uno.model;

/**
 * Abstract class for uno card, classic or special
 * 
 * @author christian
 *
 */
public abstract class UnoCard {
	private CARD_COLOR color;

	public UnoCard(CARD_COLOR color) {
		super();
		this.color = color;
	}

	public CARD_COLOR getColor() {
		return color;
	}

	public void setColor(CARD_COLOR color) {
		this.color = color;
	}
}
