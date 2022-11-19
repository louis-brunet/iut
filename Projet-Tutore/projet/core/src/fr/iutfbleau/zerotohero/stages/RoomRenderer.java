package fr.iutfbleau.zerotohero.stages;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FillViewport;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.entities.Interactable;
import fr.iutfbleau.zerotohero.room.Debugger;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.utils.CameraHelper;
import fr.iutfbleau.zerotohero.utils.InputAction;

public class RoomRenderer extends Stage {

    private static final float VIEWPORT_WIDTH = 1280f;
    private static final float VIEWPORT_HEIGHT = 720f;

    private Room room;
    private ShapeRenderer shapeRenderer;
    private TiledMapRenderer tiledMapRenderer;
    private CameraHelper cameraHelper;
    private boolean renderCollisions;
    private boolean renderSpawnPoints;
    private boolean renderActorPositions;
    private boolean debug;
    private float timeScale;
    
    private boolean renderFlash;
    private Color flashColor;
    
    private List<ParticleEffect> particles;
    
    private Texture lightTexture;
    private SpriteBatch blendBatch;
    private Texture interactTexture;
    private SpriteBatch batch;
//    private BitmapFont interactFont;

    private RoomOverlay overlay;
    private Interactable closestInteractable;

    /**
     * Creates a Room Renderer
     */
    public RoomRenderer(Player player) {
        this.timeScale = 1f;
        this.room = player.getRoom();
        this.debug = false;
//        this.interactFont = ZeroToHero.getAssetManager().getInteractFont();
//        this.interactFont.setColor(Color.RED);
        this.addNewActors();
        create();
    }

    public void setOverlay(RoomOverlay overlay) {
        this.overlay = overlay;
    }

    private void addNewActors() {
        room.getNewActors().forEach(this::addActor);
        room.getNewActors().clear();
    }

	public void removeActor(Actor actor) {
		this.getRoot().removeActor(actor);
	}

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebug() {
        return this.debug;
    }

    private void create() {
        float w = RoomRenderer.VIEWPORT_WIDTH;
        float h = RoomRenderer.VIEWPORT_HEIGHT;
        this.setViewport(new FillViewport(w, h));
        this.getViewport().apply(true);
        ((OrthographicCamera) this.getCamera()).zoom = 0.75f;

        this.cameraHelper = new CameraHelper(this.getViewport(), ZeroToHero.getPlayer());
        
        
        ZeroToHero.getAssetManager().addAsset("light/light480.png", Texture.class);
        ZeroToHero.getAssetManager().addAsset("gui/Interact.png", Texture.class);
        lightTexture = ZeroToHero.getAssetManager().getAsset("light/light480.png", Texture.class);
        interactTexture = ZeroToHero.getAssetManager().getAsset("gui/Interact.png", Texture.class);
        batch = new SpriteBatch();
        blendBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        tiledMapRenderer = new OrthogonalTiledMapRenderer(room.getTiledMap(), batch);

        renderFlash = false;
        flashColor = Color.WHITE;
        
        particles = new ArrayList<ParticleEffect>();
        
        renderCollisions = false;
        renderSpawnPoints = false;
        renderActorPositions = false;
        System.out.println("F1 : toggle render of Collisions");
        System.out.println("F2 : toggle render of Spawn Points");
        System.out.println("F3 : toggle render of Actor positions");
        System.out.println("DOWN : decrease time scale");
        System.out.println("UP : increase time scale");
        System.out.println("LEFT SHIFT : reset time scale");

//        centerCameraOnWindow();
//        centerCameraOnPlayer();
    }

    @Override
    public void draw() {
        this.getViewport().apply();

        shapeRenderer.setProjectionMatrix(getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(0, 0, room.getWidth(true), room.getHeight(true),
        		room.getBackgroundColors().get(0), room.getBackgroundColors().get(1),
        		room.getBackgroundColors().get(2), room.getBackgroundColors().get(3));
        shapeRenderer.end();
        
//        camera.update();

        batch.begin();
        tiledMapRenderer.setView((OrthographicCamera)this.getCamera());
        room.getLayersToRender().forEach((layer) -> {
        	if (!layer.getName().toLowerCase().contains("shadows") || !layer.getName().toLowerCase().contains("uncolored"))
        		tiledMapRenderer.renderTileLayer((TiledMapTileLayer) layer);
        });
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        if (room.renderWithColor()) {
            Gdx.gl.glBlendEquation(GL20.GL_FUNC_ADD);
            Gdx.gl.glBlendFunc(GL20.GL_ZERO, GL20.GL_SRC_COLOR);
            shapeRenderer.setProjectionMatrix(getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(0, 0, room.getWidth(true), room.getHeight(true),
            		room.getRenderingColors().get(0), room.getRenderingColors().get(1),
            		room.getRenderingColors().get(2), room.getRenderingColors().get(3));
            shapeRenderer.end();
        }

		Camera camera = getViewport().getCamera();
		camera.update();

		super.draw();

        batch.begin();
        tiledMapRenderer.setView((OrthographicCamera)this.getCamera());
        room.getLayersToRender().forEach((layer) -> {
        	if (layer.getName().toLowerCase().contains("shadows") || layer.getName().toLowerCase().contains("uncolored"))
        		tiledMapRenderer.renderTileLayer((TiledMapTileLayer) layer);
        });
        batch.end();

//        batch.setProjectionMatrix(getCamera().combined);
//        batch.begin();
//        batch.setColor(Color.WHITE);
        closestInteractable = room.getClosestInteractable();
        if (this.overlay != null && closestInteractable != null) {
        	Actor closestActor = (Actor) closestInteractable;
            float x = closestActor.getX() + closestActor.getWidth() / 2;
            float y = closestActor.getY() + closestActor.getHeight() / 2f;
        	this.overlay.drawInteractText(closestInteractable, x, y, closestActor.getWidth());
        	this.closestInteractable.drawAdditionalInteractOverlay(this.overlay);
//        	this.interactFont.draw( batch, closestInteractable.getInteractText(),
//                                    closestActor.getX(),
//                                    closestActor.getY()+closestActor.getHeight()/2f,
//                                    closestActor.getWidth(),
//                                    Align.center, false);

//        	batch.draw(interactTexture,
//        			closestActor.getX()+closestActor.getWidth()/2f-interactTexture.getWidth()/2f+(interactTexture.getWidth()-48)/2,
//        			closestActor.getY()+closestActor.getHeight()/2f-interactTexture.getHeight()/2f+(interactTexture.getHeight()-48)/2,
//        			48, 48);
        }
//        batch.end();
        
        blendBatch.setBlendFunctionSeparate(
        		GL20.GL_SRC_ALPHA,
        		GL20.GL_ONE,
        		GL20.GL_SRC_COLOR, 
        		GL20.GL_ONE);
        blendBatch.setProjectionMatrix(getCamera().combined);
        blendBatch.begin();
        room.getLights().forEach((light) -> {
        	blendBatch.setColor(light.b);
        	blendBatch.draw(lightTexture,
        			light.a.getX()-light.c*ZeroToHero.getTileWidth(),
        			light.a.getY()-light.c*ZeroToHero.getTileWidth(),
        			ZeroToHero.getTileWidth()*2*light.c,
        			ZeroToHero.getTileHeight()*2*light.c);
        });
        blendBatch.end();
        
        Gdx.gl.glDisable(GL20.GL_BLEND);
        for (int i = 0; i < particles.size(); i++) {
        	ParticleEffect particleEffect = particles.get(i);
        	particleEffect.update(Gdx.graphics.getDeltaTime());
            batch.begin();
            particleEffect.draw(batch);
            batch.end();
            if (particleEffect.isComplete())
            	particles.remove(i);
        }
        
        if (this.debug) {
            if (renderCollisions)
                Debugger.renderCollisions(room, this.getCamera());
            if (renderSpawnPoints)
                Debugger.renderSpawnPoints(room, this.getCamera());
            if (renderActorPositions)
                Debugger.renderActorPositions(room, this.getCamera());
        }

        Gdx.gl.glDisable(GL20.GL_BLEND);
        if (renderFlash) {
            shapeRenderer.setProjectionMatrix(getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(flashColor);
            shapeRenderer.rect(0, 0, room.getWidth(true), room.getHeight(true));
            shapeRenderer.setColor(Color.WHITE);
            shapeRenderer.end();
            this.renderFlash = false;
        }
    }

    @Override
    public void act(float delta) {
        this.updateTimeScale();
        delta *= this.timeScale;
//        System.out.println((1f / delta) + " fps");
        this.addNewActors();
//        this.removeActors();

        this.room.getWorld().update(delta);
        super.act(delta);
        this.cameraHelper.update(delta);
        float zoomSpeed = 0.1f;

//        if ( ! ZeroToHero.isPaused())
//            gui.act(Gdx.graphics.getDeltaTime());

        if (this.debug) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
                renderCollisions = !renderCollisions;

            if (Gdx.input.isKeyJustPressed(Input.Keys.F2))
                renderSpawnPoints = !renderSpawnPoints;

            if (Gdx.input.isKeyJustPressed(Input.Keys.F3))
                renderActorPositions = !renderActorPositions;

            if (Gdx.input.isKeyPressed(Input.Keys.K))
            	this.cameraHelper.shake(0.5f);
            
            if (Gdx.input.isKeyPressed(Input.Keys.PAGE_UP) && ((OrthographicCamera)this.getCamera()).zoom > 0+zoomSpeed)
                ((OrthographicCamera)this.getCamera()).zoom -= zoomSpeed*5*Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN))
                ((OrthographicCamera)this.getCamera()).zoom += zoomSpeed*5*Gdx.graphics.getDeltaTime();
            if (Gdx.input.isKeyJustPressed(Input.Keys.END))
                ((OrthographicCamera)this.getCamera()).zoom = 1;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.shapeRenderer.dispose();
        this.batch.dispose();
        this.blendBatch.dispose();
        this.interactTexture.dispose();
        this.lightTexture.dispose();
        this.room.dispose();
    }

	public void shake(float duration) {
		this.cameraHelper.shake(duration);
	}

	public void flash(Color color) {
		this.renderFlash = true;
		this.flashColor = color;
	}

	public void addParticles(String file, float f, float g) {
		ParticleEffect particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal(file),Gdx.files.internal("particleEffects"));
		particleEffect.getEmitters().first().setPosition(f, g);
		particleEffect.start();
		
		particles.add(particleEffect);
	}

	public void updateRoom(Player player) {
		this.room = player.getRoom();
	}

	private void updateTimeScale() {
        if (ZeroToHero.getInputMapper().isTriggered(InputAction.RESET_TIME)) {
            this.timeScale = 1f;
        } else if (ZeroToHero.getInputMapper().isTriggered(InputAction.SLOWER)) {
            this.timeScale = Math.max(0f, this.timeScale - 0.1f);
        } else if (ZeroToHero.getInputMapper().isTriggered(InputAction.FASTER)) {
            this.timeScale = Math.min(10f, this.timeScale + 0.1f);
        }
    }
//    @Override
//    public void addActor(Actor actor) {
//        super.addActor(actor);
//        if (actor instanceof TestPlayer)
//            System.out.println("RoomRenderer.addActor(): TestPlayer added.");
//        else if (actor instanceof SimpleGun)
//            System.out.println("RoomRenderer.addActor(): SimpleGun added.");
//    }


    //    /**
//     * Centers the camera on the Window
//     */
//    private void centerCameraOnWindow() {
//        TiledMap tiledMap = map.getTiledMap();
//        camera.translate(-(camera.viewportWidth - (int)tiledMap.getProperties().get("tilewidth")*(int)tiledMap.getProperties().get("width"))/2,
//                -(camera.viewportHeight - (int)tiledMap.getProperties().get("tileheight")*(int)tiledMap.getProperties().get("height"))/2);
//    }
//
//    /**
//     * Centers the camera on the Player
//     */
//    private void centerCameraOnPlayer() {
//        camera.translate(-camera.viewportWidth/2, -camera.viewportHeight/2);
//        camera.translate(this.map.getPlayerSpawnPoint().toVector2());
//    }
}
