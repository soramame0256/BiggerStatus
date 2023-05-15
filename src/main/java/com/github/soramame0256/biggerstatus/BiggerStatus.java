package com.github.soramame0256.biggerstatus;

import com.wynntils.modules.utilities.configs.UtilitiesConfig;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.minecraft.client.renderer.GlStateManager.scale;

@Mod(
        modid = BiggerStatus.MOD_ID,
        name = BiggerStatus.MOD_NAME,
        version = BiggerStatus.VERSION
)
public class BiggerStatus {

    public static final String MOD_ID = "biggerstatus";
    public static final String MOD_NAME = "BiggerStatus";
    public static final String VERSION = "1.0-SNAPSHOT";
    public static final Pattern HEALTH_NAME_TAG_REG = Pattern.compile("§4\\[§.?\\|§?.?\\|§?.?\\|§?.?\\|§?.?\\|.*§?.?\\|§?.?\\|§?.?\\|§?.?\\|§?.?\\|§4] (?<status>.*)");

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static BiggerStatus INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }
    @SubscribeEvent
    public void onTick(RenderLivingEvent.Specials.Pre<EntityLiving> e) {
        Matcher matcher;
        if ((matcher = HEALTH_NAME_TAG_REG.matcher(e.getEntity().getCustomNameTag())).matches()) {
            String s = matcher.group("status");
            //System.out.println(e.getY()+e.getEntity().height + " nm" + e.getEntity().toString());
            drawNameplate(e.getRenderer().getFontRendererFromRenderManager(), s, (float) e.getX(), (float) (e.getY()+e.getEntity().height + 3F), (float) e.getZ(),0,e.getRenderer().getRenderManager().playerViewY,e.getRenderer().getRenderManager().playerViewX,e.getRenderer().getRenderManager().options.thirdPersonView == 2,false,6f);
        }
    }
    /**
     * Taken from Wynntils and modified on 2023/05/14 under GNU Affero General Public License v3.0
     * https://github.com/Wynntils/Wynntils/blob/development/LICENSE
     * @author Wynntils
     */
    public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, float scale)
    {
        GlStateManager.pushMatrix();
        if (scale != 1) scale(scale, scale, scale);
        GlStateManager.translate(x/scale, y/scale, z/scale);
        verticalShift = (int)(verticalShift/scale);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        scale(-0.025F, -0.025F, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        if (!isSneaking && !UtilitiesConfig.INSTANCE.hideNametags)
        {
            GlStateManager.disableDepth();
        }
        if (!UtilitiesConfig.INSTANCE.hideNametagBox) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int i = fontRendererIn.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(-i - 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            bufferbuilder.pos(-i - 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            bufferbuilder.pos(i + 1, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            bufferbuilder.pos(i + 1, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }
}
