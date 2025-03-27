/**
 * The main game screen where the Jeopardy game is played.
 * This screen handles:
 * - Displaying the game board with categories and questions
 * - Managing player turns and scores
 * - Showing questions and handling answers
 * - Saving and loading game states
 * - Development mode controls (when enabled)
 */
package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTextArea;
import me.runthebot.jeopardy.Main;
import me.runthebot.jeopardy.model.CategoryType;
import me.runthebot.jeopardy.model.Player;
import me.runthebot.jeopardy.model.GameState;
import me.runthebot.jeopardy.ui.MultipleChoiceQuestion;
import me.runthebot.jeopardy.config.GameConfig;

public class GameScreen extends BaseScreen {
    // List of players in the game
    private Array<Player> players;
    // Index of the current player's turn
    private int currentPlayerIndex;
    // Table displaying player scores
    private VisTable scoreTable;
    // Labels for each player's score
    private Array<VisLabel> scoreLabels;
    // Total number of questions in the game
    private int totalQuestions;
    // Number of questions that have been answered
    private int answeredQuestions;
    // 2D array tracking which questions have been answered
    private boolean[][] gameState;
    // Table containing the game board UI
    private VisTable gameBoardTable;

    /**
     * Creates a new game screen with the specified players.
     * @param game The main game instance
     * @param players The list of players participating in the game
     */
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

    /**
     * Creates and sets up all UI elements for the game screen.
     * This includes the header with scores and controls, and the game board.
     */
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

        // Save button
        VisTextButton saveButton = new VisTextButton("Save Game");
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSaveDialog();
            }
        });
        headerTable.add(saveButton).right().padRight(10);

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
        gameBoardTable = new VisTable();

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

    /**
     * Updates the score display table with current player scores and turn indicator.
     */
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

    /**
     * Updates the current player's score and advances to the next player's turn.
     * If all questions have been answered, transitions to the winner screen.
     * @param points The points to add to the current player's score
     */
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
     * Creates a deep copy of a 2D boolean array.
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
     * Loads a complete game state, including board state, players, and current turn.
     * @param state The GameState object containing all game data to load
     */
    public void loadFullGameState(GameState state) {
        if (state == null) {
            Gdx.app.error("GameScreen", "Invalid game state: null");
            return;
        }

        boolean[][] newState = state.getBoardState();
        Array<Player> newPlayers = state.getPlayers();
        int newCurrentPlayerIndex = state.getCurrentPlayerIndex();

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

        // Update game state
        this.gameState = deepCopyArray(newState);
        this.players = newPlayers;
        this.currentPlayerIndex = newCurrentPlayerIndex;
        this.answeredQuestions = countAnsweredQuestions();

        // Refresh UI
        updateScoreTable();
        refreshGameBoard();
    }

    /**
     * Creates a GameState object containing the current game state.
     * @return A GameState object with the current game data
     */
    public GameState saveGameState() {
        return new GameState(deepCopyArray(gameState), players, currentPlayerIndex);
    }

    /**
     * Shows a dialog for saving the current game.
     */
    private void showSaveDialog() {
        VisDialog dialog = new VisDialog("Save Game");
        dialog.getContentTable().defaults().pad(5);

        VisTextField nameField = new VisTextField();
        dialog.getContentTable().add(new VisLabel("Enter save name:")).row();
        dialog.getContentTable().add(nameField).width(200).row();

        VisTextButton cancelButton = new VisTextButton("Cancel");
        VisTextButton saveButton = new VisTextButton("Save");

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String saveName = nameField.getText().trim();
                if (!saveName.isEmpty()) {
                    saveGameToStorage(saveName);
                    dialog.hide();
                }
            }
        });

        dialog.getButtonsTable().add(cancelButton).padRight(5);
        dialog.getButtonsTable().add(saveButton);

        dialog.show(stage);
    }

    /**
     * Saves the current game state to persistent storage.
     * @param saveName The name to save the game under
     */
    private void saveGameToStorage(String saveName) {
        try {
            GameState state = saveGameState();
            String json = state.toJson();
            Gdx.app.getPreferences("jeopardy_saves").putString(saveName, json);
            Gdx.app.getPreferences("jeopardy_saves").flush();
            showSuccessDialog("Game saved successfully!");
        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Failed to save game: " + e.getMessage());
            showErrorDialog("Failed to save game: " + e.getMessage());
        }
    }

    /**
     * Shows a success dialog with the specified message.
     * @param message The success message to display
     */
    private void showSuccessDialog(String message) {
        VisDialog dialog = new VisDialog("Success");
        dialog.getContentTable().defaults().pad(5);
        dialog.getContentTable().add(new VisLabel(message)).row();
        dialog.button("OK");
        dialog.show(stage);
    }

    /**
     * Shows an error dialog with the specified message.
     * @param message The error message to display
     */
    private void showErrorDialog(String message) {
        VisDialog dialog = new VisDialog("Error");
        dialog.getContentTable().defaults().pad(5);
        dialog.getContentTable().add(new VisLabel(message)).row();
        dialog.button("OK");
        dialog.show(stage);
    }

    /**
     * Counts the number of questions that have been answered.
     * @return The number of answered questions
     */
    private int countAnsweredQuestions() {
        int count = 0;
        for (boolean[] category : gameState) {
            for (boolean answered : category) {
                if (answered) count++;
            }
        }
        return count;
    }

    /**
     * Refreshes the game board UI to reflect the current game state.
     */
    private void refreshGameBoard() {
        // Clear existing buttons
        gameBoardTable.clear();

        // Re-add categories row
        CategoryType[] categories = CategoryType.values();
        for (CategoryType category : categories) {
            VisLabel categoryLabel = new VisLabel(category.getDisplayName());
            categoryLabel.setAlignment(Align.center);
            gameBoardTable.add(categoryLabel).pad(10).width(120).height(50).fill();
        }
        gameBoardTable.row();

        // Re-add questions with updated states
        int[] values = {200, 400, 600, 800, 1000};
        for (int value : values) {
            for (int i = 0; i < categories.length; i++) {
                final int categoryIndex = i;
                final int questionValue = value;
                final int questionIndex = value / 200 - 1;
                final VisTextButton button = new VisTextButton("$" + value);
                button.setFocusBorderEnabled(false);

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
    }
}
