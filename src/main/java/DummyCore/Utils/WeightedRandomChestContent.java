package DummyCore.Utils;

import java.util.Arrays;
import java.util.Random;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.WeightedRandom;

/**
 *
 * @author thelmiffy1111
 * @Description
 * Basically the old WeightRandomChestContent class, made this because this is still useful.
 */
public class WeightedRandomChestContent extends WeightedRandom.Item {
	/** The Item/Block to generate in the Chest. */
	public ItemStack theItem;
	/** The minimum chance of item generating. */
	public int theMinimumChanceToGenerateItem;
	/** The maximum chance of item generating. */
	public int theMaximumChanceToGenerateItem;

	public WeightedRandomChestContent(Item item, int meta, int minChance, int maxChance, int weight) {
		super(weight);
		theItem = new ItemStack(item, 1, meta);
		theMinimumChanceToGenerateItem = minChance;
		theMaximumChanceToGenerateItem = maxChance;
	}

	public WeightedRandomChestContent(ItemStack stack, int minChance, int maxChance, int weight) {
		super(weight);
		theItem = stack;
		theMinimumChanceToGenerateItem = minChance;
		theMaximumChanceToGenerateItem = maxChance;
	}

	/**
	 * Generates the Chest contents.
	 */
	public static void generateChestContents(Random rand, WeightedRandomChestContent[] arr, IInventory inv, int tries) {
		for(int j = 0; j < tries; ++j) {
			WeightedRandomChestContent weightedrandomchestcontent = WeightedRandom.getRandomItem(rand, Arrays.asList(arr));
			ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(rand, inv);

			for(ItemStack item : stacks) {
				inv.setInventorySlotContents(rand.nextInt(inv.getSizeInventory()), item);
			}
		}
	}

	public static void generateDispenserContents(Random rand, WeightedRandomChestContent[] arr, TileEntityDispenser dispenser, int tries) {
		for(int j = 0; j < tries; ++j) {
			WeightedRandomChestContent weightedrandomchestcontent = WeightedRandom.getRandomItem(rand, Arrays.asList(arr));
			int k = weightedrandomchestcontent.theMinimumChanceToGenerateItem + rand.nextInt(weightedrandomchestcontent.theMaximumChanceToGenerateItem - weightedrandomchestcontent.theMinimumChanceToGenerateItem + 1);
			ItemStack[] stacks = weightedrandomchestcontent.generateChestContent(rand, dispenser);
			for(ItemStack item : stacks) {
				dispenser.setInventorySlotContents(rand.nextInt(dispenser.getSizeInventory()), item);
			}
		}
	}

	public static WeightedRandomChestContent[] merge(WeightedRandomChestContent[] arr, WeightedRandomChestContent... arr1) {
		WeightedRandomChestContent[] aweightedrandomchestcontent1 = new WeightedRandomChestContent[arr.length + arr1.length];
		int i = 0;

		for (WeightedRandomChestContent element : arr) {
			aweightedrandomchestcontent1[i++] = element;
		}

		WeightedRandomChestContent[] aweightedrandomchestcontent2 = arr1;
		int k = arr1.length;

		for(int l = 0; l < k; ++l) {
			WeightedRandomChestContent weightedrandomchestcontent1 = aweightedrandomchestcontent2[l];
			aweightedrandomchestcontent1[i++] = weightedrandomchestcontent1;
		}

		return aweightedrandomchestcontent1;
	}

	/**
	 * Allow a mod to submit a custom implementation that can delegate item stack generation beyond simple stack lookup
	 *
	 * @param random The current random for generation
	 * @param newInventory The inventory being generated (do not populate it, but you can refer to it)
	 * @return An array of {@link ItemStack} to put into the chest
	 */
	protected ItemStack[] generateChestContent(Random random, IInventory newInventory) {
		return generateStacks(random, theItem, theMinimumChanceToGenerateItem, theMaximumChanceToGenerateItem);
	}

	/**
	 * Generates an array of items based on the input min/max count.
	 * If the stack can not hold the total amount, it will be split into
	 * stacks of size 1.
	 *
	 * @param rand A random number generator
	 * @param source Source item stack
	 * @param min Minimum number of items
	 * @param max Maximum number of items
	 * @return An array containing the generated item stacks
	 */
	public static ItemStack[] generateStacks(Random rand, ItemStack source, int min, int max) {
		int count = min + rand.nextInt(max - min + 1);

		ItemStack[] ret;
		if(source.getItem() == null) {
			ret = new ItemStack[0];
		}
		else if(count > source.getMaxStackSize()) {
			ret = new ItemStack[count];
			for(int x = 0; x < count; x++) {
				ret[x] = source.copy();
				ret[x].setCount(1);;
			}
		}
		else {
			ret = new ItemStack[1];
			ret[0] = source.copy();
			ret[0].setCount(count);
		}
		return ret;
	}
}
