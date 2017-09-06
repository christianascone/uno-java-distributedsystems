package sistemidistribuiti.uno.rmi.server;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Timer class responsible for player timeout
 * 
 * @author Christian Ascone
 *
 */
public class UNOTimer {

	private TimerCallback callback;
	public int seconds;
	private TimerTask timerTask;
	private Timer timer;

	public UNOTimer(TimerCallback id, int seconds) {
		this.callback = id;
		this.seconds = seconds;
	}

	public void start() {
		// init
		final UNOTimer timer = this;
		this.timerTask = new TimerTask() {
			public void run() {
				try {
					callback.timeUp(timer);
					stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		this.timer = new Timer(true);
		// start
		int delayInMilliseconds = this.seconds * 1000;
		this.timer.scheduleAtFixedRate(this.timerTask, delayInMilliseconds,
				delayInMilliseconds);
	}

	public void stop() {
		timerTask.cancel();
		timer.cancel();
	}

}
