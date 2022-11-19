package fr.iutfbleau.zerotohero.actors.enemies;

import com.badlogic.gdx.math.MathUtils;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.Character;
import fr.iutfbleau.zerotohero.registries.Weapons;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.weapons.Projectile;
import fr.iutfbleau.zerotohero.weapons.WeaponActor;
import fr.iutfbleau.zerotohero.weapons.WeaponFactory;

public class SkeletonArcher implements ICharacterAI {//extends EnemyAI {
    private float cycleDuration, cycleTimer;
    private WeaponActor weapon = WeaponFactory.create(Weapons.SIMPLE_BOW.getProperties(), Projectile.Collision.ENEMY);
    private Projectile fired;
    boolean use = false;

    float timer = 0;

    public SkeletonArcher() {
        this.cycleDuration = 0.4f;
        this.cycleTimer = 0f;
        this.weapon = WeaponFactory.create(Weapons.SIMPLE_BOW.getProperties(), Projectile.Collision.ENEMY);
    }

    @Override
    public void onAddedToCharacter(Character character) {
        character.setMoveLeft(false);
        character.setMoveRight(false);
    }

    @Override
    public void onCharacterAddedToRoom(Character character) {
        character.getRoom().addActor(weapon);
        weapon.setRoom(character.getRoom());

    }

    @Override
    public void updateCharacter(fr.iutfbleau.zerotohero.actors.Character character, float delta) {
        float player_Position_X = ZeroToHero.getPlayer().getBody().getPosition().getX();
        float player_Position_Y = ZeroToHero.getPlayer().getBody().getPosition().getY();
        Coordinates worldCursor = new Coordinates(player_Position_X,player_Position_Y);

        if (weapon != null) {
            weapon.setAnchor(character.getBody().getPosition().getX(),
                    character.getBody().getPosition().getY());

            if (player_Position_Y >= character.getBody().getPosition().getY()-5)
            {
                use = true;
                fired = weapon.update(delta, worldCursor.getX(), worldCursor.getY(), use);
            }
            else if (player_Position_X >= character.getBody().getPosition().getX()/2 || player_Position_X <= character.getBody().getPosition().getX()){
                use=false;
                fired = weapon.update(delta, worldCursor.getX(), worldCursor.getY(), use);
            }
        }
    }

    @Override
    public void running(Character character) {
        character.setCurrentAnimation(EnemyActor.RUN_ANIMATION_ID, true);
    }

    @Override
    public void idle(Character character) {
        character.setCurrentAnimation(EnemyActor.IDLE_ANIMATION_ID, true);
    }

    @Override
    public void jumping(Character character) {
        character.setCurrentAnimation(EnemyActor.JUMP_ANIMATION_ID, false);
    }

    @Override
    public void onJumpStart(Character character) {

    }

    public WeaponActor getWeapon() {
        return weapon;
    }

    //    @Override
//    public void running(fr.iutfbleau.zerotohero.actors.Character character) {
//        character.setCurrentAnimation(Test.RUN_ANIMATION_ID, true);
//    }
//
//    @Override
//    public void idle(fr.iutfbleau.zerotohero.actors.Character character) {
//        character.setCurrentAnimation(Test.IDLE_ANIMATION_ID, true);
//    }
//
//    @Override
//    public void jumping(fr.iutfbleau.zerotohero.actors.Character character) {
//        character.setCurrentAnimation(Test.JUMP_ANIMATION_ID, false);
//    }
}
