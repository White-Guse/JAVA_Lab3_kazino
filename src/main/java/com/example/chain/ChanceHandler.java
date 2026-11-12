package com.example.chain;

public class ChanceHandler extends Handler {
    public ChanceHandler(Handler nextHandler) {
        super(nextHandler);
    }

    @Override
    protected boolean canHandle(ResultType type) {
        return type == ResultType.CHANCE;
    }

    @Override
    protected GameDecision handle(Player player) {
        return new GameDecision(
                ResultType.CHANCE,
                "Ещё один шанс",
                "Сегодня автомат добрый: следующая попытка будет бесплатной.",
                true
        );
    }
}
