package sistemidistribuiti.uno.rmi.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;

public interface TimerCallback {
	void timeUp(UNOTimer caller) throws NextPlayerNotFoundException, RemoteException, NotBoundException;
}
