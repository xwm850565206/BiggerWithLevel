package com.creeper.levelbigger.event;

import com.creeper.levelbigger.util.ScaleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ClientEventLoader 
{
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerRenderPre(RenderPlayerEvent.Pre event)
    {
        EntityPlayer player = event.getEntityPlayer();
        int level = player.experienceLevel;
        float scale = ScaleHandler.getScaleFromLevel(level);
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(event.getX() / scale - event.getX(), event.getY() / scale - event.getX(), event.getZ() / scale - event.getZ());
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerRenderPost(RenderPlayerEvent.Post event)
    {
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onCameraSetup(EntityViewRenderEvent.CameraSetup event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        float scale = ScaleHandler.getScaleFromLevel(player.experienceLevel);

        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 1)
        {
            if(player.height > 1.8F) GlStateManager.translate(0, 0, -scale * 2);
        }

        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 2)
        {
            if(player.height > 1.8F) GlStateManager.translate(0, 0, scale * 2);
        }
    }
}
