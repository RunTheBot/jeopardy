package me.runthebot.jeopardy.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class HighScoreManager {
    private static final String PREFS_NAME = "jeopardy_prefs";
    private static final String HIGH_SCORE_KEY = "high_score";
    private static final String HIGH_SCORE_PLAYER_KEY = "high_score_player";

    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

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

    public static int getHighScore() {
        return getPrefs().getInteger(HIGH_SCORE_KEY, 0);
    }

    public static String getHighScorePlayer() {
        return getPrefs().getString(HIGH_SCORE_PLAYER_KEY, "None");
    }

    public static void resetHighScore() {
        Preferences prefs = getPrefs();
        prefs.remove(HIGH_SCORE_KEY);
        prefs.remove(HIGH_SCORE_PLAYER_KEY);
        prefs.flush();
    }
}
