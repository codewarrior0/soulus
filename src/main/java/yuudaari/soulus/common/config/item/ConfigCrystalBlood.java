package yuudaari.soulus.common.config.item;

import yuudaari.soulus.Soulus;
import yuudaari.soulus.common.config.ConfigFile;
import yuudaari.soulus.common.util.ModPotionEffect;
import yuudaari.soulus.common.util.serializer.Serializable;
import yuudaari.soulus.common.util.serializer.Serialized;

@ConfigFile(file = "item/crystal_blood", id = Soulus.MODID)
@Serializable
public class ConfigCrystalBlood {

	@Serialized public int requiredBlood = 1000;
	@Serialized public int prickAmount = 9;
	@Serialized public int prickWorth = 90;
	@Serialized public int creaturePrickRequiredHealth = 9999999;
	@Serialized public int creaturePrickAmount = 1;
	@Serialized public int creaturePrickWorth = 3;
	@Serialized public int particleCount = 50;
	@Serialized public ModPotionEffect[] prickEffects = new ModPotionEffect[] {
		new ModPotionEffect("hunger", 100), new ModPotionEffect("nausea", 200)
	};
}