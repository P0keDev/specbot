package p0ke.specbot.spectator;

import java.util.List;
import java.util.Timer;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import p0ke.specbot.SpecBot;

public class SpectatorContainer {

	private List<Spectator> spectators;
	private String owner;
	private String ownerName;
	private String pLeader = "";
	private boolean sentIntro = false;
	private boolean finished = false;
	private Timer timer = new Timer();
	private DateTime lastGameStart = new DateTime(0);

	public SpectatorContainer(String o, String n, List<Spectator> s) {
		owner = o;
		ownerName = n;
		spectators = s;
		if(spectators.size() < 3){
			SpecBot.instance.usageStats.addContainersRequested(1);
			SpecBot.instance.usageStats.addSpecsRequested(spectators.size());
			SpecBot.instance.usageStats.save();
		}
		for (Spectator spec : spectators) {
			spec.assignContainer(this);
			spec.login();
		}
		

	}

	public boolean sentIntro() {
		if (sentIntro) {
			return false;
		} else {
			sentIntro = true;
			return true;
		}
	}
	
	public boolean isPartied(){
		return !pLeader.isEmpty();
	}
	
	public String getPartyLeader(){
		return pLeader;
	}

	public String getOwner() {
		return owner;
	}
	
	public String getOwnerName() {
		return ownerName;
	}

	public boolean isFinished() {
		return finished;
	}
	
	public void party(String n){
		if(pLeader.isEmpty()){
			pLeader = n;
			if(spectators.size() < 3){
				SpecBot.instance.usageStats.addPartiesJoined(1);
				SpecBot.instance.usageStats.save();
			}
		}
	}
	
	public void registerGame(DateTime dt){
		Duration duration = new Duration(lastGameStart, dt);
		if(duration.getStandardSeconds() > 12){ //if game is different
			lastGameStart = dt;
			if(spectators.size() < 3){
				SpecBot.instance.usageStats.addGamesJoined(1);
				SpecBot.instance.usageStats.save();
			}
		}
		
	}

	public void finish(boolean forced) {
		finished = true;
		timer.cancel();
		for (Spectator spec : spectators) {
			if(spectators.size() < 3){
				SpecBot.instance.usageStats.addIngameTime(spec.getIngameTime());
			}
			spec.finish(forced);
			forced = false;
		}
		SpecBot.instance.usageStats.save();
		SpecBot.instance.specManager.removeContainer(this);

	}


}
