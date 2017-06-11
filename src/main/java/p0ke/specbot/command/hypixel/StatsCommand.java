package p0ke.specbot.command.hypixel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;

import com.google.gson.JsonObject;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;

public class StatsCommand extends GCommandBase {

	@Override
	public void run(List<String> args, PacketReceivedEvent event) throws Exception {
		if(args.size() > 0){
			String name = args.get(0);
			HypixelAPI.getInstance().setApiKey(UUID.fromString("87b042bc-cdc3-47ef-bb82-0aa5ab04fdb7"));
			
			Request request = RequestBuilder.newBuilder(RequestType.PLAYER).addParam(RequestParam.PLAYER_BY_NAME, name).createRequest();
			JsonObject stats = ((PlayerReply)HypixelAPI.getInstance().getSync(request)).getPlayer();
			
			JsonObject smashHeroes = stats.get("stats").getAsJsonObject().get("SuperSmash").getAsJsonObject();
			
			double kills = smashHeroes.get("kills").getAsDouble();
			double deaths = smashHeroes.get("deaths").getAsDouble();
			double wins = smashHeroes.get("wins").getAsDouble();
			double losses = smashHeroes.get("losses").getAsDouble();
			String kd = Double.toString(new BigDecimal(kills/deaths).setScale(2, RoundingMode.HALF_UP).doubleValue());
			String wl = Double.toString(new BigDecimal(wins/losses).setScale(2, RoundingMode.HALF_UP).doubleValue());
			
			String message = name + " | Wins: " + ((int) wins) + " | Kills: " + ((int) kills) + " | K/D: " + kd + " | W/L: " + wl;
			
			event.getSession().send(new ClientChatPacket("/gchat " + message));

			HypixelAPI.getInstance().finish();
		}
	}
	
	

}
