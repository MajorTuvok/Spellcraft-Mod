package mt.mcmods.spellcraft.server;


import mt.mcmods.spellcraft.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ServerProxy extends CommonProxy {
    public ServerProxy() {
        super();
    }

    @Override
    public boolean isClient() {
        return false;
    }

    @Override
    public void init(FMLInitializationEvent e) {
        super.init(e);
    }
}
