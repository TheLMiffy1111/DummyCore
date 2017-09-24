package DummyCore.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Core.CoreInitialiser;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @version From DummyCore 1.0
 * @author Modbder
 * Internal. Used to work with Blocks. 
 */
public final class CreativePageBlocks extends CreativeTabs{
	public int delayTime = 0;
	public ItemStack displayStack = new ItemStack(Blocks.CRAFTING_TABLE,1,0);
	private final String tabLabel;
	public List<ItemStack> blockList = new ArrayList<ItemStack>();
	public int tries = 0;
	public ItemStack overrideDisplayStack = ItemStack.EMPTY;

	public CreativePageBlocks(String m) {
		super(m + " Blocks");
		tabLabel = m + " Blocks";
	}

	@Override
	public ItemStack getIconItemStack()
	{
		if(!overrideDisplayStack.isEmpty())
			return overrideDisplayStack;
		CoreInitialiser.proxy.choseDisplayStack(this);
		return this.displayStack;
	}

	public List<ItemStack> initialiseBlocksList()
	{
		++tries;
		if(this.blockList.isEmpty() && tries <= 1)
		{
			for(int t = 0; t < Block.REGISTRY.getKeys().size(); ++t)
			{
				Block b = Block.getBlockFromName(((ResourceLocation) Block.REGISTRY.getKeys().toArray()[t]).toString());
				if(b != null && b.getCreativeTabToDisplayOn() == this)
				{
					Item itm = Item.getItemFromBlock(b);
					if(itm != null)
					{
						NonNullList<ItemStack> lst = NonNullList.<ItemStack>create();
						itm.getSubItems(this,lst);
						if(!lst.isEmpty())
						{
							for(ItemStack stk : lst)
							{
								if(!stk.isEmpty())
								{
									this.blockList.add(stk);
								}
							}

						}
					}

				}
			}

			return blockList;
		}
		return this.blockList;
	}
	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslatedTabLabel()
	{
		return this.tabLabel;
	}

	@Override
	public ItemStack getTabIconItem() {
		return displayStack;
	}
}