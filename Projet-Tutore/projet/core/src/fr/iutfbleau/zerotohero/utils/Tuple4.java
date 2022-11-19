package fr.iutfbleau.zerotohero.utils;

public class Tuple4<A, B, C, D> {
	public final A a;
	public final B b;
	public final C c;
	public final D d;
	
	public Tuple4(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(getClass().equals(obj.getClass()))) return false;
		
		Tuple4<?, ?, ?, ?> tuple = (Tuple4<?, ?, ?, ?>) obj;
		if (!a.equals(tuple.a)) return false;
		if (!b.equals(tuple.b)) return false;
		if (!c.equals(tuple.c)) return false;
		return d.equals(tuple.d);
	}
	
	@Override
	public String toString() {
		return "("+a+" ; "+b+" ; "+c+" ; "+d+")";
	}
}
