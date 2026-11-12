package com.example.chain;

public class WinHandler extends Handler {
    private static final int PRIZE = 3;

    public WinHandler(Handler nextHandler) {
        super(nextHandler);
    }

    @Override
    protected boolean canHandle(ResultType type) {
        return type == ResultType.WIN;
    }

    @Override
    protected GameDecision handle(Player player) {
        player.addCoins(PRIZE);
        return new GameDecision(
                ResultType.WIN,
                "Выигрыш!",
                "В мешочке нашлось " + PRIZE + " монеты. Забрать выигрыш или сыграть ещё?",
                false
        );
    }
}
