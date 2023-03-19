import bagel.*;
import bagel.util.Rectangle;
import bagel.util.Point;

public class Entity {

    private Point entityPos;
    private Image entitySprite;

    public Entity(Point entityPos, Image entitySprite) {
        this.entityPos = entityPos;
        this.entitySprite = entitySprite;
    }
    public double getX() {
        return entityPos.x;
    }
    public double getY() {
        return entityPos.y;
    }

    public void setEntityPos(Point point) {
        this.entityPos = point;
    }
    public void setEntitySprite(Image image) {
        this.entitySprite = image;
    }
    public Point getEntityPos() {return this.entityPos;}

    public Image getEntitySprite() {return this.entitySprite;}



}
