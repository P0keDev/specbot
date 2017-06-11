package p0ke.specbot.command.hypixel;

import java.util.List;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;

public class HelloCommand extends GCommandBase {
	

	@Override
	public void run(List<String> args, PacketReceivedEvent event) throws Exception {
		event.getSession().send(new ClientChatPacket("/gchat Hello!"));
	}

}
