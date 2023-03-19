import bagel.*;
import bagel.util.Rectangle;
import bagel.util.Point;
public class Wall extends Entity {
    private Rectangle wallBoxCollider2D;

    public Wall(Point point) {
        super(point, new Image("res/wall.png"));
        wallBoxCollider2D = new Rectangle(getX(), getY(), getEntitySprite().getWidth() - 10, getEntitySprite().getHeight() - 20);
    }

    public Rectangle getWallBoxCollider2D() {return this.wallBoxCollider2D;}

}
