package rena.toraracreatures.common.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import rena.toraracreatures.common.recipe.AnalyzerRecipe;
import rena.toraracreatures.common.tileentity.AnalyzerTileEntity;
import rena.toraracreatures.init.ContainerInit;
import rena.toraracreatures.init.RecipeInit;

public class AnalyzerContainer extends Container {

    private final IInventory container;
    private final IIntArray data;
    private final World level;
    private final IRecipeType<AnalyzerRecipe> recipeType = RecipeInit.ANALYZER_RECIPE;
    public final AnalyzerTileEntity tile;

    public AnalyzerContainer(int windowID, PlayerInventory playerInv, AnalyzerTileEntity tileEntity, IInventory tile)
    {
        super(ContainerInit.ANALYZER_CONTAINER.get(), windowID);
        this.container = tile;
        this.level = playerInv.player.level;
        this.data = tileEntity.getGrinderData();
        this.tile = tileEntity;

        this.addSlot(new Slot(tile, 0, 53, 35));
        this.addSlot(new FurnaceResultSlot(playerInv.player, tile, 1, 116, 35));

        for(int i = 0; i < 3; ++i)
        {
            for(int j = 0; j < 9; ++j)
            {
                this.addSlot(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k)
        {
            this.addSlot(new Slot(playerInv, k, 8 + k * 18, 142));
        }

        this.addDataSlots(this.data);
    }

    public AnalyzerContainer(int windowID, PlayerInventory playerInv, PacketBuffer data)
    {
        this(windowID, playerInv, new AnalyzerTileEntity(), new Inventory(2));
    }

    @Override
    public boolean stillValid(PlayerEntity player)
    {
        return this.container.stillValid(player);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int i)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if(slot != null && slot.hasItem())
        {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if(i == 1)
            {
                if(!this.moveItemStackTo(itemstack1, 2, 38, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if(i != 0)
            {
                if(canGrind(itemstack1))
                {
                    if(!this.moveItemStackTo(itemstack1, 0, 1, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(i >= 2 && i < 29)
                {
                    if(!this.moveItemStackTo(itemstack1, 29, 38, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if(i >= 29 && i < 38 && !this.moveItemStackTo(itemstack1, 2, 29, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.moveItemStackTo(itemstack1, 2, 38, false))
            {
                return ItemStack.EMPTY;
            }

            if(itemstack1.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else
            {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    protected boolean canGrind(ItemStack stack)
    {
        return this.level.getRecipeManager().getRecipeFor((IRecipeType)this.recipeType, new Inventory(stack), this.level).isPresent();
    }

    @OnlyIn(Dist.CLIENT)
    public int getProgress()
    {
        int grindingProgress = this.data.get(2);
        int grindingTotalTime = this.data.get(3);
        return grindingTotalTime != 0 && grindingProgress != 0 ? grindingProgress * 35 / grindingTotalTime : 0;
    }
}
