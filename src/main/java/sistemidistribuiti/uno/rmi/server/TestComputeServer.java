package sistemidistribuiti.uno.rmi.server;

import java.net.URL;
import java.util.Scanner;

import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;
import sistemidistribuiti.uno.rmi.utils.ServerHelper;

public class TestComputeServer implements UnoRemoteGameInterface {

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

			Scanner scan = new Scanner(System.in);

			System.out.println("Insert server name:");
			String name = scan.nextLine();

			System.out.println("Insert server port:");
			int port = scan.nextInt();
			scan.close();

			UnoRemoteGameInterface remoteServer = new TestComputeServer();
			ServerHelper.setupServer(remoteServer, name, port);
			System.out.println("ComputeEngine bound");
		} catch (Exception e) {
			System.err.println("ComputeEngine exception:");
			e.printStackTrace();
		}
	}
}
