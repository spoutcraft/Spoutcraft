// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode 

package net.minecraft.src;

import java.util.Random;

// Referenced classes of package net.minecraft.src:
//            BlockBreakable, Material, IBlockAccess, World, 
//            Block, BlockFire, Entity, AxisAlignedBB

public class BlockPortal extends BlockBreakable
{

    public BlockPortal(int i, int j)
    {
        super(i, j, Material.portal, false);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        if(iblockaccess.getBlockId(i - 1, j, k) == blockID || iblockaccess.getBlockId(i + 1, j, k) == blockID)
        {
            float f = 0.5F;
            float f2 = 0.125F;
            setBlockBounds(0.5F - f, 0.0F, 0.5F - f2, 0.5F + f, 1.0F, 0.5F + f2);
        } else
        {
            float f1 = 0.125F;
            float f3 = 0.5F;
            setBlockBounds(0.5F - f1, 0.0F, 0.5F - f3, 0.5F + f1, 1.0F, 0.5F + f3);
        }
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean tryToCreatePortal(World world, int i, int j, int k)
    {
        int l = 0;
        int i1 = 0;
        if(world.getBlockId(i - 1, j, k) == Block.obsidian.blockID || world.getBlockId(i + 1, j, k) == Block.obsidian.blockID)
        {
            l = 1;
        }
        if(world.getBlockId(i, j, k - 1) == Block.obsidian.blockID || world.getBlockId(i, j, k + 1) == Block.obsidian.blockID)
        {
            i1 = 1;
        }
        if(l == i1)
        {
            return false;
        }
        if(world.getBlockId(i - l, j, k - i1) == 0)
        {
            i -= l;
            k -= i1;
        }
        for(int j1 = -1; j1 <= 2; j1++)
        {
            for(int l1 = -1; l1 <= 3; l1++)
            {
                boolean flag = j1 == -1 || j1 == 2 || l1 == -1 || l1 == 3;
                if((j1 == -1 || j1 == 2) && (l1 == -1 || l1 == 3))
                {
                    continue;
                }
                int j2 = world.getBlockId(i + l * j1, j + l1, k + i1 * j1);
                if(flag)
                {
                    if(j2 != Block.obsidian.blockID)
                    {
                        return false;
                    }
                    continue;
                }
                if(j2 != 0 && j2 != Block.fire.blockID)
                {
                    return false;
                }
            }

        }

        world.editingBlocks = true;
        for(int k1 = 0; k1 < 2; k1++)
        {
            for(int i2 = 0; i2 < 3; i2++)
            {
                world.setBlockWithNotify(i + l * k1, j + i2, k + i1 * k1, Block.portal.blockID);
            }

        }

        world.editingBlocks = false;
        return true;
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = 0;
        int j1 = 1;
        if(world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID)
        {
            i1 = 1;
            j1 = 0;
        }
        int k1;
        for(k1 = j; world.getBlockId(i, k1 - 1, k) == blockID; k1--) { }
        if(world.getBlockId(i, k1 - 1, k) != Block.obsidian.blockID)
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        int l1;
        for(l1 = 1; l1 < 4 && world.getBlockId(i, k1 + l1, k) == blockID; l1++) { }
        if(l1 != 3 || world.getBlockId(i, k1 + l1, k) != Block.obsidian.blockID)
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        boolean flag = world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID;
        boolean flag1 = world.getBlockId(i, j, k - 1) == blockID || world.getBlockId(i, j, k + 1) == blockID;
        if(flag && flag1)
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        if((world.getBlockId(i + i1, j, k + j1) != Block.obsidian.blockID || world.getBlockId(i - i1, j, k - j1) != blockID) && (world.getBlockId(i - i1, j, k - j1) != Block.obsidian.blockID || world.getBlockId(i + i1, j, k + j1) != blockID))
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        } else
        {
            return;
        }
    }

    public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l)
    {
        if(iblockaccess.getBlockId(i, j, k) == blockID)
        {
            return false;
        }
        boolean flag = iblockaccess.getBlockId(i - 1, j, k) == blockID && iblockaccess.getBlockId(i - 2, j, k) != blockID;
        boolean flag1 = iblockaccess.getBlockId(i + 1, j, k) == blockID && iblockaccess.getBlockId(i + 2, j, k) != blockID;
        boolean flag2 = iblockaccess.getBlockId(i, j, k - 1) == blockID && iblockaccess.getBlockId(i, j, k - 2) != blockID;
        boolean flag3 = iblockaccess.getBlockId(i, j, k + 1) == blockID && iblockaccess.getBlockId(i, j, k + 2) != blockID;
        boolean flag4 = flag || flag1;
        boolean flag5 = flag2 || flag3;
        if(flag4 && l == 4)
        {
            return true;
        }
        if(flag4 && l == 5)
        {
            return true;
        }
        if(flag5 && l == 2)
        {
            return true;
        }
        return flag5 && l == 3;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public int getRenderBlockPass()
    {
        return 2;
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if(entity.ridingEntity == null && entity.riddenByEntity == null)
        {
            entity.setInPortal();
        }
    }

    public void randomDisplayTick(World world, int i, int j, int k, Random random)
    {
        if(random.nextInt(100) == 0)
        {
            world.playSoundEffect((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, "portal.portal", 1.0F, random.nextFloat() * 0.4F + 0.8F);
        }
        for(int l = 0; l < 4; l++)
        {
            double d = (float)i + random.nextFloat();
            double d1 = (float)j + random.nextFloat();
            double d2 = (float)k + random.nextFloat();
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            int i1 = random.nextInt(2) * 2 - 1;
            d3 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            d4 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            d5 = ((double)random.nextFloat() - 0.5D) * 0.5D;
            if(world.getBlockId(i - 1, j, k) == blockID || world.getBlockId(i + 1, j, k) == blockID)
            {
                d2 = (double)k + 0.5D + 0.25D * (double)i1;
                d5 = random.nextFloat() * 2.0F * (float)i1;
            } else
            {
                d = (double)i + 0.5D + 0.25D * (double)i1;
                d3 = random.nextFloat() * 2.0F * (float)i1;
            }
            world.spawnParticle("portal", d, d1, d2, d3, d4, d5);
        }

    }
}
