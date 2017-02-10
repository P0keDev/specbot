package p0ke.specbot.util;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class RMessageBuilder extends MessageBuilder {

	public RMessageBuilder(IDiscordClient client) {
		super(client);
	}

	@Override
	public RMessageBuilder appendCode(String language, String content) {
		super.appendCode(language, content);
		return this;
	}

	@Override
	public RMessageBuilder appendContent(String arg0, Styles... arg1) {
		super.appendContent(arg0, arg1);
		return this;
	}

	@Override
	public RMessageBuilder appendContent(String content) {
		super.appendContent(content);
		return this;
	}

	@Override
	public RMessageBuilder appendQuote(String content) {
		super.appendQuote(content);
		return this;
	}

	@Override
	public IMessage build() throws RateLimitException, DiscordException, MissingPermissionsException {
		IMessage r = null;
		for(int i = 0; i < 5; i++){
			try{
				r = super.build();
				break;
			} catch (Exception e){
				System.out.println("Message send failed, resending!");
			}
		}
		return r;
	}

	@Override
	public IChannel getChannel() {
		return super.getChannel();
	}

	@Override
	public String getContent() {
		return super.getContent();
	}

	@Override
	public IMessage send() throws RateLimitException, DiscordException, MissingPermissionsException {
		return super.send();
	}

	@Override
	public RMessageBuilder withChannel(IChannel channel) {
		super.withChannel(channel);
		return this;
	}

	@Override
	public RMessageBuilder withChannel(String channelID) {
		super.withChannel(channelID);
		return this;
	}

	@Override
	public RMessageBuilder withCode(String language, String content) {
		super.withCode(language, content);
		return this;
	}

	@Override
	public RMessageBuilder withContent(String content, Styles... styles) {
		super.withContent(content, styles);
		return this;
	}

	@Override
	public RMessageBuilder withContent(String content) {
		super.withContent(content);
		return this;
	}

	@Override
	public RMessageBuilder withEmbed(EmbedObject embed) {
		super.withEmbed(embed);
		return this;
	}

	@Override
	public RMessageBuilder withQuote(String content) {
		super.withQuote(content);
		return this;
	}

	@Override
	public RMessageBuilder withTTS() {
		super.withTTS();
		return this;
	}

}
