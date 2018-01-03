package mt.mcmods.spellcraft.common.blocks;

import mt.mcmods.spellcraft.common.tiles.TileEntityContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BaseTileEntityBlock<T extends TileEntity> extends BaseBlock implements TileEntityContainer<T> {
    public BaseTileEntityBlock(Material material, @Nonnull String displayName) {
        super(material, displayName);
    }

    @Override
    public abstract Class<T> getTileEntityClass();

    @Override
    @SuppressWarnings("unchecked")
//Because we created this Block to provide TileEntity's of Type T we can return them safely. Be aware that calling this on a Block who is not an Corresponding Block will result in an CalssCastException.
    public T getTileEntity(IBlockAccess world, BlockPos pos) {
        return (T) world.getTileEntity(pos);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public abstract @Nullable
    T createTileEntity(World world, IBlockState state);
}

