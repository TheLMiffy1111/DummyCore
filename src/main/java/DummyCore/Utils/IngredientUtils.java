package DummyCore.Utils;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.IngredientNBT;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;

public class IngredientUtils {

	public static Ingredient getIngredient(Object obj) {
		if(obj instanceof Ingredient) {
			return (Ingredient)obj;
		}
		else if(obj instanceof ItemStack) {
			return Ingredient.fromStacks(((ItemStack)obj).copy());
		}
		else if(obj instanceof Item) {
			return Ingredient.fromItem((Item)obj);
		}
		else if(obj instanceof Block) {
			return Ingredient.fromStacks(new ItemStack((Block)obj, 1, OreDictionary.WILDCARD_VALUE));
		}
		else if(obj instanceof String) {
			return new OreIngredient((String)obj);
		}
		else if(obj instanceof UnformedItemStack) {
			return Ingredient.fromStacks(((UnformedItemStack)obj).possibleStacks.toArray(new ItemStack[0]));
		}
		else if(obj instanceof Item[]) {
			return Ingredient.fromItems((Item[])obj);
		}
		else if(obj instanceof ItemStack[]) {
			return Ingredient.fromStacks((ItemStack[])obj);
		}
		else if(obj instanceof Object[]) {
			List<Ingredient> ingredients = Lists.newArrayList();
			List<Ingredient> vanilla = Lists.newArrayList();
			for(Object obj1 : (Object[])obj) {
				Ingredient ingredient = getIngredient(obj1);
				if(ingredient.getClass() == Ingredient.class) {
					vanilla.add(ingredient);
				}
				else {
					ingredients.add(ingredient);
				}
			}
			if(!vanilla.isEmpty()) {
				ingredients.add(Ingredient.merge(vanilla));
			}
			if(ingredients.size() == 0) {
				return Ingredient.EMPTY;
			}
			if(ingredients.size() == 1) {
				return ingredients.get(0);
			}
			//constructor is protected, use subclass
			return new CompoundIngredient(ingredients) {};
		}
		else if(obj instanceof List<?>) {
			List<Ingredient> ingredients = Lists.newArrayList();
			List<Ingredient> vanilla = Lists.newArrayList();
			for(Object obj1 : (List<?>)obj) {
				Ingredient ingredient = getIngredient(obj1);
				if(ingredient.getClass() == Ingredient.class) {
					vanilla.add(ingredient);
				}
				else {
					ingredients.add(ingredient);
				}
			}
			if(!vanilla.isEmpty()) {
				ingredients.add(Ingredient.merge(vanilla));
			}
			if(ingredients.size() == 0) {
				return Ingredient.EMPTY;
			}
			if(ingredients.size() == 1) {
				return ingredients.get(0);
			}
			//constructor is protected, use subclass
			return new CompoundIngredient(ingredients) {};
		}
		return Ingredient.EMPTY;
	}

	public static IngredientNBT getIngredientNBT(ItemStack stack) {
		//constructor is protected, use subclass
		return new IngredientNBT(stack.copy()) {};
	}

	public static Ingredient getIngredientNBT(Object obj) {
		if(obj instanceof Ingredient) {
			return (Ingredient)obj;
		}
		else if(obj instanceof ItemStack) {
			return getIngredientNBT(((ItemStack)obj).copy());
		}
		else if(obj instanceof Item) {
			return Ingredient.fromItem((Item)obj);
		}
		else if(obj instanceof Block) {
			return Ingredient.fromStacks(new ItemStack((Block)obj, 1, OreDictionary.WILDCARD_VALUE));
		}
		else if(obj instanceof String) {
			return new OreIngredient((String)obj);
		}
		else if(obj instanceof UnformedItemStack) {
			return getIngredientNBT(((UnformedItemStack)obj).possibleStacks);
		}
		else if(obj instanceof Item[]) {
			return Ingredient.fromItems((Item[])obj);
		}
		else if(obj instanceof Object[]) {
			List<Ingredient> ingredients = Lists.newArrayList();
			List<Ingredient> vanilla = Lists.newArrayList();
			for(Object obj1 : (Object[])obj) {
				Ingredient ingredient = getIngredient(obj1);
				if(ingredient.getClass() == Ingredient.class) {
					vanilla.add(ingredient);
				}
				else {
					ingredients.add(ingredient);
				}
			}
			if(!vanilla.isEmpty()) {
				ingredients.add(Ingredient.merge(vanilla));
			}
			if(ingredients.size() == 0) {
				return Ingredient.EMPTY;
			}
			if(ingredients.size() == 1) {
				return ingredients.get(0);
			}
			//constructor is protected, use subclass
			return new CompoundIngredient(ingredients) {};
		}
		else if(obj instanceof List<?>) {
			List<Ingredient> ingredients = Lists.newArrayList();
			List<Ingredient> vanilla = Lists.newArrayList();
			for(Object obj1 : (List<?>)obj) {
				Ingredient ingredient = getIngredient(obj1);
				if(ingredient.getClass() == Ingredient.class) {
					vanilla.add(ingredient);
				}
				else {
					ingredients.add(ingredient);
				}
			}
			if(!vanilla.isEmpty()) {
				ingredients.add(Ingredient.merge(vanilla));
			}
			if(ingredients.size() == 0) {
				return Ingredient.EMPTY;
			}
			if(ingredients.size() == 1) {
				return ingredients.get(0);
			}
			//constructor is protected, use subclass
			return new CompoundIngredient(ingredients) {};
		}
		return Ingredient.EMPTY;
	}

	public static ItemStack getStackToDraw(Ingredient ingredient, long time) {
		int size = ingredient.getMatchingStacks().length;
		if(size <= 0) {
			return ItemStack.EMPTY;
		}
		return ingredient.getMatchingStacks()[(int)(time/30)%size];
	}
}
