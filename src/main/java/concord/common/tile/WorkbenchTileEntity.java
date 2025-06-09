package concord.common.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;

public class WorkbenchTileEntity extends TileEntity {

    public WorkbenchTileEntity() {
        super(TileRegistry.WORKBENCH_TILE.get());
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos()).expandTowards(-1f, -1f, -1f).expandTowards(1f, 1f, 1f);
    }
}
