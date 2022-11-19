package fr.iutfbleau.zerotohero.utils;

public class Tuple3<A, B, C> {
	public final A a;
	public final B b;
	public final C c;
	
	public Tuple3(A a, B b, C c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(getClass().equals(obj.getClass()))) return false;
		
		Tuple3<?, ?, ?> tuple = (Tuple3<?, ?, ?>) obj;
		if (!a.equals(tuple.a)) return false;
		if (!b.equals(tuple.b)) return false;
		return c.equals(tuple.c);
	}
	
	@Override
	public String toString() {
		return "("+a+" ; "+b+" ; "+c+")";
	}
}
