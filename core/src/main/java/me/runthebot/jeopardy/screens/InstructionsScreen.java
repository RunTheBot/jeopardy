package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTable;
import me.runthebot.jeopardy.Main;

public class InstructionsScreen extends BaseScreen {
    public InstructionsScreen(Main game) {
        super(game);
        createUI();
    }

    private void createUI() {
        // Title
        VisLabel titleLabel = new VisLabel("How to Play");
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(2.0f);
        mainTable.add(titleLabel).pad(30).row();

        // Instructions text
        VisTable instructionsTable = new VisTable();
        instructionsTable.align(Align.left);

        addInstruction(instructionsTable, "1. Choose a category and dollar value");
        addInstruction(instructionsTable, "2. Read the question carefully");
        addInstruction(instructionsTable, "3. Select your answer from the multiple choices");
        addInstruction(instructionsTable, "4. Correct answers earn you points");
        addInstruction(instructionsTable, "5. Wrong answers don't penalize you");
        addInstruction(instructionsTable, "6. You have 20 seconds to answer each question");
        addInstruction(instructionsTable, "7. Try to get the highest score possible!");

        mainTable.add(instructionsTable).pad(20).row();

        // Back button
        VisTextButton backButton = new VisTextButton("Back to Menu");
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        mainTable.add(backButton).width(200).pad(20).row();

        // Center everything
        mainTable.setFillParent(true);
        mainTable.align(Align.top);
    }

    private void addInstruction(VisTable table, String text) {
        VisLabel label = new VisLabel(text);
        label.setWrap(true);
        table.add(label).width(600).pad(5).row();
    }
}
