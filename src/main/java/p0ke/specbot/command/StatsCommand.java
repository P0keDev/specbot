package p0ke.specbot.command;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.jpaste.pastebin.PastebinPaste;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.hypixel.api.request.Request;
import net.hypixel.api.request.RequestBuilder;
import net.hypixel.api.request.RequestParam;
import net.hypixel.api.request.RequestType;
import p0ke.specbot.SpecBot;
import p0ke.specbot.util.RMessageBuilder;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class StatsCommand extends CommandBase {
	
	private static HashMap<String, LocalDateTime> cooldown = new HashMap<String, LocalDateTime>();

	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		
		if(cooldown.containsKey(event.getMessage().getAuthor().getID())){
			if(cooldown.get(event.getMessage().getAuthor().getID()).plusHours(24).isAfter(event.getMessage().getCreationDate())){
				msg.withContent("You may only request a player's stats once every 24 hours!").build();
				return;
			}
		}
		
		if(args.size() > 0){
			String name = args.get(0);
			
			HypixelAPI.getInstance().setApiKey(UUID.fromString(SpecBot.HYPIXEL_API_KEY));
			
			Request request = RequestBuilder.newBuilder(RequestType.PLAYER).addParam(RequestParam.PLAYER_BY_NAME, name).createRequest();
			JsonObject stats;
			try {
				stats = ((PlayerReply)HypixelAPI.getInstance().getSync(request)).getPlayer().getAsJsonObject("stats").getAsJsonObject("SuperSmash");
			} catch (NullPointerException e){
				msg.withContent("Not a valid username!").build();
				HypixelAPI.getInstance().finish();
				return;
			}
			
			HypixelAPI.getInstance().finish();
			
			try {
			
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				PastebinPaste paste = new PastebinPaste();
				paste.setPasteTitle(name + "'s stats");
				paste.setContents(gson.toJson(stats));
				paste.setPasteFormat("JSON");
				paste.setDeveloperKey(SpecBot.PASTEBIN_API_KEY);
				
				msg.withContent(paste.paste().getLink().toString()).build();
				
				cooldown.put(event.getMessage().getAuthor().getID(), event.getMessage().getCreationDate());
			
			} catch (Exception e){
				msg.withContent("Error creating pastebin link").build();
			}
			
		} else {
			msg.withContent("Please include a username!").build();
		}
	}
	
	

}
