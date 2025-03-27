package me.runthebot.jeopardy.model;

public class Player {
    private String name;
    private int score;

    public Player(String name) {
        this.name = name;
        this.score = 0;
    }

    public Player(String name2, int score2) {
        //TODO Auto-generated constructor stub
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
}
