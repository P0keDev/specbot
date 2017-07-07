package p0ke.specbot.spectator;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.event.session.DisconnectedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

import p0ke.specbot.util.EmojiMovieCountdown;

public class SpectatorChatBot extends SessionAdapter {

	private Spectator parent;

	public SpectatorChatBot(Spectator s) {
		parent = s;
	}

	@Override
	public void packetReceived(PacketReceivedEvent event) {
		try {
			if (event.getPacket() instanceof ServerChatPacket) {
				Message message = event.<ServerChatPacket> getPacket().getMessage();
				String content = message.getFullText();
				if(content.contains("Friend request from")){
					System.out.println(content);
				}
				if (!content.contains(":")) {
					if (content.contains("has invited you to join") && content.contains("party")
							&& !parent.isInParty()) {
						content = content.replaceAll("-", "").trim();
						if (content.contains("'s")) {
							content = StringUtils.substringBeforeLast(content, "'s");
							if (content.contains("join [")) {
								content = StringUtils.substringAfterLast(content, "] ");
							} else {
								content = StringUtils.substringAfterLast(content, "join ");
							}
						} else {
							if (content.startsWith("[")) {
								content = StringUtils.substringAfter(content, "] ");
							}
							content = StringUtils.substringBefore(content, " has");
						}
						if (parent.getContainer().isPartied()
								&& !content.equalsIgnoreCase(parent.getContainer().getPartyLeader())) {
							return;
						}
						parent.isPartied(content);
						event.getSession().send(new ClientChatPacket("/p accept " + content));
						Thread.sleep(1000);
						if (parent.getContainer().sentIntro()) {
							event.getSession().send(new ClientChatPacket(
									"/pchat I'm SmashHeroesSpec, an automatic 1v1 spectator bot!"));
						}
					}

					if (content.contains("                              Smash Heroes")) {
						
						event.getSession().send(new ClientChatPacket("/lobby smash"));
						parent.getContainer().registerGame(DateTime.now());
					}

					if (content.endsWith("has disbanded the party!")
							|| content.startsWith("You have been kicked from the party")
							|| content.endsWith("the party has been disbanded.")) {
						if (!parent.getContainer().isFinished()) {
							parent.getContainer().finish(false);
						}
					}

				} else if(content.startsWith("From")){
					event.getSession().send(new ClientChatPacket(
							"/r " + EmojiMovieCountdown.getCountdown()));
					
				} else if(content.startsWith("§m---") && content.contains("Friend request from")){ // Why must this message have a : hypixel please fix this
					content = StringUtils.substringAfterLast(StringUtils.substringBeforeLast(content, "Click").trim(), " ").trim();
					event.getSession().send(new ClientChatPacket(
							"/f " + content));
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void disconnected(DisconnectedEvent event) {
		System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
		if (!parent.getContainer().isFinished()) {
			parent.getContainer().finish(false);
		}
		if (event.getCause() != null) {
			event.getCause().printStackTrace();
		}
	}

}