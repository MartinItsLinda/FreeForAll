package me.angrypostman.freeforall.commands;

import me.angrypostman.freeforall.FreeForAll;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DelSpawnCommand implements CommandExecutor {

    private FreeForAll plugin = null;
    public DelSpawnCommand(FreeForAll plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        return false;
    }
}