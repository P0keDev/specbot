package p0ke.specbot.command.hypixel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;

import p0ke.specbot.command.hypixel.GCommandBase;

public class GCommandHandler {

	private static HashMap<String, GCommandBase> commandRegistry = new HashMap<String, GCommandBase>();
	private static List<String> cNames = new ArrayList<String>();

	public GCommandHandler() {
		File commandList = new File("./guildcommands.txt");
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
				GCommandBase ctr = (GCommandBase) Class
						.forName("p0ke.specbot.command.hypixel." + parts[parts.length - 1]).getConstructor()
						.newInstance();
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < (parts.length - 1); i++) {
					commandRegistry.put(parts[i].toLowerCase(), ctr);
					builder.append(parts[i].toLowerCase() + "/");
				}
				builder.setLength(builder.length() - 1);
				cNames.add(builder.toString());
			}
			System.out.println("Registered " + commandRegistry.size() + " hypixel commands!");
			reader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handleCommand(PacketReceivedEvent event, String content) {
		try {
			List<String> args = Arrays.asList(StringUtils.substringAfter(content, " ").split(" "));
			if (args.size() == 1 && args.get(0).isEmpty()) {
				args = new ArrayList<String>();
			}
			String command = content.split(" ")[0].substring(1).toLowerCase();

			if (commandRegistry.containsKey(command)) {
				commandRegistry.get(command).run(args, event);

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
