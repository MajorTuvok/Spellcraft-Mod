package mt.mcmods.spellcraft.common.util;


import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public final class NetworkUtils {

    private NetworkUtils() {
    }

    public static boolean mightBeClient() {
        return physicalClient() && FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    public static boolean mightBeServer() {
        return physicalServer() || FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
    }

    public static boolean physicalServer() {
        return FMLCommonHandler.instance().getSide() == Side.SERVER;
    }

    public static boolean physicalClient() {
        return FMLCommonHandler.instance().getSide() == Side.CLIENT;
    }

    public static boolean isServer(World world) {
        return !world.isRemote;
    }

    public static boolean isClient(World world) {
        return world.isRemote;
    }
}
