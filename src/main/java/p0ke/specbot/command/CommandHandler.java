package p0ke.specbot.command;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import p0ke.specbot.util.RMessageBuilder;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;

public class CommandHandler {

	private static HashMap<String, CommandBase> commandRegistry = new HashMap<String, CommandBase>();
	private static List<String> cNames = new ArrayList<String>();

	public CommandHandler() {
		File commandList = new File("./commands.txt");
		if (!commandList.exists()) {
			try {
				commandList.createNewFile();
			} catch (Exception e) {

			}
		}
		try {

			BufferedReader reader = new BufferedReader(new FileReader(commandList));
			String line;
			while ((line = reader.readLine()) != null) {
				String parts[] = line.split(":");
				CommandBase ctr = (CommandBase) Class.forName("p0ke.specbot.command." + parts[parts.length - 1])
						.getConstructor().newInstance();
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < (parts.length - 1); i++) {
					commandRegistry.put(parts[i].toLowerCase(), ctr);
					builder.append(parts[i].toLowerCase() + "/");
				}
				builder.setLength(builder.length() - 1);
				cNames.add(builder.toString());
			}
			System.out.println("Registered " + commandRegistry.size() + " commands!");
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handleCommand(MessageReceivedEvent event) {
		try {
			List<String> args = Arrays
					.asList(StringUtils.substringAfter(event.getMessage().getContent(), " ").split(" "));
			if (args.size() == 1 && args.get(0).isEmpty()) {
				args = new ArrayList<String>();
			}
			String command = event.getMessage().getContent().split(" ")[0].substring(1).toLowerCase();
			RMessageBuilder msg = new RMessageBuilder(event.getClient()).withChannel(event.getMessage().getChannel());

			if (commandRegistry.containsKey(command)) {
				commandRegistry.get(command).run(args, event, msg);
				try {
					event.getMessage().delete();
				} catch (Exception e) {

				}
			}

		} catch (Exception e) {
			System.out.println("Error handling command!");
			e.printStackTrace();
		}

	}

	public static List<String> getCommands() {
		return cNames;
	}

}
