//package fr.iutfbleau.zerotohero.entities;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Animation;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//
//public abstract class TexturedActor extends Actor {
//	private Sprite sprite;
//
//	private static final int FRAME_COLS = 4, FRAME_ROWS = 1;
//	Animation<TextureRegion> runAnimation;
////	Texture runSheet;
////	SpriteBatch spriteBatch;
//	float stateTime;
//	boolean animated;
//
//	public TexturedActor(String textureFilePath, boolean animated) {
//		this.animated = animated;
//		if (animated) {
//			Texture runSheet = new Texture(textureFilePath);
//
//			TextureRegion[][] tmp = TextureRegion.split(runSheet,
//					runSheet.getWidth() / FRAME_COLS,
//					runSheet.getHeight() / FRAME_ROWS);
//
//			TextureRegion[] runFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
//			int index = 0;
//			for (int i = 0; i < FRAME_ROWS; i++) {
//				for (int j = 0; j < FRAME_COLS; j++) {
//					runFrames[index++] = tmp[i][j];
//				}
//			}
//
//			runAnimation = new Animation<TextureRegion>(0.5f, runFrames);
//
////			spriteBatch = new SpriteBatch();
//			stateTime = 0f;
//		} else {
//			try {
//				this.sprite = new Sprite(new Texture(textureFilePath));
//				sprite.setOrigin(sprite.getWidth() / 2f,
//								 sprite.getHeight() / 2f);
//			} catch (Exception e) {
//				this.sprite = null;
//			}
//		}
//	}
//
//	@Override
//	public void draw(Batch batch, float parentAlpha) {
//		if (animated) {
//			stateTime += Gdx.graphics.getDeltaTime();
//			TextureRegion currentFrame = runAnimation.getKeyFrame(stateTime, true);
//			batch.draw(currentFrame, getX(), getY());
//		} else {
//			if (sprite != null)
//				batch.draw(sprite, getX(), getY(), sprite.getOriginX(), sprite.getOriginY(),
//						   sprite.getWidth(), sprite.getHeight(),
//						   sprite.getScaleX(), sprite.getScaleY(), sprite.getRotation());
//		}
//	}
//
//	public Sprite getSprite() {
//		return sprite;
//	}
//}
