package net.bagelvulture.properpaper.screen;

import net.bagelvulture.properpaper.ProperPaper;
import net.bagelvulture.properpaper.screen.custom.DryingRackScreenHandler;
import net.bagelvulture.properpaper.screen.custom.SieveScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class ModScreenHandlers {
    public static final ScreenHandlerType<SieveScreenHandler> SIEVE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(ProperPaper.MOD_ID, "sieve_screen_handler"),
                    new ExtendedScreenHandlerType<>(SieveScreenHandler::new, BlockPos.PACKET_CODEC));
    public static final ScreenHandlerType<DryingRackScreenHandler> DRYING_RACK_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, Identifier.of(ProperPaper.MOD_ID, "drying_rack_screen_handler"),
                    new ExtendedScreenHandlerType<>(DryingRackScreenHandler::new, BlockPos.PACKET_CODEC));

    public static void registerScreenHandlers() {}
}