package me.runthebot.jeopardy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import me.runthebot.jeopardy.model.JeopardyGame;
import me.runthebot.jeopardy.ui.JeopardyScreen;
import me.runthebot.jeopardy.util.JeopardyLoader;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Stage stage;
    private JeopardyScreen jeopardyScreen;

    @Override
    public void create () {
        VisUI.setSkipGdxVersionCheck(true);
        VisUI.load(SkinScale.X1);

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load the Jeopardy game data from JSON
        JeopardyGame game = JeopardyLoader.loadGame("jeopardy.json");

        // Create and display the Jeopardy game screen
        jeopardyScreen = new JeopardyScreen(stage, game);
    }

    @Override
    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render () {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose () {
        VisUI.dispose();
        stage.dispose();
    }
}
