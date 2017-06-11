package p0ke.specbot.spectator;

import java.net.Proxy;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.event.session.SessionListener;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

public class Spectator {

	private String name;
	private String username;
	private String password;
	private String pLeader = "----";
	private boolean guildBot = false;
	private boolean inUse = false;
	private boolean inParty = false;
	private SpectatorContainer container = null;
	private Client client;
	private SessionListener specListener = null;
	private SessionListener guildListener = null;
	private DateTime login;
	Timer timer = new Timer();

	public Spectator(String n, String u, String p, boolean gbot) {
		name = n;
		username = u;
		password = p;
		guildBot = gbot;
		specListener = new SpectatorChatBot(this);
		if(guildBot){
			guildListener = new GuildChatBot(this);
			guildBotLogin();
			timer.schedule(new RelogTimer(this), 3600000, 3600000);
		}
	}

	public void assignContainer(SpectatorContainer c) {
		container = c;
		inUse = true;
	}
	
	public void isPartied(String n){
		inParty = true;
		pLeader = n;
		container.party(pLeader);
	}
	
	public String getPartyLeader(){
		return pLeader;
	}

	public String getName(){
		return name;
	}
	
	public boolean isInUse(){
		return inUse;
	}
	
	public boolean isInParty(){
		return inParty;
	}
	
	public boolean isGuildBot(){
		return guildBot;
	}
	
	public SpectatorContainer getContainer(){
		return container;
	}
	
	public Duration getIngameTime(){
		return new Duration(login, DateTime.now());
	}
	
	
	public void finish(boolean forced) {
		inUse = false;
		inParty = false;
		pLeader = "----";
		container = null;
		if(forced){
			client.getSession().send(new ClientChatPacket("/pchat Spectator session expired! Logging off."));
		}
		client.getSession().send(new ClientChatPacket("/p leave"));
		if(!guildBot){
			try {
				timer.schedule(new DisconnectTimer(client), 500);
			} catch (Exception e){
				e.printStackTrace();
			}
		} else {
			client.getSession().removeListener(specListener);
		}
	}

	public void login() {
		if(!guildBot){
			MinecraftProtocol protocol = null;
			try {
				protocol = new MinecraftProtocol(username, password, false);
				System.out.println("Successfully authenticated user.");
			} catch (RequestException e) {
				e.printStackTrace();
				return;
			}
	
			client = new Client("mc.hypixel.net", 25565, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
			client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
			client.getSession().connect();
		}
		
		client.getSession().addListener(specListener);
		login = DateTime.now();
		
	}
	
	public void guildBotLogin(){
		MinecraftProtocol protocol = null;
		try {
			protocol = new MinecraftProtocol(username, password, false);
			System.out.println("Successfully authenticated guild bot.");
		} catch (RequestException e) {
			e.printStackTrace();
			return;
		}

		client = new Client("mc.hypixel.net", 25565, protocol, new TcpSessionFactory(Proxy.NO_PROXY));
		client.getSession().setFlag(MinecraftConstants.AUTH_PROXY_KEY, Proxy.NO_PROXY);
		client.getSession().addListener(guildListener);
		client.getSession().connect();
	}
	
	public void guildBotFinish(){
		client.getSession().disconnect("Guild bot relog!");
	}
	
	public void guildBotRelog(){
		guildBotFinish();
		guildBotLogin();
	}
	
	public void kill(){
		timer.cancel();
	}
	
	class DisconnectTimer extends TimerTask {
		Client caller;
		
		public DisconnectTimer(Client c){
			caller = c;
		}
		
		
		@Override
		public void run() {
			caller.getSession().disconnect("Finished!");
		}
		
	}
	
	class RelogTimer extends TimerTask {

		Spectator parent;
		
		public RelogTimer(Spectator s){
			parent = s;
		}
		
		@Override
		public void run() {
			parent.guildBotRelog();
		}
		
	}

}
