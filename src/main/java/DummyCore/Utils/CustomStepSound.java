package DummyCore.Utils;

import DummyCore.Registries.GenericRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.GameData;

/**
 * A simple wrapper for custom step sounds for blocks
 * @author modbder
 *
 */
public class CustomStepSound extends SoundType {

	public CustomStepSound(String name, float volume, float frequency) {
		super(volume, frequency, reg(name+".break"), reg(name+".step"), reg(name+".place"), reg(name+".hit"), reg(name+".fall"));
	}

	static SoundEvent reg(String name) {
		SoundEvent ret = new SoundEvent(new ResourceLocation(name));
		return GenericRegistry.register(ret, name);
	}
}
