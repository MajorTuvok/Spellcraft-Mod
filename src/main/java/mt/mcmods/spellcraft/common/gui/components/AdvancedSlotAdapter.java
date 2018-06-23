package mt.mcmods.spellcraft.common.gui.components;

import mt.mcmods.spellcraft.common.gui.components.AbsScrollingList.ISlotAdapter;
import mt.mcmods.spellcraft.common.gui.components.AdvancedSlotAdapter.IListSlot;
import mt.mcmods.spellcraft.common.util.GuiUtil;
import net.minecraft.client.renderer.Tessellator;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class AdvancedSlotAdapter<T extends IListSlot> implements ISlotAdapter {
    @Nullable
    private T mHeader;
    private boolean mHeaderSelected;
    private List<T> mListSlots;

    protected AdvancedSlotAdapter(List<T> listSlots) {
        mHeaderSelected = false;
        mHeader = null;
        mListSlots = listSlots;
    }

    public static <K extends IListSlot> AdvancedSlotAdapter<K> newInstance() {
        return new AdvancedSlotAdapter<>(new ArrayList<>());
    }

    public static <K extends IListSlot> AdvancedSlotAdapter<K> newInstance(K p1) {
        ArrayList<K> list = new ArrayList<>();
        list.add(p1);
        return new AdvancedSlotAdapter<>(list);
    }

    public static <K extends IListSlot> AdvancedSlotAdapter<K> newInstance(K p1, K p2) {
        ArrayList<K> list = new ArrayList<>();
        list.add(p1);
        list.add(p1);
        return new AdvancedSlotAdapter<>(list);
    }

    public static <K extends IListSlot> AdvancedSlotAdapter<K> newInstance(K p1, K p2, K p3) {
        ArrayList<K> list = new ArrayList<>();
        list.add(p1);
        list.add(p2);
        list.add(p3);
        return new AdvancedSlotAdapter<>(list);
    }

    @SafeVarargs
    public static <K extends IListSlot> AdvancedSlotAdapter<K> newInstance(K p1, K p2, K p3, K... args) {
        List<K> list = Arrays.asList(args);
        list.add(p1);
        list.add(p2);
        list.add(p3);
        return new AdvancedSlotAdapter<>(list);
    }

    public static <K extends IListSlot> AdvancedSlotAdapter<K> newInstance(Collection<K> c) {
        return new AdvancedSlotAdapter<>(new ArrayList<>(c));
    }

    @Override
    public int getSize() {
        return mListSlots.size();
    }

    @Override
    public void drawSlot(int index, int left, int right, int top, int bottom, boolean hoveredOver, boolean selected, int slotBuffer, Tessellator tess) {
        getSlot(index).onDraw(left, right, top, bottom, hoveredOver, selected, slotBuffer, tess);
    }

    @Override
    public void drawHeader(Tessellator tessellator, int left, int right, int top, int bottom, int mouseX, int mouseY) {
        if (getHeader() != null) {
            getHeader().onDraw(left, right, top, bottom, GuiUtil.isWithinBounds(mouseX, mouseY, left, right, top, bottom), mHeaderSelected, -1, tessellator);
        }
    }

    /**
     * Called when a slot was clicked
     *
     * @param index       The clicked index
     * @param doubleClick whether this was a double click
     * @return If this Index should now be selected
     */
    @Override
    public boolean onSlotClicked(int index, boolean doubleClick) {
        return getSlot(index).onClick(doubleClick);
    }

    @Override
    public void onHeaderClicked(int x, int y) {
        if (getHeader() != null) {
            mHeaderSelected = getHeader().onClick(false);
        }
    }

    @Nullable
    public T getHeader() {
        return mHeader;
    }

    /**
     * @param header The Header to be used. Pass null to remove any previous set Header.
     */
    public void setHeader(@Nullable T header) {
        mHeader = header;
    }

    public T getSlot(int index) {
        return mListSlots.get(index);
    }

    public interface IListSlot {
        public void onDraw(int left, int right, int top, int bottom, boolean hoveredOver, boolean selected, int slotBuffer, Tessellator tess);

        public boolean onClick(boolean doubleClick);
    }
}
