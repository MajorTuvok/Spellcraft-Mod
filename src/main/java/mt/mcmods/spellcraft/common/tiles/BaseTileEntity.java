package mt.mcmods.spellcraft.common.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BaseTileEntity extends TileEntity {
    private boolean mMayUpdate;

    public BaseTileEntity() {
        super();
        setMayUpdate(false);
    }

    public void setMayUpdate(boolean mayUpdate) {
        mMayUpdate = mayUpdate;
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        if (mayUpdate()) {
            return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
        }
        return null;
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
    }

    protected IBlockState getState() {
        return getWorld().getBlockState(getPos());
    }

    public boolean mayUpdate() {
        return mMayUpdate;
    }

    protected void updateAll() {
        performBlockRenderUpdate();
        performBlockUpdate();
        markDirty();
    }

    protected void performBlockUpdate() {
        getWorld().notifyBlockUpdate(pos, getState(), getState(), 3);
        getWorld().scheduleBlockUpdate(pos, this.getBlockType(), 0, 0);
    }

    protected void performBlockRenderUpdate() {
        getWorld().markBlockRangeForRenderUpdate(pos, pos);
    }
}
