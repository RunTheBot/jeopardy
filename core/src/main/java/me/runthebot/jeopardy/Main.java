/**
 * Main entry point for the Jeopardy game application.
 * This class extends LibGDX's Game class and handles the core game initialization
 * and cleanup. It sets up the UI framework and manages the game's screen transitions.
 */
package me.runthebot.jeopardy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import me.runthebot.jeopardy.screens.MainMenuScreen;

public class Main extends Game {
    /**
     * Initializes the game application.
     * Sets up logging, UI framework, and the initial game screen.
     */
    @Override
    public void create() {
        // Enable debug logging for development
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Skip GDX version check for compatibility
        VisUI.setSkipGdxVersionCheck(true);
        // Load the UI skin with normal scale
        VisUI.load(SkinScale.X1);

        // Set the initial screen to the main menu
        setScreen(new MainMenuScreen(this));
    }

    /**
     * Cleans up resources when the game is closed.
     * Disposes of UI resources and the current screen.
     */
    @Override
    public void dispose() {
        super.dispose();
        // Clean up UI resources
        VisUI.dispose();
        // Clean up current screen resources
        if (screen != null) {
            screen.dispose();
        }
    }
}
