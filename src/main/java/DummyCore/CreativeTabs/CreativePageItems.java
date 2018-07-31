package DummyCore.CreativeTabs;

import java.util.ArrayList;
import java.util.List;

import DummyCore.Core.CoreInitialiser;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @version From DummyCore 1.0
 * @author Modbder
 * Internal. Used to work with Items.
 */
public final class CreativePageItems extends CreativeTabs{
	public int delayTime = 0;
	public ItemStack displayStack = new ItemStack(Items.IRON_AXE,1,0);
	private final String tabLabel;
	public List<ItemStack> itemList = new ArrayList<ItemStack>();
	public int tries = 0;
	public ItemStack overrideDisplayStack = ItemStack.EMPTY;

	public CreativePageItems(String m) {
		super(m + " Items");
		tabLabel = m + " Items";
	}

	@Override
	public ItemStack getIconItemStack() {
		if(!overrideDisplayStack.isEmpty())
			return overrideDisplayStack;
		CoreInitialiser.proxy.choseDisplayStack(this);
		return this.displayStack;
	}

	public List<ItemStack> initialiseItemsList() {
		++tries;
		if(this.itemList.isEmpty() && tries <= 1) {
			for(Item itm : Item.REGISTRY) {
				if(itm != null && itm.getCreativeTab() == this) {
					NonNullList<ItemStack> lst = NonNullList.<ItemStack>create();
					itm.getSubItems(this,lst);
					if(!lst.isEmpty()) {
						for(ItemStack stk : lst) {
							if(!stk.isEmpty()) {
								this.itemList.add(stk);
							}
						}
					}
				}
			}
			return this.itemList;
		}
		return this.itemList;

	}

	@SideOnly(Side.CLIENT)
	@Override
	public String getTranslatedTabLabel() {
		return this.tabLabel;
	}

	@Override
	public ItemStack getTabIconItem() {
		return displayStack;
	}
}