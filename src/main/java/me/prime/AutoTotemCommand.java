package me.prime;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;

public class AutoTotemCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        Autototem.AutoTotemCommand.execute(context.getInput().split(" "));
        return 0;
    }
}
