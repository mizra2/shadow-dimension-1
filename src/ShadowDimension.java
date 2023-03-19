import bagel.*;
import bagel.Image;
import bagel.Window;
import bagel.util.Rectangle;
import bagel.util.Point;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;


/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2022
 *
 * Please enter your name below
 * Milad Izra
 */

public class ShadowDimension extends AbstractGame {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DIMENSION";
    private final static String MENU_TEXT_1 = "PRESS SPACE TO START";
    private final static String MENU_TEXT_2 = "USE ARROW KEYS TO FIND GATE";
    private final Image BACKGROUND_IMAGE = new Image("res/background0.png");
    private static Point TOP_LEFT_BOUND;
    private static Point BOTTOM_RIGHT_BOUND;

    // ArrayLists of Walls and Collidables
    private ArrayList<Wall> walls = new ArrayList<>();
    private ArrayList<Collidable> collidables = new ArrayList<Collidable>();

    // Fonts
    private Font healthFont = new Font("res/frostbite.ttf", 30);
    private Font gameStartFont = new Font("res/frostbite.ttf", 40);
    private Font FONT = new Font("res/frostbite.ttf", 75);
    private DrawOptions healthOptions = new DrawOptions();

    private DrawOptions startScreen = new DrawOptions();
    private boolean endGameState = false;
    private boolean startGameState = false;
    private boolean failedGameState = false;
    private Player player;

    private final int MAX_HEALTH = 100;
    private Rectangle gatePortalCollider2D = new Rectangle(new Point(950, 670), 49, 74);
    public ShadowDimension(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
        // Generate the map
        this.readCSV("res/level0.csv");
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDimension game = new ShadowDimension();
        game.run();
    }

    /**
     * Method used to read file and create objects (You can change this
     * method as you wish).
     */
    private void readCSV(String fileName){
        // Read a CSV file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String text;
            int count;
            while((text = br.readLine()) != null) {
                double x, y;
                String cells[] = text.split(",");
                switch (cells[0].toLowerCase()) {
                    case "player":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        this.player = new Player(new Point(x, y));
                        break;

                    case "wall":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        this.walls.add(new Wall(new Point(x, y)));
                        break;

                    case "sinkhole":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        this.collidables.add(new Collidable(new Point(x, y)));
                        break;

                    case "topleft":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        TOP_LEFT_BOUND = new Point(x, y);
                        break;
                    case "bottomright":
                        x = Double.parseDouble(cells[1]);
                        y = Double.parseDouble(cells[2]);
                        BOTTOM_RIGHT_BOUND = new Point(x, y);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a state update.
     * allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        // If game hasn't started yet, draw the start screen
        if (!startGameState) {
            startGameState(input);
            // Game has started
        } else if (startGameState && !endGameState && !failedGameState){
            BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
            playerMover(input);
            drawMap();
            player.drawPlayer();
            drawHealth();
            endGameState();
            // Win game condition
        } else if(endGameState) {
            drawEndGameState();
            // Lost the game state
        } else if (failedGameState && !endGameState) {
            drawFailedGameState();
        }
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }
    }

    public void printInfo(Player player) {
        System.out.format("Sinkhole inflicts 30 damage points on Fae. Fae's Current Health is: %d/100\n", player.getHealth());
    }
    private void startGameState(Input input) {
        FONT.drawString(GAME_TITLE, 260, 250);
        gameStartFont.drawString(MENU_TEXT_1, 350, 440);
        gameStartFont.drawString(MENU_TEXT_2, 350, 480);
        if (input.wasPressed(Keys.SPACE)) {
            startGameState = true;
        }
    }
    // End game screen
    private void drawEndGameState() {
        FONT.drawString("CONGRATULATIONS!", Window.getWidth() / 2.0 - FONT.getWidth("CONGRATULATIONS!") / 2.0, Window.getHeight() / 2.0);
    }
    // Failed game screen
    private void drawFailedGameState() {
        FONT.drawString("GAME OVER!", Window.getWidth() / 2.0 - FONT.getWidth("GAME OVER!") / 2.0, Window.getHeight() / 2.0);
    }

    // If player collides with the portal
    private void endGameState() {
        if (player.onCollision(gatePortalCollider2D)) {
            endGameState = true;
        }
    }

    // Draw the map and it's colidables. Damage dealt logic included here as well
    private void drawMap() {
        for (Wall wall : this.walls) {
            wall.getEntitySprite().drawFromTopLeft(wall.getX(), wall.getY());
        }
        // If player collides with wall, move player to it's old pos.
        for (Wall wall : this.walls) {
            if (player.onCollision(wall.getWallBoxCollider2D())) {
                player.movePlayer(player.getOldPos());
            }
        }
        // draw collideables and checks if player collides with a wall.
        for (Collidable sinkhole: this.collidables) {
            if (!sinkhole.getTrigger()) {
                sinkhole.getEntitySprite().drawFromTopLeft(sinkhole.getX(), sinkhole.getY());
            }
            if (player.onCollision(sinkhole.getBoxCollider2D()) && !(sinkhole.getTrigger())) {
                sinkhole.dealDmg(this.player);
                this.printInfo(this.player);
            }
        }
    }
    private void playerMover(Input input) {
        // Move the player using input, setting the players old position at each step of the way
        if (input.isDown(Keys.LEFT)) {
            player.setLeftSprite();
            player.setOldPoint();
            player.movePlayer(new Point(player.getX() - player.getSpeed(), player.getY()));
        }
        else if (input.isDown(Keys.RIGHT)) {
            player.setRightSprite();
            player.setOldPoint();
            player.movePlayer(new Point(player.getX() + player.getSpeed(), player.getY()));
        }
        else if (input.isDown(Keys.UP)) {
            player.setOldPoint();
            player.movePlayer(new Point(player.getX(), player.getY() - player.getSpeed()));
        }
        else if (input.isDown(Keys.DOWN)) {
            player.setOldPoint();
            player.movePlayer(new Point(player.getX(), player.getY() + player.getSpeed()));
        }
        // If player is over bounds, go back to old pos.
        if (player.getX() < TOP_LEFT_BOUND.x || player.getY() < TOP_LEFT_BOUND.y
                || player.getX() > BOTTOM_RIGHT_BOUND.x || player.getY() > BOTTOM_RIGHT_BOUND.y) {
            player.movePlayer(player.getOldPos());
        }
    }
    // Draws players health on the top left
    private void drawHealth() {
        // If players health is below 0 due to the damage dealt, set it back to 0
        if (player.getHealth() <= 0) {
            player.setHealth(0);
        }
        /** If players health greater than 65, then set it to green, if between 35 & 65 orange,
         if less than 35, set to red **/

        if (player.getHealth() > 65) {
            healthOptions.setBlendColour(0, 0.8, 0.2);
            String healthString = String.format("%d%%", this.player.getHealth());
            healthFont.drawString(healthString, 20, 25, healthOptions);
        } else if (player.getHealth() < 65 && player.getHealth() > 35) {
            healthOptions.setBlendColour(0.9, 0.6, 0);
            String healthString = String.format("%d%%", this.player.getHealth());
            healthFont.drawString(healthString, 20, 25, healthOptions);
        } else {
            healthOptions.setBlendColour(1, 0, 0);
            String healthString = String.format("%d%%", this.player.getHealth());
            healthFont.drawString(healthString, 20, 25, healthOptions);
        }
        // If health less than 0; game over!
        if (player.getHealth() <= 0) {
            failedGameState = true;
        }
    }

}
