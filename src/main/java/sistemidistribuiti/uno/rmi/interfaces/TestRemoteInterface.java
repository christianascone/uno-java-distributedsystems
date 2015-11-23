package sistemidistribuiti.uno.rmi.interfaces;

import java.rmi.Remote;

public interface TestRemoteInterface extends Remote{
	int multiply(int a, int b);
}
