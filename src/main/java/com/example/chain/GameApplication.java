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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
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
    private Button createBagButton(int number) {
        Pane picture = createBagPicture();

        Label text = new Label("МЕШОЧЕК " + number + "\nВыбрать");
        text.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #4b2c18; -fx-text-alignment: center;");

        VBox content = new VBox(0, picture, text);
        content.setAlignment(Pos.CENTER);

        Button bag = new Button();
        bag.setGraphic(content);
        bag.setPrefSize(185, 170);
        bag.setMinSize(185, 170);
        bag.setStyle("-fx-background-color: #f8e7cc; -fx-background-radius: 16; -fx-border-radius: 16; "
                + "-fx-border-color: #c99053; -fx-border-width: 2; -fx-cursor: hand;");
        return bag;
    }

    // Рисует простую фигуру мешочка из стандартных JavaFX-форм.
    private Pane createBagPicture() {
        Pane picture = new Pane();
        picture.setPrefSize(135, 118);

        Path body = new Path(
                new MoveTo(47, 42),
                new CubicCurveTo(45, 54, 26, 58, 21, 74),
                new CubicCurveTo(17, 91, 29, 111, 31, 112),
                new LineTo(104, 112),
                new CubicCurveTo(106, 111, 118, 91, 114, 74),
                new CubicCurveTo(109, 58, 90, 54, 88, 42),
                new ClosePath()
        );
        body.setFill(Color.web("#cf9857"));
        body.setStroke(Color.web("#82491f"));
        body.setStrokeWidth(2);

        Rectangle neck = new Rectangle(47, 23, 41, 22);
        neck.setArcWidth(10);
        neck.setArcHeight(10);
        neck.setFill(Color.web("#d7a665"));
        neck.setStroke(Color.web("#82491f"));
        neck.setStrokeWidth(2);

        Rectangle tie = new Rectangle(40, 40, 55, 8);
        tie.setArcWidth(6);
        tie.setArcHeight(6);
        tie.setFill(Color.web("#75401c"));

        Circle knot = new Circle(67, 44, 8, Color.web("#e8c176"));
        knot.setStroke(Color.web("#75401c"));

        Path leftFold = new Path(new MoveTo(42, 64), new CubicCurveTo(35, 79, 37, 91, 43, 101));
        leftFold.setFill(Color.TRANSPARENT);
        leftFold.setStroke(Color.web("#b6793e"));
        leftFold.setStrokeWidth(2);

        Path rightFold = new Path(new MoveTo(93, 64), new CubicCurveTo(100, 79, 98, 91, 92, 101));
        rightFold.setFill(Color.TRANSPARENT);
        rightFold.setStroke(Color.web("#b6793e"));
        rightFold.setStrokeWidth(2);

        StackPane coin = new StackPane();
        coin.setLayoutX(53);
        coin.setLayoutY(71);
        coin.setPrefSize(28, 28);
        coin.setStyle("-fx-background-color: #f1c94d; -fx-background-radius: 20; -fx-border-color: #a97617; "
                + "-fx-border-radius: 20; -fx-border-width: 2;");
        Label coinText = new Label("1");
        coinText.setStyle("-fx-font-weight: bold; -fx-text-fill: #865b11;");
        coin.getChildren().add(coinText);

        picture.getChildren().addAll(body, neck, tie, knot, leftFold, rightFold, coin);
        return picture;
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
