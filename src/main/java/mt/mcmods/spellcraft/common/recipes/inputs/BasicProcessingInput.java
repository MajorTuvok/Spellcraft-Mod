package mt.mcmods.spellcraft.common.recipes.inputs;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

public abstract class BasicProcessingInput<INPUT extends BasicProcessingInput<INPUT>> implements ProcessingInput<INPUT>, Cloneable {
    @Override
    public abstract boolean isValid();

    @Override
    public abstract INPUT copy();

    @Override
    public abstract void load(NBTTagCompound compound);

    @Override
    public abstract boolean testEquality(INPUT var1);

    @Override
    public abstract boolean isInstance(Object var1);

    @Override
    public abstract void write(NBTTagCompound compound);

    @Override
    public abstract int hashCode();

    @Override
    public boolean equals(Object other) {
        return this.isInstance(other) && this.testEquality((INPUT) other);
    }

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be {@code true}, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be {@code true}, this is not an absolute requirement.
     *
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}.  If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     *
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by {@code super.clone} before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by {@code super.clone}
     * need to be modified.
     *
     * The method {@code clone} for class {@code Object} performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface {@code Cloneable}, then a
     * {@code CloneNotSupportedException} is thrown. Note that all arrays
     * are considered to implement the interface {@code Cloneable} and that
     * the return type of the {@code clone} method of an array type {@code T[]}
     * is {@code T[]} where T is any reference or primitive type.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     *
     * The class {@code Object} does not itself implement the interface
     * {@code Cloneable}, so calling the {@code clone} method on an object
     * whose class is {@code Object} will result in throwing an
     * exception at run time.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @see Cloneable
     */
    @Override
    protected INPUT clone() throws CloneNotSupportedException {
        return copy();
    }

    /**
     * Attempts to consume the amount of SpellcraftItems specified in consumeAmount from toConsume
     *
     * @param toConsume     The Stack to reduce the item amount from
     * @param consumeAmount The Stack specifying which SpellcraftItems and how much should be taken
     * @param simulate      Whether or not the SpellcraftItems in toConsume should actually be consumed
     * @return True if he could have consumed (or did consume) the specified amount of items from toConsume
     */
    protected boolean consumeStack(ItemStack toConsume, ItemStack consumeAmount, boolean simulate) {
        if (toConsume != null && consumeAmount != null &&
                areItemsEqual(toConsume, consumeAmount) && toConsume.getCount() >= consumeAmount.getCount()) {
            if (!simulate) {
                toConsume.setCount(toConsume.getCount() - consumeAmount.getCount());
            }
            return true;
        } else {
            return false;
        }
    }

    protected boolean revertStack(ItemStack toConsume, ItemStack consumeAmount, boolean simulate) {
        if (toConsume != null && consumeAmount != null &&
                areItemsEqual(toConsume, consumeAmount) && toConsume.getCount() + consumeAmount.getCount() <= toConsume.getMaxStackSize()) {
            if (!simulate) {
                toConsume.setCount(toConsume.getCount() + consumeAmount.getCount());
            }
            return true;
        } else {
            return false;
        }
    }

    protected boolean areItemsEqual(ItemStack first, ItemStack second) {
        return ItemStack.areItemsEqual(first, second) || OreDictionary.itemMatches(first, second, false);
    }
}
