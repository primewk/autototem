package me.prime;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;

import java.util.concurrent.TimeUnit;

public class Autototem implements ClientModInitializer {
    private static boolean isEnabled = true;

    @Override
    public void onInitializeClient() {
        // Register command
        CommandRegistry.registerCommand(new AutoTotemCommand());

        // Register event handler
        MinecraftClient.getInstance().execute(() -> {
            ClientPlayerEntity player = MinecraftClient.getInstance().player;
            player.events.invoker().onInventoryChanged((inventory, slot) -> {
                if (isEnabled && slot.getStack().getItem() == Items.TOTEM_OF_UNDYING) {
                    // Check if totem was used up
                    if (slot.getStack().isEmpty()) {
                        // Delay to avoid instant replacement
                        MinecraftClient.getInstance().execute(() -> {
                            replaceTotem(player);
                        }, 20); // 1 second delay
                    }
                }
            });
        });
    }

    private static void replaceTotem(ClientPlayerEntity player) {
        // Find a Totem of Undying in the player's inventory
        for (int i = 0; i < player.getInventory().size(); i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.getItem() == Items.TOTEM_OF_UNDYING) {
                // Move totem to offhand
                player.getInventory().setStack(40, stack); // 40 is the offhand slot
                break;
            }
        }
    }

    // Command class
    public static class AutoTotemCommand {
        public static void execute(String[] args) {
            if (args.length == 2 && args[0].equals("toggle")) {
                isEnabled = Boolean.parseBoolean(args[1]);
                System.out.println("AutoTotem is now " + (isEnabled ? "enabled" : "disabled"));
            } else {
                System.out.println("Usage: .autototem toggle <true/false>");
            }
        }
    }
}