package helpertools.Common.Tools;

import helpertools.Common.Entity.Renders.Models_Crossbow;
import helpertools.Utils.HelpTab;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTorchLauncher extends ToolBase_Crossbow{

   public ItemTorchLauncher(ToolMaterial material, String unlocalizedName) {
       super(material);
       this.maxStackSize = 1;  
       setUnlocalizedName(unlocalizedName);
       setCreativeTab(HelpTab.HelperTools);
       this.addPropertyOverride(new ResourceLocation("mode"), new IItemPropertyGetter()
       {
           @SideOnly(Side.CLIENT)
           public float apply(ItemStack stack, World worldIn, EntityLivingBase entityIn)
           {	if(entityIn == null){
        	   		return 0F;
           	   	}
           		float mode = 0F;
           		if(entityIn instanceof EntityPlayer && entityIn.getHeldItemMainhand() == stack){
        	   		if(getTload(stack)== 0){
        	   			return 0F;
        	   		}
        	   		switch(getMode(stack)){
        	   		case 0:	mode = 0.1F;				 
       					break;		
        	   		case 1: mode = 0.2F;
       					break;
        	   		case 2: mode = 0.3F;
       					break;
        	   		case 3: mode = 0.4F;
       					break;
        	   		default: mode = 0F;
       					break;
        	   		}
           		}
       		 return mode;
               //return entityIn == null ? 0.0F : (entityIn.getHeldItemMainhand() == stack && entityIn instanceof EntityPlayer && ((EntityPlayer)entityIn).fishEntity != null ? 1.0F : 0.0F);
           }
       });
       
   }
   @Override
   public String getUnlocalizedName(ItemStack stack) {
	   String name= "Torch";
	   if (stack.hasTagCompound()) {
			name =  whatModeString(stack);}
	   
	   return super.getUnlocalizedName() + "_" + (name);
	   
      // return super.getUnlocalizedName() + "." + (stack.getItemDamage() == 0 ? "white" : "black");
   }
   
   /**https://github.com/TheGreyGhost/MinecraftByExample/tree/master/src/main/java/minecraftbyexample/mbe12_item_nbt_animate **/
   /**
   @Override
   @SideOnly(Side.CLIENT)
   public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int ticksRemaining) {
	   
	 if (!stack.hasTagCompound()) {return Models_Crossbow.getInstance().getModel(0);} 
	 int loaded = getTload(stack);
	 if (loaded == 0){
		 return Models_Crossbow.getInstance().getModel(0);
	 }
	 else{
     int mode = getMode(stack)+1;     
     return Models_Crossbow.getInstance().getModel(mode);
	 }
   }
     **/ 
   
   /**
   @Override
   public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
       subItems.add(new ItemStack(itemIn, 1, 0));
       subItems.add(new ItemStack(itemIn, 1, 1));
   }
   **/
   @Override
   public void addInformation(ItemStack stack, EntityPlayer entity, List list, boolean par4)
   {
	   list.add(TextFormatting.ITALIC + "fires torches ");
	   list.add(TextFormatting.ITALIC + "& more");
	   if (stack.hasTagCompound()){
		    if(whatModeString(stack) != "null"){
		    	list.add(whatModeString(stack)+ " mode");
		    	//list.add(getTload(stack)+ "");
		    	}}
   }
   
     
	 public void onCreated(ItemStack p_77622_1_, World p_77622_2_, EntityPlayer p_77622_3_) 
	 {
		 //Add unique name for bow
		 // Player's Torch Launcher
	 }
	
	//Tinker's Construct Stone Torch Support
	
	String TinkTorch = "TConstruct:decoration.stonetorch";
	//String TinkTorch = "HelperToolsID:SugarBlock";
	String Tcon = "TConstruct";
	//String Tcon = "HelperToolsID";
	
	public boolean StoneTorchSearch(EntityLivingBase entityLiving) {
		
		if(Loader.isModLoaded(Tcon))
		 {			
			 return ((EntityPlayer) entityLiving).inventory.hasItem(Item.getItemFromBlock(Block.getBlockFromName(TinkTorch)));
			 
			}
		 else
			return false;
			};
			
	public boolean StoneTorchConsume(EntityLivingBase entityLiving) {
		
		if(Loader.isModLoaded(Tcon))
		 {			
			 return ((EntityPlayer)entityLiving).inventory.consumeInventoryItem(Item.getItemFromBlock(Block.getBlockFromName(TinkTorch)));
					 
			}
		 else
			return false;
			};
			
			
   @Override
	public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
   {
	   EntityPlayer player = (EntityPlayer) entityLiving;
	   World world = entityLiving.worldObj;
	   
	   if(world.isRemote){return false;}
		  
		if (entityLiving.isSneaking() )
		{ 	
			if(Transfer_Mode(stack, world, player)){
				return true;
			}
			else {return false;}
		}  
		 return false;
   }
   
   @Override
   public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) 
   {	
	   if(player.worldObj.isRemote){return stack;}
	   
	   if(!player.isSneaking() && getTload(stack) ==2){
		   //Firing function
		   crossbow_FIRE(stack, world, player);       	   
   		}
	   	if(player.isSneaking() && getTload(stack)== 0){
	   		//Loading function
	   		crossbow_LOAD(stack, world, player);
	   	}	  
	   	return stack;
   }
}
