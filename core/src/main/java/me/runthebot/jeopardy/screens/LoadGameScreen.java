package me.runthebot.jeopardy.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisList;
import me.runthebot.jeopardy.Main;
import me.runthebot.jeopardy.model.GameState;

public class LoadGameScreen extends BaseScreen {
    private VisList<String> saveList;
    private Array<String> saveNames;

    public LoadGameScreen(Main game) {
        super(game);
        createUI();
        loadSaveList();
    }

    private void createUI() {
        // Title
        VisLabel titleLabel = new VisLabel("LOAD GAME");
        titleLabel.setColor(Color.YELLOW);
        titleLabel.setFontScale(3.0f);
        mainTable.add(titleLabel).pad(50).row();

        // Create scrollable list of saves
        saveList = new VisList<>();
        VisScrollPane scrollPane = new VisScrollPane(saveList);
        scrollPane.setFadeScrollBars(false);
        mainTable.add(scrollPane).width(400).height(300).pad(20).row();

        // Buttons table
        VisTable buttonTable = new VisTable();
        buttonTable.defaults().pad(5);

        // Load button
        VisTextButton loadButton = new VisTextButton("Load Game");
        loadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selected = saveList.getSelected();
                if (selected != null) {
                    loadGame(selected);
                }
            }
        });
        buttonTable.add(loadButton).width(200).pad(10).row();

        // Delete button
        VisTextButton deleteButton = new VisTextButton("Delete Save");
        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selected = saveList.getSelected();
                if (selected != null) {
                    showDeleteConfirmDialog(selected);
                }
            }
        });
        buttonTable.add(deleteButton).width(200).pad(10).row();

        // Back button
        VisTextButton backButton = new VisTextButton("Back to Menu");
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });
        buttonTable.add(backButton).width(200).pad(10).row();

        mainTable.add(buttonTable).pad(20).row();

        // Center everything
        mainTable.setFillParent(true);
        mainTable.align(Align.center);
    }

    private void loadSaveList() {
        saveNames = new Array<>();
        String[] allKeys = Gdx.app.getPreferences("jeopardy_saves").get().keySet().toArray(new String[0]);
        for (String key : allKeys) {
            saveNames.add(key);
        }
        saveList.setItems(saveNames);
    }

    private void loadGame(String saveName) {
        try {
            String savedGameJson = Gdx.app.getPreferences("jeopardy_saves").getString(saveName);
            if (savedGameJson != null && !savedGameJson.isEmpty()) {
                GameState loadedState = GameState.fromJson(savedGameJson);
                GameScreen gameScreen = new GameScreen(game, new Array<>()); // Create with empty players
                gameScreen.loadFullGameState(loadedState); // Load the saved state
                game.setScreen(gameScreen);
            } else {
                showErrorDialog("Save file is empty or corrupted");
            }
        } catch (Exception e) {
            Gdx.app.error("LoadGameScreen", "Failed to load game: " + e.getMessage());
            showErrorDialog("Failed to load game: " + e.getMessage());
        }
    }

    private void deleteGame(String saveName) {
        try {
            Gdx.app.getPreferences("jeopardy_saves").remove(saveName);
            Gdx.app.getPreferences("jeopardy_saves").flush();
            loadSaveList(); // Refresh the list
            Gdx.app.log("LoadGameScreen", "Game deleted: " + saveName);
        } catch (Exception e) {
            Gdx.app.error("LoadGameScreen", "Failed to delete game: " + e.getMessage());
            showErrorDialog("Failed to delete game: " + e.getMessage());
        }
    }

    private void showDeleteConfirmDialog(String saveName) {
        VisDialog dialog = new VisDialog("Delete Save");
        dialog.getContentTable().defaults().pad(5);

        dialog.getContentTable().add(new VisLabel("Are you sure you want to delete '" + saveName + "'?")).row();

        VisTextButton cancelButton = new VisTextButton("Cancel");
        VisTextButton deleteButton = new VisTextButton("Delete");

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                deleteGame(saveName);
                dialog.hide();
            }
        });

        dialog.getButtonsTable().add(cancelButton).padRight(5);
        dialog.getButtonsTable().add(deleteButton);

        dialog.show(stage);
    }

    private void showErrorDialog(String message) {
        VisDialog dialog = new VisDialog("Error");
        dialog.getContentTable().defaults().pad(5);

        dialog.getContentTable().add(new VisLabel(message)).row();
        dialog.button("OK");

        dialog.show(stage);
    }
}
