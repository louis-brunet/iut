package fr.iutfbleau.zerotohero.stages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.actors.Stat;
import fr.iutfbleau.zerotohero.actors.ui.*;
import fr.iutfbleau.zerotohero.room.Room;


/**
 * Utilise com.badlogic.gdx.scenes.scene2d.ui
 */
public class GameplayUI extends Stage {

    // player Stats used in UI
    public static final List<Stat> PLAYER_STATS = Arrays.asList( Stat.HEALTH,
                                                                 Stat.MAX_HEARTS,
                                                                 Stat.MAX_HEALTH,
                                                                 Stat.SHIELDS,
                                                                 Stat.KEYS,
                                                                 Stat.BOMBS,
                                                                 Stat.GOLD );

    private static final float DEFAULT_MESSAGE_UPTIME = 2f, MESSAGE_FADE_OUT_TIME = 2f;
    private static final String PAUSE_TEXT = "Pause";
    private static final Label.LabelStyle TITLE_STYLE = new Label.LabelStyle(ZeroToHero.getAssetManager().getBiggerFont(), Color.WHITE);
    private static final Label.LabelStyle HEADING_STYLE = new Label.LabelStyle(ZeroToHero.getAssetManager().getBigFont(), Color.WHITE);
    private static final Label.LabelStyle LABEL_STYLE = new Label.LabelStyle(ZeroToHero.getAssetManager().getDefaultFont(), Color.WHITE);
    private static final Color ITEM_ROW_BACKGROUND = null;// new Color(1f,1f,1f, 1f);
    private static final Drawable MINIMAP_BACKGROUND =  ZeroToHero.getAssetManager().createColorDrawable(0,0,0,0.1f);


    private final ZeroToHero game;
    private final Set<Label> toRemove;
    private final Map<Label, Float> uptimes;

    // Widgets
    private final Stack root;
    private final Table hud;
    private final SettingsPanel settingsPanel;
    private final PauseMenu pauseMenu;
    private final ItemIconsRow itemIcons;
    private final Table playerInfo, eventInfo, gameButtonsTable;
    private final Minimap minimap;
    private final GameOverPanel gameOverPanel;

    public GameplayUI(ZeroToHero game, String seed) {
        super(new ScreenViewport());

        this.game = game;
        this.uptimes = new HashMap<Label, Float>();
        this.toRemove = new HashSet<Label>();
        this.root = new Stack();
        this.root.setFillParent(true);

        // player info
        this.playerInfo = new Table();
        this.initPlayerTable();

        // event info
        this.eventInfo = new Table();
        this.initEventTable();

        // side buttons when not paused
        this.gameButtonsTable = new Table();
        this.initGameButtons();

        // player's held items
        this.itemIcons = new ItemIconsRow(40f, 8f, GameplayUI.ITEM_ROW_BACKGROUND);
        this.initPlayerItems();

        this.minimap = new Minimap(GameplayUI.MINIMAP_BACKGROUND);

        // hud layout
        this.hud = new Table();
        this.initHud();


        // settings
        this.settingsPanel = new SettingsPanel(ZeroToHero.getSettings());
        this.initSettingsPanel();

        // pause menu
        this.pauseMenu = new PauseMenu(GameplayUI.TITLE_STYLE,
                                       GameplayUI.HEADING_STYLE,
                                       GameplayUI.LABEL_STYLE,
                                       this.settingsPanel, this.game, seed);
        this.initPauseMenu();

        // game over menu
        this.gameOverPanel = new GameOverPanel(game, seed,
                                               GameplayUI.TITLE_STYLE, GameplayUI.LABEL_STYLE,
                                               ZeroToHero.getAssetManager().createColorDrawable(0,0,0,0.8f));
        this.initGameOverPanel();

        // added last to display on top of other widgets
        this.root.add(this.settingsPanel);

        this.addActor(this.root);
    }

    public void addMessage(String message) {
    	this.addMessage(message, GameplayUI.LABEL_STYLE, GameplayUI.DEFAULT_MESSAGE_UPTIME);
    }

    public void addMessage(String message, Color color) {
    	this.addMessage(message, color, GameplayUI.DEFAULT_MESSAGE_UPTIME);
    }

    public void addMessage(String message, Color color, float uptime) {
        this.addMessage(message, new Label.LabelStyle(ZeroToHero.getAssetManager().getAsset("fonts/arial_narrow_7.ttf", BitmapFont.class), color), uptime);
    }

    private void addMessage(String message, Label.LabelStyle style, float uptime) {
        Label label = new Label(message, style);
        this.uptimes.put(label, uptime);

        this.eventInfo.row();
        this.eventInfo.add(label).right();
        
        if (this.eventInfo.getCells().size > 5) {
        	this.eventInfo.removeActor(this.eventInfo.getCells().get(0).getActor());
        	this.eventInfo.getCells().removeIndex(0);
        }
    }

    private void initHud() {
        Value sideColumnsWidth = new Value() {
            @Override
            public float get(Actor context) {
                return Math.max(minimap.getPrefWidth(), playerInfo.getPrefWidth());
            }
        };

        this.hud.add(this.playerInfo).top().left().width(sideColumnsWidth);
        this.hud.add(this.itemIcons).top().expand();
        this.hud.add(this.gameButtonsTable).top().right().width(sideColumnsWidth);
        this.hud.row();
        this.hud.add(this.minimap).bottom().left();
        this.hud.add((Actor) null);
        this.hud.add(this.eventInfo).bottom().right();

        this.root.add(this.hud);
    }

    private void initEventTable() {
        eventInfo.align(Align.bottomLeft);
        eventInfo.moveBy(ZeroToHero.getTileWidth()/3, ZeroToHero.getTileHeight()/3);
    }
    
    private void initPlayerTable() {
        playerInfo.align(Align.topLeft);
        playerInfo.moveBy(ZeroToHero.getTileWidth()/3, -ZeroToHero.getTileHeight()/3);
        
        ZeroToHero.getAssetManager().addAsset("gui/keysIcon.png", Texture.class);
        ZeroToHero.getAssetManager().addAsset("gui/bombsIcon.png", Texture.class);
        ZeroToHero.getAssetManager().addAsset("gui/moneyIcon.png", Texture.class);
        ZeroToHero.getAssetManager().addAsset("gui/heartIcon.png", Texture.class);
        ZeroToHero.getAssetManager().addAsset("gui/emptyHeartIcon.png", Texture.class);
        ZeroToHero.getAssetManager().addAsset("gui/blueHeartIcon.png", Texture.class);
        ZeroToHero.getAssetManager().addAsset("gui/shieldIcon.png", Texture.class);
        
        refreshPlayerInfo();
    }

    private void initPlayerItems() {
        this.itemIcons.align(Align.bottom);
    }

    private void initPauseMenu() {
        this.pauseMenu.align(Align.top);
        this.pauseMenu.setVisible(false);

        this.root.add(this.pauseMenu);
    }

    private void initGameOverPanel() {
        this.gameOverPanel.setVisible(false);
        this.root.add(this.gameOverPanel);
    }

    private void initGameButtons() {
        this.gameButtonsTable.align(Align.topRight);

        GameButton pauseButton = new GameButton(GameplayUI.PAUSE_TEXT, new TogglePauseListener());
        pauseButton.align(Align.bottomRight);
        this.gameButtonsTable.add(pauseButton).pad(GameButton.DEFAULT_PADDING);
    }

    private void initSettingsPanel() {
        this.settingsPanel.setVisible(false);
        this.settingsPanel.setFillParent(true);
    }

    public void setPauseMenuVisible(boolean isVisible) {
        this.pauseMenu.setVisible(isVisible);
        this.gameButtonsTable.setVisible( ! isVisible);
    }

    public void showGameOver() {
        this.gameOverPanel.setVisible(true);
    }
    
    public void refreshPlayerInfo() {
    	playerInfo.clear();
    	int playerHealth = ZeroToHero.getPlayer().getHealth(),
    		playerMaxHealth = ZeroToHero.getPlayer().getMaxHearts(),
    		hpMax = Math.max(playerHealth, playerMaxHealth),
    		playerShields = ZeroToHero.getPlayer().getShields();
        for (int hp = 0; hp < hpMax; hp++) {
        	if (hp < playerMaxHealth) {
        		if (hp < playerHealth)
        			playerInfo.add(new Image(ZeroToHero.getAssetManager().getAsset("gui/heartIcon.png", Texture.class)));
        		else
        			playerInfo.add(new Image(ZeroToHero.getAssetManager().getAsset("gui/emptyHeartIcon.png", Texture.class)));
        	}
        	if (hp >= playerMaxHealth) {
        		playerInfo.add(new Image(ZeroToHero.getAssetManager().getAsset("gui/blueHeartIcon.png", Texture.class)));
        	}
        }
        playerInfo.row();
        
        for (int shields = 0; shields < playerShields; shields++) {
        	playerInfo.add(new Image(ZeroToHero.getAssetManager().getAsset("gui/shieldIcon.png", Texture.class)));
        }
        playerInfo.row();
        
        Label amountOfKeys = new Label(Integer.toString(ZeroToHero.getPlayer().getAmountOfKeys()), GameplayUI.LABEL_STYLE),
        		amountOfBombs = new Label(Integer.toString(ZeroToHero.getPlayer().getAmountOfBombs()), GameplayUI.LABEL_STYLE),
        		money = new Label(Integer.toString(ZeroToHero.getPlayer().getMoney()), GameplayUI.LABEL_STYLE);
        Image keysIcon = new Image(ZeroToHero.getAssetManager().getAsset("gui/keysIcon.png", Texture.class)),
        		bombsIcon = new Image(ZeroToHero.getAssetManager().getAsset("gui/bombsIcon.png", Texture.class)),
        		moneyIcon = new Image(ZeroToHero.getAssetManager().getAsset("gui/moneyIcon.png", Texture.class));
        this.playerInfo.add(keysIcon, amountOfKeys);
        this.playerInfo.getCell(amountOfKeys).align(Align.left);
        playerInfo.row();
        this.playerInfo.add(bombsIcon, amountOfBombs);
        this.playerInfo.getCell(amountOfBombs).align(Align.left);
        playerInfo.row();
        this.playerInfo.add(moneyIcon, money);
        this.playerInfo.getCell(money).align(Align.left);
    }

    public void updatePlayerItems() {
        this.itemIcons.update();
    }

    public void updateMap(Room[][] rooms) {
        this.minimap.setRooms(rooms);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        
        Label label;
        float uptime;
        for(Map.Entry<Label, Float> entry : this.uptimes.entrySet() ) {
            label = entry.getKey();
            uptime = entry.getValue() - delta;

            if (uptime <= - GameplayUI.MESSAGE_FADE_OUT_TIME) {
                this.toRemove.add(label);
            } else if (uptime <= 0f) {
            	label.setColor(label.getColor().sub(0,0,0,1/(MESSAGE_FADE_OUT_TIME/delta)));
            }
            this.uptimes.put(label, uptime);
        }

        for(Label l: this.toRemove) {
            this.eventInfo.removeActor(l);
            this.uptimes.remove(l);
        }
        this.toRemove.clear();
    }
}
