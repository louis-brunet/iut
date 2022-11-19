package fr.iutfbleau.zerotohero.screens;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.PlayerAI;
import fr.iutfbleau.zerotohero.actors.Player;
import fr.iutfbleau.zerotohero.utils.Coordinates;
import fr.iutfbleau.zerotohero.physics.AxisAlignedBoundingBox;
import fr.iutfbleau.zerotohero.physics.Body;
import fr.iutfbleau.zerotohero.physics.BoundingShape;
import fr.iutfbleau.zerotohero.registries.Items;
import fr.iutfbleau.zerotohero.registries.Weapons;
import fr.iutfbleau.zerotohero.room.Level;
import fr.iutfbleau.zerotohero.room.Room;
import fr.iutfbleau.zerotohero.stages.GameplayUI;
import fr.iutfbleau.zerotohero.stages.RoomOverlay;
import fr.iutfbleau.zerotohero.stages.RoomRenderer;
import fr.iutfbleau.zerotohero.utils.InputAction;
import fr.iutfbleau.zerotohero.utils.Seed;

public class GameplayScreen implements Screen {
    private static final String BGM_FILE_NAME = "bgm.mp3";
//    private final Random random = MathUtils.random;
    private Random seededRandom;
    private Seed seed;

    private ZeroToHero game;
    private RoomRenderer roomRenderer;
    private RoomOverlay roomOverlay;
    private GameplayUI ui;

    private Player player;
    private Level level;
    private static int DEFAULT_WIDTH = 11, DEFAULT_HEIGHT = 6;
//    private Room firstRoom, leftRoom, rightRoom;
//    private Connection leftConnection, rightConnection;
    private boolean isPaused;
//    private Music music;

    /**
     * @param seed Graine de génération aléatoire<br/>
     * Chaine de caractères de longueur 12 maximum<br/>
     * Si plus courte, vide, ou nulle, elle sera générée ou complétée aléatoirement
     */
    public GameplayScreen(ZeroToHero game, String seed) {
        this.game = game;
        
        if (seed == null) {
        	this.seed = new Seed();
        } else {
        	this.seed = new Seed(seed);
        }
		System.out.println(this.seed.getSeedText());
        seededRandom = new Random(this.seed.getSeedValue());
        /*level = new Level(this, seededRandom, DEFAULT_WIDTH, DEFAULT_HEIGHT, 15, 30,
        		new Color(45/255f,132/255f,126/255f,1),
        		new Color(45/255f,132/255f,126/255f,1),
        		new Color(102/255f,186/255f,179/255f,1),
        		new Color(102/255f,186/255f,179/255f,1));*/
        level = new Level(this, seededRandom, DEFAULT_WIDTH, DEFAULT_HEIGHT, 15, 30,
        		new Color(152/255f,127/255f,3/255f,1),
        		new Color(152/255f,127/255f,3/255f,1),
        		new Color(201/255f,171/255f,61/255f,1),
        		new Color(201/255f,171/255f,61/255f,1));
        
//        firstRoom = new Room("testmaps/TestMapBigWhite.tmx", Room.RoomSize.NORMAL,
//                             Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
//        firstRoom = new Room(this, "rooms/dungeonNormal/normal/normal1.tmx", Room.RoomSize.NORMAL, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
//        firstRoom.setRenderingColor(
//        		new Color(0,0.5f,1,1),
//        		new Color(0,0.5f,1,1),
//        		new Color(0.25f,0.75f,1,1),
//        		new Color(0.25f,0.75f,1,1));
//        leftRoom = new Room(this, "rooms/dungeonNormal/tall/tall1.tmx", Room.RoomSize.TALL, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
//        leftRoom.setRenderingColor(
//        		new Color(1,0,0,1),
//        		new Color(1,0,0,1),
//        		new Color(1,0.5f,0.5f,1),
//        		new Color(1,0.5f,0.5f,1));
//        rightRoom = new Room(this, "rooms/dungeonNormal/normal/normal1.tmx", Room.RoomSize.NORMAL, Color.BLACK, Color.BLACK, Color.GRAY, Color.GRAY);
//        rightRoom.setRenderingColor(
//        		new Color(0,1,0,1),
//        		new Color(0,1,0,1),
//        		new Color(0.5f,1,0.5f,1),
//        		new Color(0.5f,1,0.5f,1));
//        leftConnection = new Connection(leftRoom, ConnectionPosition.BOTTOM_RIGHT,
//                                        firstRoom, ConnectionPosition.TOP_LEFT);
//        rightConnection = new Connection(rightRoom, ConnectionPosition.TOP_LEFT,
//                                         firstRoom, ConnectionPosition.TOP_RIGHT);
        
//        firstRoom = new Room("testmaps/TestMapBigWhite.tmx", Color.DARK_GRAY, Color.DARK_GRAY, Color.LIGHT_GRAY, Color.LIGHT_GRAY);

        createPlayer();
        ZeroToHero.setPlayer(player);

        roomRenderer = new RoomRenderer(player);
        roomRenderer.setDebug(true);

        this.roomOverlay = new RoomOverlay(this.roomRenderer.getViewport());
        this.roomRenderer.setOverlay(this.roomOverlay);
        
        this.ui = new GameplayUI(this.game, this.seed.getSeedText());
        this.ui.updateMap(level.getRooms());
        Gdx.input.setInputProcessor(this.ui);

        ZeroToHero.getInputMapper().setCamera(roomRenderer.getCamera());
        
//        updatePlayer();

        this.isPaused = false;

//        this.music = ZeroToHero.getAssetManager().getMusic("bgm.mp3");
//        this.music.setVolume(ZeroToHero.getSettings().getMusicVolume());
//        this.music.setLooping(true);

    }
    
    public Seed getSeed() {
		return seed;
	}
    
//    public Random getRandom() {
//		return random;
//	}
    
    public Random getSeededRandom() {
		return seededRandom;
	}

	public void togglePause() {
        this.isPaused = !this.isPaused;
        this.ui.setPauseMenuVisible(this.isPaused);
    }

    @Override
    public void show() {
//        System.out.println("GameplayScreen.show()");
        ZeroToHero.getAudioPlayer().playMusic(GameplayScreen.BGM_FILE_NAME, true);
    }

    @Override
    public void render(float delta) {
        if (ZeroToHero.getInputMapper().isTriggered(InputAction.PAUSE)) {
            this.togglePause();
        }

        if ( ! this.isPaused ) {
            this.roomRenderer.act(delta);
        }
        this.ui.act(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glEnable(GL20.GL_BLEND);

        this.roomRenderer.draw();
        this.ui.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.roomRenderer.getViewport().update(width, height);
        this.ui.getViewport().update(width, height);
    }

    @Override
    public void pause() {
//        System.out.println("GameplayScreen pause");
    }

    @Override
    public void resume() {
//        System.out.println("GameplayScreen resume");

    }

    @Override
    public void hide() {
//        System.out.println("GameplayScreen hide");
        ZeroToHero.getAudioPlayer().stopMusic(GameplayScreen.BGM_FILE_NAME);
    }

    @Override
    public void dispose() {
        this.roomRenderer.dispose();
        this.ui.dispose();
    }

    public GameplayUI getUI() {
        return this.ui;
    }

    private void createPlayer() {
        float playerWidth = 24f;
        float playerHeight = 84f;
        
        Room spawnRoom = level.getRooms()[Math.round(DEFAULT_WIDTH/2f)-1][Math.round(DEFAULT_HEIGHT/2f)-1];
        
        BoundingShape shape = new AxisAlignedBoundingBox(
                new Coordinates(spawnRoom.getPlayerSpawnPoint().getX() ,
                                spawnRoom.getPlayerSpawnPoint().getY() + playerHeight/2),
                playerWidth/2, playerHeight/2);
        Body body = new Body(
                Body.Type.PLAYER,
                true,
                shape,
                new Coordinates(spawnRoom.getPlayerSpawnPoint().getX(),
                                spawnRoom.getPlayerSpawnPoint().getY() + playerHeight/2));

//        spawnRoom.getWorld().addBody(body);


        player = new Player(spawnRoom, ZeroToHero.getInputMapper(), body, new PlayerAI());
        player.setInvertColor(true);
        player.setSize(playerWidth, playerHeight);
        player.setOffsetX(-playerWidth*1.5f);

        String runAnimationPath = "testplayer/player/run_spritesheet.png";
		ZeroToHero.getAssetManager().addAsset(runAnimationPath, Texture.class);
		Texture runAnimation = ZeroToHero.getAssetManager().getAsset(runAnimationPath, Texture.class);
        player.addAnimation(Player.RUN_ANIMATION_ID,
                            runAnimation,
                            1, 4, 0.05f);

        String jumpAnimationPath = "testplayer/player/jump_spritesheet.png";
		ZeroToHero.getAssetManager().addAsset(jumpAnimationPath, Texture.class);
		Texture jumpAnimation = ZeroToHero.getAssetManager().getAsset(jumpAnimationPath, Texture.class);
		player.addAnimation(Player.JUMP_ANIMATION_ID,
                            jumpAnimation,
                            1, 3, 0.05f);

        String idleAnimationPath = "testplayer/player/idle.png";
		ZeroToHero.getAssetManager().addAsset(idleAnimationPath, Texture.class);
		Texture idleAnimation = ZeroToHero.getAssetManager().getAsset(idleAnimationPath, Texture.class);
		player.addAnimation(Player.IDLE_ANIMATION_ID,
                            idleAnimation,
                            1, 1, 1f);
        player.setCurrentAnimation(Player.IDLE_ANIMATION_ID, false);

        player.setScale(0.25f*1.5f);
        spawnRoom.addActor(player);

        this.addTestWeapon();

    }

    private void addTestWeapon() {
        Room spawnRoom = level.getRooms()[Math.round(DEFAULT_WIDTH/2f)-1][Math.round(DEFAULT_HEIGHT/2f)-1];
        
        spawnRoom.spawnWeapon(Weapons.SMG,
        		player.getBody().getPosition().getX(),
        		player.getBody().getPosition().getY() + 120f);
        spawnRoom.spawnWeapon(Weapons.SIMPLE_BOW,
        		player.getBody().getPosition().getX(),
        		player.getBody().getPosition().getY() + 200f);
        spawnRoom.spawnItem(Items.FAST_PROJECTILES,
                            player.getBody().getPosition().getX() + 50f,
                            player.getBody().getPosition().getY() + 250f);
        spawnRoom.spawnItem(Items.GLUE,
                            player.getBody().getPosition().getX() - 50f,
                            player.getBody().getPosition().getY() + 250f);

    }
    
    public Level getLevel() {
		return level;
	}
    
    public RoomRenderer getRoomRenderer() {
		return roomRenderer;
	}

	public void onPlayerDeath() {
        this.isPaused = true;
        this.ui.showGameOver();
    }
}
