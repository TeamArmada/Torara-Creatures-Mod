package rena.toraracreatures;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rena.toraracreatures.client.ClientModEventSubscriber;
import rena.toraracreatures.config.ConfigHolder;
import rena.toraracreatures.config.ToraraConfig;
import rena.toraracreatures.core.init.*;
import rena.toraracreatures.entities.ToraraSpawnPlacement;
import rena.toraracreatures.event.EntityEvent;
import software.bernie.geckolib3.GeckoLib;


@Mod(ToraraCreatures.MOD_ID)
@Mod.EventBusSubscriber(modid = ToraraCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ToraraCreatures {

    public static ToraraCreatures instance;

    public static final String NAME = "Torara Creatures";
    public static final String MOD_ID = "toraracreatures";
    public static final Logger LOGGER = LogManager.getLogger();

    public static TranslationTextComponent tTC(String displayText)
    {
        return new TranslationTextComponent(MOD_ID + "." + displayText);
    }

    public static ResourceLocation rL(String id)
    {
        return new ResourceLocation(MOD_ID, id);
    }


    public ToraraCreatures() {
        instance = this;

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        modEventBus.addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onBiomeLoadFromJSON);

        context.registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC, "toraracreatures.toml");
        ItemInit.ITEMS.register(modEventBus);
        BlockInit.BLOCKS.register(modEventBus);
        ContainerInit.CONTAINER.register(modEventBus);
        RecipeInit.RECIPES.register(modEventBus);
        TileEntityInit.TILE_ENTITY_TYPES.register(modEventBus);
        EntityInit.register();

        GeckoLib.initialize();
    }

    private void setup(final FMLCommonSetupEvent event) {
            ToraraSpawnPlacement.spawnPlacement();
    }

    public static final ItemGroup ITEM_GROUP = new ItemGroup("toraracreatures_items") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.BLACK_FEATHER.get());
        }
    };

    public static final ItemGroup BLOCK_GROUP = new ItemGroup("toraracreatures_blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.VOLCANIC_ROCK.get());
        }
    };

    public static final ItemGroup FOSSIL_GROUP = new ItemGroup("toraracreatures_eggs") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ItemInit.BROWN_FEATHER.get());
        }
    };

    @SubscribeEvent
    public void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();

        if (config.getSpec() == ConfigHolder.COMMON_SPEC) {
            ToraraConfig.bake(config);
        }
    }

    @SubscribeEvent
    public void onBiomeLoadFromJSON(BiomeLoadingEvent event) {
        EntityEvent.onBiomesLoad(event);
    }
}
