package fr.iutfbleau.zerotohero.weapons;

import com.badlogic.gdx.math.MathUtils;

import fr.iutfbleau.zerotohero.registries.WeaponProperties;

public abstract class ChargeableWeapon extends WeaponActor {
    private float chargeTimer, chargeDuration;
    private float chargePercent; // 0.0f - 1.0f

    public ChargeableWeapon(WeaponProperties type, float chargeDuration) {
        super(type);

        this.chargeDuration = chargeDuration;
        this.chargeTimer = 0f;
        this.chargePercent = 0f;
    }

    protected abstract Projectile fire(float chargePercent);

    @Override
    public Projectile update(float delta, float cursorX, float cursorY, boolean use) {
        Projectile res = null;

        this.aim(cursorX, cursorY);

        if (use) {
            this.chargeTimer += delta;
        } else if (this.chargePercent > 0f) {
            res = this.fire(this.chargePercent);
            this.chargeTimer = 0f;
        }
        this.chargePercent = MathUtils.clamp(chargeTimer / chargeDuration, 0f, 1f);


        if (chargePercent >= 1f)
            System.out.println(getName()+" is fully charged.");
        else if (chargePercent > 0f)
            System.out.println("weapon charge : "+chargePercent);

        return res;

    }
}
