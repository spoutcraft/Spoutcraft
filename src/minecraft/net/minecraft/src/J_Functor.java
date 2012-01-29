package net.minecraft.src;

interface J_Functor {
	public abstract boolean matchesNode(Object obj);

	public abstract Object applyTo(Object obj);

	public abstract String shortForm();
}
