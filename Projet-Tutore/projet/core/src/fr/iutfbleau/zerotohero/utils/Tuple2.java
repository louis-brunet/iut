package fr.iutfbleau.zerotohero.utils;

public class Tuple2<A, B> {
	public final A a;
	public final B b;
	
	public Tuple2(A a, B b) {
		this.a = a;
		this.b = b;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || !(getClass().equals(obj.getClass()))) return false;
		
		Tuple2<?, ?> tuple = (Tuple2<?, ?>) obj;
		if (!a.equals(tuple.a)) return false;
		return b.equals(tuple.b);
	}
	
	@Override
	public String toString() {
		return "("+a+" ; "+b+")";
	}
}
