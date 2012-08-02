package net.minecraft.src;

import org.spoutcraft.client.entity.CraftVillager; //Spout

public class EntityVillager extends EntityAgeable implements INpc, IMerchant {
	private int randomTickDivider;
	private boolean isMating;
	private boolean isPlaying;
	Village villageObj;
	private EntityPlayer field_70962_h;
	private MerchantRecipeList field_70963_i;
	private int field_70961_j;
	private boolean field_70959_by;
	private int field_70956_bz;
	private MerchantRecipe field_70957_bA;
	private static final Map field_70958_bB = new HashMap();
	private static final Map field_70960_bC = new HashMap();

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
		//Spout start
		this.spoutEntity = new CraftVillager(this);
		//Spout end
	}

	public boolean isAIEnabled() {
		return true;
	}

	protected void updateAITick() {
		if (--this.randomTickDivider <= 0) {
			this.worldObj.villageCollectionObj.addVillagerPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
			this.randomTickDivider = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);
			if (this.villageObj == null) {
				this.detachHome();
			} else {
				ChunkCoordinates var1 = this.villageObj.getCenter();
				this.setHomeArea(var1.posX, var1.posY, var1.posZ, this.villageObj.getVillageRadius());
			}
		}

		if (!this.func_70940_q() && this.field_70961_j > 0) {
			--this.field_70961_j;

			if (this.field_70961_j <= 0) {
				if (this.field_70959_by) {
					this.func_70950_c(1);
					this.field_70959_by = false;
				}

				if (this.field_70957_bA != null) {
					this.field_70963_i.remove(this.field_70957_bA);
					this.field_70957_bA = null;
				}

				this.addPotionEffect(new PotionEffect(Potion.regeneration.id, 200, 0));
			}
		}

		super.updateAITick();
	}

	public boolean interact(EntityPlayer par1EntityPlayer) {
		if (this.isEntityAlive() && !this.func_70940_q() && !this.isChild()) {
			if (!this.worldObj.isRemote) {
				this.func_70932_a_(par1EntityPlayer);
				par1EntityPlayer.func_71030_a(this);
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

	public void onLivingUpdate() {
		super.onLivingUpdate();
	}

	public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeEntityToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("Profession", this.getProfession());
		par1NBTTagCompound.setInteger("Riches", this.field_70956_bz);

		if (this.field_70963_i != null) {
			par1NBTTagCompound.setCompoundTag("Offers", this.field_70963_i.func_77202_a());
		}
	}

	public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
		super.readEntityFromNBT(par1NBTTagCompound);
		this.setProfession(par1NBTTagCompound.getInteger("Profession"));
		this.field_70956_bz = par1NBTTagCompound.getInteger("Riches");

		if (par1NBTTagCompound.hasKey("Offers")) {
			NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Offers");
			this.field_70963_i = new MerchantRecipeList(var2);
		}
	}

	public String getTexture() {
		switch(this.getProfession()) {
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

	protected boolean canDespawn() {
		return false;
	}

	protected String getLivingSound() {
		return "mob.villager.default";
	}

	protected String getHurtSound() {
		return "mob.villager.defaulthurt";
	}

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
		}
	}

	public void func_70932_a_(EntityPlayer par1EntityPlayer) {
		this.field_70962_h = par1EntityPlayer;
	}

	public EntityPlayer func_70931_l_() {
		return this.field_70962_h;
	}

	public boolean func_70940_q() {
		return this.field_70962_h != null;
	}

	public void func_70933_a(MerchantRecipe par1MerchantRecipe) {
		par1MerchantRecipe.func_77399_f();

		if (par1MerchantRecipe.func_77393_a((MerchantRecipe)this.field_70963_i.get(this.field_70963_i.size() - 1))) {
			this.field_70961_j = 60;
			this.field_70959_by = true;
		} else if (this.field_70963_i.size() > 1) {
			int var2 = this.rand.nextInt(6) + this.rand.nextInt(6) + 3;

			if (var2 <= par1MerchantRecipe.func_77392_e()) {
				this.field_70961_j = 20;
				this.field_70957_bA = par1MerchantRecipe;
			}
		}

		if (par1MerchantRecipe.func_77394_a().itemID == Item.diamond.shiftedIndex) {
			this.field_70956_bz += par1MerchantRecipe.func_77394_a().stackSize;
		}
	}

	public MerchantRecipeList func_70934_b(EntityPlayer par1EntityPlayer) {
		if (this.field_70963_i == null) {
			this.func_70950_c(1);
		}

		return this.field_70963_i;
	}

	private void func_70950_c(int par1) {
		MerchantRecipeList var2;
		var2 = new MerchantRecipeList();
		label44:

		switch (this.getProfession()) {
			case 0:
				func_70948_a(var2, Item.wheat.shiftedIndex, this.rand, 0.9F);
				func_70948_a(var2, Block.cloth.blockID, this.rand, 0.5F);
				func_70948_a(var2, Item.chickenRaw.shiftedIndex, this.rand, 0.5F);
				func_70948_a(var2, Item.fishCooked.shiftedIndex, this.rand, 0.4F);
				func_70949_b(var2, Item.bread.shiftedIndex, this.rand, 0.9F);
				func_70949_b(var2, Item.melon.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.appleRed.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.cookie.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.shears.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.flintAndSteel.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.chickenCooked.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.arrow.shiftedIndex, this.rand, 0.5F);

				if (this.rand.nextFloat() < 0.5F) {
					var2.add(new MerchantRecipe(new ItemStack(Block.gravel, 10), new ItemStack(Item.diamond), new ItemStack(Item.flint.shiftedIndex, 2 + this.rand.nextInt(2), 0)));
				}

				break;

			case 1:
				func_70948_a(var2, Item.paper.shiftedIndex, this.rand, 0.8F);
				func_70948_a(var2, Item.book.shiftedIndex, this.rand, 0.8F);
				func_70948_a(var2, Item.field_77823_bG.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Block.bookShelf.blockID, this.rand, 0.8F);
				func_70949_b(var2, Block.glass.blockID, this.rand, 0.2F);
				func_70949_b(var2, Item.compass.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.pocketSundial.shiftedIndex, this.rand, 0.2F);
				break;

			case 2:
				func_70949_b(var2, Item.eyeOfEnder.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.expBottle.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.redstone.shiftedIndex, this.rand, 0.4F);
				func_70949_b(var2, Block.glowStone.blockID, this.rand, 0.3F);
				int[] var3 = new int[] {Item.swordSteel.shiftedIndex, Item.swordDiamond.shiftedIndex, Item.plateSteel.shiftedIndex, Item.plateDiamond.shiftedIndex, Item.axeSteel.shiftedIndex, Item.axeDiamond.shiftedIndex, Item.pickaxeSteel.shiftedIndex, Item.pickaxeDiamond.shiftedIndex};
				int[] var4 = var3;
				int var5 = var3.length;
				int var6 = 0;

				while (true) {
					if (var6 >= var5) {
						break label44;
					}

					int var7 = var4[var6];

					if (this.rand.nextFloat() < 0.1F) {
						var2.add(new MerchantRecipe(new ItemStack(var7, 1, 0), new ItemStack(Item.diamond, 2 + this.rand.nextInt(3), 0), EnchantmentHelper.func_77504_a(this.rand, new ItemStack(var7, 1, 0), 5 + this.rand.nextInt(15))));
					}

					++var6;
				}

			case 3:
				func_70948_a(var2, Item.coal.shiftedIndex, this.rand, 0.7F);
				func_70948_a(var2, Item.ingotIron.shiftedIndex, this.rand, 0.5F);
				func_70948_a(var2, Item.ingotGold.shiftedIndex, this.rand, 0.5F);
				func_70948_a(var2, Item.field_77702_n.shiftedIndex, this.rand, 0.5F);
				func_70949_b(var2, Item.swordSteel.shiftedIndex, this.rand, 0.5F);
				func_70949_b(var2, Item.swordDiamond.shiftedIndex, this.rand, 0.5F);
				func_70949_b(var2, Item.axeSteel.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.axeDiamond.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.pickaxeSteel.shiftedIndex, this.rand, 0.5F);
				func_70949_b(var2, Item.pickaxeDiamond.shiftedIndex, this.rand, 0.5F);
				func_70949_b(var2, Item.shovelSteel.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.shovelDiamond.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.hoeSteel.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.hoeDiamond.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.bootsSteel.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.bootsDiamond.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.helmetSteel.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.helmetDiamond.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.plateSteel.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.plateDiamond.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.legsSteel.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.legsDiamond.shiftedIndex, this.rand, 0.2F);
				func_70949_b(var2, Item.bootsChain.shiftedIndex, this.rand, 0.1F);
				func_70949_b(var2, Item.helmetChain.shiftedIndex, this.rand, 0.1F);
				func_70949_b(var2, Item.plateChain.shiftedIndex, this.rand, 0.1F);
				func_70949_b(var2, Item.legsChain.shiftedIndex, this.rand, 0.1F);
				break;

			case 4:
				func_70948_a(var2, Item.coal.shiftedIndex, this.rand, 0.7F);
				func_70948_a(var2, Item.porkRaw.shiftedIndex, this.rand, 0.5F);
				func_70948_a(var2, Item.beefRaw.shiftedIndex, this.rand, 0.5F);
				func_70949_b(var2, Item.saddle.shiftedIndex, this.rand, 0.1F);
				func_70949_b(var2, Item.plateLeather.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.bootsLeather.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.helmetLeather.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.legsLeather.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.porkCooked.shiftedIndex, this.rand, 0.3F);
				func_70949_b(var2, Item.beefCooked.shiftedIndex, this.rand, 0.3F);
		}

		if (var2.isEmpty()) {
			func_70948_a(var2, Item.ingotGold.shiftedIndex, this.rand, 1.0F);
		}

		Collections.shuffle(var2);

		if (this.field_70963_i == null) {
			this.field_70963_i = new MerchantRecipeList();
		}

		for (int var8 = 0; var8 < par1 && var8 < var2.size(); ++var8) {
			this.field_70963_i.func_77205_a((MerchantRecipe)var2.get(var8));
		}
	}

	public void func_70930_a(MerchantRecipeList par1MerchantRecipeList) {}

	private static void func_70948_a(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3) {
		if (par2Random.nextFloat() < par3) {
			par0MerchantRecipeList.add(new MerchantRecipe(func_70951_a(par1, par2Random), Item.diamond));
		}
	}

	private static ItemStack func_70951_a(int par0, Random par1Random) {
		return new ItemStack(par0, func_70944_b(par0, par1Random), 0);
	}

	private static int func_70944_b(int par0, Random par1Random) {
		Tuple var2 = (Tuple)field_70958_bB.get(Integer.valueOf(par0));
		return var2 == null ? 1 : (((Integer)var2.func_76341_a()).intValue() >= ((Integer)var2.func_76340_b()).intValue() ? ((Integer)var2.func_76341_a()).intValue() : ((Integer)var2.func_76341_a()).intValue() + par1Random.nextInt(((Integer)var2.func_76340_b()).intValue() - ((Integer)var2.func_76341_a()).intValue()));
	}

	private static void func_70949_b(MerchantRecipeList par0MerchantRecipeList, int par1, Random par2Random, float par3) {
		if (par2Random.nextFloat() < par3) {
			int var4 = func_70943_c(par1, par2Random);
			ItemStack var5;
			ItemStack var6;

			if (var4 < 0) {
				var5 = new ItemStack(Item.diamond.shiftedIndex, 1, 0);
				var6 = new ItemStack(par1, -var4, 0);
			} else {
				var5 = new ItemStack(Item.diamond.shiftedIndex, var4, 0);
				var6 = new ItemStack(par1, 1, 0);
			}

			par0MerchantRecipeList.add(new MerchantRecipe(var5, var6));
		}
	}

	private static int func_70943_c(int par0, Random par1Random) {
		Tuple var2 = (Tuple)field_70960_bC.get(Integer.valueOf(par0));
		return var2 == null ? 1 : (((Integer)var2.func_76341_a()).intValue() >= ((Integer)var2.func_76340_b()).intValue() ? ((Integer)var2.func_76341_a()).intValue() : ((Integer)var2.func_76341_a()).intValue() + par1Random.nextInt(((Integer)var2.func_76340_b()).intValue() - ((Integer)var2.func_76341_a()).intValue()));
	}

	public void handleHealthUpdate(byte par1) {
		if (par1 == 12) {
			this.func_70942_a("heart");
		} else {
			super.handleHealthUpdate(par1);
		}
	}

	private void func_70942_a(String par1Str) {
		for (int var2 = 0; var2 < 5; ++var2) {
			double var3 = this.rand.nextGaussian() * 0.02D;
			double var5 = this.rand.nextGaussian() * 0.02D;
			double var7 = this.rand.nextGaussian() * 0.02D;
			this.worldObj.spawnParticle(par1Str, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 1.0D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, var3, var5, var7);
		}
	}

	static {
		field_70958_bB.put(Integer.valueOf(Item.coal.shiftedIndex), new Tuple(Integer.valueOf(16), Integer.valueOf(24)));
		field_70958_bB.put(Integer.valueOf(Item.ingotIron.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		field_70958_bB.put(Integer.valueOf(Item.ingotGold.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		field_70958_bB.put(Integer.valueOf(Item.field_77702_n.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		field_70958_bB.put(Integer.valueOf(Item.paper.shiftedIndex), new Tuple(Integer.valueOf(19), Integer.valueOf(30)));
		field_70958_bB.put(Integer.valueOf(Item.book.shiftedIndex), new Tuple(Integer.valueOf(12), Integer.valueOf(15)));
		field_70958_bB.put(Integer.valueOf(Item.field_77823_bG.shiftedIndex), new Tuple(Integer.valueOf(1), Integer.valueOf(1)));
		field_70958_bB.put(Integer.valueOf(Item.enderPearl.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		field_70958_bB.put(Integer.valueOf(Item.eyeOfEnder.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(3)));
		field_70958_bB.put(Integer.valueOf(Item.porkRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		field_70958_bB.put(Integer.valueOf(Item.beefRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		field_70958_bB.put(Integer.valueOf(Item.chickenRaw.shiftedIndex), new Tuple(Integer.valueOf(14), Integer.valueOf(18)));
		field_70958_bB.put(Integer.valueOf(Item.fishCooked.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(13)));
		field_70958_bB.put(Integer.valueOf(Item.seeds.shiftedIndex), new Tuple(Integer.valueOf(34), Integer.valueOf(48)));
		field_70958_bB.put(Integer.valueOf(Item.melonSeeds.shiftedIndex), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		field_70958_bB.put(Integer.valueOf(Item.pumpkinSeeds.shiftedIndex), new Tuple(Integer.valueOf(30), Integer.valueOf(38)));
		field_70958_bB.put(Integer.valueOf(Item.wheat.shiftedIndex), new Tuple(Integer.valueOf(18), Integer.valueOf(22)));
		field_70958_bB.put(Integer.valueOf(Block.cloth.blockID), new Tuple(Integer.valueOf(14), Integer.valueOf(22)));
		field_70958_bB.put(Integer.valueOf(Item.rottenFlesh.shiftedIndex), new Tuple(Integer.valueOf(36), Integer.valueOf(64)));
		field_70960_bC.put(Integer.valueOf(Item.flintAndSteel.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		field_70960_bC.put(Integer.valueOf(Item.shears.shiftedIndex), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		field_70960_bC.put(Integer.valueOf(Item.swordSteel.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
		field_70960_bC.put(Integer.valueOf(Item.swordDiamond.shiftedIndex), new Tuple(Integer.valueOf(12), Integer.valueOf(14)));
		field_70960_bC.put(Integer.valueOf(Item.axeSteel.shiftedIndex), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
		field_70960_bC.put(Integer.valueOf(Item.axeDiamond.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(12)));
		field_70960_bC.put(Integer.valueOf(Item.pickaxeSteel.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(9)));
		field_70960_bC.put(Integer.valueOf(Item.pickaxeDiamond.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		field_70960_bC.put(Integer.valueOf(Item.shovelSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		field_70960_bC.put(Integer.valueOf(Item.shovelDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		field_70960_bC.put(Integer.valueOf(Item.hoeSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		field_70960_bC.put(Integer.valueOf(Item.hoeDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		field_70960_bC.put(Integer.valueOf(Item.bootsSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		field_70960_bC.put(Integer.valueOf(Item.bootsDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		field_70960_bC.put(Integer.valueOf(Item.helmetSteel.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(6)));
		field_70960_bC.put(Integer.valueOf(Item.helmetDiamond.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(8)));
		field_70960_bC.put(Integer.valueOf(Item.plateSteel.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(14)));
		field_70960_bC.put(Integer.valueOf(Item.plateDiamond.shiftedIndex), new Tuple(Integer.valueOf(16), Integer.valueOf(19)));
		field_70960_bC.put(Integer.valueOf(Item.legsSteel.shiftedIndex), new Tuple(Integer.valueOf(8), Integer.valueOf(10)));
		field_70960_bC.put(Integer.valueOf(Item.legsDiamond.shiftedIndex), new Tuple(Integer.valueOf(11), Integer.valueOf(14)));
		field_70960_bC.put(Integer.valueOf(Item.bootsChain.shiftedIndex), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		field_70960_bC.put(Integer.valueOf(Item.helmetChain.shiftedIndex), new Tuple(Integer.valueOf(5), Integer.valueOf(7)));
		field_70960_bC.put(Integer.valueOf(Item.plateChain.shiftedIndex), new Tuple(Integer.valueOf(11), Integer.valueOf(15)));
		field_70960_bC.put(Integer.valueOf(Item.legsChain.shiftedIndex), new Tuple(Integer.valueOf(9), Integer.valueOf(11)));
		field_70960_bC.put(Integer.valueOf(Item.bread.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-2)));
		field_70960_bC.put(Integer.valueOf(Item.melon.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
		field_70960_bC.put(Integer.valueOf(Item.appleRed.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-4)));
		field_70960_bC.put(Integer.valueOf(Item.cookie.shiftedIndex), new Tuple(Integer.valueOf(-10), Integer.valueOf(-7)));
		field_70960_bC.put(Integer.valueOf(Block.glass.blockID), new Tuple(Integer.valueOf(-5), Integer.valueOf(-3)));
		field_70960_bC.put(Integer.valueOf(Block.bookShelf.blockID), new Tuple(Integer.valueOf(3), Integer.valueOf(4)));
		field_70960_bC.put(Integer.valueOf(Item.plateLeather.shiftedIndex), new Tuple(Integer.valueOf(4), Integer.valueOf(5)));
		field_70960_bC.put(Integer.valueOf(Item.bootsLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		field_70960_bC.put(Integer.valueOf(Item.helmetLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		field_70960_bC.put(Integer.valueOf(Item.legsLeather.shiftedIndex), new Tuple(Integer.valueOf(2), Integer.valueOf(4)));
		field_70960_bC.put(Integer.valueOf(Item.saddle.shiftedIndex), new Tuple(Integer.valueOf(6), Integer.valueOf(8)));
		field_70960_bC.put(Integer.valueOf(Item.expBottle.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
		field_70960_bC.put(Integer.valueOf(Item.redstone.shiftedIndex), new Tuple(Integer.valueOf(-4), Integer.valueOf(-1)));
		field_70960_bC.put(Integer.valueOf(Item.compass.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		field_70960_bC.put(Integer.valueOf(Item.pocketSundial.shiftedIndex), new Tuple(Integer.valueOf(10), Integer.valueOf(12)));
		field_70960_bC.put(Integer.valueOf(Block.glowStone.blockID), new Tuple(Integer.valueOf(-3), Integer.valueOf(-1)));
		field_70960_bC.put(Integer.valueOf(Item.porkCooked.shiftedIndex), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		field_70960_bC.put(Integer.valueOf(Item.beefCooked.shiftedIndex), new Tuple(Integer.valueOf(-7), Integer.valueOf(-5)));
		field_70960_bC.put(Integer.valueOf(Item.chickenCooked.shiftedIndex), new Tuple(Integer.valueOf(-8), Integer.valueOf(-6)));
		field_70960_bC.put(Integer.valueOf(Item.eyeOfEnder.shiftedIndex), new Tuple(Integer.valueOf(7), Integer.valueOf(11)));
		field_70960_bC.put(Integer.valueOf(Item.arrow.shiftedIndex), new Tuple(Integer.valueOf(-5), Integer.valueOf(-19)));
	}
}
