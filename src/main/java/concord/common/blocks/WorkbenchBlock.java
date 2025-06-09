package concord.common.blocks;

import concord.Concord;
import concord.client.render.IModel;
import concord.client.render.Transform;
import concord.common.network.packets.SetWorkbenchScreenS2C;
import concord.common.recipes.RecipesManager;
import concord.common.tile.WorkbenchTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class WorkbenchBlock extends Block implements IModel {

    public static final DirectionProperty FACING = HorizontalBlock.FACING;
    public final RecipesManager.CraftType craftType;
    private final String model;
    private final ResourceLocation texture;
    private final int lightLevel;
    private Transform transform;

    public WorkbenchBlock(String model, ResourceLocation texture, RecipesManager.CraftType craftType, int lightLevel) {
        super(Properties.of(Material.STONE).strength(3.5F).noOcclusion());
        this.craftType = craftType;
        this.model = "workbench/" + model;
        this.texture = texture;
        this.lightLevel = lightLevel;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public List<ItemStack> getDrops(BlockState p_220076_1_, LootContext.Builder p_220076_2_) {
        return new ArrayList<ItemStack>() {{
            add(new ItemStack(WorkbenchBlock.this));
        }};
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return lightLevel;
    }

    @SuppressWarnings({"deprecation", "NullableProblems"})
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult ray) {
        if (!world.isClientSide()) {
            ServerPlayerEntity sv = (ServerPlayerEntity) player;
            Concord.network.sendTo(new SetWorkbenchScreenS2C(craftType), sv);
        }
        return super.use(state, world, pos, player, hand, ray);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        if (player != null) {
            Vector3d lookVec = player.getLookAngle();

            Direction facing = Direction.getNearest(lookVec.x, 0, lookVec.z);

            boolean flag = facing == Direction.SOUTH || facing == Direction.NORTH;
            return this.defaultBlockState().setValue(FACING, flag ? facing : facing.getOpposite());
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        Direction direction = state.getValue(FACING);
        switch (direction) {
            case NORTH:
                return VoxelShapes.or(Block.box(0, 0, 0, 32, 16, 16));
            case SOUTH:
                return VoxelShapes.or(Block.box(16, 0, 0, -16, 16, 16));
            case EAST:
                return VoxelShapes.or(Block.box(0, 0, 16, 16, 16, -16));
            case WEST:
                return VoxelShapes.or(Block.box(0, 0, 0, 16, 16, 32));
            default:
                return VoxelShapes.or(Block.box(0, 0, 0, 16, 16, 16));
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WorkbenchTileEntity();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public String getModel() {
        return model;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    @Override
    public Transform getTransform() {
        return transform;
    }

    public WorkbenchBlock setTransform(Transform transform) {
        this.transform = transform;
        return this;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.INVISIBLE;
    }

}
