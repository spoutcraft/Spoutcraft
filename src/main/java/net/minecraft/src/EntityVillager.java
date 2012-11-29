package net.minecraft.src;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.spoutcraft.client.entity.CraftVillager; // Spout

public class EntityVillager extends EntityAgeable implements INpc, IMerchant {
	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;

	/** This villager's current customer. */
	private EntityPlayer buyingPlayer;

	/** Initialises the MerchantRecipeList.java */
	private MerchantRecipeList buyingList;
	private int timeUntilReset;

	/** addDefaultEquipmentAndRecipies is called if this is true */
	private boolean needsInitilization;
	private int wealth;

	/** Last player to trade with this villager, used for aggressivity. */
	private String lastBuyingPlayer;
	private boolean field_82190_bM;
	private float field_82191_bN;

	/**
	 * a villagers recipe list is intialized off this list ; the 2 params are min/max amount they will trade for 1 emerald
	 */
	private static final Map villagerStockList = new HashMap();

	/**
	 * Selling list of Blacksmith items. negative numbers mean 1 emerald for n items, positive numbers are n emeralds for 1
	 * item
	 */
	private static final Map blacksmithSellingList = new HashMap();

	public EntityVillager(World par1World) {
		this(par1World, 0);
	}

	public EntityVillager(World par1World, int par2) {
		super(par1World);
		this.randomTickDivider = 0;
		this.isMating = false;
		this.isPlaying = false;
		this.villageObj = null;
		this.setProfession(par2);
		this.texture = "/mob/villager/villager.png";
		this.moveSpeed = 0.5F;
		this.getNavigator().setBreakDoors(true);
		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityZombie.class, 8.0F, 0.3F, 0.35F));
		this.tasks.addTask(1, new EntityAITradePlayer(this));
		this.tasks.addTask(1, new EntityAILookAtTradePlayer(this));
		this.tasks.addTask(2, new EntityAIMoveIndoors(this));
		this.tasks.addTask(3, new EntityAIRestrictOpenDoor(this));
		this.tasks.addTask(4, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(5, new EntityAIMoveTwardsRestriction(this, 0.3F));
		this.tasks.addTask(6, new EntityAIVillagerMate(this));
		this.tasks.addTask(7, new EntityAIFollowGolem(this));
		this.tasks.addTask(8, new EntityAIPlay(this, 0.32F));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
		this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityVillager.class, 5.0F, 0.02F));
		this.tasks.addTask(9, new EntityAIWander(this, 0.3F));
		this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
		// Spout Start
		this.spoutEntity = new CraftVillager(this);
		// Spout End
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	public boolean isAIEnabled() {
		return true;
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	protected void updateAITick() {
		if (--this.randomTickDivider <= 0) {
			this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
			this.randomTickDivider = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);

			if (this.villageObj == null) {
				this.detachHome();
			} else {
				ChunkCoordinates var1 = this.villageObj.getCenter();
				this.setHomeArea(var1.posX, var1.posY, var1.posZ, (int)((float)this.villageObj.getVillageRadius() * 0.6F));

				if (this.field_82190_bM) {
					this.field_82190_bM = false;
					this.villageObj.func_82683_b(5);
				}
			}
		}

		if (!this.isTrading() && this.timeUntilReset > 0) {
			--this.timeUntilReset;

			if (this.timeUntilReset <= 0) {
				if (this.needsInitilization) {
					if (this.buyingList.size() > 1) {
						Iterator var3 = this.buyingList.iterator();

						while (var3.hasNext()) {
							MerchantRecipe var2 = (MerchantRecipe)var3.next();

							if (var2.func_82784_g()) {
								var2.func_82783_a(this.rand.nextInt(6) + this.rand.nextInt(6) + 2);
							}
						}
					}

					this.addDefaultEquipmentAndRecipies(1);
					this.needsInitilization = false;

					if (this.villageObj != null && this.lastBuyingPlayer != null) {
						this.worldObj.setEntityState(this, (byte)14);
						this.villageObj.setReputationForPlayer(this.lastBuyingPlayer, 1);
					}
				}

				this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
			}
		}

		super.updateAITick();
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	public boolean interact(EntityPlayer par1EntityPlayer) {
		ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
		boolean var3 = var2 != null && var2.itemID == Item.monsterPlacer.shiftedIndex;

		if (!var3 && this.isEntityAlive() && !this.isTrading() && !this.isChild()) {
			if (!this.worldObj.isRemote) {
				this.setCustomer(par1EntityPlayer);
				par1EntityPlayer.displayGUIMerchant(this);
			}

			return true;
		} else {
			return super.interact(par1EntityPlayer);
		}
	}

	protected void entityInit() {
		super.entityInit();
		this.dataWatcher.addObject(16, Integer.valueOf(0));
	}

	public int getMaxHealth() {
		return 20;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Profession", this.getProfession());
		par1NBTTagCompound.setInteger("Riches", this.wealth);

		if (this.buyingList != null) {
			par1NBTTagCompound.setCompoundTag("Offers", this.buyingList.getRecipiesAsTags());
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setProfession(par1NBTTagCompound.getInteger("Profession"));
		this.wealth = par1NBTTagCompound.getInteger("Riches");

		if (par1NBTTagCompound.hasKey("Offers")) {
			NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Offers");
			this.buyingList = new MerchantRecipeList(var2);
		}
	}

	/**
	 * Returns the texture's file path as a String.
	 */
	public String getTexture() {
		switch (this.getProfession()) {
			case 0:
				return "/mob/villager/farmer.png";

			case 1:
				return "/mob/villager/librarian.png";

			case 2:
				return "/mob/villager/priest.png";

			case 3:
				return "/mob/villager/smith.png";

			case 4:
				return "/mob/villager/butcher.png";

			default:
				return super.getTexture();
		}
	}

	/**
	 * Determines if an entity can be despawned, used on idle far away entities
	 */
	protected boolean canDespawn() {
		return false;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	protected String getLivingSound() {
		return "mob.villager.default";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	protected String getHurtSound() {
		return "mob.villager.defaulthurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	protected String getDeathSound() {
		return "mob.villager.defaultdeath";
	}

	public void setProfession(int par1) {
		this.dataWatcher.updateObject(16, Integer.valueOf(par1));
	}

	public int getProfession() {
		return this.dataWatcher.getWatchableObjectInt(16);
	}

	public boolean isMating() {
		return this.isMating;
	}

	public void setMating(boolean par1) {
		this.isMating = par1;
	}

	public void setPlaying(boolean par1) {
		this.isPlaying = par1;
	}

	public boolean isPlaying() {
		return this.isPlaying;
	}

	public void setRevengeTarget(EntityLiving par1EntityLiving) {
		super.setRevengeTarget(par1EntityLiving);

		if (this.villageObj != null && par1EntityLiving != null) {
			this.villageObj.addOrRenewAgressor(par1EntityLiving);

			if (par1EntityLiving instanceof EntityPlayer) {
				byte var2 = -1;

				if (this.isChild()) {
					var2 = -3;
				}

				this.villageObj.setReputationForPlayer(((EntityPlayer)par1EntityLiving).getCommandSenderName(), var2);

				if (this.isEntityAlive()) {
					this.worldObj.setEntityState(this, (byte)13);
				}
			}
		}
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	public void onDeath(DamageSource par1DamageSource) {
		if (this.villageObj != null) {
			Entity var2 = par1DamageSource.getEntity();

			if (var2 != null) {
				if (var2 instanceof EntityPlayer) {
					this.villageObj.setReputationForPlayer(((EntityPlayer)var2).getCommandSenderName(), -2);
				} else if (var2 instanceof IMob) {
					this.villageObj.func_82692_h();
				}
			} else if (var2 == null) {
				EntityPlayer var3 = this.worldObj.getClosestPlayerToEntity(this, 16.0D);

				if (var3 != null) {
					this.villageObj.func_82692_h();
				}
			}
		}

		super.onDeath(par1DamageSource);
	}

	public void setCustomer(EntityPlayer par1EntityPlayer) {
		this.buyingPlayer = par1EntityPlayer;
	}

	public EntityPlayer getCustomer() {
		return this.buyingPlayer;
	}

	public boolean isTrading() {
		return this.buyingPlayer != null;
	}

	public void useRecipe(MerchantRecipe par1MerchantRecipe) {
		par1MerchantRecipe.incrementToolUses();

		if (par1MerchantRecipe.hasSameIDsAs((MerchantRecipe)this.buyingList.get(this.buyingList.size() - 1))) {
			this.timeUntilReset = 40;
			this.needsInitilization = true;

			if (this.buyingPlayer != null) {
				this.lastBuyingPlayer = this.buyingPlayer.getCommandSenderName();
			} else {
				this.lastBuyingPlayer = null;
			}
		}

		if (par1MerchantRecipe.getItemToBuy().itemID == Item.emerald.shiftedIndex) {
			this.wealth += par1MerchantRecipe.getItemToBuy().stackSize;
		}
	}

	public MerchantRecipeList getRecipes(EntityPlayer par1EntityPlayer) {
		if (this.buyingList == null) {
			this.addDefaultEquipmentAndRecipies(1);
		}

		return this.buyingList;
	}

	private float func_82188_j(float par1) {
		float var2 = par1 + this.field_82191_bN;
		return var2 > 0.9F ? 0.9F - (var2 - 0.9F) : var2;
	}

	/**
	 * based on the villagers profession add items, equipment, and recipies adds par1 random items to the list of things
	 * that the villager wants to buy. (at most 1 of each wanted type is added)
	 */
	private void addDefaultEquipmentAndRecipies(int par1) {
		if (this.buyingList != null) {
			this.field_82191_bN = MathHelper.sqrt_float((float)this.buyingList.size()) * 0.2F;
		} else {
			this.field_82191_bN = 0.0F;
		}

		MerchantRecipeList var2;
		var2 = new MerchantRecipeList();
		label48:

		switch (this.getProfession()) {
			case 0:
				addMerchantItem(var2, Item.wheat.shiftedIndex, this.rand, this.func_82188_j(0.9F));
				addMerchantItem(var2, Block.cloth.blockID, this.rand, this.func_82188_j(0.5F));
				addMerchantItem(var2, Item.chickenRaw.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addMerchantItem(var2, Item.fishCooked.shiftedIndex, this.rand, this.func_82188_j(0.4F));
				addBlacksmithItem(var2, Item.bread.shiftedIndex, this.rand, this.func_82188_j(0.9F));
				addBlacksmithItem(var2, Item.melon.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.appleRed.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.cookie.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.shears.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.flintAndSteel.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.chickenCooked.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.arrow.shiftedIndex, this.rand, this.func_82188_j(0.5F));

				if (this.rand.nextFloat() < this.func_82188_j(0.5F)) {
					var2.add(new MerchantRecipe(new ItemStack(Block.gravel, 10), new ItemStack(Item.emerald), new ItemStack(Item.flint.shiftedIndex, 4 + this.rand.nextInt(2), 0)));
				}

				break;

			case 1:
				addMerchantItem(var2, Item.paper.shiftedIndex, this.rand, this.func_82188_j(0.8F));
				addMerchantItem(var2, Item.book.shiftedIndex, this.rand, this.func_82188_j(0.8F));
				addMerchantItem(var2, Item.writtenBook.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Block.bookShelf.blockID, this.rand, this.func_82188_j(0.8F));
				addBlacksmithItem(var2, Block.glass.blockID, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.compass.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.pocketSundial.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				break;

			case 2:
				addBlacksmithItem(var2, Item.eyeOfEnder.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.expBottle.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.redstone.shiftedIndex, this.rand, this.func_82188_j(0.4F));
				addBlacksmithItem(var2, Block.glowStone.blockID, this.rand, this.func_82188_j(0.3F));
				int[] var3 = new int[] {Item.swordSteel.shiftedIndex, Item.swordDiamond.shiftedIndex, Item.plateSteel.shiftedIndex, Item.plateDiamond.shiftedIndex, Item.axeSteel.shiftedIndex, Item.axeDiamond.shiftedIndex, Item.pickaxeSteel.shiftedIndex, Item.pickaxeDiamond.shiftedIndex};
				int[] var4 = var3;
				int var5 = var3.length;
				int var6 = 0;

				while (true) {
					if (var6 >= var5) {
						break label48;
					}

					int var7 = var4[var6];

					if (this.rand.nextFloat() < this.func_82188_j(0.05F)) {
						var2.add(new MerchantRecipe(new ItemStack(var7, 1, 0), new ItemStack(Item.emerald, 2 + this.rand.nextInt(3), 0), EnchantmentHelper.addRandomEnchantment(this.rand, new ItemStack(var7, 1, 0), 5 + this.rand.nextInt(15))));
					}

					++var6;
				}

			case 3:
				addMerchantItem(var2, Item.coal.shiftedIndex, this.rand, this.func_82188_j(0.7F));
				addMerchantItem(var2, Item.ingotIron.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addMerchantItem(var2, Item.ingotGold.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addMerchantItem(var2, Item.diamond.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addBlacksmithItem(var2, Item.swordSteel.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addBlacksmithItem(var2, Item.swordDiamond.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addBlacksmithItem(var2, Item.axeSteel.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.axeDiamond.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.pickaxeSteel.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addBlacksmithItem(var2, Item.pickaxeDiamond.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addBlacksmithItem(var2, Item.shovelSteel.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.shovelDiamond.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.hoeSteel.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.hoeDiamond.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.bootsSteel.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.bootsDiamond.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.helmetSteel.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.helmetDiamond.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.plateSteel.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.plateDiamond.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.legsSteel.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.legsDiamond.shiftedIndex, this.rand, this.func_82188_j(0.2F));
				addBlacksmithItem(var2, Item.bootsChain.shiftedIndex, this.rand, this.func_82188_j(0.1F));
				addBlacksmithItem(var2, Item.helmetChain.shiftedIndex, this.rand, this.func_82188_j(0.1F));
				addBlacksmithItem(var2, Item.plateChain.shiftedIndex, this.rand, this.func_82188_j(0.1F));
				addBlacksmithItem(var2, Item.legsChain.shiftedIndex, this.rand, this.func_82188_j(0.1F));
				break;

			case 4:
				addMerchantItem(var2, Item.coal.shiftedIndex, this.rand, this.func_82188_j(0.7F));
				addMerchantItem(var2, Item.porkRaw.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addMerchantItem(var2, Item.beefRaw.shiftedIndex, this.rand, this.func_82188_j(0.5F));
				addBlacksmithItem(var2, Item.saddle.shiftedIndex, this.rand, this.func_82188_j(0.1F));
				addBlacksmithItem(var2, Item.plateLeather.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.bootsLeather.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.helmetLeather.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.legsLeather.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.porkCooked.shiftedIndex, this.rand, this.func_82188_j(0.3F));
				addBlacksmithItem(var2, Item.beefCooked.shiftedIndex, this.rand, this.func_82188_j(0.3F));
		}

		if (var2.isEmpty()) {
			addMerchantItem(var2, Item.ingotGold.shiftedIndex, this.rand, 1.0F);
		}

		Collections.shuffle(var2);

		if (this.buyingList == null) {
			this.buyingList = new MerchantRecipeList();
		}

		for (int var8 = 0; var8 < par1 && var8 < var2.size(); ++var8) {
			this.buyingList.addToListWithCheck((MerchantRecipe)var2.get(var8));
		}
	}

	public void setRecipes(MerchantRecipeList par1MerchantRecipeList) {}

	/**
	 * each recipie takes a random stack from villagerStockList and offers it for 1 emerald
	 */
	private static void addMerchantItem(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3) {
		if (par2Random.nextFloat() < par3) {
			par0MerchantRecipeList.add(new MerchantRecipe(getRandomSizedStack(par1, par2Random), Item.emerald));
		}
	}

	private static ItemStack getRandomSizedStack(int par0, Random par1Random) {
		return new ItemStack(par0, getRandomCountForItem(par0, par1Random), 0);
	}

	/**
	 * default to 1, and villagerStockList contains a min/max amount for each index
	 */
	private static int getRandomCountForItem(int par0, Random par1Random) {
		Tuple var2 = (Tuple)villagerStockList.get(Integer.valueOf(par0));
		return var2 == null ? 1 : (((Integer)var2.getFirst()).intValue() >= ((Integer)var2.getSecond()).intValue() ? ((Integer)var2.getFirst()).intValue() : ((Integer)var2.getFirst()).intValue() + par1Random.nextInt(((Integer)var2.getSecond()).intValue() - ((Integer)var2.getFirst()).intValue()));
	}

	private static void addBlacksmithItem(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3) {
		if (par2Random.nextFloat() < par3) {
			int var4 = getRandomCountForBlacksmithItem(par1, par2Random);
			ItemStack var5;
			ItemStack var6;

			if (var4 < 0) {
				var5 = new ItemStack(Item.emerald.shiftedIndex, 1, 0);
				var6 = new ItemStack(par1, -var4, 0);
			} else {
				var5 = new ItemStack(Item.emerald.shiftedIndex, var4, 0);
				var6 = new ItemStack(par1, 1, 0);
			}

			par0MerchantRecipeList.add(new MerchantRecipe(var5, var6));
		}
	}

	private static int getRandomCountForBlacksmithItem(int par0, Random par1Random) {
		Tuple var2 = (Tuple)blacksmithSellingList.get(Integer.valueOf(par0));
		return var2 == null ? 1 : (((Integer)var2.getFirst()).intValue() >= ((Integer)var2.getSecond()).intValue() ? ((Integer)var2.getFirst()).intValue() : ((Integer)var2.getFirst()).intValue() + par1Random.nextInt(((Integer)var2.getSecond()).intValue() - ((Integer)var2.getFirst()).intValue()));
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 12) {
			this.generateRandomParticles("heart");
		} else if (par1 == 13) {
			this.generateRandomParticles("angryVillager");
		} else if (par1 == 14) {
			this.generateRandomParticles("happyVillager");
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	/**
	 * par1 is the particleName
	 */
	private void generateRandomParticles(String par1Str) {
		for (int var2 = 0; var2 < 5; ++var2) {
			double var3 = this.rand.nextGaussian() * 0.02D;
			double var5 = this.rand.nextGaussian() * 0.02D;
			double var7 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(par1Str, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var3, var5, var7);
		}
	}

	/**
	 * Initialize this creature.
	 */
	public void initCreature() {
		this.setProfession(this.worldObj.rand.nextInt(5));
	}

	public void func_82187_q() {
		this.field_82190_bM = true;
	}

	public EntityVillager func_90012_b(EntityAgeable par1EntityAgeable) {
		EntityVillager var2 = new EntityVillager(this.worldObj);
		var2.initCreature();
		return var2;
	}

	public EntityAgeable func_90011_a(EntityAgeable par1EntityAgeable) {
		return this.func_90012_b(par1EntityAgeable);
	}

	static {
		villagerStockList.put(Integer.valueOf(Item.coal.shiftedIndex), new Tuple(Integer.valueOf(16), Integer.valueOf(24)));
		villagerStockList.put(Integer.valueOf(Item.ingotIron.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		villagerStockList.put(Integer.valueOf(Item.ingotGold.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		villagerStockList.put(Integer.valueOf(Item.diamond.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		villagerStockList.put(Integer.valueOf(Item.paper.shiftedIndex), new Tuple(Integer.valueOf(24), Integer.valueOf(36)));
		villagerStockList.put(Integer.valueOf(Item.book.shiftedIndex), new Tuple(Integer.valueOf(11), Integer.valueOf(13)));
		villagerStockList.put(Integer.valueOf(Item.writtenBook.shiftedIndex), new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
		villagerStockList.put(Integer.valueOf(Item.enderPearl.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		villagerStockList.put(Integer.valueOf(Item.eyeOfEnder.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(3)));
		villagerStockList.put(Integer.valueOf(Item.porkRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		villagerStockList.put(Integer.valueOf(Item.beefRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		villagerStockList.put(Integer.valueOf(Item.chickenRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		villagerStockList.put(Integer.valueOf(Item.fishCooked.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
		villagerStockList.put(Integer.valueOf(Item.seeds.shiftedIndex), new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
		villagerStockList.put(Integer.valueOf(Item.melonSeeds.shiftedIndex), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		villagerStockList.put(Integer.valueOf(Item.pumpkinSeeds.shiftedIndex), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		villagerStockList.put(Integer.valueOf(Item.wheat.shiftedIndex), new Tuple(Integer.valueOf(18), Integer.valueOf(22)));
		villagerStockList.put(Integer.valueOf(Block.cloth.blockID), new Tuple(Integer.valueOf(14), Integer.valueOf(22)));
		villagerStockList.put(Integer.valueOf(Item.rottenFlesh.shiftedIndex), new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
		blacksmithSellingList.put(Integer.valueOf(Item.flintAndSteel.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Integer.valueOf(Item.shears.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Integer.valueOf(Item.swordSteel.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
		blacksmithSellingList.put(Integer.valueOf(Item.swordDiamond.shiftedIndex), new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
		blacksmithSellingList.put(Integer.valueOf(Item.axeSteel.shiftedIndex), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
		blacksmithSellingList.put(Integer.valueOf(Item.axeDiamond.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
		blacksmithSellingList.put(Integer.valueOf(Item.pickaxeSteel.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
		blacksmithSellingList.put(Integer.valueOf(Item.pickaxeDiamond.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		blacksmithSellingList.put(Integer.valueOf(Item.shovelSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Integer.valueOf(Item.shovelDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Integer.valueOf(Item.hoeSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Integer.valueOf(Item.hoeDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Integer.valueOf(Item.bootsSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Integer.valueOf(Item.bootsDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Integer.valueOf(Item.helmetSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		blacksmithSellingList.put(Integer.valueOf(Item.helmetDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		blacksmithSellingList.put(Integer.valueOf(Item.plateSteel.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
		blacksmithSellingList.put(Integer.valueOf(Item.plateDiamond.shiftedIndex), new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
		blacksmithSellingList.put(Integer.valueOf(Item.legsSteel.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		blacksmithSellingList.put(Integer.valueOf(Item.legsDiamond.shiftedIndex), new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
		blacksmithSellingList.put(Integer.valueOf(Item.bootsChain.shiftedIndex), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		blacksmithSellingList.put(Integer.valueOf(Item.helmetChain.shiftedIndex), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		blacksmithSellingList.put(Integer.valueOf(Item.plateChain.shiftedIndex), new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
		blacksmithSellingList.put(Integer.valueOf(Item.legsChain.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
		blacksmithSellingList.put(Integer.valueOf(Item.bread.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-2)));
		blacksmithSellingList.put(Integer.valueOf(Item.melon.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
		blacksmithSellingList.put(Integer.valueOf(Item.appleRed.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
		blacksmithSellingList.put(Integer.valueOf(Item.cookie.shiftedIndex), new Tuple(Integer.valueOf(-10), Integer.valueOf(-7)));
		blacksmithSellingList.put(Integer.valueOf(Block.glass.blockID), new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
		blacksmithSellingList.put(Integer.valueOf(Block.bookShelf.blockID), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		blacksmithSellingList.put(Integer.valueOf(Item.plateLeather.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
		blacksmithSellingList.put(Integer.valueOf(Item.bootsLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Integer.valueOf(Item.helmetLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Integer.valueOf(Item.legsLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		blacksmithSellingList.put(Integer.valueOf(Item.saddle.shiftedIndex), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
		blacksmithSellingList.put(Integer.valueOf(Item.expBottle.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
		blacksmithSellingList.put(Integer.valueOf(Item.redstone.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
		blacksmithSellingList.put(Integer.valueOf(Item.compass.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		blacksmithSellingList.put(Integer.valueOf(Item.pocketSundial.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		blacksmithSellingList.put(Integer.valueOf(Block.glowStone.blockID), new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
		blacksmithSellingList.put(Integer.valueOf(Item.porkCooked.shiftedIndex), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		blacksmithSellingList.put(Integer.valueOf(Item.beefCooked.shiftedIndex), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		blacksmithSellingList.put(Integer.valueOf(Item.chickenCooked.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
		blacksmithSellingList.put(Integer.valueOf(Item.eyeOfEnder.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
		blacksmithSellingList.put(Integer.valueOf(Item.arrow.shiftedIndex), new Tuple(Integer.valueOf(-12), Integer.valueOf(-8)));
	}
}
