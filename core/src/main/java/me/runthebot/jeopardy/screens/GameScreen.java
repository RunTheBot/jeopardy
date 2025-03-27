package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import me.runthebot.jeopardy.Main;
import me.runthebot.jeopardy.model.CategoryType;
import me.runthebot.jeopardy.ui.MultipleChoiceQuestion;

public class GameScreen extends BaseScreen {
    private int score = 0;
    private VisLabel scoreLabel;

    public GameScreen(Main game) {
        super(game);
        createUI();
    }

    private void createUI() {
        // Create a header table for score and menu button
        VisTable headerTable = new VisTable();
        headerTable.defaults().pad(10);

        // Score display
        scoreLabel = new VisLabel("Score: $" + score);
        headerTable.add(scoreLabel).left();

        // Add empty cell to push menu button to the right
        headerTable.add().expandX();

        // Menu button
        VisTextButton menuButton = new VisTextButton("Menu");
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        headerTable.add(menuButton).right();

        // Create game board table
        VisTable gameBoardTable = new VisTable();

        // Categories
        CategoryType[] categories = CategoryType.values();
        int[] values = {200, 400, 600, 800, 1000};

        // Add categories row
        for (CategoryType category : categories) {
            VisLabel categoryLabel = new VisLabel(category.getDisplayName());
            categoryLabel.setAlignment(Align.center);
            gameBoardTable.add(categoryLabel).pad(10).width(120).height(50).fill();
        }
        gameBoardTable.row();

        // Add questions
        for (int value : values) {
            for (int i = 0; i < categories.length; i++) {
                final int categoryIndex = i;
                final int questionValue = value;
                final VisTextButton button = new VisTextButton("$" + value);
                button.setFocusBorderEnabled(false);
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        button.setText("");
                        button.setDisabled(true);
                        CategoryType category = categories[categoryIndex];
                        Gdx.app.log("Jeopardy", "Question clicked: " + category.getDisplayName() + " for $" + questionValue);

                        // Create and show the MultipleChoiceQuestion component
                        MultipleChoiceQuestion questionDialog = new MultipleChoiceQuestion(category.getDisplayName(), questionValue, GameScreen.this);
                        questionDialog.show(stage);
                    }
                });
                float screenWidth = Gdx.graphics.getWidth();
                float screenHeight = Gdx.graphics.getHeight();
                gameBoardTable.add(button).pad(5).width(screenWidth * 0.15f).height(screenHeight * 0.1f).fill();
            }
            gameBoardTable.row();
        }

        // Add tables to main table
        mainTable.add(headerTable).growX().row();
        mainTable.add(gameBoardTable).expand().fill().row();
    }

    public void updateScore(int points) {
        score += points;
        scoreLabel.setText("Score: $" + score);
    }
}
