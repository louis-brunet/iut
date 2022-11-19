package fr.iutfbleau.zerotohero.actors;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class AnimatedActor extends Actor {

    protected boolean flipX, flipY;
    private final Map<Short, Animation<TextureRegion>> animations;
    private Animation<TextureRegion> currentAnimation;
    private float animationStateTime;
    private boolean isLooping;
    private TextureRegion currentFrame;
    private float offsetX, offsetY;


    public AnimatedActor(String name) {
    	this(name, 0f, 0f);
    }
    
    public AnimatedActor(String name, float offsetX, float offsetY) {
        this.animations = new HashMap<Short, Animation<TextureRegion>>();
        this.setName(name);

        this.init();
    }

    private void init() {
        this.currentAnimation = null;
        this.isLooping = false;
        this.flipX = false;
        this.flipY = false;
        this.resetAnimationStateTime();
        this.setOrigin(0f,0f);
    }

    public void addAnimation(short animationId, Animation<TextureRegion> animation) {
        if (this.animations.containsKey(animationId))
            throw new IllegalArgumentException("Animation "+animationId+" already exists.");
        Objects.requireNonNull(animation);

        this.animations.put(animationId, animation);
    }

    public void addAnimation(short animationId, Texture texture, int frameRows,
                             int frameColumns, float frameDuration) {
        Objects.requireNonNull(texture);

        TextureRegion[][] temp = TextureRegion.split(texture,
                                                     texture.getWidth() /
                                                     frameColumns,
                                                     texture.getHeight() / frameRows);
        TextureRegion[] frames = new TextureRegion[frameRows * frameColumns];

        int index = 0;
        for (int row = 0; row < frameRows; row++)
            for (int col = 0; col < frameColumns; col++)
                 frames[index++] = temp[row][col];

        Animation<TextureRegion> animation = new Animation<>(frameDuration,
                                                             frames);

        this.addAnimation(animationId, animation);
    }

    /**
     * Sets current animation if it exists and if current animation isn't given animation.
     * @param animationId
     * @param isLooping
     */
    public void setCurrentAnimation(short animationId, boolean isLooping) {

        Animation<TextureRegion> animation = this.animations.get(animationId);
        if (animation == null)
            throw new IllegalArgumentException("Animation "+animationId+" doesn't exist.");

        if (this.currentAnimation == null || ! this.currentAnimation.equals(animation)) {
            this.currentAnimation = animation;
            this.isLooping = isLooping;
            this.resetAnimationStateTime();
        }
    }

    public Animation<TextureRegion> getCurrentAnimation() {
        return currentAnimation;
    }

    public void removeAnimations() {
        this.animations.clear();
        this.init();
    }

    public void resetAnimationStateTime() {
        this.animationStateTime = 0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
//        Objects.requireNonNull(this.currentAnimation, "Need to set current animation.");

        this.animationStateTime += delta;
//        this.currentFrame = this.currentAnimation.getKeyFrame(this.animationStateTime,
//                                                              this.isLooping);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (this.currentAnimation == null)
            return;

        this.currentFrame = this.currentAnimation.getKeyFrame(this.animationStateTime,
                                                              this.isLooping);

        batch.draw(this.currentFrame.getTexture(),
                   this.getX()+this.offsetX, this.getY()+this.offsetY,
                   this.getOriginX(), this.getOriginY(),
                   this.currentFrame.getRegionWidth(),
                   this.currentFrame.getRegionHeight(),
                   this.getScaleX(), this.getScaleY(),
                   this.getRotation(),
                   this.currentFrame.getRegionX(), this.currentFrame.getRegionY(),
                   this.currentFrame.getRegionWidth(),
                   this.currentFrame.getRegionHeight(),
                   this.flipX, this.flipY);
    }
    
    public float getOffsetX() {
		return offsetX;
	}
    
    public float getOffsetY() {
		return offsetY;
	}

    public boolean isFlipX() {
        return flipX;
    }

    public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}
    
    public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

    public void setFlipX(boolean flipX) {
        this.flipX = flipX;
    }
}
