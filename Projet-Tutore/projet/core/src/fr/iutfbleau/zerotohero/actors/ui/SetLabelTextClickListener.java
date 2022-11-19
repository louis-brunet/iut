package fr.iutfbleau.zerotohero.actors.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class SetLabelTextClickListener extends ClickListener {
    private final String text;
    private final Label label;

    public SetLabelTextClickListener(String text, Label label) {
        this.text  = text;
        this.label = label;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        this.label.setText(this.text);
    }
}
