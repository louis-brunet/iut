package fr.iutfbleau.zerotohero.actors;

import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.Body.Type;
import fr.iutfbleau.zerotohero.physics.ContactListener;

public class DamageContactListener implements ContactListener {
	private Character actor;
	
	public DamageContactListener(Character actor) {
		this.actor = actor;
	}

	@Override
	public void onContact(Body b) {
//		if (b.getType() == Type.DAMAGE) {
//			this.actor.damage(1);
//			this.actor.propulse(5000,1000,this.actor.getFacing().getOpposite());
//		}
	}

	@Override
	public void onContactEnded(Body b) {
	}

	@Override
	public void onContactStarted(Body b) {
		if (b.getType() == Type.DAMAGE) {
			this.actor.damage(1);
			this.actor.propulse(5000,2000,this.actor.getFacing().getOpposite());
		}
	}
}
