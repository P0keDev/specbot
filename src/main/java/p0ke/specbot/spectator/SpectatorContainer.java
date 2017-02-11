package p0ke.specbot.spectator;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import p0ke.specbot.SpecBot;

public class SpectatorContainer {

	private List<Spectator> spectators;
	private String owner;
	private boolean sentIntro = false;
	private boolean finished = false;
	private Timer timer = new Timer();

	public SpectatorContainer(String o, List<Spectator> s) {
		owner = o;
		spectators = s;
		for (Spectator spec : spectators) {
			spec.assignContainer(this);
			spec.login();
		}

		timer.schedule(new ExpireTimer(this), 45 * 60 * 1000);
		

	}

	public boolean sentIntro() {
		if (sentIntro) {
			return false;
		} else {
			sentIntro = true;
			return true;
		}
	}

	public String getOwner() {
		return owner;
	}

	public boolean isFinished() {
		return finished;
	}

	public void finish(boolean forced) {
		finished = true;
		timer.cancel();
		for (Spectator spec : spectators) {
			spec.finish(forced);
			forced = false;
		}
		SpecBot.instance.specManager.removeContainer(this);

	}

	public class ExpireTimer extends TimerTask {
		SpectatorContainer caller;

		public ExpireTimer(SpectatorContainer c) {
			caller = c;
		}

		@Override
		public void run() {
			caller.finish(true);
		}

	}

}
