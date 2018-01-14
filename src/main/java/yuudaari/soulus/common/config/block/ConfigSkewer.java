package yuudaari.soulus.common.config.block;

import yuudaari.soulus.common.block.skewer.Skewer;
import yuudaari.soulus.common.config.ConfigFile;
import yuudaari.soulus.common.util.serializer.Serializable;
import yuudaari.soulus.common.util.serializer.Serialized;
import yuudaari.soulus.Soulus;

@ConfigFile(file = "block/skewer", id = Soulus.MODID)
@Serializable
public class ConfigSkewer extends ConfigUpgradeableBlock<Skewer> {

	@Override
	protected Skewer getBlock () {
		return Skewer.INSTANCE;
	}

	@Serialized public float baseDamage = 1;
	@Serialized public float upgradeDamageEffectiveness = 0.04f;
	@Serialized public int bloodPerDamage = 1;
	@Serialized public double chanceForBloodPerHit = 0.5;
	@Serialized public int ticksBetweenDamage = 15;
}
