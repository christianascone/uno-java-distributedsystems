package sistemidistribuiti.uno.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote interface with game methods
 * 
 * @author christian
 *
 */
public interface UnoRemoteGameInterface extends Remote{
	// IMPORTANTISSIMA LA REMOTE EXCEPTION
	
	int multiply(int a, int b) throws RemoteException;
	
}
