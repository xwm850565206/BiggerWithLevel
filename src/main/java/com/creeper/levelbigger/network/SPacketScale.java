package com.creeper.levelbigger.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class SPacketScale implements IMessage
{
    private int experienceLevel;
    private UUID playerUUID;

    public SPacketScale() {

    }

    public SPacketScale(int experienceLevel, UUID playerUUID) {
        this.experienceLevel = experienceLevel;
        this.playerUUID = playerUUID;
    }

    public int getExperienceLevel() {
        return experienceLevel;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        experienceLevel = packetBuffer.readInt();
        playerUUID = packetBuffer.readUniqueId();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer packetBuffer = new PacketBuffer(buf);
        packetBuffer.writeInt(experienceLevel);
        packetBuffer.writeUniqueId(playerUUID);
    }

    public static class Handler implements IMessageHandler<SPacketScale, IMessage> {
        @Override
        public IMessage onMessage(SPacketScale message, MessageContext ctx) {

            if (ctx.side != Side.CLIENT)
                return null;
            else {
                Minecraft.getMinecraft().addScheduledTask(() -> processMessage(message, ctx));
                return null;
            }
        }

        @SideOnly(Side.CLIENT)
        private void processMessage(SPacketScale message, MessageContext ctx)
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            EntityPlayer player = minecraft.world.getPlayerEntityByUUID(message.getPlayerUUID());
            if (player != null && !player.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID()))
                player.experienceLevel = message.getExperienceLevel();
        }
    }
}
