package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Effect;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class Window extends Application {

    // just constants for window
    private static final int WIDTH = 900;
    private static final int HEIGHT = 550;

    private static final int SQUARE_SIZE = 50;
    public static final int ROWS = HEIGHT / SQUARE_SIZE;
    public static final int COLS = WIDTH / SQUARE_SIZE;
    public static final String[] FOODS_IMAGE = new String[]{"/img/ic_cherry.png", "/img/ic_apple.png", "/img/ic_pomegranate.png"};

    private static final int RIGHT = 0;
    public static boolean wallCollision = true;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private GraphicsContext gc;
    // Here im using arraylist
    private List<Point> snakeBody = new ArrayList();
    private Point snakeHead;

    private Food food;
    private boolean gameOver;
    private int currentDirection;
    private int score = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                KeyCode key = event.getCode();
                if (key == KeyCode.RIGHT || key == KeyCode.D) {
                    if (currentDirection != LEFT) {
                        currentDirection = RIGHT;
                    }
                } else if (key == KeyCode.LEFT || key == KeyCode.A) {
                    if (currentDirection != RIGHT) {
                        currentDirection = LEFT;
                    }
                } else if (key == KeyCode.UP || key == KeyCode.W) {
                    if (currentDirection != DOWN) {
                        currentDirection = UP;
                    }
                } else if (key == KeyCode.DOWN || key == KeyCode.S) {
                    if (currentDirection != UP) {
                        currentDirection = DOWN;
                    }
                }
            }
        });

        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        spawnFood();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), e -> run(gc)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void run(GraphicsContext gc) {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font(100));
            gc.fillText("Game Over", 200, HEIGHT / 3);
            return;
        }
        drawBackground(gc);
        drawFood(gc);
        drawSnake(gc);
        drawScore();

        for (int i = snakeBody.size() - 1; i >= 1; i--) {
            snakeBody.get(i).x = snakeBody.get(i - 1).x;
            snakeBody.get(i).y = snakeBody.get(i - 1).y;
        }

        switch (currentDirection) {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
        }
        gameOver();
        eatFood();
    }
    // he im drawing this grid background
    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void spawnFood() {
        start:
        while (true) {
            int foodType = (int)(Window.FOODS_IMAGE.length * Math.random());
            switch (foodType){
                case 0:
                    food = new NormalFood();
                    break;
                case 1:
                    food = new LargeFood ();
                    break;
                case 2:
                    food = new HyperFood ();
                    break;
            }
            for (Point snake : snakeBody) {
                if (snake.getX() == food.x && snake.getY() == food.y) {
                    continue start;
                }
            }
            break;
        }
    }

    private void drawFood(GraphicsContext gc) {
        gc.drawImage(food.getIcon(), food.x * SQUARE_SIZE, food.y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawSnake(GraphicsContext gc) {
        gc.setFill(Color.web("4674E9"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE - 1, SQUARE_SIZE - 1, 35, 35);

        for (int i = 1; i < snakeBody.size(); i++) {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE - 1,
                    SQUARE_SIZE - 1, 20, 20);
        }
    }

    private void moveRight() {
        snakeHead.x++;
        if (!wallCollision){
            snakeHead.x %= COLS;
        }
    }

    private void moveLeft() {
        snakeHead.x--;
        if (!wallCollision){
            snakeHead.x %= COLS;
            snakeHead.x = Math.abs(snakeHead.x);
        }
    }

    private void moveUp() {
        snakeHead.y--;
        if (!wallCollision){
            snakeHead.y %= ROWS;
            snakeHead.y = Math.abs(snakeHead.y);
        }
    }

    private void moveDown() {
        snakeHead.y++;
        if (!wallCollision){
            snakeHead.y %= ROWS;
        }
    }
    public void gameOver() {
        if (wallCollision && (snakeHead.x < 0 || snakeHead.y < 0 || snakeHead.x * SQUARE_SIZE >= WIDTH || snakeHead.y * SQUARE_SIZE >= HEIGHT)) {
            gameOver = true;
        }

        //destroy itself
        for (int i = 1; i < snakeBody.size(); i++) {
            if (snakeHead.x == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY()) {
                gameOver = true;
                break;
            }
        }
    }
    private void eatFood() {
        if (snakeHead.getX() == food.x && snakeHead.getY() == food.y) {
            int energy = food.getScore();
            score += energy * 5;
            food.apply();
            for (int i = 0; i < energy; ++i){
                snakeBody.add (new Point (-1, -1));
            }
            spawnFood();
        }
    }

    private void drawScore() {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font(30));
        gc.fillText("Score: " + score, 10, 35);
    }
    public static void main(String[] args) {
        launch(args);
    }
}