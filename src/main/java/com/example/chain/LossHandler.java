package com.example.chain;

public class LossHandler extends Handler {
    public LossHandler(Handler nextHandler) {
        super(nextHandler);
    }

    @Override
    protected boolean canHandle(ResultType type) {
        return type == ResultType.LOSS;
    }

    @Override
    protected GameDecision handle(Player player) {
        return new GameDecision(
                ResultType.LOSS,
                "Не повезло",
                "В мешочке нет монет. Можно попробовать ещё раз.",
                false
        );
    }
}
