package concord.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class WorkbenchTileEntity extends BlockEntity {

    public WorkbenchTileEntity(BlockPos pos, BlockState state) {
        super(TileRegistry.WORKBENCH_TILE.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        // Expands bounding box 1 block in all directions around this block
        return new AABB(worldPosition).inflate(1.0D);
    }
}
