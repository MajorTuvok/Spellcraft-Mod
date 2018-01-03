package mt.mcmods.spellcraft.common.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.network.NetworkRegistry;


public class MessageUtils {
    public static NetworkRegistry.TargetPoint readTargetPoint(ByteBuf buf) {
        int dim = buf.readInt();
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        double range = buf.readDouble();
        return new NetworkRegistry.TargetPoint(dim, x, y, z, range);
    }

    public static void writeTargetPoint(ByteBuf buf, NetworkRegistry.TargetPoint targetPoint) {
        buf.writeInt(targetPoint.dimension);
        buf.writeDouble(targetPoint.x);
        buf.writeDouble(targetPoint.y);
        buf.writeDouble(targetPoint.z);
        buf.writeDouble(targetPoint.range);
    }

    public static NetworkRegistry.TargetPoint getTargetPoint(Entity entity, double range) {
        int dim = entity.world.provider.getDimension();
        double x = entity.posX;
        double y = entity.posY;
        double z = entity.posZ;
        return new NetworkRegistry.TargetPoint(dim, x, y, z, range);
    }
}
