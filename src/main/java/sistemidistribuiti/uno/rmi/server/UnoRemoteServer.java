package sistemidistribuiti.uno.rmi.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;

/**
 * Server class which implements all the required methods for game communication
 * @author christian
 *
 */
public class UnoRemoteServer implements UnoRemoteGameInterface {
	private final static Logger logger = Logger.getLogger(UnoRemoteServer.class.getName());
	
	private DataReceiverListener mListener;
	
	public UnoRemoteServer(DataReceiverListener mListener) {
        super();	
        this.mListener = mListener;
    }	
	
	public void sendGame(Game game){
		logger.log(Level.INFO, "Received game");
		mListener.setGame(game);
	}
}