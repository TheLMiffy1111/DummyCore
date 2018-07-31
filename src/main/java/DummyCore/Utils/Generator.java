package DummyCore.Utils;

import java.util.ArrayList;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Is pretty much a Tessellator from 1.7 for worldgen
 * <br>Is not multithread-safe?
 * @author modbder
 *
 */
public class Generator
{
	public static final Generator instance = new Generator();

	public World world;
	public boolean isWorking;
	public boolean hasOffset;

	public Coord3D offset;

	public Block setTo;
	public int genMetadata;

	public int flag;

	/**
	 * Shifts the given ExtendedAABB to roughly be an equal box
	 * @param genBox - your ExtendedAABB
	 * @return the new ExtendedAABB
	 */
	public static ExtendedAABB centerBB(ExtendedAABB genBox)
	{
		return ExtendedAABB.fromBounds(genBox.minX-(genBox.maxX-genBox.minX)/2, genBox.minY-(genBox.maxY-genBox.minY)/2, genBox.minZ-(genBox.maxZ-genBox.minZ)/2, genBox.maxX-(genBox.maxX-genBox.minX)/2, genBox.maxY-(genBox.maxY-genBox.minY)/2, genBox.maxZ-(genBox.maxZ-genBox.minZ)/2);
	}

	/**
	 * if the minX < maxX - switches them places. Repeat for all axis
	 * @param genBox - your ExtendedAABB
	 * @return the new ExtendedAABB
	 */
	public static ExtendedAABB normaliseBB(ExtendedAABB genBox)
	{
		double minX = genBox.minX;
		double minY = genBox.minY;
		double minZ = genBox.minZ;
		double maxX = genBox.maxX;
		double maxY = genBox.maxY;
		double maxZ = genBox.maxZ;

		return ExtendedAABB.fromBounds(maxX < minX ? genBox.maxX : genBox.minX, maxY < minY ? genBox.maxY : genBox.minY, maxZ < minZ ? genBox.maxZ : genBox.minZ, maxX < minX ? genBox.minX : genBox.maxX, maxY < minY ? genBox.minY : genBox.maxY, maxZ < minZ ? genBox.minZ : genBox.maxZ);
	}

	/**
	 * Gives you a list with 3d coordinates of all specified blocks in the given rect.
	 * @param genBox - your rect
	 * @return an ArrayList with all blocks matching the current settings of the Generator
	 */
	public ArrayList<Coord3D> getBlocksOfType(ExtendedAABB genBox)
	{
		gen();

		prepareBB(genBox);

		ArrayList<Coord3D> lst = new ArrayList<Coord3D>();

		for(int x = MathHelper.floor(genBox.minX); x <= MathHelper.floor(genBox.maxX); ++x)
		{
			for(int y = MathHelper.floor(genBox.minY); y <= MathHelper.floor(genBox.maxY); ++y)
			{
				for(int z = MathHelper.floor(genBox.minZ); z <= MathHelper.floor(genBox.maxZ); ++z)
				{
					if(world.getBlockState(new BlockPos(x, y, z)).getBlock() == setTo && (world.getBlockState(new BlockPos(x, y, z)).getBlock().getMetaFromState(world.getBlockState(new BlockPos(x, y, z))) == genMetadata || genMetadata == OreDictionary.WILDCARD_VALUE))
					{
						Coord3D c = new Coord3D(x,y,z);
						lst.add(c);
					}
				}
			}
		}

		restoreBB(genBox);

		return lst;
	}

	/**
	 * Offsets all Generator's functions by a given 3d point
	 * @param coord - the coord to offset against
	 */
	public void setOffset(Coord3D coord)
	{
		offset = coord;
		hasOffset = true;
	}

	/**
	 * Offsets all Generator's functions by a given 3d point
	 * @param x - x offset
	 * @param y - y offset
	 * @param z - z offset
	 */
	public void setOffset(int x, int y, int z)
	{
		offset = new Coord3D(x,y,z);
		hasOffset = true;
	}

	/**
	 * Starts your work - locks the Generator. You should do something to your generator ONLY after calling this method.
	 * @param world - the world to start in. Must be server-side!
	 */
	public void startWorldgen(World world)
	{
		if(world.isRemote)
			throw new IllegalArgumentException("Worldgen on CLIENT side is not allowed!");

		if(isWorking)
			throw new IllegalStateException("Already generating!");

		offset = null;
		hasOffset = false;
		isWorking = true;
		this.world = world;
		setTo = Blocks.AIR;
		genMetadata = 0;
		flag = 2;
	}

	/**
	 * Ends your generation. Releases the Generator, so it can be used by other code.
	 */
	public void endWorldgen()
	{
		if(!isWorking)
			throw new IllegalStateException("Not generating!");

		offset = null;
		isWorking = false;
		world = null;
		hasOffset = false;
		setTo = null;
		genMetadata = 0;
		flag = 0;
	}

	/**
	 * Sets the flag for block placement.
	 * @param i - the new flags param
	 * @see {@linkplain World#setBlockState(BlockPos, net.minecraft.block.state.IBlockState, int)}
	 */
	public void setFlag(int i)
	{
		gen();

		flag = i;
	}

	/**
	 * Checks if the current Generator is locked.
	 * @return true if the generation is possible, false otherwise
	 */
	public boolean gen()
	{
		if(!isWorking)
			throw new IllegalStateException("Can't worlgen if not generating!");

		if(world.isRemote)
			throw new IllegalArgumentException("Worldgen on CLIENT side is not allowed!");

		return isWorking;
	}


	/**
	 * De-applies the offset to the given ExtendedAABB, so it is returned into it's normal state
	 * @param genBox - the Box
	 */
	public void restoreBB(ExtendedAABB genBox)
	{
		gen();

		if(hasOffset)
		{
			genBox.minX -= offset.x;
			genBox.minY -= offset.y;
			genBox.minZ -= offset.z;
			genBox.maxX -= offset.x;
			genBox.maxY -= offset.y;
			genBox.maxZ -= offset.z;
		}
	}

	/**
	 * Applies the offset for the given box
	 * @param genBox - the Box
	 */
	public void prepareBB(ExtendedAABB genBox)
	{
		gen();

		if(hasOffset)
		{
			genBox.minX += offset.x;
			genBox.minY += offset.y;
			genBox.minZ += offset.z;
			genBox.maxX += offset.x;
			genBox.maxY += offset.y;
			genBox.maxZ += offset.z;
		}
	}

	/**
	 * Sets the functional block to the given block. This is the block that will be generated/checked against
	 * @param b - the block
	 */
	public void setBlock(Block b)
	{
		gen();

		setTo = b;
		genMetadata = 0;
	}

	/**
	 * Sets the functional metadata to the given int. This is the metadata that will be generated/checked against
	 * @param i - the metadata
	 */
	public void setMeta(int i)
	{
		gen();

		genMetadata = i;
	}

	/**
	 * Gets all blocks in the given ExtendedAABB which match the Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}
	 * <br>Then gets a random Block/Metadata pair from the given array and sets the block to those params.
	 * <br>Each block is only iterated upon once
	 * @param genBox- the box
	 * @param pairs - your Block/Metadata pair
	 */
	public void randomiseCuboid(ExtendedAABB genBox, Pair<Block,Integer>...pairs)
	{
		gen();

		prepareBB(genBox);

		int x = MathHelper.floor(genBox.minX);
		int y = MathHelper.floor(genBox.minY);
		int z = MathHelper.floor(genBox.minZ);
		int eX = MathHelper.floor(genBox.maxX);
		int eY = MathHelper.floor(genBox.maxY);
		int eZ = MathHelper.floor(genBox.maxZ);

		for(int dx = x; dx <= eX; ++dx)
		{
			for(int dy = y; dy <= eY; ++dy)
			{
				for(int dz = z; dz <= eZ; ++dz)
				{
					int i = world.rand.nextInt(pairs.length);
					if(world.getBlockState(new BlockPos(dx,dy,dz)).getBlock() == setTo)
						world.setBlockState(new BlockPos(dx, dy, dz), pairs[i].getLeft().getStateFromMeta(pairs[i].getRight()), flag);
				}
			}
		}

		restoreBB(genBox);
	}

	/**
	 * Creates a filled sphere with the Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}. The sphere will roughly be centered in the given cuboid
	 * @param genBox - the box to place in
	 */
	public void addFullSphere(ExtendedAABB genBox)
	{
		gen();

		genBox = normaliseBB(genBox);
		prepareBB(genBox);

		double radiusX = (genBox.maxX-genBox.minX)/2;
		double radiusY = (genBox.maxY-genBox.minY)/2;
		double radiusZ = (genBox.maxZ-genBox.minZ)/2;

		int dx = MathHelper.floor(genBox.minX+radiusX);
		int dy = MathHelper.floor(genBox.minY+radiusY);
		int dz = MathHelper.floor(genBox.minZ+radiusZ);

		double invRadiusX = 1.0D / radiusX;
		double invRadiusY = 1.0D / radiusY;
		double invRadiusZ = 1.0D / radiusZ;
		int ceilRadiusX = (int)Math.ceil(radiusX);
		int ceilRadiusY = (int)Math.ceil(radiusY);
		int ceilRadiusZ = (int)Math.ceil(radiusZ);
		double nextXn = 0.0D;
		boolean filled = true;

		fX:for(int x = 0; x <= ceilRadiusX; x++)
		{
			double xn = nextXn;
			nextXn = (x + 1) * invRadiusX;
			double nextYn = 0.0D;
			fZ:for(int y = 0; y <= ceilRadiusY; y++)
			{
				double yn = nextYn;
				nextYn = (y + 1) * invRadiusY;
				double nextZn = 0.0D;
				for(int z = 0; z <= ceilRadiusZ; z++)
				{
					double zn = nextZn;
					nextZn = (z + 1) * invRadiusZ;
					double distanceSq = lengthSq(xn, yn, zn);
					if(distanceSq > 1.0D)
					{
						if(z != 0)
							break;
						if(y == 0)
							break fX;
						break fZ;
					}
					if(!filled && lengthSq(nextXn, yn, zn) <= 1.0D && lengthSq(xn, nextYn, zn) <= 1.0D && lengthSq(xn, yn, nextZn) <= 1.0D)
						continue;

					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy-y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy-y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy-y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy-y), MathHelper.floor(dz-z));
				}
			}
		}
		restoreBB(genBox);
	}

	/**
	 * Creates a hollow cylinder with the Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}. The cylinder will roughly be centered in the given cuboid
	 * @param genBox - the box to place in
	 */
	public void addHollowCylinder(ExtendedAABB genBox)
	{
		gen();

		genBox = normaliseBB(genBox);
		prepareBB(genBox);

		double radiusX = (genBox.maxX-genBox.minX)/2;
		double height = genBox.maxY-genBox.minY;
		double radiusZ = (genBox.maxZ-genBox.minZ)/2;

		int dx = MathHelper.floor(genBox.minX+radiusX);
		int dy = MathHelper.floor(genBox.minY);
		int dz = MathHelper.floor(genBox.minZ+radiusZ);

		boolean filled = false;

		radiusX += 0.5D;
		radiusZ += 0.5D;
		if(height == 0)
			return;

		if(height < 0)
		{
			height = -height;
			dy = MathHelper.floor(-height);
		}
		if(dy < 0)
			dy = 0;
		else
			if(dy + height - 1 > world.getActualHeight())
				height = world.getActualHeight() - dy + 1;
		double invRadiusX = 1.0D / radiusX;
		double invRadiusZ = 1.0D / radiusZ;
		int ceilRadiusX = (int)Math.ceil(radiusX);
		int ceilRadiusZ = (int)Math.ceil(radiusZ);
		double nextXn = 0.0D;
		D:for(int x = 0; x <= ceilRadiusX; x++)
		{
			double xn = nextXn;
			nextXn = (x + 1) * invRadiusX;
			double nextZn = 0.0D;
			for(int z = 0; z <= ceilRadiusZ; z++)
			{
				double zn = nextZn;
				nextZn = (z + 1) * invRadiusZ;
				double distanceSq = lengthSq(xn, zn);
				if(distanceSq > 1.0D) {
					if(z == 0)
						break D;
					break;
				}
				if(!filled && lengthSq(nextXn, zn) <= 1.0D && lengthSq(xn, nextZn) <= 1.0D)
					continue;

				for(int y = 0; y < height; y++)
				{
					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
				}
			}
		}

		restoreBB(genBox);
	}

	/**
	 * Creates a filled cylinder with the Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}. The cylinder will roughly be centered in the given cuboid
	 * @param genBox - the box to place in
	 */
	public void addFullCylinder(ExtendedAABB genBox)
	{
		gen();

		genBox = normaliseBB(genBox);
		prepareBB(genBox);

		double radiusX = (genBox.maxX-genBox.minX)/2;
		double height = genBox.maxY-genBox.minY;
		double radiusZ = (genBox.maxZ-genBox.minZ)/2;

		int dx = MathHelper.floor(genBox.minX+radiusX);
		int dy = MathHelper.floor(genBox.minY);
		int dz = MathHelper.floor(genBox.minZ+radiusZ);

		boolean filled = true;

		radiusX += 0.5D;
		radiusZ += 0.5D;
		if(height == 0)
			return;

		if(height < 0)
		{
			height = -height;
			dy = MathHelper.floor(-height);
		}
		if(dy < 0)
			dy = 0;
		else
			if(dy + height - 1 > world.getActualHeight())
				height = world.getActualHeight() - dy + 1;
		double invRadiusX = 1.0D / radiusX;
		double invRadiusZ = 1.0D / radiusZ;
		int ceilRadiusX = (int)Math.ceil(radiusX);
		int ceilRadiusZ = (int)Math.ceil(radiusZ);
		double nextXn = 0.0D;
		D:for(int x = 0; x <= ceilRadiusX; x++)
		{
			double xn = nextXn;
			nextXn = (x + 1) * invRadiusX;
			double nextZn = 0.0D;
			for(int z = 0; z <= ceilRadiusZ; z++)
			{
				double zn = nextZn;
				nextZn = (z + 1) * invRadiusZ;
				double distanceSq = lengthSq(xn, zn);
				if(distanceSq > 1.0D) {
					if(z == 0)
						break D;
					break;
				}
				if(!filled && lengthSq(nextXn, zn) <= 1.0D && lengthSq(xn, nextZn) <= 1.0D)
					continue;

				for(int y = 0; y < height; y++)
				{
					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
				}
			}
		}

		restoreBB(genBox);
	}

	/**
	 * Creates a hollow sphere with the Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}. The sphere will roughly be centered in the given cuboid
	 * @param genBox - the box to place in
	 */
	public void addHollowSphere(ExtendedAABB genBox)
	{
		gen();

		genBox = normaliseBB(genBox);
		prepareBB(genBox);

		double radiusX = (genBox.maxX-genBox.minX)/2;
		double radiusY = (genBox.maxY-genBox.minY)/2;
		double radiusZ = (genBox.maxZ-genBox.minZ)/2;

		int dx = MathHelper.floor(genBox.minX+radiusX);
		int dy = MathHelper.floor(genBox.minY+radiusY);
		int dz = MathHelper.floor(genBox.minZ+radiusZ);

		double invRadiusX = 1.0D / radiusX;
		double invRadiusY = 1.0D / radiusY;
		double invRadiusZ = 1.0D / radiusZ;
		int ceilRadiusX = (int)Math.ceil(radiusX);
		int ceilRadiusY = (int)Math.ceil(radiusY);
		int ceilRadiusZ = (int)Math.ceil(radiusZ);
		double nextXn = 0.0D;
		boolean filled = false;

		fX:for(int x = 0; x <= ceilRadiusX; x++)
		{
			double xn = nextXn;
			nextXn = (x + 1) * invRadiusX;
			double nextYn = 0.0D;
			fZ:for(int y = 0; y <= ceilRadiusY; y++)
			{
				double yn = nextYn;
				nextYn = (y + 1) * invRadiusY;
				double nextZn = 0.0D;
				for(int z = 0; z <= ceilRadiusZ; z++)
				{
					double zn = nextZn;
					nextZn = (z + 1) * invRadiusZ;
					double distanceSq = lengthSq(xn, yn, zn);
					if(distanceSq > 1.0D)
					{
						if(z != 0)
							break;
						if(y == 0)
							break fX;
						break fZ;
					}
					if(!filled && lengthSq(nextXn, yn, zn) <= 1.0D && lengthSq(xn, nextYn, zn) <= 1.0D && lengthSq(xn, yn, nextZn) <= 1.0D)
						continue;

					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy-y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy-y), MathHelper.floor(dz+z));
					block(MathHelper.floor(dx+x), MathHelper.floor(dy-y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy+y), MathHelper.floor(dz-z));
					block(MathHelper.floor(dx-x), MathHelper.floor(dy-y), MathHelper.floor(dz-z));
				}
			}
		}
		restoreBB(genBox);
	}

	/**
	 * Creates a hollow box with the Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}. The box will roughly be centered in the given cuboid
	 * @param genBox - the box to place in
	 */
	public void addWallsCuboid(ExtendedAABB genBox)
	{
		gen();

		//Bottom
		addCuboid(ExtendedAABB.fromBounds(genBox.minX, genBox.minY, genBox.minZ, genBox.maxX, genBox.minY, genBox.maxZ));
		//Top
		addCuboid(ExtendedAABB.fromBounds(genBox.minX, genBox.maxY, genBox.minZ, genBox.maxX, genBox.maxY, genBox.maxZ));
		//X Neg
		addCuboid(ExtendedAABB.fromBounds(genBox.minX, genBox.minY, genBox.minZ, genBox.minX, genBox.maxY, genBox.maxZ));
		//X Pos
		addCuboid(ExtendedAABB.fromBounds(genBox.maxX, genBox.minY, genBox.minZ, genBox.maxX, genBox.maxY, genBox.maxZ));
		//Z Neg
		addCuboid(ExtendedAABB.fromBounds(genBox.minX, genBox.minY, genBox.minZ, genBox.maxX, genBox.maxY, genBox.minZ));
		//Z Pos
		addCuboid(ExtendedAABB.fromBounds(genBox.minX, genBox.minY, genBox.maxZ, genBox.maxX, genBox.maxY, genBox.maxZ));

	}

	/**
	 * Generates a block at the given pos
	 * @param x - x pos
	 * @param y - y pos
	 * @param z - z pos
	 * @return true if the generation was successful, false otherwise
	 */
	public boolean block(int x, int y, int z)
	{
		gen();

		return world.setBlockState(new BlockPos(x, y, z), setTo.getStateFromMeta(genMetadata), flag);
	}

	/**
	 * Fills a given cuboud with Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}
	 * @param genBox
	 */
	public void addCuboid(ExtendedAABB genBox)
	{
		gen();
		prepareBB(genBox);

		int x = MathHelper.floor(genBox.minX);
		int y = MathHelper.floor(genBox.minY);
		int z = MathHelper.floor(genBox.minZ);
		int eX = MathHelper.floor(genBox.maxX);
		int eY = MathHelper.floor(genBox.maxY);
		int eZ = MathHelper.floor(genBox.maxZ);

		addCuboid(x,y,z,eX,eY,eZ);

		restoreBB(genBox);
	}

	/**
	 * Fills the region with Block/Metadata setted by {@linkplain #setBlock(Block)} and {@linkplain #setMeta(int)}
	 * @param x - start x
	 * @param y - start y
	 * @param z - start z
	 * @param eX - end x
	 * @param eY - end y
	 * @param eZ - end z
	 */
	public void addCuboid(int x, int y, int z, int eX, int eY, int eZ)
	{
		gen();

		for(int dx = x; dx <= eX; ++dx)
		{
			for(int dy = y; dy <= eY; ++dy)
			{
				for(int dz = z; dz <= eZ; ++dz)
				{
					block(dx,dy,dz);
				}
			}
		}
	}

	//Internal
	private static double lengthSq(double x, double y, double z)
	{
		return x * x + y * y + z * z;
	}

	//Internal
	private static double lengthSq(double x, double z)
	{
		return x * x + z * z;
	}
}
