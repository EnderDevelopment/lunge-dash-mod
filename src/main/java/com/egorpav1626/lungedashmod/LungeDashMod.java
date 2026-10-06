package com.egorpav1626.lungedashmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public
class LungeDashMod implements ClientModInitializer {
    private static KeyBinding lungeDashKeyBinding;
    private static int previousSlot = -1;

    @Override
    public void onInitializeClient() {
        lungeDashKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
        "key.lunge_dash_mod.lunge_dash",
        InputUtil.Type.MOUSE,
        GLFW.GLFW_MOUSE_BUTTON_1,
        "category.lunge_dash_mod.lunge_dash"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (lungeDashKeyBinding.wasPressed()) {
                handleLungeDash(client);
            }
        });
    }

    private void handleLungeDash(MinecraftClient client) {
        if (client.player == null) return;

        PlayerInventory inventory = client.player.getInventory();
        int tridentSlot = -1;

        for (int i = 0; i < 9; i++) {
            if (inventory.getStack(i).getItem() == Items.TRIDENT) {
                tridentSlot = i;
                break;
            }
        }

        if (tridentSlot == -1) return;

        previousSlot = inventory.selectedSlot;
        inventory.selectedSlot = tridentSlot;

        client.player.swingHand(Hand.MAIN_HAND);
        client.player.setVelocity(client.player.getRotationVec(1.0f).multiply(2.0f));

        client.getTickScheduler().schedule(() -> {
            inventory.selectedSlot = previousSlot;
        }, 0);
    }
}
