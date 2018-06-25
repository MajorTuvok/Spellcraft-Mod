package mt.mcmods.spellcraft.common.interfaces;

import mt.mcmods.spellcraft.common.gui.helper.GuiDrawingDelegate;
import mt.mcmods.spellcraft.common.gui.helper.GuiMeasurements;
import net.minecraft.client.gui.ScaledResolution;

public interface IGuiInitialisationListener extends IGuiListener {
    public void onGuiInit(IDelegateProvider<? extends GuiDrawingDelegate> delegateProvider, GuiMeasurements measurements, ScaledResolution screenMeasurements);
}
