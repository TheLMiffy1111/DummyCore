package DummyCore.Client.ctm;

import team.chisel.ctm.api.texture.ICTMTexture;
import team.chisel.ctm.api.texture.TextureType;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.type.TextureTypeCTM;

@TextureType("ctm_full")
public class TextureTypeCTMFull extends TextureTypeCTM {

	@Override
	public ICTMTexture<? extends TextureTypeCTM> makeTexture(TextureInfo info) {
		return new TextureCTMFull(this, info);
	}

	@Override
	public int requiredTextures() {
		return 2;
	}

	@Override
	public int getQuadsPerSide() {
		return 1;
	}
}
