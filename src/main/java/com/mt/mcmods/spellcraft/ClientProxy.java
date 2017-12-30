package com.mt.mcmods.spellcraft;


import com.mt.mcmods.spellcraft.Client.model.ModelDynWand;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
        //doesn't work like I excpected it to work... => I can pre-create my textures
        //ModelLoaderRegistry.registerLoader(ModelDynWand.LoaderDynBlockArmor.INSTANCE);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        this.items.clientInit();
        this.blocks.clientInit();
        OBJLoader.INSTANCE.addDomain(MODID);
    }

    @Override
    public boolean isClient() {
        return true;
    }

    private void mapTextures() {
        //create inventory icons
        int numIcons = ModelDynWand.BakedDynWandOverrideHandler.createInventoryIcons();
        Log.info("Created " + numIcons + " inventory icons for Wands!");
    }


}
