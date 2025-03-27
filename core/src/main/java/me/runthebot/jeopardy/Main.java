package me.runthebot.jeopardy;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import me.runthebot.jeopardy.screens.MainMenuScreen;

public class Main extends Game {
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        VisUI.setSkipGdxVersionCheck(true);
        VisUI.load(SkinScale.X1);

        // Set the initial screen to the main menu
        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        VisUI.dispose();
        if (screen != null) {
            screen.dispose();
        }
    }
}
