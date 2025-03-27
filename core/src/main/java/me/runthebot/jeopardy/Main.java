/**
 * Name: Aaron
 * Date: 2025-03-27
 * Description: The game show Jeopardy! made in LibGDX
 * Challenge Features:
 * - Timer - Used in the Jeopardy! board when a question is displayed
 * - Saving - Saves the games state to the browser's local storage and saves top score
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
