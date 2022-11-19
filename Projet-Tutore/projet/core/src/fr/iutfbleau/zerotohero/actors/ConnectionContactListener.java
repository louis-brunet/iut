package fr.iutfbleau.zerotohero.actors;

import com.badlogic.gdx.math.Rectangle;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.CollisionDirection;
import fr.iutfbleau.zerotohero.physics.Body.Type;
import fr.iutfbleau.zerotohero.physics.ConnectionBody;
import fr.iutfbleau.zerotohero.physics.ContactListener;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.screens.GameplayScreen;

public class ConnectionContactListener implements ContactListener {
	private final Player player;
	
	public ConnectionContactListener(Player player) {
		this.player = player;
	}

	@Override
	public void onContact(Body b) {
		if (b.getType() == Type.CONNECTION) {
			ConnectionBody body = (ConnectionBody) b;
			if (body.getRoom().getConnectionAtPosition(body.getConnectionPosition()) != null) {
				Room otherRoom = body.getRoom().getConnectionAtPosition(body.getConnectionPosition()).getOtherRoom(body.getRoom());
				Rectangle rectangle = otherRoom.getConnectionZoneAtPosition(
						body.getRoom().getConnectionAtPosition(
								body.getConnectionPosition())
						.getOtherPosition(
								body.getRoom()));
				float x = rectangle.getX() + ((body.getConnectionPosition().name().contains("RIGHT")) ? rectangle.getWidth()*1.5f: -player
						.getWidth() - rectangle.getWidth() / 2),
						y = rectangle.getY();
	//			actor.setPosition(x, y);
				player.getRoom().removeActor(player, player.getBody());
				player.getRoom().removeActor(player.getWeapon(), null);

				GameplayScreen screen = null;
				if (ZeroToHero.getCurrentScreen() instanceof GameplayScreen) {
					screen = (GameplayScreen) ZeroToHero.getCurrentScreen();
					player.getRoom().getAllActors().forEach(screen.getRoomRenderer()::removeActor);
				}
				player.setRoom(otherRoom);
				player.getRoom().addActor(player);
				if (player.getWeapon() != null) {
					player.getWeapon().remove();
					player.getRoom().addActor(player.getWeapon());
					player.getWeapon().setRoom(player.getRoom());
				}

				if (screen != null) {
					player.remove();
					player.getRoom().getAllActors().forEach(screen.getRoomRenderer()::addActor);
				}
				player.getBody().setPosition(x, y + 50f);
			} else {
				// prevents getting stuck when landing on corner of rectangle
	            boolean canSidesBeBlocked = true;

	            if(this.player.isActuallyTouchingDown(b)) {
	                this.player.addTouching(CollisionDirection.DOWN, b);
	                canSidesBeBlocked = false;
	            }
	            if(this.player.isActuallyTouchingUp(b)) {
	                this.player.addTouching(CollisionDirection.UP, b);
	                canSidesBeBlocked = false;
	            }
	            if(canSidesBeBlocked && this.player.isActuallyTouchingRight(b)) {
	                this.player.addTouching(CollisionDirection.RIGHT, b);
	            }
	            if(canSidesBeBlocked && this.player.isActuallyTouchingLeft(b)) {
	                this.player.addTouching(CollisionDirection.LEFT, b);
	            }
			}
		}
	}

	@Override
	public void onContactEnded(Body b) {
	}



	@Override
	public void onContactStarted(Body b) { }
}
