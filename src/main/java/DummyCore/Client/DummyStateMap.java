package DummyCore.Client;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;

public class DummyStateMap<T extends Comparable<T>> extends StateMapperBase {

	private final IProperty<T> nameProperty;
	private final Function<T, String> nameFunc;
	private final String suffix;
	private final List<IProperty<?>> ignored;

	private DummyStateMap(IProperty<T> nameProperty, Function<T, String> nameFunc, String suffix, List<IProperty<?>> ignored) {
		this.nameProperty = nameProperty;
		this.nameFunc = nameFunc;
		this.suffix = suffix;
		this.ignored = ignored;
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		Map<IProperty<?>, Comparable<?>> map = Maps.<IProperty<?>, Comparable<?>>newLinkedHashMap(state.getProperties());
		String s;

		if(this.nameProperty == null) {
			s = Block.REGISTRY.getNameForObject(state.getBlock()).toString();
		}
		else {
			s = String.format("%s:%s", Block.REGISTRY.getNameForObject(state.getBlock()).getResourceDomain(), this.removeName(map));
		}

		if(this.suffix != null) {
			s = s + this.suffix;
		}

		for(IProperty<?> iproperty : this.ignored) {
			map.remove(iproperty);
		}

		return new ModelResourceLocation(s, this.getPropertyString(map));
	}

	private String removeName(Map<IProperty<?>, Comparable<?>> map) {
		return nameFunc.apply((T)map.remove(this.nameProperty));
	}

	public static class Builder<T extends Comparable<T>> {

		private IProperty<T> nameProperty;
		private Function<T, String> nameFunc = value->nameProperty.getName(value);
		private String suffix;
		private List<IProperty<?>> ignored = Lists.<IProperty<?>>newArrayList();

		public DummyStateMap.Builder withName(IProperty<T> nameProperty, Function<T, String> nameFunc) {
			this.nameProperty = nameProperty;
			this.nameFunc = nameFunc;
			return this;
		}

		public DummyStateMap.Builder withSuffix(String suffix) {
			this.suffix = suffix;
			return this;
		}

		public DummyStateMap.Builder ignore(IProperty<?>... properties) {
			Collections.addAll(this.ignored, properties);
			return this;
		}

		public DummyStateMap build() {
			return new DummyStateMap(this.nameProperty, this.nameFunc, this.suffix, this.ignored);
		}
	}
}
