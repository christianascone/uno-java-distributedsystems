package sistemidistribuiti.uno.model;

/**
 * Abstract class for uno card, classic or special
 * 
 * @author christian
 *
 */
public abstract class UnoCard {
	private CARD_COLOR color;
	private byte[] image;
	
	public UnoCard(CARD_COLOR color, byte[] image) {
		super();
		this.color = color;
		this.image = image;
	}

	public CARD_COLOR getColor() {
		return color;
	}

	public void setColor(CARD_COLOR color) {
		this.color = color;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}
}
