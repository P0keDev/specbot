package p0ke.specbot.spectator;

import org.apache.commons.lang3.StringUtils;
import org.spacehq.mc.protocol.data.message.Message;
import org.spacehq.mc.protocol.packet.ingame.server.ServerChatPacket;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;

import p0ke.specbot.SpecBot;

public class GuildChatBot extends SessionAdapter {

	private Spectator parent;

	public GuildChatBot(Spectator s) {
		parent = s;
	}

	@Override
	public void packetReceived(PacketReceivedEvent event) {
		try {
			if (event.getPacket() instanceof ServerChatPacket) {
				Message message = event.<ServerChatPacket> getPacket().getMessage();
				String content = message.getFullText();
				if(content.startsWith("Guild >")){
					content = StringUtils.substringAfter(content, ": ");
					if(content.startsWith("!")){ // we got a command!
						SpecBot.instance.guildCommandHandler.handleCommand(event, content);
						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}