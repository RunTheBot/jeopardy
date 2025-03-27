/**
 * Represents the current state of a Jeopardy game.
 * This class manages the game board state, player information, and current player turn.
 * It implements Json.Serializable to support saving and loading game states.
 */
package me.runthebot.jeopardy.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonReader;
import me.runthebot.jeopardy.model.CategoryType;

public class GameState implements Json.Serializable {
    // Tracks which questions have been answered (true = answered, false = available)
    private boolean[][] boardState;
    // List of players participating in the game
    private Array<Player> players;
    // Index of the current player's turn
    private int currentPlayerIndex;

    /**
     * Creates a new game state with the specified board state, players, and current player.
     * @param boardState 2D array tracking answered questions
     * @param players Array of players in the game
     * @param currentPlayerIndex Index of the current player's turn
     */
    public GameState(boolean[][] boardState, Array<Player> players, int currentPlayerIndex) {
        this.boardState = boardState;
        this.players = players;
        this.currentPlayerIndex = currentPlayerIndex;
    }

    /**
     * Default constructor for JSON deserialization.
     * Initializes an empty game state with no answered questions and no players.
     */
    public GameState() {
        this.boardState = new boolean[CategoryType.values().length][5];
        this.players = new Array<>();
        this.currentPlayerIndex = 0;
    }

    // Getters for game state properties
    public boolean[][] getBoardState() {
        return boardState;
    }

    public Array<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Serializes the game state to JSON format.
     * Converts the boolean board state to integers for JSON compatibility.
     * @param json The JSON serializer
     */
    @Override
    public void write(Json json) {
        json.writeValue("currentPlayerIndex", currentPlayerIndex);

        // Convert boolean[][] to int[][] for JSON serialization
        int[][] serializedBoard = new int[boardState.length][boardState[0].length];
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState[i].length; j++) {
                serializedBoard[i][j] = boardState[i][j] ? 1 : 0;
            }
        }
        json.writeValue("boardState", serializedBoard);

        // Write players array
        json.writeValue("players", players, Array.class, Player.class);
    }

    /**
     * Deserializes the game state from JSON format.
     * Converts the integer board state back to boolean values.
     * @param json The JSON serializer
     * @param jsonData The JSON data to deserialize
     */
    @Override
    public void read(Json json, JsonValue jsonData) {
        this.currentPlayerIndex = jsonData.getInt("currentPlayerIndex");

        // Convert int[][] back to boolean[][]
        int[][] serializedBoard = json.readValue("boardState", int[][].class, jsonData);
        this.boardState = new boolean[serializedBoard.length][serializedBoard[0].length];
        for (int i = 0; i < serializedBoard.length; i++) {
            for (int j = 0; j < serializedBoard[i].length; j++) {
                this.boardState[i][j] = serializedBoard[i][j] == 1;
            }
        }

        // Manually read players array for GWT compatibility
        JsonValue playersJson = jsonData.get("players");
        this.players = new Array<>();
        for (JsonValue playerJson = playersJson.child; playerJson != null; playerJson = playerJson.next) {
            Player player = new Player();
            player.read(json, playerJson);
            this.players.add(player);
        }
    }

    /**
     * Converts the game state to a JSON string.
     * @return JSON string representation of the game state
     */
    public String toJson() {
        Json json = new Json();
        json.setTypeName(null); // Disable type names in output
        return json.toJson(this);
    }

    /**
     * Creates a game state from a JSON string.
     * @param jsonString JSON string representation of a game state
     * @return New GameState instance created from the JSON data
     */
    public static GameState fromJson(String jsonString) {
        Json json = new Json();
        json.setIgnoreUnknownFields(true);
        // Important for GWT compatibility
        GameState gameState = new GameState();
        JsonValue jsonValue = new JsonReader().parse(jsonString);
        gameState.read(json, jsonValue);
        return gameState;
    }
}
