package DummyCore.Utils;

import static DummyCore.Utils.OreDictUtils.CommonOres.AGATE;
import static DummyCore.Utils.OreDictUtils.CommonOres.ALUMINIUM;
import static DummyCore.Utils.OreDictUtils.CommonOres.AMBER;
import static DummyCore.Utils.OreDictUtils.CommonOres.AMETHYST;
import static DummyCore.Utils.OreDictUtils.CommonOres.APATITE;
import static DummyCore.Utils.OreDictUtils.CommonOres.ARDITE;
import static DummyCore.Utils.OreDictUtils.CommonOres.BLUETOPAZ;
import static DummyCore.Utils.OreDictUtils.CommonOres.CHIMERITE;
import static DummyCore.Utils.OreDictUtils.CommonOres.CINNABAR;
import static DummyCore.Utils.OreDictUtils.CommonOres.COAL;
import static DummyCore.Utils.OreDictUtils.CommonOres.COBALT;
import static DummyCore.Utils.OreDictUtils.CommonOres.COPPER;
import static DummyCore.Utils.OreDictUtils.CommonOres.DIAMOND;
import static DummyCore.Utils.OreDictUtils.CommonOres.EMERALD;
import static DummyCore.Utils.OreDictUtils.CommonOres.GALENA;
import static DummyCore.Utils.OreDictUtils.CommonOres.GOLD;
import static DummyCore.Utils.OreDictUtils.CommonOres.HEMATITE;
import static DummyCore.Utils.OreDictUtils.CommonOres.IRIDIUM;
import static DummyCore.Utils.OreDictUtils.CommonOres.IRON;
import static DummyCore.Utils.OreDictUtils.CommonOres.JASPER;
import static DummyCore.Utils.OreDictUtils.CommonOres.LAPIS;
import static DummyCore.Utils.OreDictUtils.CommonOres.LEAD;
import static DummyCore.Utils.OreDictUtils.CommonOres.MALACHITE;
import static DummyCore.Utils.OreDictUtils.CommonOres.MOONSTONE;
import static DummyCore.Utils.OreDictUtils.CommonOres.MYTHRIL;
import static DummyCore.Utils.OreDictUtils.CommonOres.NICKEL;
import static DummyCore.Utils.OreDictUtils.CommonOres.NICKOLITE;
import static DummyCore.Utils.OreDictUtils.CommonOres.OSMIUM;
import static DummyCore.Utils.OreDictUtils.CommonOres.PERIDOT;
import static DummyCore.Utils.OreDictUtils.CommonOres.PLATINUM;
import static DummyCore.Utils.OreDictUtils.CommonOres.PLUTONIUM;
import static DummyCore.Utils.OreDictUtils.CommonOres.QUARTZ;
import static DummyCore.Utils.OreDictUtils.CommonOres.REDSTONE;
import static DummyCore.Utils.OreDictUtils.CommonOres.RUBY;
import static DummyCore.Utils.OreDictUtils.CommonOres.SALTPETER;
import static DummyCore.Utils.OreDictUtils.CommonOres.SAPPHIRE;
import static DummyCore.Utils.OreDictUtils.CommonOres.SILICON;
import static DummyCore.Utils.OreDictUtils.CommonOres.SILVER;
import static DummyCore.Utils.OreDictUtils.CommonOres.SULFUR;
import static DummyCore.Utils.OreDictUtils.CommonOres.SUNSTONE;
import static DummyCore.Utils.OreDictUtils.CommonOres.TESLATITE;
import static DummyCore.Utils.OreDictUtils.CommonOres.TIN;
import static DummyCore.Utils.OreDictUtils.CommonOres.TOURMALINE;
import static DummyCore.Utils.OreDictUtils.CommonOres.TUNGSTEN;
import static DummyCore.Utils.OreDictUtils.CommonOres.TURQUOISE;
import static DummyCore.Utils.OreDictUtils.CommonOres.URANIUM;
import static DummyCore.Utils.OreDictUtils.CommonOres.ZINC;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Some OreDict utils are here
 * @author modbder
 *
 */
public class OreDictUtils {
	
	/**
	 * These ores can be found in various mods. Just a simple list of all I can remember ;)
	 * @author modbder
	 *
	 */
	public static enum CommonOres
	{
		COAL("oreCoal"),
		COPPER("oreCopper"),
		TIN("oreTin"),
		ZINC("oreZinc"),
		IRON("oreIron"),
		SILVER("oreSilver"),
		GOLD("oreGold"),
		URANIUM("oreUranium","oreUran","oreYellorite","oreYellorium"),
		PLUTONIUM("orePlutonium"),
		NICKEL("oreNickel"),
		LEAD("oreLead"),
		GALENA("oreGalena"),
		ALUMINIUM("oreAluminium","oreAluminum","oreBauxite"),
		IRIDIUM("oreIridium"),
		CINNABAR("oreCinnabar","oreMercury","oreQuicksilver"),
		AMBER("oreAmber"),
		DIAMOND("oreDiamond"),
		SAPPHIRE("oreSapphire"),
		EMERALD("oreEmerald"),
		RUBY("oreRuby"),
		PERIDOT("orePeridot"),
		APATITE("oreApatite"),
		AMETHYST("oreAmethyst","oreAmetyst"),
		QUARTZ("oreQuartz","oreNetherQuartz","oreCertusQuartz"),
		LAPIS("oreLapis","oreLapisLazuli"),
		AGATE("oreAgate"),
		JASPER("oreJasper"),
		MALACHITE("oreMalachite"),
		TOURMALINE("oreTourmaline"),
		TURQUOISE("oreTurquoise","oreTurquoisite"),
		HEMATITE("oreHematite"),
		CHIMERITE("oreChimerite"),
		BLUETOPAZ("oreBlueTopaz"),
		MOONSTONE("oreMoonstone"),
		SUNSTONE("oreSunstone"),
		TUNGSTEN("oreTungsten"),
		COBALT("oreCobalt"),
		ARDITE("oreArdite"),
		PLATINUM("orePlatinum"),
		OSMIUM("oreOsmium"),
		SILICON("oreSilicon"),
		MYTHRIL("oreMythril","oreMithril","oreMana"),
		REDSTONE("oreRedstone"),
		NICKOLITE("oreNickolite"),
		TESLATITE("oreTeslatite"),
		SULFUR("oreSulfur"),
		SALTPETER("oreSalpeter")
		;
		
		CommonOres(String... names)
		{
			this.names = names;
		}
		
		public String[] names;
		
		public String[] getOreDictNames()
		{
			return names;
		}
		
		public boolean exists()
		{
			return oreDictionaryContains(names);
		}
		
		public UnformedItemStack fromThis()
		{
			return new UnformedItemStack(names);
		}
		
		@Override
		public String toString()
		{
			return names != null && names.length > 0 ? Arrays.asList(names).toString() : super.toString();
		}
		
		public CommonOreRarity getRarity()
		{
			return CommonOreRarity.byOre(this);
		}
	}
	
	public static enum CommonOreRarity
	{
		COMMON(COAL),
		NORMAL(COPPER,TIN,ZINC,IRON,NICKEL,LEAD,GALENA,ALUMINIUM,AMBER,SULFUR,SALTPETER),
		UNCOMMON(SILVER,GOLD,CINNABAR,QUARTZ,MALACHITE,OSMIUM,REDSTONE,NICKOLITE,TESLATITE),
		SCATTERED(URANIUM,APATITE,LAPIS,HEMATITE,CHIMERITE,SILICON),
		RARE(PLUTONIUM,PERIDOT,AGATE,JASPER,TOURMALINE,TURQUOISE,BLUETOPAZ),
		EXCEPTIONAL(DIAMOND,SAPPHIRE,EMERALD,RUBY,AMETHYST,MOONSTONE,TUNGSTEN,COBALT,ARDITE),
		IMPOSSIBLE(IRIDIUM,SUNSTONE,PLATINUM,MYTHRIL);
		
		CommonOreRarity(CommonOres... ore)
		{
			theOre = ore;
		}
		
		public final CommonOres[] theOre;
		
		public static CommonOres[] byRarity(CommonOreRarity rarity)
		{
			return rarity.theOre;
		}
		
		public static CommonOreRarity byOre(CommonOres ore)
		{
			for(CommonOreRarity cor : CommonOreRarity.values())
				for(CommonOres ores : cor.theOre)
					if(ores.equals(ore))
						return cor;
			
			return null;
		}
	}
	
	/**
	 * Used to check, if the Forge Ore Dictionary contains any of the given names in it. 
	 * @version From DummyCore 2.1
	 * @param oreNames - the ore names to search
	 * @return true if OreDictionary contains at least one  from the array, false if not.
	 */
	public static boolean oreDictionaryContains(String... oreNames)
	{
		for(String oreName : oreNames)
			if(!OreDictionary.getOres(oreName).isEmpty())
				
				return true;
		return false;
	}
	
	public static boolean compareIS(ItemStack is, String oreName)
	{
		if(!is.isEmpty() && oreDictionaryContains(oreName))
		{
			int[] ids = OreDictionary.getOreIDs(is);
			if(ids != null && ids.length > 0)
				for(int i : ids)
					if(OreDictionary.getOreName(i).equalsIgnoreCase(oreName))
						return true;
		}
		return false;
	}
	
	/**
	 * Used to check, if the Forge Ore Dictionary contains the given name in it. 
	 * @version From DummyCore 2.1
	 * @param oreName - the ore name to search
	 * @return true if OreDictionary contains the given ore, false if not.
	 */
	public static boolean oreDictionaryContains(String oreName)
	{
		return !OreDictionary.getOres(oreName).isEmpty();
	}
	
	/**
	 * Compares if 2 itemstacks are equal on the oredict side
	 * @param stk
	 * @param stk1
	 * @return
	 * @version From DummyCore 2.1
	 */
	public static boolean oreDictionaryCompare(ItemStack stk, ItemStack stk1)
	{
		if(stk.isEmpty() || stk1.isEmpty())
			return false;
		
		if(OreDictionary.getOreIDs(stk) == null || OreDictionary.getOreIDs(stk).length == 0 || OreDictionary.getOreIDs(stk1) == null || OreDictionary.getOreIDs(stk1).length == 0)
			return false;
		
		int[] ids = OreDictionary.getOreIDs(stk);
		int[] ids1 = OreDictionary.getOreIDs(stk1);
		
		for(int i = 0; i < ids.length; ++i)
		{
			for(int j = 0; j < ids1.length; ++j)
			{
				if(ids[i] == ids1[j])
					return true;
			}
		}
		
		return false;
	}
}
