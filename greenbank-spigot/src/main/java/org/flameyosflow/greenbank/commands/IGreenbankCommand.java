package org.flameyosflow.greenbank.commands;

import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.flameyosflow.greenbank.IGreenBank;
import org.flameyosflow.greenbank.api.entities.User;

import java.util.List;
import java.util.Map;

public interface IGreenbankCommand {
    String getName();

    Map<String, String> getUsageStrings();

    void run(Server server, User user, String commandLabel, Command cmd, String[] args) throws Exception;

    void run(Server server, CommandSource sender, String commandLabel, Command cmd, String[] args) throws Exception;

    List<String> tabComplete(Server server, User user, String commandLabel, Command cmd, String[] args);

    List<String> tabComplete(Server server, CommandSource sender, String commandLabel, Command cmd, String[] args);

    void setEssentials(IGreenBank ess);

    void showError(CommandSender sender, Throwable throwable, String commandLabel);
}
