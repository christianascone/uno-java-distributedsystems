package sistemidistribuiti.uno.workflow;

import java.net.URL;
import java.util.Scanner;

import sistemidistribuiti.uno.listener.DataReceiverListener;
import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.rmi.server.UnoRemoteServer;
import sistemidistribuiti.uno.rmi.utils.ServerHelper;

/**
 * Starter class for every server/client
 * 
 * @author christian
 *
 */
public class Starter {
	private static UnoRemoteGameInterface remoteServer;
	
	private static DataReceiverListener dataReceiverListener;
	
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		URL url = UnoRemoteServer.class.getClassLoader().getResource(
				"java.policy");

		System.setProperty("java.security.policy", url.getPath());

		try {
			dataReceiverListener = new DataReceiver();
			Scanner scan = new Scanner(System.in);

			System.out.println("Insert server name:");
			String name = scan.nextLine();

			System.out.println("Insert server port:");
			int port = scan.nextInt();
			scan.close();

			remoteServer = new UnoRemoteServer(dataReceiverListener);
			ServerHelper.setupServer(remoteServer, name, port);
			System.out.println("ComputeEngine bound");
		} catch (Exception e) {
			System.err.println("ComputeEngine exception:");
			e.printStackTrace();
		}
	}

}
