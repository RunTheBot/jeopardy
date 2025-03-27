package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import me.runthebot.jeopardy.Main;

public class MainMenuScreen extends BaseScreen {
    public MainMenuScreen(Main game) {
        super(game);
        createUI();
    }

    private void createUI() {
        // Title
        VisLabel titleLabel = new VisLabel("JEOPARDY!");
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(3.0f);
        mainTable.add(titleLabel).pad(50).row();

        // Play button
        VisTextButton playButton = new VisTextButton("Play Game");
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new GameScreen(game));
            }
        });
        mainTable.add(playButton).width(200).pad(10).row();

        // Instructions button
        VisTextButton instructionsButton = new VisTextButton("Instructions");
        instructionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InstructionsScreen(game));
            }
        });
        mainTable.add(instructionsButton).width(200).pad(10).row();

        // Exit button
        VisTextButton exitButton = new VisTextButton("Exit");
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        mainTable.add(exitButton).width(200).pad(10).row();

        // Center everything
        mainTable.setFillParent(true);
        mainTable.align(Align.center);
    }
}
