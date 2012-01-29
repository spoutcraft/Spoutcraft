package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityAITasks {
	private ArrayList field_46120_a;
	private ArrayList field_46119_b;

	public EntityAITasks() {
		field_46120_a = new ArrayList();
		field_46119_b = new ArrayList();
	}

	public void func_46118_a(int i, EntityAIBase entityaibase) {
		field_46120_a.add(new EntityAITaskEntry(this, i, entityaibase));
	}

	public void func_46115_a() {
		ArrayList arraylist = new ArrayList();
		Iterator iterator = field_46120_a.iterator();
		do {
			if (!iterator.hasNext()) {
				break;
			}
			EntityAITaskEntry entityaitaskentry = (EntityAITaskEntry)iterator.next();
			boolean flag = field_46119_b.contains(entityaitaskentry);
			if (flag) {
				if (!entityaitaskentry.field_46114_a.func_46084_g() || !func_46116_a(entityaitaskentry)) {
					entityaitaskentry.field_46114_a.func_46077_d();
					field_46119_b.remove(entityaitaskentry);
				}
			}
			else if (entityaitaskentry.field_46114_a.func_46082_a() && func_46116_a(entityaitaskentry)) {
				arraylist.add(entityaitaskentry);
				field_46119_b.add(entityaitaskentry);
			}
		}
		while (true);
		EntityAITaskEntry entityaitaskentry1;
		for (Iterator iterator1 = arraylist.iterator(); iterator1.hasNext(); entityaitaskentry1.field_46114_a.func_46080_e()) {
			entityaitaskentry1 = (EntityAITaskEntry)iterator1.next();
		}

		EntityAITaskEntry entityaitaskentry2;
		for (Iterator iterator2 = field_46119_b.iterator(); iterator2.hasNext(); entityaitaskentry2.field_46114_a.func_46081_b()) {
			entityaitaskentry2 = (EntityAITaskEntry)iterator2.next();
		}
	}

	private boolean func_46116_a(EntityAITaskEntry entityaitaskentry) {
		label0: {
			Iterator iterator = field_46120_a.iterator();
			EntityAITaskEntry entityaitaskentry1;
			label1:
			do {
				do {
					do {
						if (!iterator.hasNext()) {
							break label0;
						}
						entityaitaskentry1 = (EntityAITaskEntry)iterator.next();
					}
					while (entityaitaskentry1 == entityaitaskentry);
					if (entityaitaskentry.field_46112_b < entityaitaskentry1.field_46112_b) {
						continue label1;
					}
				}
				while (!field_46119_b.contains(entityaitaskentry1) || func_46117_a(entityaitaskentry, entityaitaskentry1));
				return false;
			}
			while (!field_46119_b.contains(entityaitaskentry1) || entityaitaskentry1.field_46114_a.func_46078_f());
			return false;
		}
		return true;
	}

	private boolean func_46117_a(EntityAITaskEntry entityaitaskentry, EntityAITaskEntry entityaitaskentry1) {
		return (entityaitaskentry.field_46114_a.func_46083_c() & entityaitaskentry1.field_46114_a.func_46083_c()) == 0;
	}
}
