package sistemidistribuiti.uno.model.game;

/**
 * Enumeration for game direction.
 * 
 * @author Christian Ascone
 *
 */
public enum Direction {
	// Next player is currentIndex+1
	FORWARD,
	// Next player is currentIndex-1
	BACKWARD;

	/**
	 * Change the game directoion inverting the current one
	 * 
	 * @param direction
	 *            Current direction
	 * @return The updated direction
	 */
	public static Direction reverseDirection(Direction direction) {
		switch (direction) {
		case FORWARD:
			return BACKWARD;
		case BACKWARD:
			return FORWARD;
		default:
			return getDefault();
		}
	}

	public static Direction getDefault() {
		return FORWARD;
	}
}
