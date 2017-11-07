package p0ke.specbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import p0ke.specbot.command.CommandHandler;
import p0ke.specbot.spectator.SpectatorManager;
import p0ke.specbot.util.UsageStats;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;

public class SpecBot {
	
	public static String DISCORD_TOKEN;
	public static String HYPIXEL_API_KEY;
	public static String PASTEBIN_API_KEY;

	public static SpecBot instance;
	public IDiscordClient client;
	
	public CommandHandler commandHandler;
	public SpectatorManager specManager;
	
	public UsageStats usageStats;
	
	public List<String> banList = new ArrayList<String>();

	public static void main(String[] args) throws Exception {

		((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Discord4J.Discord4JLogger.Level.INFO);
		
		
		try {
			File tokenFile = new File("./token.txt");
			BufferedReader br = new BufferedReader(new FileReader(tokenFile));
			DISCORD_TOKEN = br.readLine();
			HYPIXEL_API_KEY = br.readLine();
			PASTEBIN_API_KEY = br.readLine();
			br.close();
		} catch (Exception e){
			System.out.println("Failed to read token.txt. Stacktrace below:");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			instance = login(DISCORD_TOKEN);
		} catch (Exception e) {
			System.out.println("Failed to connect to server. Stacktrace below:");
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	@EventSubscriber
	public void handle(ReadyEvent event){
		
		commandHandler = new CommandHandler();
		specManager = new SpectatorManager();
		
		usageStats = UsageStats.getStatsFromFile();
		
		try {
			File banFile = new File("./bans.txt");
			BufferedReader br = new BufferedReader(new FileReader(banFile));
			String line;
			while((line = br.readLine()) != null){
				banList.add(line.toLowerCase());
			}
			br.close();
		} catch (Exception e){
			System.out.println("Failed to read bans.txt. Stacktrade below:");
			e.printStackTrace();
			System.exit(1);
		}
		
		client.changePlayingText("Smash Heroes");
				
		System.out.println("Ready!");
		
	}
	
	@EventSubscriber
	public void onMessage(MessageReceivedEvent event){
		if(event.getMessage().getContent().startsWith("!") || event.getMessage().getContent().startsWith("/")){
			commandHandler.handleCommand(event);
		}
	}
	
	
	public static SpecBot login(String token) throws DiscordException { // Returns an instance of the Discord client
	    ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
	    clientBuilder.withToken(token); // Adds the login info 1to the builder
	    return new SpecBot(clientBuilder.login()); // Creates the client instance and logs the client in
	}
	
	public SpecBot(IDiscordClient c){
		this.client = c;
		c.getDispatcher().registerListener(this);
	}

}
