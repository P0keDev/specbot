package p0ke.specbot;

import p0ke.specbot.command.CommandHandler;
import p0ke.specbot.spectator.SpectatorManager;
import sx.blah.discord.Discord4J;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.util.DiscordException;

public class SpecBot {

	public static SpecBot instance;
	public IDiscordClient client;
	
	public CommandHandler commandHandler;
	public SpectatorManager specManager;

	public static void main(String[] args) throws Exception {

		((Discord4J.Discord4JLogger) Discord4J.LOGGER).setLevel(Discord4J.Discord4JLogger.Level.INFO);

		try {
			instance = login("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
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
		
		
		System.out.println("Ready!");
		
	}
	
	@EventSubscriber
	public void onMessage(MessageReceivedEvent event){
		if(event.getMessage().getContent().startsWith("!")){
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
