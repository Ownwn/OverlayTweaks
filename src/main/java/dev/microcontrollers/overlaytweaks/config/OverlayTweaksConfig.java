package dev.microcontrollers.overlaytweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.ConfigEntry;
import dev.isxander.yacl3.config.ConfigInstance;
import dev.isxander.yacl3.config.GsonConfigInstance;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class OverlayTweaksConfig {
    public static final ConfigInstance<OverlayTweaksConfig> INSTANCE = GsonConfigInstance.createBuilder(OverlayTweaksConfig.class)
            .setPath(FabricLoader.getInstance().getConfigDir().resolve("overlaytweaks.json"))
            .build();

    // Water and Fire

    @ConfigEntry public boolean removeWaterOverlay = true;
    @ConfigEntry public boolean removeWaterFov = true;
    @ConfigEntry public boolean removeFireOverlay = true;
    @ConfigEntry public double fireOverlayHeight = 0.0;
    @ConfigEntry public float customFireOverlayOpacity = 100F;

    // Held Items

    @ConfigEntry public float customShieldHeight = 0F;
    @ConfigEntry public float customShieldOpacity = 100F;
    @ConfigEntry public boolean colorShieldCooldown = false;

    // Screens and HUD

    @ConfigEntry public boolean shouldMoveHotbar = true;
    @ConfigEntry public float containerOpacity = (208/255F) * 100F;
    @ConfigEntry public boolean hideCrosshairInContainers = true;
    @ConfigEntry public boolean showCrosshairInPerspective = false;
    @ConfigEntry public boolean removeCrosshairBlending = false;
    @ConfigEntry public boolean useNormalCrosshair = false;
    @ConfigEntry public boolean useDebugCrosshair = false;
    @ConfigEntry public boolean fixDebugCooldown = true;
    @ConfigEntry public float tabPlayerListOpacity = (32/255F) * 100F;
    @ConfigEntry public boolean showPingInTab = false;
    @ConfigEntry public boolean hideFalsePing = false;
    @ConfigEntry public boolean disableTitles = false;
    @ConfigEntry public float titleScale = 100F;
    @ConfigEntry public boolean autoTitleScale = true;
    @ConfigEntry public float titleOpacity = 100F;

    // Effects

    @ConfigEntry public boolean cleanerNightVision = true;
    @ConfigEntry public boolean removeElderGuardianJumpscare = false;
    @ConfigEntry public boolean customVignetteDarkness = false;
    @ConfigEntry public float customVignetteDarknessValue = 0F;

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(INSTANCE, ((defaults, config, builder) -> builder
                .title(Text.literal("Overlay Tweaks"))

                // Water and Fire

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Water and Fire"))

                        // Water

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Water"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Water Overlay"))
                                        .description(OptionDescription.of(Text.of("Removes the underwater overlay when in water.")))
                                        .binding(defaults.removeWaterOverlay, () -> config.removeWaterOverlay, newVal -> config.removeWaterOverlay = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Underwater FOV Change"))
                                        .description(OptionDescription.of(Text.of("Stops the FOV from changing when you go underwater.")))
                                        .binding(defaults.removeWaterFov, () -> config.removeWaterFov, newVal -> config.removeWaterFov = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Fire

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Fire"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Fire Overlay When Resistant"))
                                        .description(OptionDescription.of(Text.of("Removes the fire overlay when you are resistant to fire, such as when you have fire resistance or are in creative mode.")))
                                        .binding(defaults.removeFireOverlay, () -> config.removeFireOverlay, newVal -> config.removeFireOverlay = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(double.class)
                                        .name(Text.literal("Fire Overlay Height"))
                                        .description(OptionDescription.of(Text.of("Change the height of the fire overlay if your pack does not have low fire. May improve visibility.")))
                                        .binding(0.0, () -> config.fireOverlayHeight, newVal -> config.fireOverlayHeight = newVal)
                                        .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                                .range(-0.5, 0.0)
                                                .step(0.01))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Fire Overlay Opacity"))
                                        .description(OptionDescription.of(Text.of("The value for fire overlay opacity.")))
                                        .binding(100F, () -> config.customFireOverlayOpacity, newVal -> config.customFireOverlayOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())
                        .build())

                // Held Items

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Held Items"))

                        // Shield

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Shield"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Shield Height"))
                                        .description(OptionDescription.of(Text.of("The value for shield height.")))
                                        .binding(0F, () -> config.customShieldHeight, newVal -> config.customShieldHeight = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.2f", value)))
                                                .range(-0.5F, 0F)
                                                .step(0.01F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Shield Opacity"))
                                        .description(OptionDescription.of(Text.of("The value for shield opacity.")))
                                        .binding(100F, () -> config.customShieldOpacity, newVal -> config.customShieldOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Color Shield Cooldown"))
                                        .description(OptionDescription.of(Text.of("Adds a color to your shield depending on the cooldown remaining")))
                                        .binding(defaults.colorShieldCooldown, () -> config.colorShieldCooldown, newVal -> config.colorShieldCooldown = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())

                // Screens and HUD

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Screens and HUD"))

                        // Inventory and Hotbar

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Inventory and Hotbar"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Move Hotbar Up"))
                                        .description(OptionDescription.of(Text.of("Moves the hotbar up by 2 pixels to show the entire hotbar on the screen with no cutoff. This may cause chat to interfere with hearts or armor status on smaller screens or GUI scales. Use the mod \"Chat Patches\" on Modrinth to move chat up. May cause issues with other mods.")))
                                        .binding(defaults.shouldMoveHotbar, () -> config.shouldMoveHotbar, newVal -> config.shouldMoveHotbar = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Container Background Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the transparency of the container background. Set to 0 to make it completely transparent.")))
                                        .binding((208/255F) * 100F, () -> config.containerOpacity, newVal -> config.containerOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())

                        // Crosshair

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Crosshair"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Hide Crosshair in Containers"))
                                        .description(OptionDescription.of(Text.of("Hides crosshair when a container is opened. Great for containers with translucent backgrounds.")))
                                        .binding(defaults.hideCrosshairInContainers, () -> config.hideCrosshairInContainers, newVal -> config.hideCrosshairInContainers = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Show Crosshair in Third Person"))
                                        .description(OptionDescription.of(Text.of("Shows the crosshair when in third person.")))
                                        .binding(defaults.showCrosshairInPerspective, () -> config.showCrosshairInPerspective, newVal -> config.showCrosshairInPerspective = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Crosshair Blending"))
                                        .description(OptionDescription.of(Text.of("Removes color blending on the crosshair, making it always white.")))
                                        .binding(defaults.removeCrosshairBlending, () -> config.removeCrosshairBlending, newVal -> config.removeCrosshairBlending = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Ignore Debug Mode"))
                                        .description(OptionDescription.of(Text.of("Uses the normal crosshair instead of the debug crosshair when F3 is open. Takes priority over \"Always Use Debug Mode\".")))
                                        .binding(defaults.useNormalCrosshair, () -> config.useNormalCrosshair, newVal -> config.useNormalCrosshair = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Always Use Debug Mode"))
                                        .description(OptionDescription.of(Text.of("Uses the debug crosshair instead of the normal crosshair regardless of when F3 is open. Requires \"Ignore Debug Mode\" to be disabled.")))
                                        .binding(defaults.useDebugCrosshair, () -> config.useDebugCrosshair, newVal -> config.useDebugCrosshair = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Fix Debug Cooldown Icon"))
                                        .description(OptionDescription.of(Text.of("Due to an oversight, the debug crosshair does not have an attack cooldown. This feature adds it back.")))
                                        .binding(defaults.fixDebugCooldown, () -> config.fixDebugCooldown, newVal -> config.fixDebugCooldown = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Player Tab List

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Player Tab List"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Tab Player List Background Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the transparency of the player list in tab. Set to 0 to make it completely transparent.")))
                                        .binding((32/255F) * 100F, () -> config.tabPlayerListOpacity, newVal -> config.tabPlayerListOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Show Numerical Ping"))
                                        .description(OptionDescription.of(Text.of("Replace the ping icon with a numerical value.")))
                                        .binding(defaults.showPingInTab, () -> config.showPingInTab, newVal -> config.showPingInTab = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Hide Fake Ping"))
                                        .description(OptionDescription.of(Text.of("Some servers force a ping of 0 or 1 or very high numbers to hide players ping. This will hide the number from being displayed as it is useless.")))
                                        .binding(defaults.hideFalsePing, () -> config.hideFalsePing, newVal -> config.hideFalsePing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Titles

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Titles"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Titles"))
                                        .description(OptionDescription.of(Text.of("Remove titles entirely.")))
                                        .binding(defaults.disableTitles, () -> config.disableTitles, newVal -> config.disableTitles = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Title Scale"))
                                        .description(OptionDescription.of(Text.of("Set the scale for titles.")))
                                        .binding(100F, () -> config.titleScale, newVal -> config.titleScale = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Automatically Scale Titles"))
                                        .description(OptionDescription.of(Text.of("Scale titles automatically if they go past the edges of your screen.")))
                                        .binding(defaults.autoTitleScale, () -> config.autoTitleScale, newVal -> config.autoTitleScale = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Title Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the opacity for titles.")))
                                        .binding(100F, () -> config.titleOpacity, newVal -> config.titleOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())
                        .build())

                // Effects

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Effects"))

                        // World

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("World"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Cleaner Night Vision Decay"))
                                        .description(OptionDescription.of(Text.of("Makes the night vision loss a gradual effect instead of an on and off flicker.")))
                                        .binding(defaults.cleanerNightVision, () -> config.cleanerNightVision, newVal -> config.cleanerNightVision = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Elder Guardian Jumpscare"))
                                        .description(OptionDescription.of(Text.of("Removed the elder guardian particle effect from showing on your screen.")))
                                        .binding(defaults.removeElderGuardianJumpscare, () -> config.removeElderGuardianJumpscare, newVal -> config.removeElderGuardianJumpscare = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Vignette

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Vignette"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Constant Vignette Darkness"))
                                        .description(OptionDescription.of(Text.of("Apply a constant vignette regardless of sky light level.")))
                                        .binding(defaults.customVignetteDarkness, () -> config.customVignetteDarkness, newVal -> config.customVignetteDarkness = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Custom Vignette Darkness Value"))
                                        .description(OptionDescription.of(Text.of("The value for constant vignette.")))
                                        .binding(0F, () -> config.customVignetteDarknessValue, newVal -> config.customVignetteDarknessValue = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())
                        .build())
        )).generateScreen(parent);
    }

}