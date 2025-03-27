package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisTable;
import me.runthebot.jeopardy.Main;

public abstract class BaseScreen implements Screen {
    protected final Main game;
    protected Stage stage;
    protected VisTable mainTable;

    public BaseScreen(Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        mainTable = new VisTable();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear screen with dark background
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
