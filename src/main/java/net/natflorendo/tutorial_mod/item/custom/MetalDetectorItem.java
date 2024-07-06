package net.natflorendo.tutorial_mod.item.custom;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        //only on the server
        Player player = null;
        if(!pContext.getLevel().isClientSide) {
            BlockPos positionClicked = pContext.getClickedPos();
            player = pContext.getPlayer();
            boolean foundBlock = false;

            //check if there is a valuable block in the XZ coordinate
            for(int i = 0; i <= positionClicked.getY(); i++) {
                BlockState state = pContext.getLevel().getBlockState(pContext.getClickedPos());
                if(isValueableBlock(state)) {
                    outputValuableCoordinates(positionClicked.below(i), player, state.getBlock());
                    foundBlock = true;
                    break;
                }
            }

            //if no valuable block was found
            if(!foundBlock) {
                player.sendSystemMessage(Component.literal("No valuables found!"));
            }
        }

        //damage item for using it
        pContext.getItemInHand().hurtAndBreak(1, Objects.requireNonNull(player),
                LivingEntity.getSlotForHand(pContext.getHand()));

        return InteractionResult.SUCCESS;
    }

    private boolean isValueableBlock(BlockState state) {
        return state.is(Blocks.IRON_ORE) || state.is(Blocks.DIAMOND_ORE);
    }

    private void outputValuableCoordinates(BlockPos blockPos, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Found " + I18n.get(block.getDescriptionId()) + " at " +
                "(" + blockPos.getX() + ", " + blockPos.getY() + "," + blockPos.getZ() + ")"));
    }
}
