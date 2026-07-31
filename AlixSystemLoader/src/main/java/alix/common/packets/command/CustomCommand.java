package alix.common.packets.command;

import alix.common.commands.file.CommandsFileManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record CustomCommand(List<String> aliases, String argName) {

    public static CustomCommand of(String name, String argName) {
        var list = new ArrayList<>(Arrays.asList(CommandsFileManager.getAliases(name)));
        list.add(name);
        return new CustomCommand(list, argName);
    }
}