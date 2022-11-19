package fr.iutfbleau.zerotohero.entities.shop;

public class Currencies {
    public static final GoldCurrency GOLD = new GoldCurrency();
    public static final HeartsCurrency HEARTS = new HeartsCurrency();

    public static Currency get(String name) {
        switch(name) {
            case "GOLD":
                return Currencies.GOLD;
            case "HEARTS":
                return Currencies.HEARTS;

            default:
                throw new IllegalArgumentException("Currency not recognized : "+name);
        }
    }
}
