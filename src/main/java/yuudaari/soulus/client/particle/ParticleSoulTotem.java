package yuudaari.soulus.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ParticleSoulTotem extends Particle {

	private final float portalParticleScale;
	private final double portalPosX;
	private final double portalPosY;
	private final double portalPosZ;

	protected ParticleSoulTotem (World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		super(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
		this.motionX = xSpeed;
		this.motionY = ySpeed;
		this.motionZ = zSpeed;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.portalPosX = this.posX;
		this.portalPosY = this.posY;
		this.portalPosZ = this.posZ;
		this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
		this.portalParticleScale = this.particleScale;
		float brightness = MathHelper.nextFloat(this.rand, 0F, 0.2F);
		this.particleRed = 1F;
		this.particleGreen = 0.68F + brightness;
		this.particleBlue = 0.0F + brightness;
		this.particleMaxAge = (int) (Math.random() * 10.0D) + 40;
		this.setParticleTextureIndex((int) (Math.random() * 8.0D));
	}

	public void move (double x, double y, double z) {
		this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
		this.resetPositionToBB();
	}

	/**
	 * Renders the particle
	 */
	public void renderParticle (BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float f = ((float) this.particleAge + partialTicks) / (float) this.particleMaxAge;
		f = 1.0F - f;
		f = f * f;
		f = 1.0F - f;
		this.particleScale = this.portalParticleScale * f;
		super.renderParticle(buffer, entityIn, partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
	}

	public int getBrightnessForRender (float p_189214_1_) {
		int i = super.getBrightnessForRender(p_189214_1_);
		float f = (float) this.particleAge / (float) this.particleMaxAge;
		f = f * f;
		f = f * f;
		int j = i & 255;
		int k = i >> 16 & 255;
		k = k + (int) (f * 15.0F * 16.0F);

		if (k > 240) {
			k = 240;
		}

		return j | k << 16;
	}

	public void onUpdate () {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		float f = (float) this.particleAge / (float) this.particleMaxAge;
		float f1 = -f + f * f * 2.0F;
		float f2 = 1.0F - f1;
		this.posX = this.portalPosX + this.motionX * (double) f2;
		this.posY = this.portalPosY + this.motionY * (double) f2;
		this.posZ = this.portalPosZ + this.motionZ * (double) f2;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {

		public Particle createParticle (int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_) {
			return new ParticleSoulTotem(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		}
	}
}
