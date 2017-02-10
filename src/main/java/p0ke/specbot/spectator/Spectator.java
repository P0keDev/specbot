package p0ke.specbot.spectator;

import java.net.Proxy;

import org.spacehq.mc.auth.exception.request.RequestException;
import org.spacehq.mc.protocol.MinecraftConstants;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Client;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

public class Spectator {

	private String name;
	private String username;
	private String password;
	private boolean inUse = false;
	private boolean inParty = false;
	private SpectatorContainer container = null;
	private Client client;

	public Spectator(String n, String u, String p) {
		name = n;
		username = u;
		password = p;
	}

	public void assignContainer(SpectatorContainer c) {
		container = c;
		inUse = true;
	}
	
	public void isPartied(){
		inParty = true;
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
	
	public SpectatorContainer getContainer(){
		return container;
	}
	
	
	public void finish(boolean forced) {
		inUse = false;
		inParty = false;
		container = null;
		if(forced){
			client.getSession().send(new ClientChatPacket("/pchat Spectator session expired! Logging off."));
		}
		client.getSession().send(new ClientChatPacket("/p leave"));
		try {
			client.getSession().disconnect("Finished!");
		} catch (Exception e){
			
		}
	}

	public void login() {
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
		client.getSession().addListener(new SpectatorChatBot(this));
		client.getSession().connect();
	}

}
