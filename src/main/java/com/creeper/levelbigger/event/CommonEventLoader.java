package com.creeper.levelbigger.event;

import com.creeper.levelbigger.util.Reference;
import com.creeper.levelbigger.util.ScaleHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

@Mod.EventBusSubscriber
public class CommonEventLoader
{
    private static final UUID MOVE_SPEED_MODIFIER_NAME = UUID.nameUUIDFromBytes((Reference.MODID + "_move_speed").getBytes());
    private static final UUID REACH_DISTANCE_MODIFIER_NAME = UUID.nameUUIDFromBytes((Reference.MODID + "_reach_distance").getBytes());
    private static final UUID SWIM_SPEED_MODIFIER_NAME = UUID.nameUUIDFromBytes((Reference.MODID + "_swim_speed").getBytes());
    private static final UUID ATTACK_DAMAGE_MODIFIER_NAME = UUID.nameUUIDFromBytes((Reference.MODID + "_attack_damage").getBytes());
    private static final UUID MAX_HEALTH_MODIFIER_NAME = UUID.nameUUIDFromBytes((Reference.MODID + "_max_health").getBytes());

    private static final Map<UUID, Integer> PLAYER_LEVEL_MAP = new HashMap<>();
    private static final Map<UUID, Integer> CLIENT_PLAYER_LEVEL_MAP = new HashMap<>();

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event)
    {
        event.player.sendMessage(new TextComponentString(TextFormatting.YELLOW + "【苦力怕】成长体型  " + TextFormatting.GREEN+"已加载!"));
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.getEntityWorld();
        Map<UUID, Integer> LEVEL_MAP;
        if (!world.isRemote) {
            LEVEL_MAP = PLAYER_LEVEL_MAP;
        }
        else {
            LEVEL_MAP = CLIENT_PLAYER_LEVEL_MAP;
        }

        int level = player.experienceLevel;
        float width = 0.8f;
        float height = 1.8f;
        float eyeHeight = player.getDefaultEyeHeight();

        if (player.isSneaking())
        {
            height *= 0.91666666666f;
            eyeHeight *= 0.9382716f;
        }
        if (player.isElytraFlying())
        {
            height *= 0.33f;
        }
        if (player.isPlayerSleeping())
        {
            width = 0.2f;
            height = 0.2f;
        }

        width = MathHelper.clamp(width, 0.15f, width);
        height = MathHelper.clamp(height, 0.25f, height);

        width *= ScaleHandler.getScaleFromLevel(level);
        height *= ScaleHandler.getScaleFromLevel(level);

        player.width = width;
        player.height = height;
//        player.eyeHeight = eyeHeight;
        player.eyeHeight = eyeHeight * ScaleHandler.getEyeHeightFromLevel(level);
        double d0 = width / 2.0D;
        AxisAlignedBB aabb = player.getEntityBoundingBox();
        player.setEntityBoundingBox(new AxisAlignedBB(player.posX - d0, aabb.minY, player.posZ - d0,
                player.posX + d0, aabb.minY + height, player.posZ + d0));

        if (LEVEL_MAP.containsKey(player.getUniqueID()))
        {
            int oldLevel = LEVEL_MAP.get(player.getUniqueID());
            if (oldLevel != player.experienceLevel)
            {
                LEVEL_MAP.put(player.getUniqueID(), level);
                updatePlayerAttribute(player);
            }
        }
        else {
            LEVEL_MAP.put(player.getUniqueID(), level);
            updatePlayerAttribute(player);
        }
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event)
    {
        if(event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            float jumpHeight = ScaleHandler.getJumpDistanceFromLevel(player.experienceLevel);
            jumpHeight = MathHelper.clamp(jumpHeight, 0.65F, jumpHeight);
            if (jumpHeight > 1)
                player.motionY *= jumpHeight;

            if(player.isSneaking() || player.isSprinting())
            {
                if(player.height < 1.8F) player.motionY = 0.42F;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerFall(LivingFallEvent event)
    {
        if (event.getEntityLiving() instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer) event.getEntityLiving();
            float scale = ScaleHandler.getScaleFromLevel(player.experienceLevel);
            if (scale > 1)
                event.setDistance(event.getDistance() / (scale * 2));
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event)
    {
        EntityPlayer player = event.player;
        World world = player.getEntityWorld();
        Map<UUID, Integer> LEVEL_MAP;
        if (!world.isRemote) {
            LEVEL_MAP = PLAYER_LEVEL_MAP;
        }
        else {
            LEVEL_MAP = CLIENT_PLAYER_LEVEL_MAP;
        }

        LEVEL_MAP.remove(player.getUniqueID());
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (event instanceof PlayerInteractEvent.EntityInteract) {
            EntityPlayer player = event.getEntityPlayer();
            Entity entity = event.getEntity();
            float playerSize = player.width * player.height;
            float entitySize = entity.width * entity.height;

            if (player.getHeldItemMainhand().isEmpty() && entity instanceof EntityLivingBase)
            {
                if (playerSize * 2 > entitySize)
                {
                    entity.startRiding(player);
                }
                else {
                    player.startRiding(entity);
                }
            }
        }
        else if (event instanceof PlayerInteractEvent.RightClickBlock)
        {                               df yuz//////////
            EntityPlayer player = event.getEntityPlayer();
            player.dismountRidingEntity();
        }
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase entity = event.getEntityLiving();
        World world = event.getEntityLiving().world;
        if (entity instanceof EntityPlayer)
        {
            for(EntityLivingBase entities : world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox()))
            {
                if(!entity.isSneaking())
                {
                    if(entity.height / entities.height >= 4 && entities.getRidingEntity() != entity)
                    {
                        entities.attackEntityFrom(causeCrushingDamage(entity), entity.height - entities.height);
                    }
                }
            }
        }
    }

    private void updatePlayerAttribute(EntityPlayer player)
    {
        int level = player.experienceLevel;
        player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(MOVE_SPEED_MODIFIER_NAME);
        player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).
                applyModifier(new AttributeModifier(MOVE_SPEED_MODIFIER_NAME, Reference.MODID, ScaleHandler.getMovementSpeedFromLevel(level), 2).setSaved(true));

        player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(ATTACK_DAMAGE_MODIFIER_NAME);
        player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).
                applyModifier(new AttributeModifier(ATTACK_DAMAGE_MODIFIER_NAME, Reference.MODID, ScaleHandler.getAttackDamageFromLevel(level), 2).setSaved(true));

        player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(MAX_HEALTH_MODIFIER_NAME);
        player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).
                applyModifier(new AttributeModifier(MAX_HEALTH_MODIFIER_NAME, Reference.MODID, ScaleHandler.getMaxHealthFromLevel(level), 2).setSaved(true));


        player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).removeModifier(REACH_DISTANCE_MODIFIER_NAME);
        player.getEntityAttribute(EntityPlayer.REACH_DISTANCE).
                applyModifier(new AttributeModifier(REACH_DISTANCE_MODIFIER_NAME, Reference.MODID, ScaleHandler.getReachDistanceFromLevel(level), 2).setSaved(true));

        player.getEntityAttribute(EntityPlayer.SWIM_SPEED).removeModifier(SWIM_SPEED_MODIFIER_NAME);
        player.getEntityAttribute(EntityPlayer.SWIM_SPEED).
                applyModifier(new AttributeModifier(SWIM_SPEED_MODIFIER_NAME, Reference.MODID, ScaleHandler.getMovementSpeedFromLevel(level), 2).setSaved(true));


        player.jumpMovementFactor = ScaleHandler.getJumpMovementFromLevel(level);
    }

    private DamageSource causeCrushingDamage(EntityLivingBase entity)
    {
        return new EntityDamageSource(Reference.MODID + ".crushing", entity);
    }
}
