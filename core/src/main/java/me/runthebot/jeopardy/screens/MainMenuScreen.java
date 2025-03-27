/**
 * The main menu screen of the Jeopardy game.
 * This screen allows players to:
 * - View the current high score
 * - Add/remove players
 * - Start a new game
 * - Load a saved game
 * - View game instructions
 * - Reset the high score
 * - Exit the game
 */
package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.*;
import me.runthebot.jeopardy.Main;
import me.runthebot.jeopardy.model.Player;
import me.runthebot.jeopardy.model.HighScoreManager;

public class MainMenuScreen extends BaseScreen {
    // List of players participating in the game
    private Array<Player> players;
    // Table displaying the list of current players
    private VisTable playerTable;
    // Text field for entering new player names
    private VisTextField playerNameField;

    /**
     * Creates a new main menu screen.
     * @param game The main game instance
     */
    public MainMenuScreen(Main game) {
        super(game);
        this.players = new Array<>();
        createUI();
    }

    /**
     * Creates and sets up all UI elements for the main menu.
     * This includes the title, high score display, player management,
     * and various navigation buttons.
     */
    private void createUI() {
        // Title
        VisLabel titleLabel = new VisLabel("JEOPARDY!");
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(3.0f);
        mainTable.add(titleLabel).pad(50).row();

        // High Score display
        VisTable highScoreTable = new VisTable();
        int highScore = HighScoreManager.getHighScore();
        if (highScore > 0) {
            String highScoreText = "High Score: $" + highScore +
                                 "\nHeld by: " + HighScoreManager.getHighScorePlayer();
            VisLabel highScoreLabel = new VisLabel(highScoreText);
            highScoreLabel.setColor(Color.GOLD);
            highScoreTable.add(highScoreLabel).pad(10);
            mainTable.add(highScoreTable).pad(20).row();
        }

        // Player management section
        VisTable playerSection = new VisTable();

        // Player name input
        playerNameField = new VisTextField();
        playerNameField.setMessageText("Enter player name");
        playerSection.add(new VisLabel("Player Name: ")).left();
        playerSection.add(playerNameField).width(200).pad(5);

        // Add player button
        VisTextButton addPlayerButton = new VisTextButton("Add Player");
        addPlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String playerName = playerNameField.getText().trim();
                if (!playerName.isEmpty()) {
                    players.add(new Player(playerName));
                    playerNameField.setText("");
                    updatePlayerTable();
                }
            }
        });
        playerSection.add(addPlayerButton).pad(5).row();

        mainTable.add(playerSection).pad(10).row();

        // Player list
        playerTable = new VisTable();
        mainTable.add(playerTable).pad(10).row();

        // Play button
        VisTextButton playButton = new VisTextButton("Start Game");
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (players.size >= 2) {
                    game.setScreen(new GameScreen(game, players));
                } else {
                    showError("Please add at least 2 players!");
                }
            }
        });
        mainTable.add(playButton).width(200).pad(10).row();

        // Load Game button
        VisTextButton loadGameButton = new VisTextButton("Load Game");
        loadGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LoadGameScreen(game));
            }
        });
        mainTable.add(loadGameButton).width(200).pad(10).row();

        // Instructions button
        VisTextButton instructionsButton = new VisTextButton("Instructions");
        instructionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new InstructionsScreen(game));
            }
        });
        mainTable.add(instructionsButton).width(200).pad(10).row();

        // Reset High Score button (only show if there is a high score)
        if (highScore > 0) {
            VisTextButton resetHighScoreButton = new VisTextButton("Reset High Score");
            resetHighScoreButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    HighScoreManager.resetHighScore();
                    game.setScreen(new MainMenuScreen(game)); // Refresh screen
                }
            });
            mainTable.add(resetHighScoreButton).width(200).pad(10).row();
        }

        // Exit button
        VisTextButton exitButton = new VisTextButton("Exit");
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Hacky fix to close the browser window
                Gdx.net.openURI("javascript:close()");
            }
        });
        mainTable.add(exitButton).width(200).pad(10).row();

        // Center everything
        mainTable.setFillParent(true);
        mainTable.align(Align.center);
    }

    /**
     * Updates the player list display.
     * Shows all current players with options to remove them.
     */
    private void updatePlayerTable() {
        playerTable.clear();
        playerTable.add(new VisLabel("Current Players:")).pad(5).row();

        for (final Player player : players) {
            VisTable playerRow = new VisTable();
            playerRow.add(new VisLabel(player.getName())).pad(5);

            VisTextButton removeButton = new VisTextButton("Remove");
            removeButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    players.removeValue(player, true);
                    updatePlayerTable();
                }
            });
            playerRow.add(removeButton).pad(5);

            playerTable.add(playerRow).pad(2).row();
        }
    }

    /**
     * Shows an error dialog with the specified message.
     * @param message The error message to display
     */
    private void showError(String message) {
        VisDialog dialog = new VisDialog("Error");
        dialog.text(message);
        dialog.button("OK");
        dialog.pack();
        dialog.centerWindow();
        dialog.show(stage);
    }
}
