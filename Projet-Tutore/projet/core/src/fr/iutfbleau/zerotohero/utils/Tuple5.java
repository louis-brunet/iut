package fr.iutfbleau.zerotohero.utils;

public class Tuple5<A, B, C, D, E> {
	public final A a;
	public final B b;
	public final C c;
	public final D d;
	public final E e;
	
	public Tuple5(A a, B b, C c, D d, E e) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
		this.e = e;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(getClass().equals(obj.getClass()))) return false;
		
		Tuple5<?, ?, ?, ?, ?> tuple = (Tuple5<?, ?, ?, ?, ?>) obj;
		if (!a.equals(tuple.a)) return false;
		if (!b.equals(tuple.b)) return false;
		if (!c.equals(tuple.c)) return false;
		if (!d.equals(tuple.d)) return false;
		return e.equals(tuple.e);
	}
	
	@Override
	public String toString() {
		return "("+a+" ; "+b+" ; "+c+" ; "+d+" ; "+e+")";
	}
}
