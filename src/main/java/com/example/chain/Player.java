package com.example.chain;

public class Player {
    private final String name;
    private int coins;
    private int gamesCount;

    public Player(String name, int coins) {
        this.name = name;
        this.coins = coins;
    }

    // Списывает монету за обычный ход.
    public boolean payForGame() {
        if (coins < 1) {
            return false;
        }

        coins--;
        gamesCount++;
        return true;
    }

    public void addCoins(int count) {
        coins += count;
    }

    public String getName() {
        return name;
    }

    public int getCoins() {
        return coins;
    }

    public int getGamesCount() {
        return gamesCount;
    }
}
