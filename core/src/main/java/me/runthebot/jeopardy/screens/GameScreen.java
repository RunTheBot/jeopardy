package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import me.runthebot.jeopardy.Main;
import me.runthebot.jeopardy.model.CategoryType;
import me.runthebot.jeopardy.model.Player;
import me.runthebot.jeopardy.ui.MultipleChoiceQuestion;
import me.runthebot.jeopardy.config.GameConfig;

public class GameScreen extends BaseScreen {
    private Array<Player> players;
    private int currentPlayerIndex;
    private VisTable scoreTable;
    private Array<VisLabel> scoreLabels;
    private int totalQuestions;
    private int answeredQuestions;
    private boolean[][] gameState; // 2D array to track which questions have been answered
    private VisTable gameBoardTable; // Add this field to store reference to game board table

    public GameScreen(Main game, Array<Player> players) {
        super(game);
        this.players = players;
        this.currentPlayerIndex = 0;
        this.scoreLabels = new Array<>();
        this.totalQuestions = CategoryType.values().length * 5; // 5 questions per category
        this.answeredQuestions = 0;
        // Initialize game state array
        this.gameState = new boolean[CategoryType.values().length][5];
        createUI();
    }

    private void createUI() {
        // Create a header table for scores and menu button
        VisTable headerTable = new VisTable();
        headerTable.defaults().pad(10);

        // Score display for all players
        scoreTable = new VisTable();
        updateScoreTable();
        headerTable.add(scoreTable).left().expandX();

        // Dev mode controls
        if (GameConfig.DEV_MODE) {
            VisTable devControls = new VisTable();
            devControls.defaults().pad(5);

            // Add score button
            VisTextButton addScoreButton = new VisTextButton("+$" + GameConfig.QUICK_SCORE_INCREMENT);
            addScoreButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Player currentPlayer = players.get(currentPlayerIndex);
                    currentPlayer.updateScore(GameConfig.QUICK_SCORE_INCREMENT);
                    updateScoreTable();
                }
            });
            devControls.add(addScoreButton);

            // Next turn button
            VisTextButton nextTurnButton = new VisTextButton("Next Turn");
            nextTurnButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentPlayerIndex = (currentPlayerIndex + 1) % players.size;
                    updateScoreTable();
                }
            });
            devControls.add(nextTurnButton);

            // End game button
            VisTextButton endGameButton = new VisTextButton("End Game");
            endGameButton.setColor(Color.RED);
            endGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new WinnerScreen(game, players));
                }
            });
            devControls.add(endGameButton);

            headerTable.add(devControls).right().padRight(20);
        }

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
        gameBoardTable = new VisTable(); // Store reference to game board table

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
                final int questionIndex = value / 200 - 1; // Convert value to 0-based index
                final VisTextButton button = new VisTextButton("$" + value);
                button.setFocusBorderEnabled(false);

                // Set initial disabled state based on game state
                if (gameState[categoryIndex][questionIndex]) {
                    button.setText("");
                    button.setDisabled(true);
                }

                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (!button.isDisabled()) {
                            button.setText("");
                            button.setDisabled(true);
                            gameState[categoryIndex][questionIndex] = true;
                            CategoryType category = categories[categoryIndex];
                            Gdx.app.log("Jeopardy", "Question clicked: " + category.getDisplayName() + " for $" + questionValue);

                            // Create and show the MultipleChoiceQuestion component
                            MultipleChoiceQuestion questionDialog = new MultipleChoiceQuestion(category.getDisplayName(), questionValue, GameScreen.this);
                            questionDialog.show(stage);
                        }
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

    private void updateScoreTable() {
        scoreTable.clear();
        scoreLabels.clear();

        for (int i = 0; i < players.size; i++) {
            Player player = players.get(i);
            String playerText = player.getName() + ": $" + player.getScore();
            if (i == currentPlayerIndex) {
                playerText += " (Current Turn)";
            }
            VisLabel scoreLabel = new VisLabel(playerText);
            scoreLabels.add(scoreLabel);
            scoreTable.add(scoreLabel).padRight(20);
        }
    }

    public void updateScore(int points) {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.updateScore(points);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size;
        answeredQuestions++;
        updateScoreTable();

        // Check if all questions have been answered
        if (answeredQuestions >= totalQuestions) {
            game.setScreen(new WinnerScreen(game, players));
        }
    }

    /**
     * Creates a deep copy of a 2D boolean array
     * @param original The original array to copy
     * @return A deep copy of the array
     */
    private boolean[][] deepCopyArray(boolean[][] original) {
        if (original == null) return null;

        boolean[][] copy = new boolean[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new boolean[original[i].length];
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    /**
     * Loads the game state from a 2D boolean array and updates the UI
     * @param newState The new game state to load
     */
    public void loadGameState(boolean[][] newState) {
        if (newState == null || newState.length != CategoryType.values().length || newState[0].length != 5) {
            Gdx.app.error("GameScreen", "Invalid game state dimensions");
            return;
        }

        // Update the game state
        this.gameState = deepCopyArray(newState);

        // Update answered questions count
        this.answeredQuestions = 0;
        for (boolean[] category : gameState) {
            for (boolean answered : category) {
                if (answered) this.answeredQuestions++;
            }
        }

        // Recreate the game board UI to reflect the new state
        if (gameBoardTable != null) {
            gameBoardTable.remove();
        }
        createUI();
    }

    /**
     * Loads both the game state and player states
     * @param newState The new game state to load
     * @param newPlayers The new player states to load
     * @param newCurrentPlayerIndex The index of the current player
     */
    public void loadFullGameState(boolean[][] newState, Array<Player> newPlayers, int newCurrentPlayerIndex) {
        if (newState == null || newState.length != CategoryType.values().length || newState[0].length != 5) {
            Gdx.app.error("GameScreen", "Invalid game state dimensions");
            return;
        }

        if (newPlayers == null || newPlayers.size == 0) {
            Gdx.app.error("GameScreen", "Invalid players array");
            return;
        }

        if (newCurrentPlayerIndex < 0 || newCurrentPlayerIndex >= newPlayers.size) {
            Gdx.app.error("GameScreen", "Invalid current player index");
            return;
        }

        // Update the game state
        this.gameState = deepCopyArray(newState);

        // Update player states
        this.players = newPlayers;
        this.currentPlayerIndex = newCurrentPlayerIndex;

        // Update answered questions count
        this.answeredQuestions = 0;
        for (boolean[] category : gameState) {
            for (boolean answered : category) {
                if (answered) this.answeredQuestions++;
            }
        }

        // Recreate the UI to reflect the new state
        if (gameBoardTable != null) {
            gameBoardTable.remove();
        }
        createUI();
        updateScoreTable();
    }
}
