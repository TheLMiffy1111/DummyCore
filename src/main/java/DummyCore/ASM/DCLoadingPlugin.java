package DummyCore.ASM;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

/**
 *
 * @author modbder
 * @Description Internal
 */
public class DCLoadingPlugin implements IFMLLoadingPlugin {

	public DCLoadingPlugin() {}

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {"DummyCore.ASM.DCASMManager"};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
