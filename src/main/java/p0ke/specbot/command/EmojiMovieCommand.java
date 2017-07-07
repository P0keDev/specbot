package p0ke.specbot.command;

import java.util.List;

import p0ke.specbot.util.EmojiMovieCountdown;
import p0ke.specbot.util.RMessageBuilder;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class EmojiMovieCommand extends CommandBase {

	@Override
	public void run(List<String> args, MessageReceivedEvent event, RMessageBuilder msg) throws Exception {

		msg.withContent(EmojiMovieCountdown.getCountdown()).build();
		
	}

}
