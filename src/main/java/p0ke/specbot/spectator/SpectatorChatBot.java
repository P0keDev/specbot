package p0ke.specbot.spectator;

import org.apache.commons.lang3.StringUtils;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

public class SpectatorChatBot extends SessionAdapter {
	
	private Spectator parent;
	
	public SpectatorChatBot(Spectator s){
		parent = s;
	}

	@Override
	public void packetReceived(PacketReceivedEvent event) {
		try {
			if (event.getPacket() instanceof ServerChatPacket) {
				Message message = event.<ServerChatPacket> getPacket().getMessage();
				String content = message.getFullText();
				if (content.contains("has invited you to join") && !parent.isInParty()) {
					parent.isPartied();
					if(content.contains("'s")){
						content = StringUtils.substringBeforeLast(content, "'s");
						if(content.contains("join [")){
							content = StringUtils.substringAfterLast(content, "] ");
						} else {
							content = StringUtils.substringAfterLast(content, "join ");
						}
					} else {
						if(content.startsWith("[")){
							content = StringUtils.substringAfter(content, "] ");
						}
						content = StringUtils.substringBefore(content, " has");
					}
					System.out.println("Partying with: " + content);
					event.getSession().send(new ClientChatPacket("/p accept " + content));
					Thread.sleep(1000);
					if(parent.getContainer().sentIntro()){
						event.getSession().send(new ClientChatPacket("/pchat I'm SmashHeroesSpec, an automatic 1v1 spectator bot!"));
					}
				} 
				
				if(content.contains("                              Smash Heroes")){
					event.getSession().send(new ClientChatPacket("/lobby smash"));
				}
				
				if(content.endsWith("has disbanded the party!") || content.startsWith("You have been kicked from the party")){
					parent.getContainer().finish(false);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	 @Override
     public void disconnected(DisconnectedEvent event) {
         System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
         if(event.getCause() != null) {
             event.getCause().printStackTrace();
         }
     }

}