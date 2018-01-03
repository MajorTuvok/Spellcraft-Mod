package mt.mcmods.spellcraft.common.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface TileEntityContainer<T extends TileEntity> {
    public Class<T> getTileEntityClass();

    public T getTileEntity(IBlockAccess world, BlockPos pos);

    public @Nullable
    T createTileEntity(World world, IBlockState state);
}
