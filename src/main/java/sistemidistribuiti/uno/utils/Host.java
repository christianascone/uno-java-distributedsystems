package sistemidistribuiti.uno.utils;

import sistemidistribuiti.uno.rmi.interfaces.UnoRemoteGameInterface;

public class Host {

	private int id;
	private String nickname;
	private String host;
	private	UnoRemoteGameInterface server;
	
	public Host(UnoRemoteGameInterface server,int id,String host,String nickname){
		this.id = id;
		this.nickname = nickname;
		this.host = host;
		this.server = server;
	}

	public int getId(){
		return this.id;
	}
	public String getNickname(){
		return this.nickname;
	}
	public String getHost(){
		return this.host;
	}
	
	public UnoRemoteGameInterface getServer(){
		return this.server;
	}
	
	
}
