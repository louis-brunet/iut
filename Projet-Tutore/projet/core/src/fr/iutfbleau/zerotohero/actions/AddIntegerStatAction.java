package fr.iutfbleau.zerotohero.actions;

import fr.iutfbleau.zerotohero.actors.Stat;

import java.util.Objects;

public class AddIntegerStatAction extends SetStatAction<Integer> {

    @Override
    public void setStat(Stat stat) {
        if ( ! stat.getValueClass().equals(Integer.class))
            throw new IllegalArgumentException("Stat value is not an integer : "+stat.name());
        super.setStat(stat);
    }

    @Override
    protected void begin() {
//        System.out.println("AddIntegerStatAction.begin()");
        super.begin();
        Objects.requireNonNull(this.getStatContainer());
        if (!this.getStatContainer().containsStat(this.getStat()))
            throw new IllegalStateException("The container does not have this stat.");

        int oldValue = this.getStatContainer().getStatValue(this.getStat(), Integer.class);
        this.setValue(this.getValue() + oldValue);
    }

}
