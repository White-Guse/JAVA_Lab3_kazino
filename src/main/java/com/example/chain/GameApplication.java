package com.example.chain;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class GameApplication extends Application {
    private final Player player = new Player("Игрок", 0);
    private final ActionChain actionChain = new ActionChain();
    private final Label coinsLabel = new Label();
    private final Label infoLabel = new Label("Добавьте монету и начните игру.");
    private final Button startButton = new Button("Начать игру");
    private final Button[] bags = new Button[3];
    private boolean freeNextGame;
    private boolean gameStarted;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Игровой автомат");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(18, 28, 24, 28));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #fff6e7, #f1dec2);");

        Label gameName = new Label("ИГРОВОЙ АВТОМАТ");
        gameName.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #80532d;");

        Label title = new Label("Выбери один из мешочков");
        title.setStyle("-fx-font-size: 27px; -fx-font-weight: bold; -fx-text-fill: #4b2c18;");

        Label subtitle = new Label("Все мешочки одинаковые, но один из исходов может принести удачу.");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #775d48;");

        coinsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #6a421f;");
        infoLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #6a5746; -fx-padding: 8 14 8 14; "
                + "-fx-background-color: #fffaf2; -fx-background-radius: 8;");

        Button addCoinButton = new Button("Добавить 1 монету");
        addCoinButton.setStyle("-fx-font-size: 14px; -fx-background-color: #f0b94f; -fx-font-weight: bold;");
        addCoinButton.setOnAction(event -> addCoin());
        startButton.setStyle("-fx-font-size: 14px; -fx-background-color: #80a95b; -fx-text-fill: white; -fx-font-weight: bold;");
        startButton.setOnAction(event -> startGame());

        HBox topButtons = new HBox(10, addCoinButton, startButton);
        topButtons.setAlignment(Pos.CENTER);

        VBox top = new VBox(8, gameName, title, subtitle, coinsLabel, infoLabel, topButtons);
        top.setAlignment(Pos.CENTER);
        root.setTop(top);

        HBox bagBox = new HBox(18);
        bagBox.setAlignment(Pos.CENTER);
        bagBox.setPadding(new Insets(28, 0, 4, 0));
        for (int i = 0; i < bags.length; i++) {
            Button bag = createBagButton(i + 1);
            bag.setDisable(true);
            bag.setOnAction(event -> chooseBag());
            bags[i] = bag;
            bagBox.getChildren().add(bag);
        }
        root.setCenter(bagBox);

        updatePlayerInfo();
        stage.setScene(new Scene(root, 690, 420));
        stage.setResizable(false);
        stage.show();
    }

    // Создаёт кнопку, которая выглядит как мешочек.
    private Button createBagButton(int number) {
        Button bag = new Button("👜\nМЕШОЧЕК " + number + "\nВыбрать");
        bag.setPrefSize(185, 145);
        bag.setMinSize(185, 145);
        bag.setWrapText(true);
        bag.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #4b2c18; "
                + "-fx-background-color: linear-gradient(to bottom, #e6b46e, #c98843); "
                + "-fx-background-radius: 16; -fx-border-radius: 16; -fx-border-color: #9d612d; "
                + "-fx-border-width: 2; -fx-cursor: hand;");
        return bag;
    }

    // Добавляет игроку одну монету.
    private void addCoin() {
        player.addCoins(1);
        infoLabel.setText("Монета добавлена. Теперь можно начать игру.");
        updatePlayerInfo();
    }

    // Запускает выбор мешочка.
    private void startGame() {
        if (player.getCoins() == 0) {
            showSimpleMessage("Недостаточно монет", "Сначала добавьте хотя бы одну монету.");
            return;
        }

        gameStarted = true;
        infoLabel.setText("Выберите любой мешочек.");
        updateBagButtons();
    }

    // Обрабатывает выбор мешочка игроком.
    private void chooseBag() {
        if (!gameStarted) {
            return;
        }

        if (!freeNextGame && !player.payForGame()) {
            finishGame("Монеты закончились. Добавьте монету для новой игры.");
            return;
        }

        freeNextGame = false;
        GameDecision decision = actionChain.play(player);
        updatePlayerInfo();
        showDecision(decision);
    }

    // Показывает результат, полученный из цепочки.
    private void showDecision(GameDecision decision) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(decision.getTitle());
        alert.setHeaderText(decision.getMessage());

        ButtonType continueButton = new ButtonType("Играть ещё", ButtonBar.ButtonData.YES);
        ButtonType finishButton = new ButtonType("Забрать и выйти", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(continueButton, finishButton);

        Optional<ButtonType> answer = alert.showAndWait();
        boolean continueGame = answer.isPresent() && answer.get() == continueButton;

        if (!continueGame) {
            finishGame("Игра завершена. Вы можете добавить монеты и начать заново.");
            return;
        }

        freeNextGame = decision.isFreeNextGame();
        if (!freeNextGame && player.getCoins() == 0) {
            finishGame("Монеты закончились. Добавьте монету для новой игры.");
            return;
        }

        infoLabel.setText(freeNextGame
                ? "Следующий выбор мешочка бесплатный."
                : "Выберите мешочек для следующей попытки.");
        updateBagButtons();
    }

    private void finishGame(String message) {
        gameStarted = false;
        freeNextGame = false;
        infoLabel.setText(message);
        updateBagButtons();
    }

    private void updatePlayerInfo() {
        coinsLabel.setText("Монеты: " + player.getCoins() + " | Сыграно попыток: " + player.getGamesCount());
    }

    private void updateBagButtons() {
        for (Button bag : bags) {
            bag.setDisable(!gameStarted);
        }
    }

    private void showSimpleMessage(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
