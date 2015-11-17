package sistemidistribuiti.uno.model.game;

/**
 * Enumeration for game direction.
 * 
 * @author christian
 *
 */
public enum Direction {
	// Next player is currentIndex+1
	FORWARD, 
	// Next player is currentIndex-1
	BACKWARD;
	
	public static Direction reverseDirection(Direction direction){
		switch (direction) {
		case FORWARD:
			return BACKWARD;
		case BACKWARD:
			return FORWARD;
		default:
			return getDefault();
		}
	}
	
	public static Direction getDefault(){
		return FORWARD;
	}
}
