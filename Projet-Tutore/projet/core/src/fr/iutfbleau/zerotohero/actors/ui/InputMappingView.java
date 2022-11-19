package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import fr.iutfbleau.zerotohero.ZeroToHero;
import fr.iutfbleau.zerotohero.game.GameSettings;
import fr.iutfbleau.zerotohero.utils.InputAction;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InputMappingView extends Table {
    private static final Label.LabelStyle LABEL_STYLE = new Label.LabelStyle(ZeroToHero.getAssetManager().getDefaultFont(), Color.WHITE);
    private static final Label.LabelStyle HEADING_STYLE = new Label.LabelStyle(ZeroToHero.getAssetManager().getBigFont(), Color.WHITE);
//    private static final Drawable TITLE_BACKGROUND = ZeroToHero.getAssetManager().createColorDrawable(new Color(0x3e3a4bff));
    private static final String ADD_BUTTON_TEXT = "Add";
    private static final String REMOVE_BUTTON_TEXT = "Clear";
    private static final String TITLE_TEXT = "Input bindings";
    private static final String MODIFICATION_PROMPT = "Press a key or mouse button to bind";
    private static final float REMOVE_BUTTON_WIDTH = 100f;
    private static final float ADD_BUTTON_WIDTH = 80f;
//    private static final Drawable BINDINGS_BACKGROUND = ZeroToHero.getAssetManager()
//                                                                  .createColorDrawable(new Color(0x3e3a4bff));

    private static String mouseButtonToString(int button) {
        switch (button) {
            case Input.Buttons.BACK:
                return "Mouse (Back)";
            case Input.Buttons.FORWARD:
                return "Mouse (Forward)";
            case Input.Buttons.LEFT:
                return "Mouse (Left)";
            case Input.Buttons.RIGHT:
                return "Mouse (Right)";
            case Input.Buttons.MIDDLE:
                return "Mouse (Middle)";

            default:
                throw new IllegalArgumentException("Button not recognized : "+button);
        }
    }


    private final InputAction[] actions;
    private final GameSettings settings;
    private final Map<InputAction, Label> bindingsLabels;
    private final Map<InputAction, Button> addButtons;
    private final List<GameButton> buttons;

    public InputMappingView(InputAction[] actions, GameSettings gs, Drawable headingBackground) {
        this.actions = actions;
        this.settings = gs;
        this.bindingsLabels = new EnumMap<InputAction, Label>(InputAction.class);
        this.addButtons     = new EnumMap<InputAction, Button>(InputAction.class);
        this.buttons        = new LinkedList<>();

        this.initTitle(headingBackground);
        this.initContent();
    }

    private void initTitle(Drawable headingBackground) {
        Label titleLabel = new Label(InputMappingView.TITLE_TEXT, InputMappingView.HEADING_STYLE);
        titleLabel.setAlignment(Align.center);

        Table titleContainer = new Table();
        titleContainer.setBackground(headingBackground);
        titleContainer.add(titleLabel).expand().fill().pad(8f);
//        titleContainer.setDebug(true);
        this.add(titleContainer).expand().fill().colspan(4);
    }

    private void initContent() {


        for (InputAction action: this.actions) {
            this.row();

            Label nameLabel = new Label(this.getActionName(action) + ": ",
                                        InputMappingView.LABEL_STYLE);
            Label bindingsLabel = new Label(this.getInputBindings(action),
                                            InputMappingView.LABEL_STYLE);
            this.bindingsLabels.put(action, bindingsLabel);

//            Table bindingsContainer = new Table();
//            bindingsContainer.add(bindingsLabel).left().expand().fill().pad(8f);
//            bindingsContainer.setBackground(InputMappingView.BINDINGS_BACKGROUND);
//            bindingsContainer.setDebug(true, true);

            GameButton removeBtn =
                    new GameButton(InputMappingView.REMOVE_BUTTON_TEXT,
                                   new RemoveInputBindingsListener(action, this.settings, this),
                                   InputMappingView.REMOVE_BUTTON_WIDTH);
            this.buttons.add(removeBtn);

            GameButton addBtn = new GameButton(
                    InputMappingView.ADD_BUTTON_TEXT,
                    new StartAddingInputMappingListener(this, action, this.settings));
            addBtn.setVisible(this.settings.canAddInputMapping(action));
            this.buttons.add(addBtn);
            this.addButtons.put(action, addBtn);



            this.add(nameLabel)
                .right()
                .pad(8);
            this.add(bindingsLabel)
                .left()
                .pad(8)
                .width(440f);
            this.add(removeBtn)
                .pad(GameButton.DEFAULT_PADDING);
            this.add(addBtn)
                .pad(GameButton.DEFAULT_PADDING)
                .width(InputMappingView.ADD_BUTTON_WIDTH);
        }
    }

    public void updateBindingsLabel(InputAction a) {
        this.bindingsLabels.get(a)
                           .setText(this.getInputBindings(a));
    }

    public void updateAllBindingsLabels() {
        for (Map.Entry<InputAction, Label> entry : this.bindingsLabels.entrySet()) {
            entry.getValue().setText(this.getInputBindings(entry.getKey()));
        }
    }

    // null to set buttons visible again
    public void showAddInputMapping(InputAction action) {
        if (action == null) {
            this.setButtonsVisible(true);
        } else {
            this.setButtonsVisible(false);
            this.bindingsLabels.get(action).setText(InputMappingView.MODIFICATION_PROMPT);
        }
    }

    public void setButtonsVisible(boolean isVisible) {
        for (GameButton btn : this.buttons) {
            btn.setVisible(isVisible);
        }
        if (isVisible) {
            for (Map.Entry<InputAction, Button> entry : this.addButtons.entrySet()) {
                if ( ! this.settings.canAddInputMapping(entry.getKey())) {
                    entry.getValue().setVisible(false);
                }
            }
        }
    }

    private String getActionName(InputAction action) {
        String s = action.name()
                         .toLowerCase()
                         .replace('_', ' ');
        String[] chars = s.split("");
        chars[0] = chars[0].toUpperCase();

        return String.join("", chars);
    }

    private CharSequence getInputBindings(InputAction action) {
        StringBuilder res = new StringBuilder();
        int[] keys = this.settings.getInputKeys(action);
        int[] buttons = this.settings.getInputMouseButtons(action);

        for (int key: keys)
            res.append(Input.Keys.toString(key))
               .append(", ");
        for (int button: buttons)
            res.append(InputMappingView.mouseButtonToString(button))
               .append(", ");
        if (res.length() >= 2)
            res.delete(res.length() - 2, res.length());
        return res;
    }
}
