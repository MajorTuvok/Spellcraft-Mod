package com.mt.mcmods.spellcraft.common.gui;


import com.mt.mcmods.spellcraft.common.gui.helper.GuiID;
import com.mt.mcmods.spellcraft.common.gui.instances.GuiContainerWandCraftingTable;
import com.mt.mcmods.spellcraft.common.gui.instances.GuiWandCraftingTable;
import com.mt.mcmods.spellcraft.common.tiles.TileEntityWandCraftingTable;
import com.mt.mcmods.spellcraft.common.util.NetworkUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class SpellcraftGuiHandler implements IGuiHandler {

    /**
     * Returns a Server side Container to be displayed to the user.
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiID id = GuiID.getFromId(ID);
        if (id != null) {
            switch (id) {
                case GUIWandCraftingTable: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity != null && entity instanceof TileEntityWandCraftingTable) {
                        player.openContainer = new GuiContainerWandCraftingTable(player.inventory, (TileEntityWandCraftingTable) entity, null);
                        return player.openContainer;
                    }
                    break;
                }
            }
        }
        return null;
    }

    /**
     * Returns a Container to be displayed to the user. On the Client side, this
     * needs to return a instance of GuiScreen On the Server side, this needs to
     * return a instance of Container
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiID id = GuiID.getFromId(ID);
        if (id != null) {
            switch (id) {
                case GUIWandCraftingTable: {
                    TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
                    if (entity != null && entity instanceof TileEntityWandCraftingTable) {
                        GuiContainerWandCraftingTable container = player.openContainer != null && player.openContainer instanceof GuiContainerWandCraftingTable ? (GuiContainerWandCraftingTable) player.openContainer : new GuiContainerWandCraftingTable(player.inventory, (TileEntityWandCraftingTable) entity, null);
                        player.openContainer = container;
                        if (NetworkUtils.isServer(world))
                            return container;
                        else
                            return new GuiWandCraftingTable(player.inventory, container);
                    }
                    break;
                }
            }
        }
        return null;
    }
}
