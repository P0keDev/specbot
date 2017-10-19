package p0ke.specbot.spectator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SpectatorManager {
	
	List<Spectator> spectators = new ArrayList<Spectator>();
	List<SpectatorContainer> containers = new ArrayList<SpectatorContainer>();
	
	
	public SpectatorManager(){
		loadSpectators();
	}
	
	public String requestSpectators(String o, String on, int n){
		try {
		
		for(SpectatorContainer c : containers){
			if(c.getOwner().equalsIgnoreCase(o)){
				return "You can only have one open request at a time! Use !spec recall then try again!";
			}
		}

		List<Spectator> specsRequested = new ArrayList<Spectator>();
		for(Spectator s : spectators){
			if(!s.isInUse()){
				specsRequested.add(s);
			}
			if(specsRequested.size() >= n){
				break;
			}
		}
		
		if(specsRequested.size() < n){
			return "Not enough spectators available! Use !spec status and try again later!";
		}
		
		containers.add(new SpectatorContainer(o, on, specsRequested));
		
		StringBuilder builder = new StringBuilder();
		builder.append(specsRequested.get(0).getName().replace("_", "\\_"));
		for(int i = 1; i < specsRequested.size() - 1; i++){
			builder.append(", ");
			builder.append(specsRequested.get(i).getName().replace("_", "\\_"));
		}
		if(specsRequested.size() > 1){
			builder.append(" and ");
			builder.append(specsRequested.get(specsRequested.size() - 1).getName().replace("_", "\\_"));
			builder.append(" are ");
		} else {
			builder.append(" is ");
		}
		builder.append("now available! Simply /party them and join a friends game!");
		return builder.toString();
		} catch (Exception e){
			e.printStackTrace();
		}
		return "";
		
	}
	
	public String getSpectatorStatus(){
		int available = 0;
		int inUse = 0;
		for(Spectator s : spectators){
			if(s.isInUse()){
				inUse++;
			} else {
				available++;
			}
		}
		
		return ("Spectators available: " + available + " | Spectators in use: " + inUse);
		
	}
	
	public String getPartiedSpectators(){
		StringBuilder status = new StringBuilder();
		for(Spectator s : spectators){
			if(s.isInUse()){
				status.append(s.getName() + ": " + s.getPartyLeader() + " (Requested by: " + s.getContainer().getOwnerName() + ")\n");
			}
		}
		if(!(status.length() == 0)){
			status.setLength(status.length() - 1);
			return status.toString();
		} else {
			return "No specs are currently in use!";
		}
	}
	
	public String recallContainer(String o, boolean force){
		for(SpectatorContainer c : containers){
			if(c.getOwner().equalsIgnoreCase(o)){
				c.finish(force);
				return "Recalled spectators!";
			}
		}
		return "You have not requested any spectators!";
	}
	
	public String recallContainerByName(String n, boolean force){
		for(SpectatorContainer c : containers){
			if(c.getOwnerName().equalsIgnoreCase(n)){
				c.finish(force);
				return "Recalled spectators!";
			}
		}
		return "No spectators have been requested by that user!";
	}
	
	public void recallAll(){
		ListIterator<SpectatorContainer> containerIterator = containers.listIterator();
		while(containerIterator.hasNext()){
			containerIterator.next().finish(true);
		}
		containers.clear();
	}
	
	public void removeContainer(SpectatorContainer c){
		containers.remove(c);
	}
	
	
	public void loadSpectators(){
		spectators.clear();
		File specList = new File("./specs.txt");
		if(!specList.exists()){
			try {
				specList.createNewFile();
			} catch (Exception e){
				
			}
		}
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(specList));
			String line;
			while((line = reader.readLine()) != null) {
				try {
					if(!line.startsWith("//")){
						String parts[] = line.split(":");
						spectators.add(new Spectator(parts[0], parts[1], parts[2]));
					}
				} catch (Exception e) {
					
				}
			}
			System.out.println("Registered " + spectators.size() + " spectators!");
			reader.close();
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
