package com.creeper.levelbigger.proxy;

import com.creeper.levelbigger.event.CommonEventLoader;
import com.creeper.levelbigger.network.NetworkLoader;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy
{
    public void preInit(FMLPreInitializationEvent event)
    {
        new NetworkLoader(event);
    }

    public void init(FMLInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new CommonEventLoader());
    }

    public void postInit(FMLPostInitializationEvent event)
    {

    }

    public void registerItemRenderer(Item item, int meta, String id)
    {

    }

    public void registerSameItemRenderer(Item item, int meta, String registerName, String id)
    {

    }

}
