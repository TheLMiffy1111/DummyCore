package DummyCore.Utils;

import net.minecraft.util.WeightedRandom;

public class WeightedEnum<E extends Enum<E>> extends WeightedRandom.Item {

	public final E enumType;

	public WeightedEnum(int weight, E enumType) {
		super(weight);
		this.enumType = enumType;
	}

	public E getEnumType() {
		return this.enumType;
	}
}
