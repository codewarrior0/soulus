package yuudaari.soulus.common.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import yuudaari.soulus.Soulus;
import yuudaari.soulus.common.registration.ItemRegistry;
import yuudaari.soulus.common.config.ConfigInjected;
import yuudaari.soulus.common.config.ConfigInjected.Inject;
import yuudaari.soulus.common.config.bones.ConfigBoneTypes;
import yuudaari.soulus.common.config.essence.ConfigEssence;
import yuudaari.soulus.common.item.Essence;
import yuudaari.soulus.common.misc.BoneChunks;
import yuudaari.soulus.common.util.EssenceType;
import yuudaari.soulus.common.util.Translation;
import java.text.DecimalFormat;
import java.util.List;

@ConfigInjected(Soulus.MODID)
public class RecipeCategoryEssence implements IRecipeCategory<IRecipeWrapper> {

	@Inject public static ConfigBoneTypes CONFIG;

	public static final String UID = "soulus:essence";

	private static final int craftInputSlot = 0;
	private static final int craftOutputSlot1 = 1;

	public static final int width = 162;
	public static final int height = 105;

	private final IDrawable background;
	private final ICraftingGridHelper craftingGridHelper;

	public RecipeCategoryEssence (IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation(Soulus.MODID, "textures/gui/jei.png");
		background = guiHelper.createDrawable(location, 0, 0, width, height);
		craftingGridHelper = guiHelper.createCraftingGridHelper(craftOutputSlot1, craftInputSlot);
	}

	@Override
	public String getUid () {
		return UID;
	}

	@Override
	public String getTitle () {
		return Translation.localize("jei.recipe." + getUid() + ".name");
	}

	@Override
	public String getModName () {
		return Soulus.MODID;
	}

	@Override
	public IDrawable getBackground () {
		return background;
	}

	@Override
	public void setRecipe (IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(craftInputSlot, false, 72, 4);

		for (int y = 0; y < 4; ++y) {
			for (int x = 0; x < 9; ++x) {
				int index = craftOutputSlot1 + x + (y * 9);
				guiItemStacks.init(index, true, x * 18, 33 + y * 18);
			}
		}

		if (recipeWrapper instanceof ICustomCraftingRecipeWrapper) {
			ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) recipeWrapper;
			customWrapper.setRecipe(recipeLayout, ingredients);
			return;
		}

		List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
		List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);

		guiItemStacks.set(craftInputSlot, inputs.get(0));
		craftingGridHelper.setInputs(guiItemStacks, outputs, 9, 4);

		guiItemStacks.addTooltipCallback( (slotIndex, input, ingredient, tooltip) -> {
			if (ingredient.getItem() == ItemRegistry.ESSENCE) {
				ConfigEssence essence = Essence.CONFIG.get(EssenceType.getEssenceType(ingredient));

				if (essence.bones == null) return;

				double dropWeight = essence.bones.dropWeight;

				double dropChance = dropWeight / BoneChunks.getChanceTotal(essence.bones.type) * 100;
				tooltip.add(0, Translation.localize("jei.recipe." + Soulus.MODID + ":essence.tooltip_chance", //
					new DecimalFormat("#.##").format(dropChance)));
			}
		});
	}

}
