package sistemidistribuiti.uno.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestRemoteInterface extends Remote{
	// IMPORTANTISSIMA LA REMOTE EXCEPTION
	int multiply(int a, int b) throws RemoteException;
}
