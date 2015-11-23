package sistemidistribuiti.uno.rmi.utils;

import java.rmi.AccessException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServerHelper {

	/**
	 * Setup a remoteInterface
	 * 
	 * @param remoteServer Class implementing a remote interface
	 * @param name Server's name
	 * @param port RMI registry's port
	 * @throws RemoteException
	 * @throws AccessException
	 */
	public static void setupServer(Remote remoteServer,
			String name, int port) throws RemoteException, AccessException {
		LocateRegistry.createRegistry(port);
		Remote stub = (Remote) UnicastRemoteObject.exportObject(remoteServer,
				port);
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind(name, stub);
	}

}
