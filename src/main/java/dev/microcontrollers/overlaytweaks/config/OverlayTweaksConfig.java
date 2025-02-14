package dev.microcontrollers.overlaytweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import dev.isxander.yacl3.gui.controllers.ColorController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;

public class OverlayTweaksConfig {
    public static final ConfigClassHandler<OverlayTweaksConfig> CONFIG = ConfigClassHandler.createBuilder(OverlayTweaksConfig.class)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("overlaytweaks.json"))
                    .build())
            .build();

    // Screens

    @SerialEntry public float containerOpacity = (208/255F) * 100F;
    @SerialEntry public float containerTextureOpacity = 100F;
    @SerialEntry public int containerSize = 1;
    @SerialEntry public boolean disableScreenBobbing = false;
    @SerialEntry public boolean disableHandBobbing = false;
    @SerialEntry public boolean disableMapBobbing = true;
    @SerialEntry public boolean disableScreenDamage = false;
    @SerialEntry public boolean disableHandDamage = false;
    @SerialEntry public boolean disableHandViewSway = false;
    //#if MC <= 1.20.4
    @SerialEntry public float tabBackgroundOpacity = 100F;
    //#endif
    @SerialEntry public float suffocationOverlayBrightness = 10F;

    // HUD

    @SerialEntry public float customShieldHeight = 0F;
    @SerialEntry public float customShieldOpacity = 100F;
    @SerialEntry public boolean colorShieldCooldown = false;
    @SerialEntry public Color shieldColorHigh = new Color(1F, 0F, 0F);
    @SerialEntry public Color shieldColorMid = new Color(0.75F, 0.37F, 0.2F);
    @SerialEntry public Color shieldColorLow = new Color(1F, 1F, 0F);
    @SerialEntry public boolean disableTotemOverlay = false;
    @SerialEntry public boolean disableTotemParticles = false;
    @SerialEntry public float totemScale = 1.0F;
    @SerialEntry public boolean removeWaterOverlay = true;
    @SerialEntry public boolean removeWaterFov = true;
    @SerialEntry public boolean removeFireOverlay = true;
    @SerialEntry public double fireOverlayHeight = 0.0;
    @SerialEntry public float customFireOverlayOpacity = 100F;
    @SerialEntry public boolean removeItemTooltip = false;
    @SerialEntry public boolean keepHand = false;
    @SerialEntry public boolean hideScoreboardInDebug = false;
    @SerialEntry public boolean classicDebugStyle = false;
    @SerialEntry public Color subtitleColor = new Color(0F, 0F, 0F, 1F);

    // Effects

    @SerialEntry public boolean potionGlint = false;
    @SerialEntry public boolean staticItems = false;
    @SerialEntry public boolean unstackedItems = false;
    @SerialEntry public boolean cleanerNightVision = true;
    @SerialEntry public boolean cleanerSkyDarkness = true;
    @SerialEntry public Color lightningColor = new Color(0.45F, 0.45F, 0.5F, 0.3F);
    @SerialEntry public Color worldBorderColor = new Color(1F, 1F, 1F, 1F);
    @SerialEntry public float elderGuardianOpacity = 100F;
    @SerialEntry public float elderGuardianScale = 1F;
    @SerialEntry public float horseOpacity = 100F;
    @SerialEntry public float pigOpacity = 100F;
    @SerialEntry public float striderOpacity = 100F;
    @SerialEntry public float camelOpacity = 100F;
    @SerialEntry public float pumpkinOpacity = 100F;
    @SerialEntry public float freezingOpacity = 100F;
    @SerialEntry public float spyglassOpacity = 100F;
    @SerialEntry public Color spyglassColor = new Color(-16777216);
    @SerialEntry public boolean customVignetteDarkness = false;
    @SerialEntry public float customVignetteDarknessValue = 0F;

    @SuppressWarnings("deprecation")
    public static Screen configScreen(Screen parent) {
        return YetAnotherConfigLib.create(CONFIG, ((defaults, config, builder) -> builder
                .title(Text.literal("Overlay Tweaks"))

                // Screens

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Screens"))

                        // Containers

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Containers"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Container Background Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the transparency of container backgrounds. Set to 0 to make it completely transparent.")))
                                        .binding((208/255F) * 100F, () -> config.containerOpacity, newVal -> config.containerOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Container Texture Opacity"))
                                        .description(OptionDescription.of(Text.of("Set the transparency of container textures. Set to 0 to make it completely transparent.")))
                                        .binding(100F, () -> config.containerTextureOpacity, newVal -> config.containerTextureOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(int.class)
                                        .name(Text.literal("Container Scale"))
                                        .description(OptionDescription.of(Text.of("Change container scaling. Some values may make containers bigger than your screen. Set to 1 to use the default.")))
                                        .binding(2, () -> config.containerSize, newVal -> config.containerSize = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%01d", value) + "x"))
                                                .range(1, 4)
                                                .step(1))
                                        .build())
                                .build())

                        // Shake

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Screen Shake"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Screen Bobbing"))
                                        .description(OptionDescription.of(Text.of("Disables the screen shake when moving.")))
                                        .binding(defaults.disableScreenBobbing, () -> config.disableScreenBobbing, newVal -> config.disableScreenBobbing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Hand Bobbing"))
                                        .description(OptionDescription.of(Text.of("Disables the hand shake when moving.")))
                                        .binding(defaults.disableHandBobbing, () -> config.disableHandBobbing, newVal -> config.disableHandBobbing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Map Bobbing"))
                                        .description(OptionDescription.of(Text.of("Disables the hand shake when holding a map. Does nothing if \"Disable Hand Bobbing\" is turned on.")))
                                        .binding(defaults.disableMapBobbing, () -> config.disableMapBobbing, newVal -> config.disableMapBobbing = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Screen Damage Tilt"))
                                        .description(OptionDescription.of(Text.of("Disables the screen shake when taking damage.")))
                                        .binding(defaults.disableScreenDamage, () -> config.disableScreenDamage, newVal -> config.disableScreenDamage = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Hand Damage Tilt"))
                                        .description(OptionDescription.of(Text.of("Disables the hand shake when taking damage.")))
                                        .binding(defaults.disableHandDamage, () -> config.disableHandDamage, newVal -> config.disableHandDamage = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Hand View Sway"))
                                        .description(OptionDescription.of(Text.of("Disables the hand view sway when moving your mouse.")))
                                        .binding(defaults.disableHandViewSway, () -> config.disableHandViewSway, newVal -> config.disableHandViewSway = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // GUIs

                        //#if MC <= 1.20.4
                        //$$ .group(OptionGroup.createBuilder()
                        //$$    .name(Text.literal("Tab Background"))
                        //$$    .option(Option.createBuilder(float.class)
                        //$$            .name(Text.literal("Tab Background Opacity"))
                        //$$            .description(OptionDescription.of(Text.of("Replaces the black bar background in tab navigation screens. Use 60% for a value similar to Benonardo's standalone mod.")))
                        //$$            .binding(10F, () -> config.tabBackgroundOpacity, newVal -> config.tabBackgroundOpacity = newVal)
                        //$$            .controller(opt -> FloatSliderControllerBuilder.create(opt)
                        //$$                    .valueFormatter(value -> Text.of(String.format("%,.0f", value)))
                        //$$                    .range(0F, 100F)
                        //$$                    .step(1F))
                        //$$            .build())
                        //$$    .build())
                        //#endif

                        // Suffocation Overlay

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Suffocation Overlay"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Change Suffocation Overlay Brightness"))
                                        .description(OptionDescription.of(Text.of("Change Suffocation Overlay Brightness")))
                                        .binding(10F, () -> config.suffocationOverlayBrightness, newVal -> config.suffocationOverlayBrightness = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value)))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())
                        .build())

                // HUD

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("HUD"))

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
                                        .description(OptionDescription.of(Text.of("The value for shield opacity. Currently does not work for shields with banner designs.")))
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
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Shield Color High"))
                                        .binding(defaults.shieldColorHigh, () -> config.shieldColorHigh, value -> config.shieldColorHigh = value)
                                        .customController(opt -> new ColorController(opt, false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Shield Color Mid"))
                                        .binding(defaults.shieldColorMid, () -> config.shieldColorMid, value -> config.shieldColorMid = value)
                                        .customController(opt -> new ColorController(opt, false))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Shield Color Low"))
                                        .binding(defaults.shieldColorLow, () -> config.shieldColorLow, value -> config.shieldColorLow = value)
                                        .customController(opt -> new ColorController(opt, false))
                                        .build())
                                .build())

                        // Totem

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Totem"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Totem Overlay"))
                                        .description(OptionDescription.of(Text.of("Removes the totem overlay animation entirely.")))
                                        .binding(defaults.disableTotemOverlay, () -> config.disableTotemOverlay, newVal -> config.disableTotemOverlay = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Disable Totem Particles"))
                                        .description(OptionDescription.of(Text.of("Removes the totem particles when a totem is used.")))
                                        .binding(defaults.disableTotemParticles, () -> config.disableTotemParticles, newVal -> config.disableTotemParticles = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Totem Scale"))
                                        .description(OptionDescription.of(Text.of("The scale at which to render the totem overlay animation.")))
                                        .binding(1F, () -> config.totemScale, newVal -> config.totemScale = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .range(0.1F, 2F)
                                                .step(0.1F))
                                        .build())
                                .build())

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

                        // Hotbar

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Hotbar"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Remove Held Item Name Tooltip"))
                                        .description(OptionDescription.of(Text.of("Removes the tooltip above the hotbar when holding an item.")))
                                        .binding(defaults.removeItemTooltip, () -> config.removeItemTooltip, newVal -> config.removeItemTooltip = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Keep Hand in Hidden HUD"))
                                        .description(OptionDescription.of(Text.of("Keep your hand/held item in view when hiding hud (pressing F1).")))
                                        .binding(defaults.keepHand, () -> config.keepHand, newVal -> config.keepHand = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // F3 Debug

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Debug"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Hide Scoreboard In Debug Menu"))
                                        .description(OptionDescription.of(Text.of("Removes the scoreboard when you have the F3 menu open.")))
                                        .binding(defaults.hideScoreboardInDebug, () -> config.hideScoreboardInDebug, newVal -> config.hideScoreboardInDebug = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Classic Debug Style"))
                                        .description(OptionDescription.of(Text.of("Removes the background of the F3 text and renders the text with a shadow instead.")))
                                        .binding(defaults.classicDebugStyle, () -> config.classicDebugStyle, newVal -> config.classicDebugStyle = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

                        // Subtitle

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Subtitles"))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Subtitle Background Color"))
                                        .binding(defaults.subtitleColor, () -> config.subtitleColor, value -> config.subtitleColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .build())


                        .build())

                // Effects

                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Effects"))

                        // Item

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Items"))
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Potion Glint"))
                                        .description(OptionDescription.of(Text.of("Add the potion glint that was removed in 1.19.4.")))
                                        .binding(defaults.potionGlint, () -> config.potionGlint, newVal -> config.potionGlint = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Static Items"))
                                        .description(OptionDescription.of(Text.of("Remove the bobbing of items dropped on the ground.")))
                                        .binding(defaults.staticItems, () -> config.staticItems, newVal -> config.staticItems = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Unstacked Items"))
                                        .description(OptionDescription.of(Text.of("Always render one item per stack of dropped items.")))
                                        .binding(defaults.unstackedItems, () -> config.unstackedItems, newVal -> config.unstackedItems = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())

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
                                        .name(Text.literal("Cleaner Sky Void Darkness"))
                                        .description(OptionDescription.of(Text.of("Lowers the required height for the sky to turn black before getting closer to the void. This prevents the sky turning black at a high Y value.")))
                                        .binding(defaults.cleanerSkyDarkness, () -> config.cleanerSkyDarkness, newVal -> config.cleanerSkyDarkness = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Lightning Bolt Color"))
                                        .description(OptionDescription.of(Text.of("Change the color of lightning bolts.")))
                                        .binding(defaults.lightningColor, () -> config.lightningColor, value -> config.lightningColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("World Border Color"))
                                        .description(OptionDescription.of(Text.of("Allows setting a custom color for the world border.")))
                                        .binding(defaults.worldBorderColor, () -> config.worldBorderColor, value -> config.worldBorderColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Elder Guardian Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the elder guardian particle effect from showing on your screen.")))
                                        .binding(100F, () -> config.elderGuardianOpacity, newVal -> config.elderGuardianOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Elder Guardian Scale"))
                                        .description(OptionDescription.of(Text.of("Changes the scale of the elder guardian particle effect from showing on your screen.")))
                                        .binding(1F, () -> config.elderGuardianScale, newVal -> config.elderGuardianScale = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.1f", value) + "%"))
                                                .range(0.1F, 3F)
                                                .step(0.1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Ridden Horse Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the horse you are currently riding.")))
                                        .binding(100F, () -> config.horseOpacity, newVal -> config.horseOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Ridden Pig Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the pig you are currently riding.")))
                                        .binding(100F, () -> config.pigOpacity, newVal -> config.pigOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Ridden Strider Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the strider you are currently riding.")))
                                        .binding(100F, () -> config.striderOpacity, newVal -> config.striderOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Ridden Camel Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the camel you are currently riding.")))
                                        .binding(100F, () -> config.camelOpacity, newVal -> config.camelOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .build())

                        // Full Screen Effects

                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Full Screen Effects"))
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Pumpkin Overlay Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the pumpkin overlay when wearing a pumpkin.")))
                                        .binding(100F, () -> config.pumpkinOpacity, newVal -> config.pumpkinOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Freezing Overlay Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the maximum opacity of the freezing overlay when inside powdered snow. It will take the same amount of time to reach the maximum opacity as vanilla does.")))
                                        .binding(100F, () -> config.freezingOpacity, newVal -> config.freezingOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Spyglass Overlay Opacity"))
                                        .description(OptionDescription.of(Text.of("Changes the opacity of the spyglass overlay.")))
                                        .binding(100F, () -> config.spyglassOpacity, newVal -> config.spyglassOpacity = newVal)
                                        .controller(opt -> FloatSliderControllerBuilder.create(opt)
                                                .valueFormatter(value -> Text.of(String.format("%,.0f", value) + "%"))
                                                .range(0F, 100F)
                                                .step(1F))
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Spyglass Background Color"))
                                        .description(OptionDescription.of(Text.of("Allows setting a custom color for the background of nametags.")))
                                        .binding(defaults.spyglassColor, () -> config.spyglassColor, value -> config.spyglassColor = value)
                                        .customController(opt -> new ColorController(opt, true))
                                        .build())
                                .option(Option.createBuilder(boolean.class)
                                        .name(Text.literal("Constant Vignette Darkness"))
                                        .description(OptionDescription.of(Text.of("Apply a constant vignette regardless of sky light level.")))
                                        .binding(defaults.customVignetteDarkness, () -> config.customVignetteDarkness, newVal -> config.customVignetteDarkness = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.createBuilder(float.class)
                                        .name(Text.literal("Constant Vignette Darkness Value"))
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