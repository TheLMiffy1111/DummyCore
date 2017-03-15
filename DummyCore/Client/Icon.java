package DummyCore.Client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * 
 * @author modbder
 * @Description
 * A simple wrapper around TextureAtlasSprite
 */
public class Icon {
	public final TextureAtlasSprite actualTexture;
	
	public Icon(TextureAtlasSprite par1)
	{
		actualTexture = par1;
	}
	
	public static Icon fromBlock(IBlockState state)
	{
		return new Icon(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
	}
	
	public static Icon fromBlock(Block b, int meta)
	{
		return fromBlock(b.getStateFromMeta(meta));
	}
	
	public static Icon fromBlock(Block b)
	{
		return fromBlock(b.getDefaultState());
	}
	
	public static Icon fromBlock(IBlockAccess w, int x, int y, int z)
	{
		return fromBlock(w.getBlockState(new BlockPos(x,y,z)));
	}
	
	public static Icon fromItem(ItemStack stk)
	{
		return fromItem(stk.getItem(),stk.getItemDamage());
	}
	
	public static Icon fromItem(Item itm, int meta)
	{
		return new Icon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(itm, meta));
	}
	
	public static Icon fromItem(Item itm)
	{
		return fromItem(itm,0);
	}
	
	public double getMinU()
	{
		return actualTexture.getMinU();
	}
	
	public double getMaxU()
	{
		return actualTexture.getMaxU();
	}
	
	public double getMinV()
	{
		return actualTexture.getMinV();
	}
	
	public double getMaxV()
	{
		return actualTexture.getMaxV();
	}
	
	public double getFrameCount()
	{
		return actualTexture.getFrameCount();
	}
	
	public double getHeight()
	{
		return actualTexture.getIconHeight();
	}
	
	public double getWidth()
	{
		return actualTexture.getIconWidth();
	}
	
	public double getInterpolatedU(double par1)
	{
		return actualTexture.getInterpolatedU(par1);
	}
	
	public double getInterpolatedV(double par1)
	{
		return actualTexture.getInterpolatedV(par1);
	}
	
	public String getIconName()
	{
		return actualTexture.getIconName();
	}
	
	public int getOriginX()
	{
		return actualTexture.getOriginX();
	}
	
	public int getOriginY()
	{
		return actualTexture.getOriginY();
	}
	
	public boolean isAnimated()
	{
		return actualTexture.hasAnimationMetadata();
	}
	
	public boolean hasAnimationMetadata()
	{
		return isAnimated();
	}
	
	public void updateAnimation()
	{
		actualTexture.updateAnimation();
	}
}
