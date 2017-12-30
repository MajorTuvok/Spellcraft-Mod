package com.mt.mcmods.spellcraft.common.interfaces;

import com.mt.mcmods.spellcraft.common.tiles.CompatStackHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public interface ICompatStackHandler  extends IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound>, IInventory {

    public void setDirtyMarkListener(IMarkDirtyCallback dirtyMarkListener);

    public void setPlayerRestrictionProvider(PlayerRestrictionProvider playerRestrictionProvider) ;

    public void setPlayerInteractionListener(PlayerInteractionListener playerInteractionListener);

    public void setItemStackHandlerListener(ItemStackHandlerListener itemStackHandlerListener);

    public interface PlayerRestrictionProvider {
        public boolean isUsableByPlayer(EntityPlayer player);
    }

    public interface PlayerInteractionListener extends CompatStackHandler.PlayerRestrictionProvider {
        public void openInventory(EntityPlayer player, List<ItemStack> stacks);

        public void closeInventory(EntityPlayer player, List<ItemStack> stacks);
    }

    public interface ItemStackHandlerListener {
        public void onContentChanged(int slot, List<ItemStack> stacks);

        public void onLoad();
    }
}
