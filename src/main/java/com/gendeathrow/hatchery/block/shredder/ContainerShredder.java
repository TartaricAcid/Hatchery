package com.gendeathrow.hatchery.block.shredder;

import javax.annotation.Nullable;

import com.gendeathrow.hatchery.core.init.ModFluids;
import com.gendeathrow.hatchery.inventory.SlotFluidContainer;
import com.gendeathrow.hatchery.inventory.SlotUpgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerShredder extends Container 
{

	private final int HOTBAR_SLOT_COUNT = 9;
	private final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private final int VANILLA_FIRST_SLOT_INDEX = 0;
	private final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private final int TE_INVENTORY_SLOT_COUNT = 9;
	
	private final IInventory inventory;
	
	private final IInventory upgrades;
	
	public ContainerShredder(InventoryPlayer playerInventory,	ShredderTileEntity tile, EntityPlayer player) 
	{
		inventory = tile;  
		upgrades = tile.getUpgradeStorage();
		
		int i;  

		addSlotToContainer(new Slot(inventory, 0, 65, 16)
		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack)
			{
				return tile.isShreddableItem(stack);
		    }
		});
		
		addSlotToContainer(new Slot(inventory, 1, 55, 54)
		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack)
			{
				return false;
		    }
		});
		
		addSlotToContainer(new Slot(inventory, 2, 76, 54)
		{
			@Override
			public boolean isItemValid(@Nullable ItemStack stack)
			{
				return false;
		    }
		});
		
		
        
		addSlotToContainer(new SlotUpgrade(upgrades, 0, 121, 55));
		addSlotToContainer(new SlotUpgrade(upgrades, 1, 141, 54));

	     for (i = 0; i < 3; ++i)
	            for (int j = 0; j < 9; ++j)
	                addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 7 + j * 18, 83 + i * 18));

        for (i = 0; i < 9; ++i)
            addSlotToContainer(new Slot(playerInventory, i, 7 + i * 18, 141));	    
        
        
        
	}
	
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex) 
	{
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (slotIndex < (this.inventory.getSizeInventory() + this.upgrades.getSizeInventory()))
            {
                if (!this.mergeItemStack(itemstack1, this.inventory.getSizeInventory(), this.inventorySlots.size(), true))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, this.inventory.getSizeInventory(), false))
            {
                return null;
            }
            
            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }
        }
        
		return  itemstack;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int id, int value) {
		this.inventory.setField(id, value);
	}

	@Override
    public void detectAndSendChanges() 
	{
		super.detectAndSendChanges();
		
		for (IContainerListener listener : this.listeners) 
		 {
					// Note that although sendProgressBarUpdate takes 2 ints on a server these are truncated to shorts
					listener.sendProgressBarUpdate(this, 0, this.inventory.getField(0));

		 }

    }

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)	{
		return true;
	}
}
