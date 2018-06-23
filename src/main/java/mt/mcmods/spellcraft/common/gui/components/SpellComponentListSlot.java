package mt.mcmods.spellcraft.common.gui.components;

import jline.internal.Log;
import mt.mcmods.spellcraft.common.gui.components.AdvancedSlotAdapter.IListSlot;
import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.interfaces.IDelegateProvider;
import mt.mcmods.spellcraft.common.spell.SpellBuilder;
import mt.mcmods.spellcraft.common.spell.components.ISpellComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import javax.annotation.Nonnull;

public class SpellComponentListSlot implements IListSlot, IDelegateProvider<GuiDrawingDelegate> {
    @Nonnull
    private final IDelegateProvider<GuiDrawingDelegate> mDelegateProvider;
    private SpellBuilder mBuilder;
    private ImageButton mButtonSwapLeft;
    private ImageButton mButtonSwapRight;
    private int mComponentIndex;
    private int mListIndex;
    @Nonnull
    private ISpellComponent<? extends ISpellComponent<?>> mSpellComponent;
    private String mState;

    public SpellComponentListSlot(@Nonnull IDelegateProvider<GuiDrawingDelegate> provider, @Nonnull SpellBuilder builder, @Nonnull String spellState, int listIndex, boolean executable, int posIndex) {
        setSpellComponent(builder, spellState, listIndex, executable, posIndex);
        mDelegateProvider = provider;
        mButtonSwapRight = new ImageButton(0, 0, 0, null, null);
    }

    @Nonnull
    public ISpellComponent<?> getSpellComponent() {
        return mSpellComponent;
    }

    public SpellBuilder getBuilder() {
        return mBuilder;
    }

    public String getState() {
        return mState;
    }

    public int getListIndex() {
        return mListIndex;
    }

    public int getComponentIndex() {
        return mComponentIndex;
    }

    @Override
    @Nonnull
    public GuiDrawingDelegate getDelegate() {
        return mDelegateProvider.getDelegate();
    }

    public void setSpellComponent(@Nonnull SpellBuilder builder, @Nonnull String spellState, int listIndex, boolean executable, int posIndex) {
        mSpellComponent = builder.getComponentFor(spellState, listIndex, executable, posIndex);
        mBuilder = builder;
        mState = spellState;
        mListIndex = listIndex;
        mComponentIndex = posIndex;
    }

    public void setSpellComponent(@Nonnull String spellState, int index, boolean executable, int posIndex) {
        setSpellComponent(mBuilder, spellState, index, executable, posIndex);
    }

    @Override
    public void onDraw(int left, int right, int top, int bottom, boolean hoveredOver, boolean selected, int slotBuffer, Tessellator tess) {
        mSpellComponent.drawIcon(getDelegate(), hoveredOver, selected, left, top);
    }

    @Override
    public boolean onClick(boolean doubleClick) {
        if (mSpellComponent.hasConfigurableAttributes()) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen != null) {
                mc.displayGuiScreen(mSpellComponent.getConfigurationGui(mc.player, mc.currentScreen, getBuilder(), getState(), getListIndex(), getComponentIndex()));
            } else {
                Log.warn("{} was clicked without an active GuiScreen?!?", this.getClass().getSimpleName());
            }
        }
        return doubleClick;
    }
}
