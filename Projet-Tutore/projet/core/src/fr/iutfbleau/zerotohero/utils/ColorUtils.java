package fr.iutfbleau.zerotohero.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorUtils {
	public static Color mul(Color color, float value) {
		return new Color(color).mul(value);
	}
}
