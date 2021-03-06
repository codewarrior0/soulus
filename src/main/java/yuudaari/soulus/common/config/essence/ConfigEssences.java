package yuudaari.soulus.common.config.essence;

import java.util.ArrayList;
import java.util.List;
import yuudaari.soulus.Soulus;
import yuudaari.soulus.common.config.ConfigFile;
import yuudaari.soulus.common.util.serializer.CollectionSerializer;
import yuudaari.soulus.common.util.serializer.Serializable;
import yuudaari.soulus.common.util.serializer.Serialized;

@ConfigFile(file = "essences/essences", id = Soulus.MODID)
@Serializable
public class ConfigEssences {

	@Serialized(value = EssencesSerializer.class) public List<ConfigEssence> essences;
	{
		essences = new ArrayList<>();

		// @formatter:off

		// normal
		essences.add(new ConfigEssence("minecraft:bat", new ConfigCreatureBone("NORMAL", 1)).addLoot(1, 1, 0.1));
		essences.add(new ConfigEssence("minecraft:chicken", new ConfigCreatureBone("NORMAL", 8)).addLoot(1, 1, 0.2));
		essences.add(new ConfigEssence("minecraft:cow", new ConfigCreatureBone("NORMAL", 8)).addLoot(1, 2, 0.4));
		essences.add(new ConfigEssence("minecraft:pig", new ConfigCreatureBone("NORMAL", 6)).addLoot(1, 2, 0.3));
		essences.add(new ConfigEssence("minecraft:rabbit", new ConfigCreatureBone("NORMAL", 4)).addLoot(1, 1, 0.1));
		essences.add(new ConfigEssence("minecraft:sheep", new ConfigCreatureBone("NORMAL", 6)).addLoot(1, 2, 0.4));
		essences.add(new ConfigEssence("minecraft:skeleton", new ConfigCreatureBone("NORMAL", 3)).addLoot(1, 2, 0.7));
		essences.add(new ConfigEssence("minecraft:spider", new ConfigCreatureBone("NORMAL", 5)));
		essences.add(new ConfigEssence("minecraft:villager", new ConfigCreatureBone("NORMAL", 1)).addLoot(1, 2, 0.6));
		essences.add(new ConfigEssence("NONE", new ConfigCreatureBone("NORMAL", 5)));

		// dry
		essences.add(new ConfigEssence("minecraft:cave_spider", new ConfigCreatureBone("DRY", 1)));
		essences.add(new ConfigEssence("minecraft:horse", new ConfigCreatureBone("DRY", 1)).addLoot(1, 3, 0.6));
		essences.add(new ConfigEssence("minecraft:husk", new ConfigCreatureBone("DRY", 5)).addLoot(1, 2, 0.3));
		essences.add(new ConfigEssence("minecraft:llama", new ConfigCreatureBone("DRY", 10)).addLoot(1, 3, 0.5));
		essences.add(new ConfigEssence("NONE", new ConfigCreatureBone("DRY", 30)));

		// fungal
		essences.add(new ConfigEssence("minecraft:mooshroom", new ConfigCreatureBone("FUNGAL", 1)).addLoot(1, 3, 0.6));
		essences.add(new ConfigEssence("minecraft:ocelot", new ConfigCreatureBone("FUNGAL", 10)).addLoot(1, 1, 0.3));
		essences.add(new ConfigEssence("minecraft:parrot", new ConfigCreatureBone("FUNGAL", 10)).addLoot(1, 1, 0.2));
		essences.add(new ConfigEssence("minecraft:vindication_illager", new ConfigCreatureBone("FUNGAL", 5)).addSpawnChance("minecraft:vindication_illager", 10).addSpawnChance("minecraft:evocation_illager", 1).addLoot("minecraft:vindication_illager", 1, 2, 0.6).addLoot("minecraft:evocation_illager", 1, 2, 0.6));
		essences.add(new ConfigEssence("minecraft:witch", new ConfigCreatureBone("FUNGAL", 5)).addLoot(1, 2, 0.6));
		essences.add(new ConfigEssence("minecraft:zombie", new ConfigCreatureBone("FUNGAL", 20)).addSpawnChance("minecraft:zombie", 100).addSpawnChance("minecraft:zombie_villager", 10).addSpawnChance("minecraft:zombie_horse", 1).addLoot("minecraft:zombie", 1, 2, 0.3).addLoot("minecraft:zombie_villager", 1, 2, 0.5).addLoot("minecraft:zombie_horse", 1, 3, 0.6));
		essences.add(new ConfigEssence("NONE", new ConfigCreatureBone("FUNGAL", 20)));

		// frozen
		essences.add(new ConfigEssence("minecraft:snowman", new ConfigCreatureBone("FROZEN", 1)).setColor(0xFFFFFF, 0xE3901D));
		essences.add(new ConfigEssence("minecraft:stray", new ConfigCreatureBone("FROZEN", 1)).addLoot(1, 2, 0.5));
		essences.add(new ConfigEssence("minecraft:wolf", new ConfigCreatureBone("FROZEN", 5)).addLoot(1, 1, 0.2));
		essences.add(new ConfigEssence("minecraft:polar_bear", new ConfigCreatureBone("FROZEN", 10)).addLoot(1, 3, 0.6));
		essences.add(new ConfigEssence("NONE", new ConfigCreatureBone("FROZEN", 10)));

		// scale
		essences.add(new ConfigEssence("minecraft:silverfish", new ConfigCreatureBone("SCALE", 1)).addLoot(1, 1, 0.2));
		essences.add(new ConfigEssence("minecraft:squid", new ConfigCreatureBone("SCALE", 20)).addLoot(1, 1, 0.2));
		essences.add(new ConfigEssence("minecraft:guardian", new ConfigCreatureBone("SCALE", 1)).addLoot(1, 3, 0.8));
		essences.add(new ConfigEssence("NONE", new ConfigCreatureBone("SCALE", 10)));

		// nether
		essences.add(new ConfigEssence("minecraft:zombie_pigman", new ConfigCreatureBone("NETHER", 20)).addLoot(1, 2, 0.5));
		essences.add(new ConfigEssence("minecraft:blaze", new ConfigCreatureBone("NETHER", 3)).addLoot(1, 2, 0.6));
		essences.add(new ConfigEssence("minecraft:wither_skeleton", new ConfigCreatureBone("NETHER", 1)).addLoot(1, 2, 0.7));
		essences.add(new ConfigEssence("minecraft:ghast", new ConfigCreatureBone("NETHER", 1)).addLoot(3, 6, 1));
		essences.add(new ConfigEssence("NONE", new ConfigCreatureBone("NETHER", 10)));

		// ender
		essences.add(new ConfigEssence("minecraft:shulker", new ConfigCreatureBone("ENDER", 1)));
		essences.add(new ConfigEssence("minecraft:endermite", new ConfigCreatureBone("ENDER", 15)).addLoot(1, 1, 0.1));
		essences.add(new ConfigEssence("minecraft:creeper", new ConfigCreatureBone("ENDER", 10)).addLoot(1, 2, 0.4));
		essences.add(new ConfigEssence("minecraft:enderman", new ConfigCreatureBone("ENDER", 2)).addLoot(1, 4, 0.8));
		essences.add(new ConfigEssence("NONE", new ConfigCreatureBone("ENDER", 20)));
		
		// no bone type
		essences.add(new ConfigEssence("minecraft:slime", null));
		essences.add(new ConfigEssence("minecraft:villager_golem", null).setColor(0xD1CBC8, 0x6A914B));
		essences.add(new ConfigEssence("minecraft:magma_cube", null));
		
		// @formatter:on
	}

	public ConfigEssence get (String type) {
		return essences.stream().filter(config -> config.essence.equalsIgnoreCase(type)).findFirst().orElse(null);
	}

	public int getSoulbookQuantity (String essenceType) {
		ConfigEssence config = get(essenceType);
		return config != null ? config.soulbookQuantity : essenceType == "unfocused" ? 1 : -1;
	}

	public static class EssencesSerializer extends CollectionSerializer<ConfigEssence> {

		@Override
		public Class<ConfigEssence> getValueClass () {
			return ConfigEssence.class;
		}
	}

}
