package sistemidistribuiti.uno.rmi.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
	
	public void setupGame(Game game) throws RemoteException, NotBoundException{
		logger.log(Level.INFO, "Received game");
		mListener.setupRemoteClient(game);
		        
	}

	@Override
	public void sendGame(Game game) throws RemoteException, NotBoundException {
		logger.log(Level.INFO, "Received game");
		mListener.setGame(game);	
	}
	

}
