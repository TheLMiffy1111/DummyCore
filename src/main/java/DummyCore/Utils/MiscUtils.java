package DummyCore.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.HashMultimap;

import DummyCore.Core.CoreInitialiser;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

/**
 * 
 * @author Modbder
 * @version From DummyCore 1.0
 * @Description can be used to save you some time writing different functions.
 *
 */
public class MiscUtils {
	public static final String genUUIDString = "CB3F55A9-6DCC-4FF8-AAC7-9B87A33";
	public static final Hashtable<String, String> descriptionTable = new Hashtable<String, String>();
	public static final Hashtable<String, TextFormatting> descriptionCTable = new Hashtable<String, TextFormatting>();
	public static final Hashtable<List<?>, String> descriptionNTable = new Hashtable<List<?>, String>();
	public static final Hashtable<List<?>, TextFormatting> descriptionNCTable = new Hashtable<List<?>, TextFormatting>();
	public static final Hashtable<String, String> registeredClientData = new Hashtable<String, String>();
	public static final Hashtable<String, String> registeredClientWorldData = new Hashtable<String, String>();
	public static final Hashtable<String, String> registeredServerData = new Hashtable<String, String>();
	public static final Hashtable<String, String> registeredServerWorldData = new Hashtable<String, String>();
	public static final List<BlockPosition> unbreakableBlocks = new ArrayList<BlockPosition>();
	public static final List<ScheduledServerAction> actions = new ArrayList<ScheduledServerAction>();
	public static final HashMultimap<Item, IItemDescriptionGraphics> itemDescriptionGraphics = HashMultimap.<Item,IItemDescriptionGraphics>create();
	public static final ArrayList<IItemDescriptionGraphics> globalDescriptionGraphics = new ArrayList<IItemDescriptionGraphics>();
	public static final ArrayList<IHUDElement> hudElements = new ArrayList<IHUDElement>();
	public static final HashMultimap<Item,IItemOverlayElement> itemOverlayElements = HashMultimap.<Item,IItemOverlayElement>create();
	//ShaderGroups IDs - 
	//0 - Pixelated
	//1 - Smooth
	//2 - Bright, Highly blured
	//3 - High contrast, Pixel outline
	//4 - Bright, Medium blured
	//5 - Bright, Black&white only, Pixel Outline
	//6 - Default, ++Colors
	//7 - 3D anaglyph
	//8 - Upside-down
	//9 - Inverted Colors
	//10 - Television Screen
	//11 - Small pixel outline, Small blur
	//12 - Moving image overlay
	//13 - Default, Television screen overlay
	//14 - Pixel outline, White-Black colors inverted, other stay the same
	//15 - Highly pixelated
	//16 - Default, --Colors
	//17 - Television Screen, Green vision, Highly pixelated
	//18 - Blured vision
	//19 - Drugs
	//20 - Pixels highly smoothened
	//21 - Small blur
	//22 - List Index End
	public static final ResourceLocation[] defaultShaders = new ResourceLocation[] {new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json")};

	/**
	 * Creates a new NBTTagCompound for the given ItemStack
	 * @version From DummyCore 1.0
	 * @param stack - the ItemStack to work with.
	 */
	public static void createNBTTag(ItemStack stack)
	{
		if(stack.hasTagCompound())
		{
			return;
		}
		NBTTagCompound itemTag = new NBTTagCompound();
		stack.setTagCompound(itemTag);
	}

	/**
	 * used to get the ItemStack's tag compound.
	 * @version From DummyCore 1.0
	 * @param stack - the ItemStack to work with.
	 * @return NBTTagCompound of the ItemStack
	 */
	public static NBTTagCompound getStackTag(ItemStack stack)
	{
		createNBTTag(stack);
		return stack.getTagCompound();
	}

	public static void handleIInventoryPossibleNameUponPlacement(World world, BlockPos pos, ItemStack stack)
	{
		if (stack.hasDisplayName())
		{
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof IInventory)
			{
				try
				{
					Class<? extends TileEntity> clazz = tile.getClass();
					Method setCustomName = clazz.getDeclaredMethod("setCustomInventoryName", String.class);
					if(setCustomName != null)
						setCustomName.invoke(tile, stack.getDisplayName());

				}catch(Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Used to sync the given tile entity with the given side using DummyCore packet handler. 
	 * @version From DummyCore 1.4
	 * @param t - the tileentity to sync.
	 * @param s - the side, that will accept the packet.
	 */
	public static void syncTileEntity(ITEHasGameData t, Side s)
	{
		String dataString = "||mod:DummyCore.TileSync"+t.getPosition()+t.getData();
		DummyPacketIMSG simplePacket = new DummyPacketIMSG(dataString);
		if(s == Side.CLIENT)
		{
			DummyPacketHandler.sendToAll(simplePacket);
		}
		if(s == Side.SERVER)
		{
			DummyPacketHandler.sendToServer(simplePacket);
		}
	}  

	/**
	 * Sends a given NBT as a packet
	 * @param tileTag - the NBT to send
	 * @param packetID - the ID of the packet
	 */
	public static void syncTileEntity(NBTTagCompound tileTag, int packetID)
	{
		DummyPacketIMSG_Tile simplePacket = new DummyPacketIMSG_Tile(tileTag);
		CoreInitialiser.network.sendToAll(simplePacket);
	}  

	/**
	 * Used to apply any attribute to the Player.
	 * 
	 * @param p - The player to apply the attribute at
	 * @param attrib - the attribute to modify
	 * @param uuidLast5Symbols - last 5 symbols of the unique ID of your modifier. Should be unique per item, however not required strictly. The String needs to hold 5 symbols, and allowed symbols are - 0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F
	 * @param modifier - the value, that the attribute will get modified for
	 * @param remove - should actually remove the attribute from the player, and not add it
	 * @param operation - the operation on the attribute(0 is numberical modification(aka currentAttributeValue+yourValue) and 2 is percentage modification(aka currentAttributeValue*yourValue))
	 * @param type - the condition, at which the modifier should be applied. 3 Conditions exist - inventory(the item needs to be in Player's inventory),hold(Player needs to hold the item) and armor(item needs to be in Player's armor slots)
	 */
	public static void applyPlayerModifier(EntityPlayer p,IAttribute attrib, String uuidLast5Symbols, double modifier, boolean remove, int operation, String type)
	{
		if(p.getAttributeMap().getAttributeInstance(attrib).getModifier(UUID.fromString(genUUIDString+uuidLast5Symbols)) == null)
		{
			if(!remove)
				p.getAttributeMap().getAttributeInstance(attrib).applyModifier(new AttributeModifier(UUID.fromString(genUUIDString+uuidLast5Symbols),"dam."+type+"."+attrib.getName(), modifier, operation));
		}else if(remove)
		{
			if(p.getAttributeMap().getAttributeInstance(attrib).getModifier(UUID.fromString(genUUIDString+uuidLast5Symbols)) != null)
				p.getAttributeMap().getAttributeInstance(attrib).removeModifier(p.getAttributeMap().getAttributeInstance(attrib).getModifier(UUID.fromString(genUUIDString+uuidLast5Symbols)));
		}
	}

	/**
	 * Used to send packets from SERVER to CLIENT.
	 * @version From DummyCore 1.7
	 * @param w - the getEntityWorld() that we are operating in
	 * @param pkt - the packet to send
	 * @param x - the X coordinate
	 * @param y - the Y coordinate
	 * @param z - the Z coordinate
	 * @param dimId - the ID of the dimension to look the players. 
	 * @param distance - the distance at which the players will get found.
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void sendPacketToAllAround(World w,Packet pkt, int x, int y, int z, int dimId, double distance) {
		List<EntityPlayer> playerLst = w.<EntityPlayer>getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(x-0.5D, y-0.5D, z-0.5D, x+0.5D, y+0.5D, z+0.5D).expand(distance, distance, distance));
		if(!playerLst.isEmpty()) {
			for(int i = 0; i < playerLst.size(); ++i) {
				EntityPlayer player = playerLst.get(i);
				if(player instanceof EntityPlayerMP) {
					if(pkt instanceof SPacketUpdateTileEntity) {
						NBTTagCompound tileTag = new NBTTagCompound();
						w.getTileEntity(new BlockPos(x, y, z)).writeToNBT(tileTag);
						CoreInitialiser.network.sendTo(new DummyPacketIMSG_Tile(tileTag,-10), (EntityPlayerMP) player);
					}
					else {
						if(player.dimension == dimId)
							((EntityPlayerMP)player).getServerWorld().getMinecraftServer().getPlayerList().sendPacketToAllPlayers(pkt);
					}
				}
				else {
					Notifier.notifyDebug("Trying to send packet "+pkt+" to all around on Client side, probably a bug, ending the packet send try");
				}
			}
		}
	}

	/**
	 * Used to send packets from SERVER to CLIENT.
	 * @version From DummyCore 1.7
	 * @param w - the getEntityWorld() that we are operating in
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void sendPacketToAll(World w,Packet pkt)
	{
		List<EntityPlayer> playerLst = w.playerEntities;
		if(!playerLst.isEmpty())
		{
			for(int i = 0; i < playerLst.size(); ++i)
			{
				EntityPlayer player = playerLst.get(i);
				if(player instanceof EntityPlayerMP)
				{
					((EntityPlayerMP)player).connection.sendPacket(pkt);
				}else
				{
					Notifier.notifyDebug("Trying to send packet "+pkt+" to all on Client side, probably a bug, ending the packet send try");
				}
			}
		}
	}

	/**
	 * Used to send packets from SERVER to CLIENT.
	 * @version From DummyCore 1.7
	 * @param w - the getEntityWorld() that we are operating in
	 * @param pkt - the packet to send
	 * @param dimId - the ID of the dimension to look the players. 
	 */
	@SuppressWarnings({ "rawtypes" })
	public static void sendPacketToAllInDim(World w,Packet pkt, int dimId)
	{
		List<EntityPlayer> playerLst = w.playerEntities;
		if(!playerLst.isEmpty())
		{
			for(int i = 0; i < playerLst.size(); ++i)
			{
				EntityPlayer player = playerLst.get(i);
				if(player instanceof EntityPlayerMP)
				{
					if(player.dimension == dimId)
						((EntityPlayerMP)player).connection.sendPacket(pkt);
				}else
				{
					Notifier.notifyDebug("Trying to send packet "+pkt+" to all in dimension "+dimId+" on Client side, probably a bug, ending the packet send try");
				}
			}
		}
	}

	/**
	 * Used to send packets from SERVER to CLIENT.
	 * @version From DummyCore 1.7
	 * @param w - the getEntityWorld() that we are operating in
	 * @param pkt - the packet to send
	 * @param player - the player to whom we are sending the packet.
	 */
	@SuppressWarnings("rawtypes")
	public static void sendPacketToPlayer(World w,Packet pkt,EntityPlayer player)
	{
		if(player instanceof EntityPlayerMP)
		{
			((EntityPlayerMP)player).connection.sendPacket(pkt);
		}else
		{
			Notifier.notifyDebug("Trying to send packet "+pkt+" to player "+player+"||"+player.getDisplayName()+" on Client side, probably a bug, ending the packet send try");
		}
	}

	@Deprecated
	public static boolean classHasMethod(Class<?> c, String mName, Class<?>... classes)
	{
		return PrimitiveUtils.classHasMethod(c, mName, classes);
	}

	/**
	 * Have you ever thought that saving inventories to NBTTag takes too much code? Here is a nifty solution to do so!
	 * @param t - the TileEntity
	 * @param saveTag - the tag
	 */
	public static void saveInventory(TileEntity t, NBTTagCompound saveTag)
	{
		if(t instanceof IInventory)
		{
			IInventory tile = (IInventory) t;
			NBTTagList nbttaglist = new NBTTagList();
			for (int i = 0; i < tile.getSizeInventory(); ++i)
			{
				if (!tile.getStackInSlot(i).isEmpty())
				{
					NBTTagCompound nbttagcompound1 = new NBTTagCompound();
					nbttagcompound1.setByte("Slot", (byte)i);
					tile.getStackInSlot(i).writeToNBT(nbttagcompound1);
					nbttaglist.appendTag(nbttagcompound1);
				}
			}
			saveTag.setTag("Items", nbttaglist);
		}
	}

	/**
	 * Have you ever thought that loading inventories from NBTTag takes too much code? Here is a nifty solution to do so!
	 * @param t - the TileEntity
	 * @param loadTag - the tag
	 */
	public static void loadInventory(TileEntity t, NBTTagCompound loadTag)
	{
		if(t instanceof IInventory)
		{
			IInventory tile = (IInventory) t;
			for(int i = 0; i < tile.getSizeInventory(); ++i)
			{
				tile.setInventorySlotContents(i, ItemStack.EMPTY);
			}
			NBTTagList nbttaglist = loadTag.getTagList("Items", 10);
			for (int i = 0; i < nbttaglist.tagCount(); ++i)
			{
				NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				byte b0 = nbttagcompound1.getByte("Slot");

				if (b0 >= 0 && b0 < tile.getSizeInventory())
				{
					tile.setInventorySlotContents(b0, new ItemStack(nbttagcompound1));
				}
			}
		}
	}

	/**
	 * Actually changes the Biome at the given coordinates. It still requires Client to update the BlockRenderer at the position!
	 * @param w - World
	 * @param biome - the biome you are changing to
	 * @param x - xCoordinate of the BLOCK
	 * @param z - zCoordinate of the BLOCK
	 */
	public static void changeBiome(World w, Biome biome, int x, int z)
	{
		Chunk chunk = w.getChunkFromBlockCoords(new BlockPos(x,w.getActualHeight(),z));
		byte[] b = chunk.getBiomeArray();
		byte cbiome = b[(z & 0xf) << 4 | x & 0xf]; //What is even going on here? Can this code be a little bit more readable?
		cbiome = (byte)(biome.getIdForBiome(biome) & 0xff);
		b[(z & 0xf) << 4 | x & 0xf] = cbiome; //Looks like not.
		chunk.setBiomeArray(b);
		notifyBiomeChange(x,z,biome.getIdForBiome(biome));
	}

	/**
	 * Actually creates the given particles for ALL players
	 * @param particleName - the name of the particle
	 * @param posX - xCoord of the particle
	 * @param posY - yCoord of the particle
	 * @param posZ - zCoord of the particle
	 * @param par5 - particle 1 gen int. Can be motion or color(depends on the particle).
	 * @param par6 - particle 2 gen int. Can be motion or color(depends on the particle).
	 * @param par7 - particle 3 gen int. Can be motion or color(depends on the particle).
	 */
	public static void spawnParticlesOnServer(String particleName, float posX, float posY, float posZ, double par5, double par6, double par7)
	{
		String dataString = "||mod:DummyCore.Particle";
		DummyData name = new DummyData("particleName",particleName);
		DummyData xpos = new DummyData("positionX",posX);
		DummyData ypos = new DummyData("positionX",posY);
		DummyData zpos = new DummyData("positionX",posZ);
		DummyData xmot = new DummyData("par1",par5);
		DummyData ymot = new DummyData("par2",par6);
		DummyData zmot = new DummyData("par3",par7);
		DataStorage.addDataToString(name);
		DataStorage.addDataToString(xpos);
		DataStorage.addDataToString(ypos);
		DataStorage.addDataToString(zpos);
		DataStorage.addDataToString(xmot);
		DataStorage.addDataToString(ymot);
		DataStorage.addDataToString(zmot);
		String newDataString = DataStorage.getDataString();
		dataString+=newDataString;
		DummyPacketIMSG simplePacket = new DummyPacketIMSG(dataString);
		DummyPacketHandler.sendToAll(simplePacket);
	}

	/**
	 * Plays a sound to all players nearby
	 * @version From DummyFore 2.0
	 * @param x - sound x
	 * @param y - sound y
	 * @param z - sound z
	 * @param sound - the sound itself
	 * @param volume - sound volume
	 * @param pitch - sound pitch
	 * @param radius - radius to play the sound in
	 * @param dim - dimension to play the sound in
	 */
	public static void playSoundOnServerToAllNearby(double x, double y, double z, String sound, float volume, float pitch, double radius, int dim)
	{
		DummyData aaa = new DummyData("x",x);
		DummyData aab = new DummyData("y",y);
		DummyData aac = new DummyData("z",z);
		DummyData aad = new DummyData("vol",volume);
		DummyData aae = new DummyData("pitch",pitch);
		DummyData aaf = new DummyData("sound",sound);
		DummyPacketIMSG pkt = new DummyPacketIMSG("||mod:DummyCore.Sound"+aaa+""+aab+""+aac+""+aad+""+aae+""+aaf);
		DummyPacketHandler.sendToAllAround(pkt, new TargetPoint(dim, x, y, z, radius));
	}

	/**
	 * Adds a potion effect to the player.<BR> If the effect exists - increases the duration.<BR> If the duration is over specified amount adds +1 level. 
	 * @param mob - the entity to add the effect
	 * @param potion - the potion to apply
	 * @param index - the duration
	 * @param index2 - the duration to add +1 level at
	 * @version From DummyCore 2.0
	 */
	public static void calculateAndAddPE(EntityLivingBase mob, Potion potion, int index, int index2)
	{
		boolean hasEffect = mob.getActivePotionEffect(potion) != null;
		if(hasEffect)
		{
			int currentDuration = mob.getActivePotionEffect(potion).getDuration();
			int newDuration = currentDuration+index2;
			int newModifier = currentDuration/index;
			mob.removePotionEffect(potion);
			mob.addPotionEffect(new PotionEffect(potion,newDuration,newModifier));
		}else
		{
			mob.addPotionEffect(new PotionEffect(potion,index2,0));
		}
	}

	/**
	 * Adds a specific action for the server to execute after some time
	 * @param ssa
	 */
	public static void addScheduledAction(ScheduledServerAction ssa)
	{
		if(FMLCommonHandler.instance().getEffectiveSide() != Side.SERVER)
			Notifier.notifyError("Trying to add a scheduled server action not on server side, aborting!");

		actions.add(ssa);
	}

	/**
	 * Clones the given Entity, including it's full NBTTag
	 * @param e - the entity to clone
	 * @return The cloned entity
	 */
	public static Entity cloneEntity(Entity e)
	{
		Entity retEntity = EntityList.createEntityByIDFromName(EntityList.getKey(e), e.getEntityWorld());
		retEntity.readFromNBT(e.writeToNBT(new NBTTagCompound()));
		return retEntity;
	}

	/**
	 * Changes biome at the given coordinates. Unlike the previous function this one takes the biomeID, not the biome itself and isn't world dependant
	 * @param x - x position of the block
	 * @param z - z position of the block
	 * @param biomeID - the new BiomeID
	 */
	public static void notifyBiomeChange(int x, int z, int biomeID)
	{
		String dataString = "||mod:DummyCore.BiomeChange";
		DummyData xpos = new DummyData("positionX",x);
		DummyData zpos = new DummyData("positionZ",z);
		DummyData id = new DummyData("biomeID",biomeID);
		DataStorage.addDataToString(xpos);
		DataStorage.addDataToString(zpos);
		DataStorage.addDataToString(id);
		String newDataString = DataStorage.getDataString();
		dataString+=newDataString;
		DummyPacketIMSG simplePacket = new DummyPacketIMSG(dataString);
		DummyPacketHandler.sendToAll(simplePacket);
	}

	/**
	 * Imitates the armor absorbption for the given damage. Can be used, if you damage your target indirectly, but still want the damage to get reduced by armor
	 * @param base - The damaged Entity
	 * @param dam - the damage source
	 * @param amount - the amount of the damage
	 * @return New amount of damage(the old one reduced by armor)
	 */
	public static float multiplyDamageByArmorAbsorbption(EntityLivingBase base, DamageSource dam, float amount)
	{
		if (!dam.isUnblockable())
		{
			int i = 25 - base.getTotalArmorValue();
			float f1 = amount * i;
			amount = f1 / 25.0F;
		}
		return amount;
	}

	/**
	 * Imitates the damage increasement by things like Strength potion and Sharpness|Power enchantments. 
	 * @param base - The damaged Entity
	 * @param dam - the damage source
	 * @param amount - the amount of the damage
	 * @return New amount of damage(the old one reduced by armor)
	 */
	public static float applyPotionDamageCalculations(EntityLivingBase base, DamageSource dam, float amount)
	{
		if (dam.isDamageAbsolute())
		{
			return amount;
		}
		int i;
		int j;
		float f1;

		if (base.isPotionActive(MobEffects.RESISTANCE) && dam != DamageSource.OUT_OF_WORLD)
		{
			i = (base.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
			j = 25 - i;
			f1 = amount * j;
			amount = f1 / 25.0F;
		}

		if (amount <= 0.0F)
		{
			return 0.0F;
		}
		i = EnchantmentHelper.getEnchantmentModifierDamage(base.getEquipmentAndArmor(), dam);

		if (i > 20)
		{
			i = 20;
		}

		if (i > 0 && i <= 20)
		{
			j = 25 - i;
			f1 = amount * j;
			amount = f1 / 25.0F;
		}

		return amount;
	}

	/**
	 * Damages the given Entity ignoring the Forge EntityHurt and EntityBeeingDamaged events.
	 * @param base - The damaged Entity
	 * @param dam - the damage source
	 * @param amount - the amount of the damage
	 */
	public static void damageEntityIgnoreEvent(EntityLivingBase base, DamageSource dam, float amount)
	{
		if (!base.isEntityInvulnerable(dam))
		{
			if (amount <= 0) return;
			amount = multiplyDamageByArmorAbsorbption(base,dam,amount);
			amount = applyPotionDamageCalculations(base,dam,amount);
			float f1 = amount;
			amount = Math.max(amount - base.getAbsorptionAmount(), 0.0F);
			base.setAbsorptionAmount(base.getAbsorptionAmount() - (f1 - amount));

			if (amount != 0.0F)
			{
				float f2 = base.getHealth();
				base.setHealth(f2 - amount);
				base.getCombatTracker().trackDamage(dam, f2, amount);
				base.setAbsorptionAmount(base.getAbsorptionAmount() - amount);
			}
		}
	}

	/**
	 * Allows changes of variables declared like private final || private static final. Advanced. Do not use if you do not know what you are doing!
	 * Sometimes considered as a dirty hacking of the java code. I agree. There is nothing more dirty, than just removing the FINAL modifier of the variable. It's like Java can't even do anything, no matter the protection given.
	 * This should not be done. However, in vanilla MC it is pretty much the only way to do so, so I can't help it.
	 * The only thing, that would be worse is using ASM to remotely change the compiled final variable. That is the most disgusting thing you can do with Java, I believe.
	 * @param classToAccess - the class in wich you are changing the variable
	 * @param instance - if you want to modify non-static field you should put the instance of the class here. Leave null for static
	 * @param value - what you actually want to be set in the variable field
	 * @param fieldNames - the names of the field you are changing. Should be both for obfuscated and compiled code.
	 */
	public static void setPrivateFinalValue(Class<?> classToAccess, Object instance, Object value, String fieldNames[])
	{
		Field field = ReflectionHelper.findField(classToAccess, ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldNames));
		try
		{
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(instance, value);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Calls the private static "register" from {@link CriteriaTriggers}
	 * Code from CyclopsCore
	 * @param criterion The criterion.
	 * @param <T> The criterion type.
	 * @return The registered instance.
	 */
	public static <T extends ICriterionTrigger<?>> T registerCriteriaTrigger(T criterion) {
		Method method = ReflectionHelper.findMethod(CriteriaTriggers.class, "register", "func_192118_a", ICriterionTrigger.class);
		try {
			return (T)method.invoke(null, criterion);
		}catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Sets the block at the given coordinates to unbreakable || breakable
	 * @param w - the World
	 * @param x - the x of the block
	 * @param y - the y of the block
	 * @param z - the z of the block
	 * @param remove - should actually set the block to breakable(true) of to unbreakable(false)
	 */
	public static void setBlockUnbreakable(World w, int x, int y, int z, boolean remove)
	{
		if(!isBlockUnbreakable(w,x,y,z) && !remove)
		{
			BlockPosition pos = new BlockPosition(w, x, y, z);
			unbreakableBlocks.add(pos);
		}else
		{
			for(int i = 0; i < unbreakableBlocks.size(); ++i)
			{
				BlockPosition pos = unbreakableBlocks.get(i);
				if(pos.x == x && pos.y == y && pos.z == z && pos.wrld.provider.getDimension() == w.provider.getDimension())
				{
					unbreakableBlocks.remove(pos);
					break;
				}
			}
		}
	}

	/**
	 * Checks if the block at the given coordinates is unbreakable
	 * @param w - the World
	 * @param x - the x of the block
	 * @param y - the y of the block
	 * @param z - the z of the block
	 * @return True if the player can break the block, false if not
	 */
	public static boolean isBlockUnbreakable(World w, int x, int y, int z)
	{
		for(int i = 0; i < unbreakableBlocks.size(); ++i)
		{
			BlockPosition pos = unbreakableBlocks.get(i);
			if(pos.x == x && pos.y == y && pos.z == z && pos.wrld.provider.getDimension() == w.provider.getDimension())
				return true;
		}
		return false;
	}

	/**
	 * Sends the packet to the server, that notifies the server about GUI button pressed. This can be actually used for any GUI, not only in world, but why would you like to do it?
	 * @param buttonID - the ID on the button in the code. Can be get via yourGuiButton.id
	 * @param parentClass - the GUI class, that contains the button
	 * @param buttonClass - the GUI class of the button
	 * @param presser - the player, who presses the button. Usually Minecraft.getMinecraft().player but sometimes you may want to send packets of other SMP players(maybe?)
	 */
	@SideOnly(Side.CLIENT)
	public static void handleButtonPress(int buttonID, Class<? extends Gui> parentClass, Class<? extends GuiButton> buttonClass, EntityPlayer presser, int bX, int bY, int bZ)
	{
		handleButtonPress(buttonID, parentClass, buttonClass, presser, bX, bY, bZ, "||data:no data");
	}

	/**
	 * Sends the packet to the server, that notifies the server about GUI button pressed. This can be actually used for any GUI, not only in world, but why would you like to do it?
	 * @param buttonID - the ID on the button in the code. Can be get via yourGuiButton.id
	 * @param parentClass - the GUI class, that contains the button
	 * @param buttonClass - the GUI class of the button
	 * @param presser - the player, who presses the button. Usually Minecraft.getMinecraft().player but sometimes you may want to send packets of other SMP players(maybe?)
	 * @param additionalData - Some additional data, that you might want to carry around. Should be a String, representing the DummyData, otherwise will get added tp the Z coordinate and make it unreadable.
	 */
	@SideOnly(Side.CLIENT)
	public static void handleButtonPress(int buttonID, Class<? extends Gui> parentClass, Class<? extends GuiButton> buttonClass, EntityPlayer presser, int bX, int bY, int bZ, String additionalData)
	{
		System.out.println("[DummyCore] Recieved Button Press From:" + parentClass.getName());
		String dataString = "||mod:DummyCore.guiButton";
		DummyData id = new DummyData("id",buttonID);
		DummyData parent = new DummyData("parent",parentClass.getName());
		DummyData button = new DummyData("button",buttonClass.getName());
		DummyData player = new DummyData("player",presser.getGameProfile().getId());
		DummyData dx = new DummyData("x",bX);
		DummyData dy = new DummyData("y",bY);
		DummyData dz = new DummyData("z",bZ);
		DataStorage.addDataToString(id);
		DataStorage.addDataToString(parent);
		DataStorage.addDataToString(button);
		DataStorage.addDataToString(player);
		DataStorage.addDataToString(dx);
		DataStorage.addDataToString(dy);
		DataStorage.addDataToString(dz);
		String newDataString = DataStorage.getDataString();
		dataString+=newDataString+additionalData;
		DummyPacketIMSG simplePacket = new DummyPacketIMSG(dataString);
		DummyPacketHandler.sendToServer(simplePacket);
	}

	/**
	 * Searches for the first block, that matches the given condition at the given coordinates in the given Y range. 
	 * @param w - the getEntityWorld() where we are searching the block at
	 * @param toSearch - the block that we are searching for
	 * @param x - the X coordinate
	 * @param z - the Z coordinate
	 * @param maxY - max Y value to search. 
	 * @param minY - min Y value to search
	 * @param metadata - the metadata of the block to search. can be -1 or OreDectionary.WILDCARD_VALUE to ignore metadata
	 * @param shouldHaveAirAbove - should the block that we find have only air blocks above it
	 * @return the actual Y coordinate, or -1 if no blocks were found.
	 */
	public static int search_firstBlock(World w,Block toSearch,int x, int z, int maxY, int minY, int metadata, boolean shouldHaveAirAbove)
	{
		int y = maxY;
		while(y > minY)
		{
			Block b = w.getBlockState(new BlockPos(x, y, z)).getBlock();
			int meta = w.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(w.getBlockState(new BlockPos(x, y, z)));
			if(b != null && b != Blocks.AIR)
			{
				if(b == toSearch && (metadata == -1 || metadata == OreDictionary.WILDCARD_VALUE || metadata == meta))
				{
					return y;
				}else if(shouldHaveAirAbove)
				{
					return -1;
				}
			}
			--y;
		}
		return -1;
	}

	/**
	 * Opens a GUI for the ID from your GuiContainerLibrary
	 * @param w - the world we are in
	 * @param x - x pos of the block
	 * @param y - y pos of the block
	 * @param z - z pos of the block
	 * @param player - the player opening the GUI
	 * @param guiID - the GUI id from the GuiContainerLibrary
	 */
	public static void openGui(World w, int x, int y, int z, EntityPlayer player, int guiID)
	{
		player.openGui(CoreInitialiser.instance, guiID, w, x, y, z);
	}

	/**
	 * Sets the current shader as a given ID
	 * @param shaderID - the ID of the default shader, or -1 to reset them
	 */
	public static void setShaders(int shaderID)
	{
		if(shaderID >= defaultShaders.length)shaderID = defaultShaders.length-1;
		if(shaderID < 0)setShaders(null);else CoreInitialiser.proxy.initShaders(defaultShaders[shaderID]);
	}

	/**
	 * Sets the current shader from a given ResourceLocation
	 * @param shaders - the shader.json file
	 */
	public static void setShaders(ResourceLocation shaders)
	{
		CoreInitialiser.proxy.initShaders(shaders);
	}

	@Deprecated
	public static boolean classExists(String className)
	{
		return PrimitiveUtils.classExists(className);
	}

	/**
	 * Gets the closest Entity from the given list relative to the given coords
	 * @param mobs -the list of entities
	 * @param x - x
	 * @param y - y
	 * @param z - z
	 * @return The closest entity to the given point
	 */
	public static Entity getClosestEntity(List<Entity> mobs, double x, double y, double z)
	{
		double minDistance = Double.MAX_VALUE;
		Entity retEntity = null;

		for(Entity elb : mobs)
		{
			double distance = elb.getDistance(x, y, z);
			if(distance < minDistance)
			{
				retEntity = elb;
				minDistance = distance;
			}
		}

		return retEntity;
	}

	/**
	 * Checks if 2 ItemStacks are equal or if their metadata is an OreDictionary.WILDCARD_VALUE checks for their OreDict entry
	 * @param is1
	 * @param is2
	 * @return
	 */
	public static boolean compareItemStacks(ItemStack is1, ItemStack is2)
	{
		return is1.getItemDamage() == OreDictionary.WILDCARD_VALUE && is2.getItemDamage() == OreDictionary.WILDCARD_VALUE ? Item.getIdFromItem(is1.getItem()) == Item.getIdFromItem(is2.getItem()) || OreDictUtils.oreDictionaryCompare(is1,is2) : is1.isItemEqual(is2) && ItemStack.areItemStacksEqual(is1, is2) || OreDictUtils.oreDictionaryCompare(is1,is2);
	}

	/**
	 * Since Enchantment.enchantmentsList is now private(why?) here is a method to get it's object
	 * @return - the Enchantment.enchantmentsList
	 */
	public static Enchantment[] enchantmentList()
	{
		try
		{
			Class<Enchantment> enchclazz = Enchantment.class;
			Field fld = enchclazz.getDeclaredFields()[0];
			fld.setAccessible(true);
			return Enchantment[].class.cast(fld.get(null));

		}catch(Exception e){e.printStackTrace();}
		return null;
	}

	@Deprecated
	public static boolean checkSameAndNullStrings(String par1, String par2)
	{
		return PrimitiveUtils.checkSameAndNullStrings(par1, par2);
	}

	/**
	 * Crop helper. Gets a growth chance for the given crop based on it's surroundings
	 * @param blockIn - the crop block
	 * @param worldIn - the world
	 * @param pos - the pos of the crop block
	 * @return The growth chance for the given crop based on it's surroundings
	 */
	public static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
	{
		float f = 1.0F;
		BlockPos blockpos1 = pos.down();

		for (int i = -1; i <= 1; ++i)
		{
			for (int j = -1; j <= 1; ++j)
			{
				float f1 = 0.0F;
				IBlockState iblockstate = worldIn.getBlockState(blockpos1.add(i, 0, j));

				if (iblockstate.getBlock().canSustainPlant(worldIn.getBlockState(blockpos1.add(i, 0, j)), worldIn, blockpos1.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)blockIn))
				{
					f1 = 1.0F;

					if (iblockstate.getBlock().isFertile(worldIn, blockpos1.add(i, 0, j)))
					{
						f1 = 3.0F;
					}
				}

				if (i != 0 || j != 0)
				{
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		BlockPos blockpos2 = pos.north();
		BlockPos blockpos3 = pos.south();
		BlockPos blockpos4 = pos.west();
		BlockPos blockpos5 = pos.east();
		boolean flag = blockIn == worldIn.getBlockState(blockpos4).getBlock() || blockIn == worldIn.getBlockState(blockpos5).getBlock();
		boolean flag1 = blockIn == worldIn.getBlockState(blockpos2).getBlock() || blockIn == worldIn.getBlockState(blockpos3).getBlock();

		if (flag && flag1)
		{
			f /= 2.0F;
		}
		else
		{
			boolean flag2 = blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos5.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock();

			if (flag2)
			{
				f /= 2.0F;
			}
		}

		return f;
	}

	/**
	 * Adds a handler to draw something graphical upon mousing over an item
	 * @param b - the block to register for
	 * @param iidg - the handler
	 */
	public static void addItemGraphicsDescription(Block b, IItemDescriptionGraphics iidg)
	{
		if(Item.getItemFromBlock(b) != null)
			addItemGraphicsDescription(Item.getItemFromBlock(b),iidg);
	}

	/**
	 * Adds a handler to draw something graphical upon mousing over an item
	 * @param iidg - the handler
	 */
	public static void addGlobalItemGraphicsDescription(IItemDescriptionGraphics iidg) {
		globalDescriptionGraphics.add(iidg);
	}

	/**
	 * Adds a handler to draw something graphical upon mousing over an item
	 * @param i - the item to register for
	 * @param iidg - the handler
	 */
	public static void addItemGraphicsDescription(Item i, IItemDescriptionGraphics iidg) {
		itemDescriptionGraphics.put(i, iidg);
	}

	/**
	 * Adds an element to be displayed on player's HUD
	 * @param ihe - the element to register
	 */
	public static void addHUDElement(IHUDElement ihe) {
		hudElements.add(ihe);
	}

	public static void addItemOverlayElement(Item i, IItemOverlayElement iioe) {
		itemOverlayElements.put(i, iioe);
	}

	@Deprecated
	public static <T>ArrayList<T> listOf(T... array) {
		return PrimitiveUtils.listOf(array);
	}

	@Deprecated
	public static <T>boolean checkArray(T[] array, T object) {
		return PrimitiveUtils.checkArray(array, object);
	}

	public static void writeBlockPosToNBT(NBTTagCompound tag, BlockPos toWrite, String key) {
		if(toWrite != null)
			tag.setIntArray(key, new int[]{toWrite.getX(),toWrite.getY(),toWrite.getZ()});
	}

	public static BlockPos readBlockPosFromNBT(NBTTagCompound tag, String key) {
		BlockPos ret = BlockPos.ORIGIN;
		if(tag.hasKey(key, 11)) {
			int[] t = tag.getIntArray(key);
			ret = new BlockPos(t[0],t[1],t[2]);
		}
		return ret;
	}

	public static ResourceLocation getUniqueIdentifierFor(Object obj) {
		if(obj instanceof Block || obj instanceof Item || obj instanceof ItemStack) {
			if(obj instanceof Block)
				return Block.REGISTRY.getNameForObject((Block) obj);
			if(obj instanceof Item)
				if(obj instanceof ItemBlock)
					return Block.REGISTRY.getNameForObject(Block.getBlockFromItem((Item) obj));
				else
					return Item.REGISTRY.getNameForObject((Item) obj);
			if(obj instanceof ItemStack)
				return getUniqueIdentifierFor(((ItemStack) obj).getItem());
		}

		return null;
	}

	public static BlockPos fromIntArray(int[] array) {
		if(array.length != 3)
			return BlockPos.ORIGIN;
		return new BlockPos(array[0],array[1],array[2]);
	}

	public static String getUsernameFromPlayer(EntityPlayer player) {
		return player.getEntityWorld().isRemote ? "" : UsernameCache.getLastKnownUsername(getUUIDFromPlayer(player));
	}

	public static EntityPlayer getPlayerFromUsername(String username) {
		if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
			return null;
		}

		return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(username);
	}

	public static EntityPlayer getPlayerFromUUID(String uuid) {
		return getPlayerFromUsername(getUsernameFromUUID(uuid));
	}

	public static EntityPlayer getPlayerFromUUID(UUID uuid) {
		return getPlayerFromUsername(getUsernameFromUUID(uuid));
	}

	public static UUID getUUIDFromPlayer(EntityPlayer player) {
		return player.getGameProfile().getId();
	}

	public static String getUsernameFromUUID(String uuid) {
		try {
			return UsernameCache.getLastKnownUsername(UUID.fromString(uuid));
		}
		catch(IllegalArgumentException e) {
			return null;
		}
	}

	public static String getUsernameFromUUID(UUID uuid) {
		return UsernameCache.getLastKnownUsername(uuid);
	}

	public static Set<ItemStack> getSubItemsToDraw(ItemStack stk) {
		stk = stk.copy();

		if(stk.getItemDamage() != OreDictionary.WILDCARD_VALUE)
			return Collections.<ItemStack>singleton(stk);

		if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			stk.setItemDamage(0);
			return Collections.<ItemStack>singleton(stk);
		}

		Item it = stk.getItem();
		LinkedHashSet<ItemStack> ret = new LinkedHashSet<ItemStack>();
		NonNullList<ItemStack> lst = NonNullList.<ItemStack>create();
		it.getSubItems(CreativeTabs.SEARCH, lst);
		for(int i = 0; i < lst.size(); i++) {
			ItemStack stk0 = lst.get(i);
			if(stk0.isEmpty())
				lst.remove(i);
			else
				stk0.setCount(stk.getCount());
		}
		ret.addAll(lst);

		return ret;
	}

	public static void unlockAdvancement(EntityPlayer player, ResourceLocation name, String criterion) {
		if(player instanceof EntityPlayerMP) {
			PlayerAdvancements advancements = ((EntityPlayerMP)player).getAdvancements();
			AdvancementManager manager = ((WorldServer)player.getEntityWorld()).getAdvancementManager();
			Advancement advancement = manager.getAdvancement(name);
			if(advancement!=null)
				advancements.grantCriterion(advancement, criterion);
		}
	}
}
