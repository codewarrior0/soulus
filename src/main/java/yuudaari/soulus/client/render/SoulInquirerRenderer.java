package yuudaari.soulus.client.render;

import yuudaari.soulus.client.util.TileEntityRenderer;
import yuudaari.soulus.common.block.soul_inquirer.SoulInquirerTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoulInquirerRenderer extends TileEntityRenderer<SoulInquirerTileEntity> {

	public Class<SoulInquirerTileEntity> getTileEntityClass () {
		return SoulInquirerTileEntity.class;
	}

	private void spawnRenderMob (SoulInquirerTileEntity tileEntity) {
		NBTTagCompound entityNbt = tileEntity.getEntityNbt();
		World world = tileEntity.getWorld();

		tileEntity.renderMob = (EntityLiving) AnvilChunkLoader.readWorldEntity(entityNbt, world, false);

		String mobName = entityNbt.getString("id");

		if (tileEntity.renderMob == null)
			throw new RuntimeException("Unable to summon mobName " + mobName);

		tileEntity.renderMob.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(tileEntity.renderMob)), null);
	}

	@Override
	public void render (SoulInquirerTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		if (te.getEssenceType() == null)
			return;

		if (te.renderMob == null || !te.getEssenceType().equals(te.lastRenderedEssenceType)) {
			te.lastRenderedEssenceType = te.getEssenceType();
			spawnRenderMob(te);
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		renderMob(te, x, y, z, partialTicks);
		GlStateManager.popMatrix();
	}

	public void renderMob (SoulInquirerTileEntity te, double posX, double posY, double posZ, float partialTicks) {
		EntityLiving renderMob = te.renderMob;
		double mobRotation = te.mobRotation;
		double prevMobRotation = te.prevMobRotation;

		float f = 0.4F;
		float f1 = Math.max(renderMob.width, renderMob.height);

		if (f1 > 1.0D) {
			f /= f1;
		}

		GlStateManager.translate(0.0F, 0.7F, 0.0F);
		float rotate = (float) (prevMobRotation + (mobRotation - prevMobRotation) * partialTicks);
		GlStateManager.rotate(rotate * 10.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(rotate * 4.0F, 0.0F, 0.0F, 1.0F);
		GlStateManager.rotate(rotate * 0.5F, 1.0F, 0.0F, 0.0F);
		GlStateManager.translate(0.0F, -0.2F, 0.0F);
		GlStateManager.scale(f, f, f);
		renderMob.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
		Minecraft.getMinecraft()
			.getRenderManager()
			.renderEntity(renderMob, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
	}
}
