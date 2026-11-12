package com.example.chain;

import java.util.Random;

public class ActionChain {
    private final Handler chain;
    private final Random random = new Random();

    public ActionChain() {
        chain = new LossHandler(new WinHandler(null));
    }

    // Разыгрывает один результат и запускает цепочку обработчиков.
    public GameDecision play(Player player) {
        ResultType type = getRandomType();
        return chain.process(type, player);
    }

    private ResultType getRandomType() {
        if (random.nextBoolean()) {
            return ResultType.LOSS;
        }
        return ResultType.WIN;
    }
}
