package me.runthebot.jeopardy.net;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.github.mrstahlfelge.gdx.websockets.WebSocket;
import com.github.mrstahlfelge.gdx.websockets.WebSocketListener;
import com.github.mrstahlfelge.gdx.websockets.WebSockets;

public class GameClient {
    private static final String SERVER_URL = "http://localhost:8081";
    private static final String WS_URL = "ws://localhost:8081/ws";
    private WebSocket webSocket;
    private String playerId;
    private String roomId;
    private GameClientListener listener;
    private Json json;

    public GameClient(GameClientListener listener) {
        this.listener = listener;
        this.json = new Json();
    }

    public void connect() {
        webSocket = WebSockets.newWebSocket(WS_URL, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket) {
                Gdx.app.log("WebSocket", "Connected");
                listener.onConnected();
            }

            @Override
            public void onClose(WebSocket webSocket, int closeCode, String reason) {
                Gdx.app.log("WebSocket", "Closed");
                listener.onDisconnected();
            }

            @Override
            public void onMessage(WebSocket webSocket, String message) {
                handleWebSocketMessage(message);
            }

            @Override
            public void onError(WebSocket webSocket, Throwable error) {
                Gdx.app.error("WebSocket", "Error", error);
                listener.onError(error.getMessage());
            }
        });
        webSocket.connect();
    }

    private void handleWebSocketMessage(String message) {
        try {
            JsonValue jsonMessage = new JsonReader().parse(message);
            String type = jsonMessage.getString("type");

            switch (type) {
                case "ROOM_JOINED":
                    String roomId = jsonMessage.getString("roomId");
                    listener.onRoomJoined(roomId);
                    break;

                case "PLAYER_JOINED":
                    String playerId = jsonMessage.getString("playerId");
                    String playerName = jsonMessage.getString("name");
                    listener.onPlayerJoined(playerId, playerName);
                    break;

                case "PLAYER_LEFT":
                    String leftPlayerId = jsonMessage.getString("playerId");
                    listener.onPlayerLeft(leftPlayerId);
                    break;

                case "GAME_STARTED":
                    listener.onGameStarted();
                    break;

                case "SCORE_UPDATED":
                    String scoredPlayerId = jsonMessage.getString("playerId");
                    int newScore = jsonMessage.getInt("score");
                    listener.onScoreUpdated(scoredPlayerId, newScore);
                    break;

                case "ERROR":
                    String error = jsonMessage.getString("message");
                    listener.onError(error);
                    break;
            }
        } catch (Exception e) {
            Gdx.app.error("WebSocket", "Error parsing message", e);
            listener.onError("Error parsing server message");
        }
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close();
            webSocket = null;
        }
    }

    public void getPublicRooms(final RoomListCallback callback) {
        HttpRequest request = new HttpRequest(HttpMethods.GET);
        request.setUrl(SERVER_URL + "/api/rooms");

        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                String responseText = httpResponse.getResultAsString();
                JsonValue json = new JsonReader().parse(responseText);
                callback.onRoomsReceived(json);
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.error("GameClient", "Failed to get rooms", t);
                callback.onError("Failed to get rooms: " + t.getMessage());
            }

            @Override
            public void cancelled() {
                callback.onError("Request cancelled");
            }
        });
    }

    public void createRoom(String name, boolean isPrivate) {
        if (webSocket != null && webSocket.isConnected()) {
            JsonValue message = new JsonValue(JsonValue.ValueType.object);
            message.addChild("type", new JsonValue("CREATE_ROOM"));
            message.addChild("name", new JsonValue(name));
            message.addChild("private", new JsonValue(isPrivate));
            webSocket.send(message.toString());
        }
    }

    public void joinRoom(String roomId) {
        if (webSocket != null && webSocket.isConnected()) {
            this.roomId = roomId;
            JsonValue message = new JsonValue(JsonValue.ValueType.object);
            message.addChild("type", new JsonValue("JOIN_ROOM"));
            message.addChild("roomId", new JsonValue(roomId));
            webSocket.send(message.toString());
        }
    }

    public void leaveRoom() {
        if (webSocket != null && webSocket.isConnected() && roomId != null) {
            JsonValue message = new JsonValue(JsonValue.ValueType.object);
            message.addChild("type", new JsonValue("LEAVE_ROOM"));
            message.addChild("roomId", new JsonValue(roomId));
            webSocket.send(message.toString());
            roomId = null;
        }
    }

    public void startGame() {
        if (webSocket != null && webSocket.isConnected() && roomId != null) {
            JsonValue message = new JsonValue(JsonValue.ValueType.object);
            message.addChild("type", new JsonValue("START_GAME"));
            message.addChild("roomId", new JsonValue(roomId));
            webSocket.send(message.toString());
        }
    }

    public void submitAnswer(boolean isCorrect) {
        if (webSocket != null && webSocket.isConnected() && roomId != null) {
            JsonValue message = new JsonValue(JsonValue.ValueType.object);
            message.addChild("type", new JsonValue("SUBMIT_ANSWER"));
            message.addChild("roomId", new JsonValue(roomId));
            message.addChild("correct", new JsonValue(isCorrect));
            webSocket.send(message.toString());
        }
    }

    public interface GameClientListener {
        void onConnected();
        void onDisconnected();
        void onRoomJoined(String roomId);
        void onPlayerJoined(String playerId, String name);
        void onPlayerLeft(String playerId);
        void onGameStarted();
        void onScoreUpdated(String playerId, int newScore);
        void onError(String error);
    }

    public interface RoomListCallback {
        void onRoomsReceived(JsonValue rooms);
        void onError(String error);
    }
}
