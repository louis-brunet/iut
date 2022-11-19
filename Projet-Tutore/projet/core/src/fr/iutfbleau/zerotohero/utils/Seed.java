package fr.iutfbleau.zerotohero.utils;

import com.badlogic.gdx.math.MathUtils;

public class Seed {
	private long seedValue;
	private String seedText;
	
    public Seed(String seed) {
    	if (seed.length() > 12) {
    		throw new IllegalArgumentException("seed should be 12 chars maximum");
    	}

		for (int i = seed.length(); i < 12; i++)
			seed += "0";

    	try {
	    	this.seedValue = Long.parseLong(seed, 36);
			this.seedText = seed;
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		return;
    	}
	}
    
    public Seed() {
    	this.seedValue = MathUtils.random(0L, Long.parseLong("zzzzzzzzzzzz", 36));
    	this.seedText = Long.toString(this.seedValue, 36);
    }
    
	public String getSeedText() {
		return seedText;
	}
	
	public long getSeedValue() {
		return seedValue;
	}
	
	@Override
	public String toString() {
		String string = this.seedText.substring(0,4)+"-"+this.seedText.substring(4,8)+"-"+this.seedText.substring(8,12);
		return string;
	}
}
