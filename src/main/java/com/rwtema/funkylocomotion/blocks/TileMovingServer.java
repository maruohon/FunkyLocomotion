package com.rwtema.funkylocomotion.blocks;

import java.lang.ref.WeakReference;
import javax.annotation.Nonnull;
import com.rwtema.funkylocomotion.movers.MoverEventHandler;
import com.rwtema.funkylocomotion.movers.MovingTileRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;

public class TileMovingServer extends TileMovingBase {

	public WeakReference<EntityPlayer> activatingPlayer = null;
	public EnumFacing activatingSide = null;
	public EnumHand activatingHand = null;
	public float activatingHitX, activatingHitY, activatingHitZ;


	public TileMovingServer() {
		super(Side.SERVER);
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		MovingTileRegistry.deregister(this);
	}

	@Override
	public void invalidate() {
		super.invalidate();
		MovingTileRegistry.deregister(this);
	}

	@Override
	public void validate() {
		super.validate();
		MovingTileRegistry.register(this);
	}

	@Override
	public void update() {
		if (time < maxTime) {
			super.update();
			this.worldObj.markChunkDirty(pos, this);
		} else {
			MoverEventHandler.registerFinisher();
//			MoveManager.finishMoving();
		}
	}

	public void cacheActivate(EntityPlayer player, EnumFacing side, EnumHand hand, float hitX, float hitY, float hitZ) {
		if (this.activatingPlayer == null || this.activatingPlayer.get() == null) {
			activatingPlayer = new WeakReference<>(player);
			activatingSide = side;
			activatingHand = hand;
			activatingHitX = hitX;
			activatingHitY = hitY;
			activatingHitZ = hitZ;
		}
	}
}
