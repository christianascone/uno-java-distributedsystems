package sistemidistribuiti.uno.rmi.server;

import java.net.URL;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import sistemidistribuiti.uno.rmi.interfaces.TestRemoteInterface;

public class TestComputeServer implements TestRemoteInterface {

	public TestComputeServer() {
        super();	
    }
	
	public int multiply(int a, int b) {
		return a * b;
	}

	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		URL url = TestComputeServer.class.getClassLoader().getResource(
				"java.policy");

		System.setProperty("java.security.policy", url.getPath());

		try {
			TestRemoteInterface remoteServer = new TestComputeServer();

			Scanner scan = new Scanner(System.in);

			System.out.println("Insert server name:");
			String name = scan.nextLine();

			System.out.println("Insert server port:");
			int port = scan.nextInt();
			scan.close();

			LocateRegistry.createRegistry(port);
			TestRemoteInterface stub = (TestRemoteInterface) UnicastRemoteObject.exportObject(remoteServer,
					port);
			Registry registry = LocateRegistry.getRegistry();
			registry.rebind(name, stub);
			System.out.println("ComputeEngine bound");
		} catch (Exception e) {
			System.err.println("ComputeEngine exception:");
			e.printStackTrace();
		}
	}
}
