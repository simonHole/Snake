import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Panel extends JPanel implements ActionListener {

    Dimension dimension = new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT);

    // Resolution of our game
    final static int SCREEN_WIDTH = 1024;
    final static int SCREEN_HEIGHT = 768;

    // This is unit which define size of single object
    final static int UNIT_SIZE = 32;

    // How many objects we can have on our screen
    final static int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;

    // Delay of our Timer
    final static int DELAY = 75;


    // Co-ordinates every single element of snake's body
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 2;

    // Counter which count how many fruits ate snake.
    int fruitsEaten;
    final int SCORE_MULTIPLE = 10;

    // Co-ordinates generated apple
    int fruitX;
    int fruitY;

    // Direction which snake is moving (init is 'R' like right)
    char snakeDirection = 'R';

    // At the start snake didn't move
    boolean move = false;

    Timer timer;
    Random random;

    Panel(){
        random = new Random();
        this.setPreferredSize(dimension);
        this.setBackground(new Color(0x111111));
        this.setFocusable(true);
        this.addKeyListener(new ControlKeys());
        startGame();
    }

    public void startGame(){
        newFruit();
        move = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        if (move) {
            // Draw width dimension lines
            for (int i = 0; i < SCREEN_WIDTH / UNIT_SIZE; i++) {
                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            }
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }

            g.setColor(new Color(0xAA2222));
            g.fillOval(fruitX, fruitY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake's head and tail
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

                g.setColor(new Color(0xFF0000));
                g.setFont(new Font("Arial",Font.BOLD,24));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + fruitsEaten * SCORE_MULTIPLE,
                        (SCREEN_WIDTH - metrics.stringWidth("Score: " + fruitsEaten * SCORE_MULTIPLE)) / 2,
                        g.getFont().getSize());
            }
        }

        else{
            gameOver(g);
        }
    }

    public void newFruit(){
        fruitX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        fruitY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    public void moveSnake(){
        for(int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

            // Switch snake's move direction { U = up, D = down, L = left, R = right }
        switch (snakeDirection) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }
        }

    public void checkFruit(){
        if((x[0] == fruitX) && (y[0] == fruitY)){
            bodyParts++;
            fruitsEaten++;
            newFruit();
        }
    }

    public void checkCrash(){
        // This method check if head crash with body
        for(int i = bodyParts; i > 0; i--){
            if (x[0] == x[i] && y[0] == y[i]) {
                move = false;
                break;
            }
        }
        // LEFT CORNER CRASH
        if(x[0] < 0){
            move = false;
        }

        // RIGHT CORNER CRASH
        if(x[0] > SCREEN_WIDTH){
            move = false;
        }

        // TOP CORNER CRASH
        if(y[0] < 0){
            move = false;
        }

        // BOTTOM CORNER CRASH
        if(y[0] > SCREEN_HEIGHT){
            move = false;
        };
    }

    public void gameOver(Graphics g){
        // Game Over text
        g.setColor(new Color(0xFF0000));
        g.setFont(new Font("Arial",Font.BOLD,48));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over, Your score is: " + fruitsEaten * SCORE_MULTIPLE,
                (SCREEN_WIDTH - metrics.stringWidth("Game Over, Your score is" + fruitsEaten * SCORE_MULTIPLE)) / 2,
                (SCREEN_HEIGHT / 2));
    }

    // ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        if(move){
            moveSnake();
            checkFruit();
            checkCrash();
        }
        repaint();
    }

    public class ControlKeys extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e){

            // Switch direction of our snakes with check if we didnt change direction to counter

            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(snakeDirection != 'R'){
                        snakeDirection = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(snakeDirection != 'L'){
                        snakeDirection = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(snakeDirection != 'D'){
                        snakeDirection = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(snakeDirection != 'U'){
                        snakeDirection = 'D';
                    }
                    break;
            }
        }
    }
}
