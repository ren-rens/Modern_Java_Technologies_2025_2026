package bg.sofia.uni.fmi.mjt.music.command.creator;

import bg.sofia.uni.fmi.mjt.music.command.Command;

public class CommandCreator {

    public static Command newCommand(String clientInput) {
        String[] splitStrs = clientInput.trim().split("\\s+");

        if (splitStrs.length == 0) {
            return new Command("", new String[0]);
        }

        String commandName = splitStrs[0];
        String[] commandArgs = new String[splitStrs.length - 1];

        System.arraycopy(splitStrs, 1, commandArgs, 0, commandArgs.length);

        return new Command(commandName, commandArgs);
    }

    /*
    private static List<String> getCommandArguments(String clientInput) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        boolean insideQuote = false;

        for (char c : clientInput.toCharArray()) {
            if (c == '"') {
                insideQuote = !insideQuote;
            }
            if (c == ' ' && !insideQuote) { // when space is not inside quote split
                tokens.add(sb.toString().replace("\"", "")); // token is ready, let's add it to list
                sb.delete(0, sb.length()); // and reset StringBuilder`s content
            } else {
                sb.append(c); //else add character to token
            }
        }
        // let's not forget about last token that doesn't have space after it
        tokens.add(sb.toString().replace("\"", ""));

        return tokens;
    }

     */

}