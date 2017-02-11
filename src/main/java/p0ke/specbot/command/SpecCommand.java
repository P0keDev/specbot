package p0ke.specbot.command;

import java.util.List;

import p0ke.specbot.SpecBot;
import p0ke.specbot.util.RMessageBuilder;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class SpecCommand extends CommandBase {

	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		if(args.size() > 0){
			
			if(args.get(0).equalsIgnoreCase("status")){
				msg.withQuote(SpecBot.instance.specManager.getSpectatorStatus()).build();
			} else
			
			
			if(args.get(0).equalsIgnoreCase("request")){
				int n = 2;
				
				if(args.size() > 1){
					try {
						n = Integer.parseInt(args.get(1));
					} catch (Exception e){
						msg.withContent("Not a valid number!").build();
						return;
					}
					
					if(n > 2 || n < 1){
						msg.withContent("You can only request 1 or 2 bots!").build();
						return;
					}
				}
				event.getMessage().getChannel().setTypingStatus(true);
				msg.withContent(SpecBot.instance.specManager.requestSpectators(event.getMessage().getAuthor().getID(), n)).build();
				event.getMessage().getChannel().setTypingStatus(false);
				
			} else
			
			
			if(args.get(0).equalsIgnoreCase("recall")){
				msg.withContent(SpecBot.instance.specManager.recallContainer(event.getMessage().getAuthor().getID())).build();
			} else
			
				
			if(args.get(0).equalsIgnoreCase("forcerecall")){
				if(event.getMessage().getAuthor().getID().equals("158865537848311809")){
					SpecBot.instance.specManager.recallAll();
				}
			} else
			
			
			if(args.get(0).equalsIgnoreCase("help")){
				StringBuilder builder = new StringBuilder();
				builder.append("!spec help            Displays this message\n");
				builder.append("!spec status          Displays all available and in-use bots\n");
				builder.append("!spec request [#]     Requests # bots (if no # is specified, 2 are requested)\n");
				builder.append("!spec recall          Recalls any bots you have requested");
				msg.withQuote(builder.toString()).build();
			} else
			
			
			if(args.get(0).equalsIgnoreCase("reload")){
				if(event.getMessage().getAuthor().getID().equals("158865537848311809")){
					SpecBot.instance.specManager.loadSpectators();
				}
			} else 
			
			
			{
				msg.withContent("Use !spec help").build();
			}
			
			
			
		} else {
			msg.withContent("Use !spec help").build();
		}
	}
	
	

}
