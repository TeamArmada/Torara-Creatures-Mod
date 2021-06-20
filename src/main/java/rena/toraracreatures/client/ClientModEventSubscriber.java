package rena.toraracreatures.client;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import rena.toraracreatures.ToraraCreatures;
import rena.toraracreatures.client.gui.AnalyzerScreen;
import rena.toraracreatures.client.render.DickinsoniaRexRender;
import rena.toraracreatures.client.render.GreenlandSharkRender;
import rena.toraracreatures.client.render.WallFossilRenderer;
import rena.toraracreatures.common.container.AnalyzerContainer;
import rena.toraracreatures.entities.mobs.DickinsoniaRexEntity;
import rena.toraracreatures.entities.mobs.GreenlandSharkEntity;
import rena.toraracreatures.init.ContainerInit;
import rena.toraracreatures.init.EntityInit;

@Mod.EventBusSubscriber(modid = ToraraCreatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventSubscriber {

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.GREENLAND_SHARK,
                GreenlandSharkRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.DICKINSONIA_REX,
                DickinsoniaRexRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityInit.WALL_FOSSIL_ENTITY,
                WallFossilRenderer::new);

        //Machine
        ScreenManager.register(ContainerInit.ANALYZER_CONTAINER.get(), AnalyzerScreen::new);
    }

    @SubscribeEvent
    public static void entityAttributes(EntityAttributeCreationEvent event){
        event.put(EntityInit.GREENLAND_SHARK, GreenlandSharkEntity.createAttributes().build());
        event.put(EntityInit.DICKINSONIA_REX, DickinsoniaRexEntity.createAttributes().build());
    }

}
