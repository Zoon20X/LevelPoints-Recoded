package levelpoints.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class lpsTabComplete implements TabCompleter {
    private static final String[] args1 = { "rank", "player"};


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        final List<String> completions = new ArrayList<>();

        
        Collections.sort(completions);
        return completions;
    }
}
