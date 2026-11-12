package com.example.chain;

public abstract class Handler {
    private final Handler nextHandler;

    protected Handler(Handler nextHandler) {
        this.nextHandler = nextHandler;
    }

    // Передаёт результат подходящему обработчику.
    public GameDecision process(ResultType type, Player player) {
        if (canHandle(type)) {
            return handle(player);
        }

        if (nextHandler != null) {
            return nextHandler.process(type, player);
        }

        return new GameDecision(type, "Ошибка", "Не найден обработчик результата.", false);
    }

    protected abstract boolean canHandle(ResultType type);

    protected abstract GameDecision handle(Player player);
}
