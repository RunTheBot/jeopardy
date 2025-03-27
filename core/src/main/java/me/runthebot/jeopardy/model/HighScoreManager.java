/**
 * Manages high scores for the Jeopardy game.
 * This class handles saving, loading, and updating high scores using LibGDX's Preferences system.
 * High scores are persisted between game sessions.
 */
package me.runthebot.jeopardy.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScoreManager {
    // Name of the preferences file used to store high scores
    private static final String PREFS_NAME = "jeopardy_prefs";
    // Key for storing the high score value
    private static final String HIGH_SCORE_KEY = "high_score";
    // Key for storing the name of the player with the high score
    private static final String HIGH_SCORE_PLAYER_KEY = "high_score_player";

    /**
     * Gets the preferences object for storing high scores.
     * @return The Preferences object for the game
     */
    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * Checks if the given player's score is a new high score and updates if necessary.
     * @param player The player whose score should be checked
     */
    public static void checkAndUpdateHighScore(Player player) {
        Preferences prefs = getPrefs();
        int currentHighScore = prefs.getInteger(HIGH_SCORE_KEY, 0);

        if (player.getScore() > currentHighScore) {
            prefs.putInteger(HIGH_SCORE_KEY, player.getScore());
            prefs.putString(HIGH_SCORE_PLAYER_KEY, player.getName());
            prefs.flush(); // Save changes immediately
            return;
        }
    }

    /**
     * Gets the current high score.
     * @return The highest score achieved in the game
     */
    public static int getHighScore() {
        return getPrefs().getInteger(HIGH_SCORE_KEY, 0);
    }

    /**
     * Gets the name of the player who achieved the high score.
     * @return The name of the player with the high score, or "None" if no high score exists
     */
    public static String getHighScorePlayer() {
        return getPrefs().getString(HIGH_SCORE_PLAYER_KEY, "None");
    }

    /**
     * Resets the high score by removing it from preferences.
     */
    public static void resetHighScore() {
        Preferences prefs = getPrefs();
        prefs.remove(HIGH_SCORE_KEY);
        prefs.remove(HIGH_SCORE_PLAYER_KEY);
        prefs.flush();
    }
}
