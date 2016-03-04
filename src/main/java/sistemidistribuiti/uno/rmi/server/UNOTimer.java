package sistemidistribuiti.uno.rmi.server;

import java.rmi.NotBoundException;
import java.util.Timer;
import java.util.TimerTask;

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
		this.timerTask = new TimerTask(){
			public void run() {
				callback.timeUp();
			}
		};
		this.timer = new Timer(true);
		// start
		timer.scheduleAtFixedRate(this.timerTask,this.seconds * 1000, this.seconds * 1000);
	}

	public void stop(){
		timerTask.cancel();
		timer.cancel(); 
	}

}
