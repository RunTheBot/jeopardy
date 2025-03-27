/**
 * Represents a player in the Jeopardy game.
 * This class manages player information including their name and score.
 * It implements Json.Serializable to support saving and loading player data.
 */
package me.runthebot.jeopardy.model;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonReader;

public class Player implements Json.Serializable {
    // Player's display name
    private String name;
    // Player's current score in the game
    private int score;

    /**
     * Creates a new player with the specified name.
     * Initializes the player's score to 0.
     * @param name The player's display name
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * Default constructor for JSON deserialization.
     * Initializes an empty player with no name and zero score.
     */
    public Player() {
        this.name = "";
        this.score = 0;
    }

    /**
     * Gets the player's display name.
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's current score.
     * @return The player's score
     */
    public int getScore() {
        return score;
    }

    /**
     * Updates the player's score by adding the specified points.
     * @param points The points to add to the player's score
     */
    public void updateScore(int points) {
        this.score += points;
    }

    /**
     * Serializes the player data to JSON format.
     * @param json The JSON serializer
     */
    @Override
    public void write(Json json) {
        json.writeValue("name", name);
        json.writeValue("score", score);
    }

    /**
     * Deserializes the player data from JSON format.
     * @param json The JSON serializer
     * @param jsonData The JSON data to deserialize
     */
    @Override
    public void read(Json json, JsonValue jsonData) {
        this.name = jsonData.getString("name");
        this.score = jsonData.getInt("score");
    }

    /**
     * Creates a player from a JSON string.
     * @param jsonString JSON string representation of a player
     * @return New Player instance created from the JSON data
     */
    public static Player fromJson(String jsonString) {
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        Player player = new Player();
        JsonValue jsonValue = new JsonReader().parse(jsonString);
        player.read(json, jsonValue);
        return player;
    }
}
