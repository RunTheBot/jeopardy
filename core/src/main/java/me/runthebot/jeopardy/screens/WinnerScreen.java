/**
 * Screen displayed when the game ends, showing the winner(s) and final scores.
 * This screen handles:
 * - Determining the winner(s)
 * - Updating high scores
 * - Displaying final scores for all players
 * - Showing the all-time high score
 * - Providing options to play again or return to the main menu
 */
package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import me.runthebot.jeopardy.Main;
import me.runthebot.jeopardy.model.Player;
import me.runthebot.jeopardy.model.HighScoreManager;

public class WinnerScreen extends BaseScreen {
    // List of players who participated in the game
    private final Array<Player> players;

    /**
     * Creates a new winner screen with the specified players.
     * @param game The main game instance
     * @param players The list of players who participated in the game
     */
    public WinnerScreen(Main game, Array<Player> players) {
        super(game);
        this.players = players;
        createUI();
    }

    /**
     * Creates and sets up all UI elements for the winner screen.
     * This includes determining the winner(s), displaying scores,
     * and providing navigation options.
     */
    private void createUI() {
        // Find winner(s)
        int highestScore = -1;
        Array<Player> winners = new Array<>();

        // Find highest score and identify winners
        for (Player player : players) {
            if (player.getScore() > highestScore) {
                highestScore = player.getScore();
                winners.clear();
                winners.add(player);
            } else if (player.getScore() == highestScore) {
                winners.add(player);
            }

            // Check for new high score
            HighScoreManager.checkAndUpdateHighScore(player);
        }

        // Title
        VisLabel titleLabel = new VisLabel("Game Over!");
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(2.0f);
        mainTable.add(titleLabel).pad(30).row();

        // Winner announcement
        String winnerText;
        if (winners.size == 1) {
            winnerText = winners.first().getName() + " Wins!";

            // Check if this is a new high score
            if (winners.first().getScore() == HighScoreManager.getHighScore()) {
                winnerText += "\nNEW HIGH SCORE!";
            }
        } else {
            winnerText = "It's a Tie!";
        }
        VisLabel winnerLabel = new VisLabel(winnerText);
        winnerLabel.setColor(Color.GREEN);
        winnerLabel.setFontScale(1.5f);
        mainTable.add(winnerLabel).pad(20).row();

        // Final scores
        VisTable scoresTable = new VisTable();
        scoresTable.add(new VisLabel("Final Scores:")).pad(10).row();

        // Sort players by score (highest to lowest)
        players.sort((p1, p2) -> p2.getScore() - p1.getScore());

        for (Player player : players) {
            VisTable playerRow = new VisTable();
            String scoreText = player.getName() + ": $" + player.getScore();
            VisLabel scoreLabel = new VisLabel(scoreText);

            // Highlight winners
            if (player.getScore() == highestScore) {
                scoreLabel.setColor(Color.YELLOW);
            }

            playerRow.add(scoreLabel).pad(5);
            scoresTable.add(playerRow).pad(5).row();
        }
        mainTable.add(scoresTable).pad(20).row();

        // High Score display
        VisTable highScoreTable = new VisTable();
        String highScoreText = "All-Time High Score: $" + HighScoreManager.getHighScore() +
                             "\nby " + HighScoreManager.getHighScorePlayer();
        VisLabel highScoreLabel = new VisLabel(highScoreText);
        highScoreLabel.setColor(Color.GOLD);
        highScoreTable.add(highScoreLabel).pad(10);
        mainTable.add(highScoreTable).pad(20).row();

        // Buttons
        VisTable buttonTable = new VisTable();

        // Play Again button
        VisTextButton playAgainButton = new VisTextButton("Play Again");
        playAgainButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Reset scores and start new game
                for (Player player : players) {
                    player.updateScore(-player.getScore()); // Reset to 0
                }
                game.setScreen(new GameScreen(game, players));
            }
        });
        buttonTable.add(playAgainButton).width(200).pad(10);

        // Main Menu button
        VisTextButton menuButton = new VisTextButton("Main Menu");
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        buttonTable.add(menuButton).width(200).pad(10);

        mainTable.add(buttonTable).pad(20).row();

        // Center everything
        mainTable.setFillParent(true);
        mainTable.align(Align.center);
    }
}
