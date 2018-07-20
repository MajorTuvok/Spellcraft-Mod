package mt.mcmods.spellcraft.client;


import mt.mcmods.spellcraft.CommonProxy;
import mt.mcmods.spellcraft.common.events.handlers.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        super();
    }

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
        EventHandler.registerClientEvents();
        this.items.clientInit();
        this.blocks.clientInit();
        this.conditions.clientInit();
        //OBJLoader.INSTANCE.addDomain(MODID);
    }

    @Override
    public boolean isClient() {
        return true;
    }


}
