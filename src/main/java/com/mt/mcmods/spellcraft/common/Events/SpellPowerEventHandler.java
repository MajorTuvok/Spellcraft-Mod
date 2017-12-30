package com.mt.mcmods.spellcraft.common.Events;

import com.google.common.collect.Lists;
import com.mt.mcmods.spellcraft.Client.net.Messages.RequestSyncEntitySpellpower;
import com.mt.mcmods.spellcraft.Server.net.Messages.SyncEntitySpellpower;
import com.mt.mcmods.spellcraft.SpellcraftMod;
import com.mt.mcmods.spellcraft.common.Capabilities.SpellcraftCapabilities;
import com.mt.mcmods.spellcraft.common.Capabilities.spellpower.EntitySpellPowerProvider;
import com.mt.mcmods.spellcraft.common.Capabilities.spellpower.ISpellPowerProvider;
import com.mt.mcmods.spellcraft.common.Capabilities.spellpower.SpellPowerProviderCapability;
import com.mt.mcmods.spellcraft.common.interfaces.ILoggable;
import com.mt.mcmods.spellcraft.common.util.NetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

import static com.mt.mcmods.spellcraft.common.interfaces.ILoggable.Log;

public class SpellPowerEventHandler {
    private static ArrayList<Tuple<Entity,ISpellPowerProvider>> serverProviders = Lists.newArrayList();
    //private static ArrayList<Tuple<Entity,ISpellPowerProvider>> clientProviders = Lists.newArrayList();
    private static final int MAX_POWER = 1000; //TODO add to config
    private static final float RECEIVE_POWER = 0.001f; //TODO add to config

    @SubscribeEvent
    public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer) {
            ISpellPowerProvider provider = new EntitySpellPowerProvider(MAX_POWER,0,event.getObject());
            event.addCapability(SpellPowerProviderCapability.ID,new SpellPowerProviderCapabilityProvider(provider));
            if(!event.getObject().world.isRemote) {
                serverProviders.add(new Tuple<>(event.getObject(), provider));
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent serverTickEvent) {
        if (serverTickEvent.phase == TickEvent.Phase.START) {
            for (Tuple<Entity, ISpellPowerProvider> tuple :
                    serverProviders) {
                ISpellPowerProvider provider = tuple.getSecond();
                provider.receivePower(RECEIVE_POWER*provider.getMaxPower(), false);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerConnected(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.world.isRemote && event.player instanceof EntityPlayerMP) {
            Log.trace("Sending Spellpower Sync to freshly logged in player");
            SpellcraftMod.CHANNEL_HOLDER.sendTo(new SyncEntitySpellpower(event.player),(EntityPlayerMP) event.player);
        }
    }

    static void clear() {
        serverProviders.clear();
    }

    private static final class SpellPowerProviderCapabilityProvider implements ICapabilitySerializable<NBTBase> {
        private ISpellPowerProvider entitySpellPowerProvider;

        public SpellPowerProviderCapabilityProvider(ISpellPowerProvider entitySpellPowerProvider) {
            this.entitySpellPowerProvider = entitySpellPowerProvider;
        }

        /**
         * Determines if this object has support for the capability in question on the specific side.
         * The return value of this MIGHT change during runtime if this object gains or looses support
         * for a capability.
         * <p>
         * Example:
         * A Pipe getting a cover placed on one side causing it loose the Inventory attachment function for that side.
         * <p>
         * This is a light weight version of getCapability, intended for metadata uses.
         *
         * @param capability The capability to check
         * @param facing     The Side to check from:
         *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
         * @return True if this object supports the capability.
         */
        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == SpellcraftCapabilities.SPELL_POWER_PROVIDER_CAPABILITY;
        }

        /**
         * Retrieves the handler for the capability requested on the specific side.
         * The return value CAN be null if the object does not support the capability.
         * The return value CAN be the same for multiple faces.
         *
         * @param capability The capability to check
         * @param facing     The Side to check from:
         *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
         * @return The requested capability. Returns null when {@link #hasCapability(Capability, EnumFacing)} would return false.
         */
        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability,facing) ?(T) entitySpellPowerProvider:null;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            return entitySpellPowerProvider.serializeNBT();
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            entitySpellPowerProvider.deserializeNBT((NBTTagCompound)nbt);
        }
    }
}
