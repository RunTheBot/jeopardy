/**
 * Base class for all game screens in the Jeopardy game.
 * This abstract class provides common functionality for screen management,
 * including stage setup, rendering, and input handling.
 * All other screens in the game should extend this class.
 */
package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTable;
import me.runthebot.jeopardy.Main;

public abstract class BaseScreen implements Screen {
    // Reference to the main game instance
    protected final Main game;
    // The stage for managing UI actors and input
    protected Stage stage;
    // The main table for organizing UI elements
    protected VisTable mainTable;

    /**
     * Creates a new base screen with the specified game instance.
     * Initializes the stage and main table for UI organization.
     * @param game The main game instance
     */
    public BaseScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        mainTable = new VisTable();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Called when the screen becomes the current screen.
     * Sets up input processing for the stage.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Renders the screen.
     * Clears the screen with a dark background and updates/draws the stage.
     * @param delta Time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        // Clear screen with dark background
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     * Updates the viewport to match the new screen dimensions.
     * @param width The new screen width
     * @param height The new screen height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the screen is paused.
     * Currently empty but can be overridden by subclasses.
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the screen is resumed.
     * Currently empty but can be overridden by subclasses.
     */
    @Override
    public void resume() {
    }

    /**
     * Called when the screen is hidden.
     * Currently empty but can be overridden by subclasses.
     */
    @Override
    public void hide() {
    }

    /**
     * Called when the screen is disposed.
     * Cleans up resources used by the stage.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}
