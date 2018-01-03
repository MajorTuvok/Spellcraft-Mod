package mt.mcmods.spellcraft.common.recipes;


import mt.mcmods.spellcraft.common.recipes.inputs.ProcessingInput;
import mt.mcmods.spellcraft.common.recipes.outputs.ProcessingOutput;
import mt.mcmods.spellcraft.common.recipes.recipes.ProcessingRecipe;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static mt.mcmods.spellcraft.common.util.ReflectionHelper.instantiate;

public class BURecipeHandler {

    public static enum Recipe {
        ;
        private HashMap<ProcessingInput, ProcessingRecipe> inputRecipeMap;
        private String name;
        private Class<? extends ProcessingInput> inputClass;
        private Class<? extends ProcessingOutput> outputClass;
        private Class<? extends ProcessingRecipe> recipeClass;

        Recipe(HashMap<ProcessingInput, ProcessingRecipe> inputRecipeMap, String name, @Nonnull Class<? extends ProcessingInput> inputClass, @Nonnull Class<? extends ProcessingOutput> outputClass, @Nonnull Class<? extends ProcessingRecipe> recipeClass) {
            this.inputRecipeMap = inputRecipeMap;
            this.name = name;
            this.inputClass = inputClass;
            this.outputClass = outputClass;
            this.recipeClass = recipeClass;
        }

        Recipe(String name, @Nonnull Class<? extends ProcessingInput> inputClass, @Nonnull Class<? extends ProcessingOutput> outputClass, @Nonnull Class<? extends ProcessingRecipe> recipeClass) {
            this(new HashMap<>(), name, inputClass, outputClass, recipeClass);
        }

        public String getName() {
            return name;
        }

        public Class<? extends ProcessingInput> getInputClass() {
            return inputClass;
        }

        public Class<? extends ProcessingOutput> getOutputClass() {
            return outputClass;
        }

        public Class<? extends ProcessingRecipe> getRecipeClass() {
            return recipeClass;
        }

        public <RECIPE extends ProcessingRecipe> RECIPE addRecipe(RECIPE recipe) {
            if (recipeClass.isAssignableFrom(recipe.getClass()) && recipe.getInput().isValid()) {
                inputRecipeMap.put(recipe.getInput(), recipe);
                return recipe;
            }
            return null;
        }

        public <IN extends ProcessingInput, RECIPE extends ProcessingRecipe> RECIPE addRecipe(IN input, RECIPE recipe) {
            if (inputClass.isAssignableFrom(input.getClass()) && recipeClass.isAssignableFrom(recipe.getClass()) && recipe.getInput().isValid()) {
                inputRecipeMap.put(input, recipe);
                return recipe;
            }
            return null;
        }

        public <IN extends ProcessingInput, OUT extends ProcessingOutput, RECIPE extends ProcessingRecipe> RECIPE createFromClass(IN input, OUT output) {
            if (getInputClass().isAssignableFrom(input.getClass()) && getOutputClass().isAssignableFrom(output.getClass())) {
                Object obj = instantiate(recipeClass, input, output);
                if (obj != null) {
                    return addRecipe((RECIPE) obj);
                }
            }
            return null;
        }

        public ProcessingRecipe getRecipe(ProcessingInput input) {
            return inputRecipeMap.get(input);
        }

        public boolean containsRecipe(ProcessingInput input) {
            Set<Map.Entry<ProcessingInput, ProcessingRecipe>> entries = inputRecipeMap.entrySet();
            for (Map.Entry<ProcessingInput, ProcessingRecipe> entry :
                    entries) {
                if (entry.getKey().equals(input)) {
                    return entry.getValue() != null;
                }
            }
            return false;
        }
    }
}
