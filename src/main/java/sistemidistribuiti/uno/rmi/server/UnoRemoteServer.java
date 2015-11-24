package sistemidistribuiti.uno.rmi.server;

import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;

/**
 * Server class which implements all the required methods for game communication
 * @author christian
 *
 */
public class UnoRemoteServer implements UnoRemoteGameInterface {
	private DataReceiverListener mListener;
	
	public UnoRemoteServer(DataReceiverListener mListener) {
        super();	
        this.mListener = mListener;
    }	
	
	public void sendGame(Game game){
		mListener.setGame(game);
	}
}
