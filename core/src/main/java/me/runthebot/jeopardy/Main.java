package me.runthebot.jeopardy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;


/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private Stage stage;

    @Override
    public void create() {
        VisUI.setSkipGdxVersionCheck(true);
        VisUI.load(SkinScale.X1);
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        // Create a grid with categories and questions
        com.kotcrab.vis.ui.widget.VisTable mainTable = new com.kotcrab.vis.ui.widget.VisTable();
        mainTable.setFillParent(true);
        
        // Categories
        String[] categories = {"Science", "History", "Sports", "Movies", "Geography"};
        int[] values = {200, 400, 600, 800, 1000};
        
        // Add categories row
        for (String category : categories) {
            com.kotcrab.vis.ui.widget.VisLabel categoryLabel = new com.kotcrab.vis.ui.widget.VisLabel(category);
            categoryLabel.setAlignment(com.badlogic.gdx.utils.Align.center);
            mainTable.add(categoryLabel).pad(10).width(120).height(50).fill();
        }
        mainTable.row();
        
        // Add questions
        for (int value : values) {
            for (int i = 0; i < categories.length; i++) {
            final com.kotcrab.vis.ui.widget.VisTextButton button = new com.kotcrab.vis.ui.widget.VisTextButton("$" + value);
            button.setFocusBorderEnabled(false);
            button.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
                @Override
                public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                button.setText(""); // When clicked, hide the value
                button.setDisabled(true);
                }
            });
            mainTable.add(button).pad(5).width(120).height(80).fill();
            }
            mainTable.row();
        }
        
        stage.addActor(mainTable);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1f);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }
}
