package sistemidistribuiti.uno.rmi.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

import sistemidistribuiti.uno.exception.NextPlayerNotFoundException;

public class UNOTimer {

	private TimerCallback callback;
	private int seconds;
	private TimerTask timerTask;
	private Timer timer;
	
	public UNOTimer(TimerCallback id,int seconds){
		this.callback = id;
		this.seconds = seconds;		
	}
	
	public void start(){
		// init
		final UNOTimer ciccio = this;
		this.timerTask = new TimerTask(){
			public void run() {
				try {
					callback.timeUp(ciccio);
					stop(); 
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.timer = new Timer(true);
		// start
		this.timer.scheduleAtFixedRate(this.timerTask,this.seconds * 1000, this.seconds * 1000);
	}

	public void stop(){
		timerTask.cancel();
		timer.cancel(); 
	}



}
