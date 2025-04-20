package org.leavesmc.example.mixins;

import org.bukkit.craftbukkit.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public final class TemplateMixin {
    @Inject(method = "main", at = @At("HEAD"))
    private static void templateMixin(String[] args, CallbackInfo ci) {
        System.err.println("This is a template mixin. boot args: " + String.join(", ", args));
        System.err.println("PLEASE REMOVE THIS MIXIN AND REPLACE IT WITH YOUR OWN!");
    }
}
