package distributedsystems.uno.model.player;

public class CurrentNode {

	/**
	 * 
	 */
	
	private int id;
	private String nickname;
	private String host;
		
    private static CurrentNode instance = null;
   
    public static CurrentNode getInstance() {
	   if(instance == null) instance = new CurrentNode();
	   return instance;
    }
	
	protected CurrentNode() {}

	public int getId() {
		return id;
	}

	public String getNickname() {
		return nickname;
	}

	public String getHost() {
		return host;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public void setHost(String host) {
		this.host = host;
	}
		

}
