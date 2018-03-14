package mt.mcmods.spellcraft.common.events.handlers;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedHashSet;


public class LeftClickEventHandler {
    private static final LinkedHashSet<IClickListener> listeners = new LinkedHashSet<>();

    public static void register(IClickListener listener) {
        listeners.add(listener);
    }

    public static void deRegister(IClickListener listener) {
        listeners.add(listener);
    }

    @SubscribeEvent
    public void onBlockLeftClicked(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getSide() == Side.CLIENT) {
            EntityPlayer player = event.getEntityPlayer();
            ItemStack stack = event.getItemStack();
            EnumHand hand = event.getHand();
            BlockPos pos = event.getPos();
            Tuple<Boolean, EnumActionResult> anyTuple = null;
            Tuple<Boolean, EnumActionResult> specificTuple = null;
            if (!stack.isEmpty() && stack.getItem() instanceof IClickListener) {
                anyTuple = ((IClickListener) stack.getItem()).onAnyLeftClick(player, stack, hand, pos);
                specificTuple = ((IClickListener) stack.getItem()).onBlockLeftClick(player, stack, hand, pos);
            }
            if (!isEventHandled(event, anyTuple, specificTuple)) {
                for (IClickListener listener :
                        listeners) {
                    anyTuple = listener.onAnyLeftClick(player, stack, hand, pos);
                    specificTuple = listener.onBlockLeftClick(player, stack, hand, pos);
                    if (isEventHandled(event, anyTuple, specificTuple)) {
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getSide() == Side.CLIENT) {
            EntityPlayer player = event.getEntityPlayer();
            ItemStack stack = event.getItemStack();
            EnumHand hand = event.getHand();
            BlockPos pos = event.getPos();
            Tuple<Boolean, EnumActionResult> anyTuple = null;
            Tuple<Boolean, EnumActionResult> specificTuple = null;
            if (!stack.isEmpty() && stack.getItem() instanceof IClickListener) {
                anyTuple = ((IClickListener) stack.getItem()).onAnyLeftClick(player, stack, hand, pos);
                specificTuple = ((IClickListener) stack.getItem()).onEmptyLeftClick(player, stack, hand, pos);
            }
            if (!isEventHandled(event, anyTuple, specificTuple)) {
                for (IClickListener listener :
                        listeners) {
                    anyTuple = listener.onAnyLeftClick(player, stack, hand, pos);
                    specificTuple = listener.onEmptyLeftClick(player, stack, hand, pos);
                    if (isEventHandled(event, anyTuple, specificTuple)) {
                        break;
                    }
                }
            }
        }
    }

    private boolean isEventHandled(@Nonnull PlayerInteractEvent event, @Nullable Tuple<Boolean, EnumActionResult> first, @Nullable Tuple<Boolean, EnumActionResult> second) {
        if (first != null && first.getSecond() != EnumActionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(first.getSecond());
        } else if (second != null && second.getSecond() != EnumActionResult.PASS) {
            event.setCanceled(true);
            event.setCancellationResult(second.getSecond());
        }
        return first != null && first.getFirst() || second != null && second.getFirst();
    }

    public static interface IClickListener {
        public @Nonnull
        Tuple<Boolean, EnumActionResult> onAnyLeftClick(EntityPlayer player, ItemStack stack, EnumHand hand, BlockPos pos);

        public @Nonnull
        Tuple<Boolean, EnumActionResult> onEmptyLeftClick(EntityPlayer player, ItemStack stack, EnumHand hand, BlockPos pos);

        public @Nonnull
        Tuple<Boolean, EnumActionResult> onBlockLeftClick(EntityPlayer player, ItemStack stack, EnumHand hand, BlockPos pos);
    }
}
