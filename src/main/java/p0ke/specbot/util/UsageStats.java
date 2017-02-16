package p0ke.specbot.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Type;

import org.joda.time.Duration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class UsageStats {
	
	private int specsRequested;
	private int containersRequested;
	private int partiesJoined;
	private int gamesJoined;
	private Duration ingameTime;
	public static File statsFile = new File("./stats.json");
	public static Gson gson = new Gson();
	
	
	
	public int getSpecsRequested(){
		return specsRequested;
	}
	
	public int getContainersRequested(){
		return containersRequested;
	}
	
	public int getPartiesJoined(){
		return partiesJoined;
	}
	
	public int getGamesJoined(){
		return gamesJoined;
	}
	
	public Duration getIngameTime(){
		return ingameTime;
	}
	
	
	public void addSpecsRequested(int n){
		specsRequested += n;
	}
	
	public void addContainersRequested(int n){
		containersRequested += n;
	}
	
	public void addPartiesJoined(int n){
		partiesJoined += n;
	}
	
	public void addIngameTime(Duration d){
		ingameTime = ingameTime.withDurationAdded(d, 1);
	}
	
	public void addGamesJoined(int n){
		gamesJoined += n;
	}
	
	
	
	public void save(){
		try {
			Writer writer = new FileWriter(statsFile);
			writer.write(gson.toJson(this));
			writer.close();
		} catch(Exception e){
			System.out.println("Could not save stats to file. Stacktrace below:");
			e.printStackTrace();
		}
		
	}
	
	
	public static UsageStats getStatsFromFile(){
		UsageStats stats = new UsageStats();
		try {
			if(!statsFile.exists()){
				statsFile.createNewFile();
			}
			
			
			BufferedReader reader = new BufferedReader(new FileReader(statsFile));
			Type type = new TypeToken<UsageStats>(){}.getType();
			stats = gson.fromJson(reader, type);
			if(stats == null){
				stats = new UsageStats();
			}
			stats.save();
			System.out.println("Loaded usage stats!");
		} catch (Exception e){
			System.out.println("Error loading usage stats from file. Stacktrace below:");
			e.printStackTrace();
		}
		
		return stats;
		
	}
	
	public UsageStats(){
		specsRequested = 0;
		containersRequested = 0;
		partiesJoined = 0;
		gamesJoined = 0;
		ingameTime = Duration.ZERO;
	}

}
