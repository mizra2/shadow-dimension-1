import bagel.*;
import bagel.util.Rectangle;
import bagel.util.Point;
public class Player extends Entity {
    private int health;
    private Rectangle playerBoxCollider2D;
    private Point oldPos;
    private final static double PLAYER_SPEED = 2;

    public Player(Point point) {
        super(point, new Image("res/faeRight.png"));
        this.health = 100;
        playerBoxCollider2D = getEntitySprite().getBoundingBox();
        this.oldPos = new Point(0, 0);
    }
    // Set player health
    public void setHealth(int health) {this.health = health;}
    // Set image to right fae
    public void setRightSprite() { setEntitySprite(new Image("res/faeRight.png")); }
    // Set image to left fae
    public void setLeftSprite() { setEntitySprite(new Image("res/faeLeft.png")); }

    public int getHealth() {return this.health;}

    public double getSpeed() {return PLAYER_SPEED;}
    // Returns true if player intersects a rectangle object
    public boolean onCollision(Rectangle object) {
        return this.playerBoxCollider2D.intersects(object);
    }

    // Move player and it's rectangle / box collider
    public void movePlayer(Point newPos) {
        setEntityPos(newPos);
        this.playerBoxCollider2D.moveTo(getEntityPos());
    }
    public void drawPlayer() {
        getEntitySprite().drawFromTopLeft(getX(), getY());
    }


    public void setOldPoint() {
        oldPos = getEntityPos();
    }

    public Point getOldPos() { return this.oldPos; }


}
