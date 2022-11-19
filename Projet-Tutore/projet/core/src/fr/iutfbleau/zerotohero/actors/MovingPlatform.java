package fr.iutfbleau.zerotohero.actors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.Body;

/**
 * Trash
 *
 * doesn't work yet
 */
public class MovingPlatform extends PhysicsActor {

    private static final short ANIMATION_ID = 0;

    private static final String ACTOR_NAME = "moving_platform";

    private float speed;
    private List<Coordinates> path = new ArrayList<Coordinates>();
    private int currentPoint = 0;
    private float waitTime, waitTimeElapsed = 0;
    private boolean didJustSpawn = true;

    public MovingPlatform(String textureFilePath, Body b, float width, float height, float speed, float waitTime) {
        super(MovingPlatform.ACTOR_NAME, b);
        this.speed = speed;
        this.waitTime = waitTime;
        this.setWidth(width);
        this.setHeight(height);

		ZeroToHero.getAssetManager().addAsset(textureFilePath, Texture.class);
        this.addAnimation(MovingPlatform.ANIMATION_ID,
                          ZeroToHero.getAssetManager().getAsset(textureFilePath, Texture.class),
                          1, 1, 0.1f);
        this.setCurrentAnimation(MovingPlatform.ANIMATION_ID, false);

//        this.body.addContactListener(new TestContactListener(this));
    }

    public List<Coordinates> getPath() {
        return this.path;
    }

    public void addAllPoints(boolean bothWays, Coordinates... coordinates) {
        this.path.addAll(Arrays.asList(coordinates));
        if (bothWays)
            for (int i = coordinates.length-2; i > 0; i--)
                path.add(path.get(i));
    }

    private void updateSpeed(float delta) {
        Coordinates pointA = path.get(this.currentPoint);
        int nextPoint = this.currentPoint + 1;
        if (nextPoint >= this.path.size()) {
            nextPoint = 0;
        }
        Coordinates pointB = path.get(nextPoint);
        Coordinates thisPos = new Coordinates(getX(), getY());
        boolean xGoBack = false;
        if (pointA.getX() > pointB.getX())
            xGoBack = true;
        boolean yGoBack = false;
        if (pointA.getY() > pointB.getY())
            yGoBack = true;
        Coordinates distanceBetweenPoints = pointA.getCoordinatesTo(pointB);
        float distanceValueBetweenPoints = pointA.getDistance(pointB);
        float factor = (this.speed*delta)/distanceValueBetweenPoints;
        float horizontalSpeed = distanceBetweenPoints.getX()*factor;
        float verticalSpeed = distanceBetweenPoints.getY()*factor;
        if (!thisPos.furtherThan(pointB, xGoBack, yGoBack)) {
            Coordinates newPos = thisPos.add(horizontalSpeed, verticalSpeed);
            if (newPos.furtherThan(pointB, xGoBack, yGoBack)) {
            	newPos.setX(pointB.getX());
            	newPos.setY(pointB.getY());
                this.body.setSpeed(0, 0);
            	this.body.setPosition(newPos.getX()+this.getWidth()/2f, newPos.getY()+this.getHeight()/2f);
            } else {
                this.body.setSpeed(horizontalSpeed / delta, verticalSpeed / delta);
            }
        } else {
            this.body.setSpeed(0, 0);
        	this.body.setPosition(pointB.getX()+this.getWidth()/2f, pointB.getY()+this.getHeight()/2f);
            if (this.waitTimeElapsed < this.waitTime)
                this.waitTimeElapsed += delta;
            else {
                this.currentPoint = nextPoint;
                this.waitTimeElapsed = 0;
            }
        }
    }

    @Override
    public void act(float delta) {
//        super.act(delta);
        if (this.didJustSpawn) {
            if (this.waitTimeElapsed < this.waitTime)
                this.waitTimeElapsed += delta;
            else {
                this.didJustSpawn = false;
                this.waitTimeElapsed = 0;
            }
            return;
        }
        this.updateSpeed(delta);
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
//        System.out.println("MovingPlatform.draw() : this.currentFrame = "+currentFrame);
        super.draw(batch, parentAlpha);
    }
}
