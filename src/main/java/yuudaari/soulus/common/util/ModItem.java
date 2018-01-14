package yuudaari.soulus.common.util;

import yuudaari.soulus.Soulus;
import yuudaari.soulus.common.CreativeTab;
import yuudaari.soulus.common.compat.JeiDescriptionRegistry;
import yuudaari.soulus.common.config.Config;
import yuudaari.soulus.common.config.item.ConfigFood;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItem extends Item implements IModThing {

	public interface ConsumeHandler {

		void consume (ItemStack item, World world, EntityLivingBase entity);
	}

	public interface CanConsumeHandler {

		boolean canConsume (World worldIn, EntityPlayer playerIn, EnumHand handIn);
	}

	protected Boolean glint = false;
	private String name;
	private List<String> oreDicts = new ArrayList<>();
	private Class<? extends ConfigFood> foodConfig = null;
	public ConsumeHandler foodHandler;
	public CanConsumeHandler foodCanEatHandler;

	public ModItem (String name) {
		setName(name);
	}

	public ModItem (String name, Integer maxStackSize) {
		this(name);
		setMaxStackSize(maxStackSize);
	}

	@SuppressWarnings("unchecked")
	protected <T extends ConfigFood> T getFoodConfig () {
		return (T) Config.INSTANCES.get(Soulus.MODID).get(foodConfig);
	}

	@Override
	public CreativeTabs getCreativeTab () {
		return CreativeTab.INSTANCE;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
		setRegistryName(Soulus.MODID, name);
		setUnlocalizedName(getRegistryName().toString());
	}

	public ModItem addOreDict (String... name) {
		for (String dict : name)
			oreDicts.add(dict);

		return this;
	}

	public ModItem removeOreDict (String... name) {
		for (String dict : name)
			oreDicts.remove(dict);

		return this;
	}

	public List<String> getOreDicts () {
		return oreDicts;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean hasEffect (ItemStack stack) {
		return glint;
	}

	private int burnTime = 0;

	@Override
	public int getItemBurnTime (ItemStack itemStack) {
		return burnTime;
	}

	public ModItem setBurnTime (int burnTime) {
		this.burnTime = burnTime;
		return this;
	}

	@SideOnly(Side.CLIENT)
	public void registerColorHandler (IItemColor itemColor) {
		Soulus.onInit( (FMLInitializationEvent event) -> {
			Minecraft.getMinecraft().getItemColors().registerItemColorHandler(itemColor, this);
		});
	}

	public boolean isFood () {
		return foodConfig != null;
	}

	public void setFood (final Class<? extends ConfigFood> config) {
		foodConfig = config;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick (World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack itemstack = playerIn.getHeldItem(handIn);
		if (isFood()) {
			if ((foodCanEatHandler != null && foodCanEatHandler.canConsume(worldIn, playerIn, handIn)) || (playerIn
				.canEat(getFoodConfig().foodAlwaysEdible) && itemstack.getCount() >= getFoodConfig().foodQuantity)) {
				playerIn.setActiveHand(handIn);
				return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
			}
		}
		return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	}

	@Override
	public EnumAction getItemUseAction (ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	public int getMaxItemUseDuration (ItemStack stack) {
		return getFoodConfig().foodDuration;
	}

	@Override
	public ItemStack onItemUseFinish (ItemStack stack, World world, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			entityplayer.getFoodStats().addStats(getFoodConfig().foodAmount, getFoodConfig().foodSaturation);
			world
				.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.rand
					.nextFloat() * 0.1F + 0.9F);

			if (getFoodConfig().foodEffects != null) {
				for (PotionEffect effect : getFoodConfig().foodEffects) {
					entityLiving.addPotionEffect(new PotionEffect(effect));
				}
			}

			if (foodHandler != null)
				foodHandler.consume(stack, world, entityLiving);

			entityplayer.addStat(StatList.getObjectUseStats(this));

			if (entityplayer instanceof EntityPlayerMP) {
				CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP) entityplayer, stack);
			}
		}

		stack.shrink(getFoodConfig().foodQuantity);
		return stack;
	}

	public boolean hasDescription = false;

	public ModItem setHasDescription () {
		hasDescription = true;
		return this;
	}

	@Override
	public void onRegisterDescription (JeiDescriptionRegistry registry) {
		if (hasDescription)
			registry.add(this);
	}
}
