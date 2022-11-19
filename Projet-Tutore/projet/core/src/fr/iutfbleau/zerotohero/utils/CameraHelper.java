package fr.iutfbleau.zerotohero.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

import fr.iutfbleau.zerotohero.actors.PhysicsActor;
import fr.iutfbleau.zerotohero.room.Room;

/**
 * This class handles camera positioning, trying to keep an Actor within a certain window.
 * This window is centered in a Viewport and is defined by its percentage size of the
 * Viewport's screen width.
 */
public class CameraHelper {
    /**
     * The default dimensions of the window within which to keep the actor.
     * Relative to the viewport's screen size.
     */
    private static final Coordinates DEFAULT_WINDOW_SIZE = new Coordinates(1f / 4f,
                                                                           1f / 3f);

    /**
     * The viewport that should be following the actor.
     */
    private final Viewport viewport;
    /**
     * The actor to follow.
     */
    private final Actor actor;

    /**
     * The dimensions of the window within which to keep the actor.
     * Relative to the viewport's screen size.
     */
    private final Coordinates windowSize;
    
    private final float[] samples;
    private float internalTimer = 0;
    private float shakeDuration = 0;
    
    private final int duration = 5;
    private final int frequency = 500;
    private final boolean falloff = true;
    private Vector2 baseCameraPos;
    
    private final int sampleCount;

    public CameraHelper(Viewport vp, Actor actor) {
        this(vp, actor,
             CameraHelper.DEFAULT_WINDOW_SIZE.getX(),
             CameraHelper.DEFAULT_WINDOW_SIZE.getY());
    }

    public CameraHelper(Viewport vp, Actor actor,
                        float windowWidth, float windowHeight) {
        if (windowWidth < 0f || windowWidth > 1f
            || windowHeight < 0f || windowHeight > 1f) {
            throw new IllegalArgumentException("Window width and height must be given relative to screen width (0.0-1.0)");
        }
        this.viewport = vp;
        this.actor = actor;
        
        sampleCount = duration * frequency;
        samples = new float[sampleCount];
        for (int i =0; i < sampleCount; i++){
           samples[i] = MathUtils.random(2f)- 1f;
        }

        this.setPosition(actor.getX() + actor.getWidth() / 2f,
                         actor.getY() + actor.getHeight() / 2f,
                         true);

        this.windowSize = new Coordinates(windowWidth, windowHeight);
    }

    public void update(float delta) {
        float actorCenterX = this.actor.getWidth() / 2f,
        		actorCenterY = this.actor.getHeight() / 2f,
        		moveX = 0,
        		moveY = 0;
        int verticalMargin =
                (int) (this.viewport.getScreenHeight() * (1f - this.windowSize.getY()) / 2f);
        int horizontalMargin =
                (int) (this.viewport.getScreenWidth() * (1f - this.windowSize.getX()) / 2f);

        Vector2 actorPos = new Vector2(this.actor.getX()+actorCenterX,this.actor.getY()+actorCenterY),
        		actorPosOnCamera = this.viewport.project(actorPos);

        if (actorPosOnCamera.x < horizontalMargin) {
        	moveX = -(horizontalMargin-actorPosOnCamera.x);
        } else if (actorPosOnCamera.x > viewport.getScreenWidth()-horizontalMargin) {
        	moveX = -(viewport.getScreenWidth()-horizontalMargin-actorPosOnCamera.x);
        }
        
        if (actorPosOnCamera.y < verticalMargin) {
        	moveY = -(verticalMargin-actorPosOnCamera.y);
        } else if (actorPosOnCamera.y > viewport.getScreenHeight()-verticalMargin) {
        	moveY = -(viewport.getScreenHeight()-verticalMargin-actorPosOnCamera.y);
        }
        
        this.move(moveX, moveY);
        
        internalTimer += delta;
        if (internalTimer > duration) internalTimer -= duration;
        if (shakeDuration > 0) {
        	shakeDuration -= delta;
        	float shakeTime = (internalTimer * frequency);
        	int first = (int)shakeTime;
        	int second = (first + 1)%sampleCount;
        	int third = (first + 2)%sampleCount;
        	float deltaT = shakeTime - (int)shakeTime;
        	float deltaX = samples[first] * deltaT + samples[second] * ( 1f - deltaT);
        	float deltaY = samples[second] * deltaT + samples[third] * ( 1f - deltaT);

            float amplitude = 50;
            setPosition(baseCameraPos.x + deltaX * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f), baseCameraPos.y + deltaY *
                                                                                                                               amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f), false);
        } else {
        	setPosition(this.baseCameraPos.x, this.baseCameraPos.y, true);
        }
    }
    
    public void shake(float duration){
    	shakeDuration = duration;
    }
    
    private Vector3 getPosition() {
    	return this.viewport.getCamera().position;
    }
    
    private void setPosition(float worldX, float worldY, boolean changeCameraPos) {
        this.viewport.getCamera().position.set(worldX, worldY, 0f);
    	if (changeCameraPos)
    		this.baseCameraPos = new Vector2(getPosition().x, getPosition().y);
    	this.viewport.getCamera().update();
    }

    /**
     * Keep the camera
     */
    private void stayWithinBounds() {
    	if (actor instanceof PhysicsActor) {
    		PhysicsActor physicsActor = (PhysicsActor) actor;
    		Room room = physicsActor.getRoom();
    		OrthographicCamera cam = (OrthographicCamera) this.viewport.getCamera();
	    	float mapWidth = room.getWidth(true),
	    			mapHeight = room.getHeight(true),
	    			cameraHalfWidth = (cam.viewportWidth*cam.zoom)/2f,
	    			cameraHalfHeight = (cam.viewportHeight*cam.zoom)/2f;
	
	    	if (cameraHalfWidth*2 > mapWidth)
	    		baseCameraPos.x = mapWidth/2f;
    		else
		    	baseCameraPos.x = MathUtils.clamp(baseCameraPos.x, cameraHalfWidth, mapWidth - cameraHalfWidth);
	    	
	    	if (cameraHalfHeight*2 > mapHeight)
	    		baseCameraPos.y = mapHeight/2f;
	    	else
	    		baseCameraPos.y = MathUtils.clamp(baseCameraPos.y, cameraHalfHeight, mapHeight - cameraHalfHeight);
    	}
	}

    /**
     * Translates the camera.
     *
     * @param speedX x offset
     * @param speedY y offset
     */
    private void move(float speedX, float speedY) {
		this.baseCameraPos = new Vector2(this.baseCameraPos.x+speedX, this.baseCameraPos.y+speedY);
        this.viewport.getCamera().update();
        stayWithinBounds();
    }
}
