package concord.common.blocks;

import concord.Concord;
import concord.client.render.IModel;
import concord.client.render.Transform;
import concord.common.network.packets.SetWorkbenchScreenS2C;
import concord.common.recipes.RecipesManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;


import java.util.List;

public class WorkbenchBlock extends Block implements IModel {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public final RecipesManager.CraftType craftType;
    private final String model;
    private final ResourceLocation texture;
    private final int lightLevel;
    private Transform transform;

    public WorkbenchBlock(String model, ResourceLocation texture, RecipesManager.CraftType craftType, int lightLevel) {
        super(Properties.of().strength(3.5F).noOcclusion().lightLevel(state -> lightLevel));
        this.craftType = craftType;
        this.model = "workbench/" + model;
        this.texture = texture;
        this.lightLevel = lightLevel;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return List.of(new ItemStack(this));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide()) {
            if (player instanceof ServerPlayer serverPlayer) {
                Concord.network.sendTo(new SetWorkbenchScreenS2C(craftType), serverPlayer);
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            Direction facing = Direction.getNearest(
                    player.getLookAngle().x,
                    0,
                    player.getLookAngle().z
            );
            boolean flag = facing == Direction.SOUTH || facing == Direction.NORTH;
            return this.defaultBlockState().setValue(FACING, flag ? facing : facing.getOpposite());
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        // Define shapes using correct 0-16 coordinates for the block
        return switch (direction) {
            case NORTH -> Shapes.box(0, 0, 0, 2, 1, 1);  // example smaller shape
            case SOUTH -> Shapes.box(14 / 16f, 0, 0, 1, 1, 1);
            case EAST -> Shapes.box(0, 0, 14 / 16f, 1, 1, 1);
            case WEST -> Shapes.box(0, 0, 0, 1 / 8f, 1, 1);
            default -> Shapes.block();
        };
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
    public RenderShape getRenderShape(BlockState state) {
        // INVISIBLE to allow custom rendering
        return RenderShape.INVISIBLE;
    }
}
