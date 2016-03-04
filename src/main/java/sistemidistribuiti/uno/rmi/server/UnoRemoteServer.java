package sistemidistribuiti.uno.rmi.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;


import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.model.game.Game;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;

/**
 * Server class which implements all the required methods for game communication
 * @author christian
 *
 */
public class UnoRemoteServer implements UnoRemoteGameInterface, TimerCallback {
	private final static Logger logger = Logger.getLogger(UnoRemoteServer.class.getName());
	
	private DataReceiverListener mListener;
	private UNOTimer timer;
	
	public UnoRemoteServer(DataReceiverListener mListener) {
        super();	
        this.mListener = mListener;

    }	
	
	public void setupGame(Game game) throws RemoteException, NotBoundException{
		logger.log(Level.INFO, "Received game");
		mListener.setupRemoteClient(game);
		
		// start timer
        this.timer = new UNOTimer(this,10);
        this.timer.start();
        
	}

	@Override
	public void sendGame(Game game) throws RemoteException, NotBoundException {
		logger.log(Level.INFO, "Received game");
		mListener.setGame(game);
		
		// reset timer
		this.timer.stop();
		this.timer.start();
	}
	

	public void timeUp(){
		// The node with the game is crashed
		// if I am the right node I ll recreate the token
		// and restart the game
	}
	

}
