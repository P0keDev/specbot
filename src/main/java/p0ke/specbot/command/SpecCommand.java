package p0ke.specbot.command;

import java.awt.Color;
import java.time.LocalDateTime;
import java.util.List;

import org.joda.time.Duration;

import p0ke.specbot.SpecBot;
import p0ke.specbot.util.RMessageBuilder;
import p0ke.specbot.util.UsageStats;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;

public class SpecCommand extends CommandBase {

	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {
		if(args.size() > 0){
			
			if(args.get(0).equalsIgnoreCase("status")){
				msg.withQuote(SpecBot.instance.specManager.getSpectatorStatus()).build();
			} else
			
			
			if(args.get(0).equalsIgnoreCase("stats")){
				EmbedBuilder eb = new EmbedBuilder();
				UsageStats stats = SpecBot.instance.usageStats;
				eb.setLenient(true);
				eb.withTitle("SpecBot Usage Stats");
				eb.withDesc("------------------------");
				
				eb.appendField("Total requests", "" + stats.getContainersRequested(), true);
				eb.appendField("Specs requested", "" + stats.getSpecsRequested(), true);
				eb.appendField("\u200B", "\u200B", true);
				//eb.appendField("Parties joined", "" + stats.getPartiesJoined(), true);
				eb.appendField("Games spectated", "" + stats.getGamesJoined(), true);
				Duration dur = stats.getIngameTime();
				eb.appendField("Time in-game", dur.getStandardDays() + "D:" + (dur.getStandardHours() % 24) + "H:" + (dur.getStandardMinutes() % 60) + "M:" + (dur.getStandardSeconds() % 60) + "S", true);
				eb.appendField("\u200B", "\u200B", true);
				eb.withColor(new Color(64, 128, 234));
				eb.withFooterIcon("http://i.imgur.com/RKJxOlH.png");
				eb.withFooterText("SpecBot");
				eb.withTimestamp(LocalDateTime.now());
				msg.withEmbed(eb.build()).build();
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
					
					if((n > 2 || n < 1) && !event.getMessage().getAuthor().getStringID().equals(("158865537848311809"))){
						msg.withContent("You can only request 1 or 2 bots!").build();
						return;
					}
				}
				setTypingStatus(event.getMessage().getChannel(), true);
				msg.withContent(SpecBot.instance.specManager.requestSpectators(event.getMessage().getAuthor().getStringID(), event.getMessage().getAuthor().getDisplayName(event.getMessage().getGuild()), n)).build();
				setTypingStatus(event.getMessage().getChannel(), false);
				
			} else
			
			
			if(args.get(0).equalsIgnoreCase("recall")){
				msg.withContent(SpecBot.instance.specManager.recallContainer(event.getMessage().getAuthor().getStringID(), false)).build();
			} else
				
				
			if(args.get(0).equalsIgnoreCase("forcerecall")){
				if(event.getMessage().getAuthor().getStringID().equals("158865537848311809")){
					if(event.getMessage().getMentions().isEmpty()){
						if(args.size() > 1){
							SpecBot.instance.specManager.recallContainerByName(args.get(1), true);
						} else {
							SpecBot.instance.specManager.recallAll();
						}
					} else {
						SpecBot.instance.specManager.recallContainer(event.getMessage().getMentions().get(0).getStringID(), true);
					}
				}
			} else
			
				
			if(args.get(0).equalsIgnoreCase("list")){
				msg.withQuote(SpecBot.instance.specManager.getPartiedSpectators()).build();
			} else
			
			if(args.get(0).equalsIgnoreCase("help")){
				StringBuilder builder = new StringBuilder();
				builder.append("!spec help            Displays this message\n");
				builder.append("!spec stats           Displays spec bot usage stats\n");
				builder.append("!spec status          Displays all available and in-use bots\n");
				builder.append("!spec list            Lists all bots in use\n");
				builder.append("!spec request [#]     Requests # bots (if no # is specified, 2 are requested)\n");
				builder.append("!spec recall          Recalls any bots you have requested");
				msg.withQuote(builder.toString()).build();
			} else
			
			
			if(args.get(0).equalsIgnoreCase("reload")){
				if(event.getMessage().getAuthor().getStringID().equals("158865537848311809")){
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
	
	public void setTypingStatus(IChannel c, boolean typing){
		for(int i = 0; i < 3; i++){
			try {
				c.setTypingStatus(typing);
				//return;
			} catch (Exception e){
				
			}
		}
	}

}
