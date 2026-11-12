package com.example.chain;

import java.util.Random;

public class ActionChain {
    private final Handler chain;
    private final Random random = new Random();

    public ActionChain() {
        chain = new LossHandler(new WinHandler(new ChanceHandler(null)));
    }

    // Разыгрывает один результат и запускает цепочку обработчиков.
    public GameDecision play(Player player) {
        ResultType type = getRandomType();
        return chain.process(type, player);
    }

    private ResultType getRandomType() {
        int value = random.nextInt(10);

        if (value < 4) {
            return ResultType.LOSS;
        }
        if (value < 7) {
            return ResultType.WIN;
        }
        return ResultType.CHANCE;
    }
}
