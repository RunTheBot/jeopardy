package me.runthebot.jeopardy.model;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonReader;

public class Player implements Json.Serializable {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    // Default constructor for JSON deserialization
    public Player() {
        this.name = "";
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int points) {
        this.score += points;
    }

    @Override
    public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("score", score);
    }

    @Override
    public void read(Json json, JsonValue jsonData) {
        this.name = jsonData.getString("name");
        this.score = jsonData.getInt("score");
    }

    public static Player fromJson(String jsonString) {
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        Player player = new Player();
        JsonValue jsonValue = new JsonReader().parse(jsonString);
        player.read(json, jsonValue);
        return player;
    }
}
