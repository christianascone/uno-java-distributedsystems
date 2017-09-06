package distributedsystems.uno.rmi.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import distributedsystems.uno.exception.NextPlayerNotFoundException;

public interface TimerCallback {
	void timeUp(UNOTimer caller) throws NextPlayerNotFoundException, RemoteException, NotBoundException;
}
