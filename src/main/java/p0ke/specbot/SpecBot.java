package p0ke.specbot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import p0ke.specbot.command.discord.DCommandHandler;
import p0ke.specbot.command.hypixel.GCommandHandler;
import p0ke.specbot.spectator.SpectatorManager;
import p0ke.specbot.util.UsageStats;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.NickNameChangeEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Status;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class SpecBot {

	public static SpecBot instance;
	public IDiscordClient client;
	
	public DCommandHandler discordCommandHandler;
	public GCommandHandler guildCommandHandler;
	public SpectatorManager specManager;
	
	public UsageStats usageStats;

	public static void main(String[] args) throws Exception {

		((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Discord4J.Discord4JLogger.Level.INFO);
		
		String token = "";
		try {
			File tokenFile = new File("./token.txt");
			BufferedReader br = new BufferedReader(new FileReader(tokenFile));
			token = br.readLine();
			br.close();
		} catch (Exception e){
			System.out.println("Failed to read token.txt. Stacktrace below:");
			e.printStackTrace();
			System.exit(1);
		}

		try {
			instance = login(token);
		} catch (Exception e) {
			System.out.println("Failed to connect to server. Stacktrace below:");
			e.printStackTrace();
			System.exit(1);
		}

	}
	
	@EventSubscriber
	public void handle(ReadyEvent event){
		
		discordCommandHandler = new DCommandHandler();
		guildCommandHandler = new GCommandHandler();
		specManager = new SpectatorManager();
		
		usageStats = UsageStats.getStatsFromFile();
		
		client.changeStatus(Status.game("Smash Heroes"));
		
		
		System.out.println("Ready!");
		
	}
	
	
	
	
	@EventSubscriber
	public void onMessage(MessageReceivedEvent event){
		if(event.getMessage().getContent().startsWith("!") || event.getMessage().getContent().startsWith("/")){
			discordCommandHandler.handleCommand(event);
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
