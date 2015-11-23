package sistemidistribuiti.uno.rmi.server;

import sistemidistribuiti.uno.rmi.interfaces.TestRemoteInterface;

public class TestComputeServer implements TestRemoteInterface{

	public int multiply(int a, int b) {
		return a*b;
	}

}
