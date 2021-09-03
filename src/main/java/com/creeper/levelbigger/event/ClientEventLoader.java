package com.creeper.levelbigger.event;

import com.creeper.levelbigger.util.ScaleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
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
    public void onPlayerRenderPre(RenderLivingEvent.Pre event)
    {
        if (event.getEntity() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntity();
            int level = player.experienceLevel;
            float scale = ScaleHandler.getScaleFromLevel(level);
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.translate(event.getX() / scale - event.getX(), event.getY() / scale - event.getY(), event.getZ() / scale - event.getZ());
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onPlayerRenderPost(RenderLivingEvent.Post event)
    {
        if (event.getEntity() instanceof EntityPlayer) {
            GlStateManager.popMatrix();
        }
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

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onFOVChange(FOVUpdateEvent event) {
        if (event.getEntity() != null) {
            EntityPlayer player = event.getEntity();
            PotionEffect speed = player.getActivePotionEffect(MobEffects.SPEED);
            float fov = 1.0f;

            if (player.isSprinting()) {
                event.setNewfov(speed != null ? fov + ((0.1F * (speed.getAmplifier() + 1)) + 0.15F) : fov + 0.1F);
            } else {
                event.setNewfov(speed != null ? fov + (0.1F * (speed.getAmplifier() + 1)) : fov);
            }
        }
    }
}
