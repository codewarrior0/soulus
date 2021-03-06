package yuudaari.soulus.common.block.fossil;

import yuudaari.soulus.common.registration.Registration;
import yuudaari.soulus.common.util.Material;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;

public class FossilNetherrack extends Registration.Block {

	public FossilNetherrack () {
		this("fossil_netherrack");
	}

	public FossilNetherrack (String name) {
		super(name, new Material(MapColor.NETHERRACK));
		setHasItem();
		setHardness(0.4F);
		setHarvestLevel("pickaxe", 0);
		setSoundType(SoundType.STONE);
	}
}
