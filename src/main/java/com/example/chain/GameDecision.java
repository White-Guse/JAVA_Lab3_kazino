package com.example.chain;

public class GameDecision {
    private final ResultType type;
    private final String title;
    private final String message;
    private final boolean freeNextGame;

    public GameDecision(ResultType type, String title, String message, boolean freeNextGame) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.freeNextGame = freeNextGame;
    }

    public ResultType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public boolean isFreeNextGame() {
        return freeNextGame;
    }
}
