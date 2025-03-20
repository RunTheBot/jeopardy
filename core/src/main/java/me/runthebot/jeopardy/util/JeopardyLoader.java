package me.runthebot.jeopardy.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import me.runthebot.jeopardy.model.JeopardyGame;

public class JeopardyLoader {

    public static JeopardyGame loadGame(String path) {
        FileHandle fileHandle = Gdx.files.internal(path);
        String jsonString = fileHandle.readString();

        Json json = new Json();
        return json.fromJson(JeopardyGame.class, jsonString);
    }
}
