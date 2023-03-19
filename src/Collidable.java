import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.*;

public class Collidable extends Entity {

    private static final int SINK_HOLE_DMG = 30;
    private Rectangle collidableBoxCollider2D;

    private boolean hasTriggered;

    public Collidable(Point entityPos) {
        super(entityPos, new Image("res/sinkhole.png"));
        this.collidableBoxCollider2D = new Rectangle(entityPos.x, entityPos.y, getEntitySprite().getWidth() - 10, getEntitySprite().getHeight() - 20);
        this.hasTriggered = false;
    }
    // Deals damage to a player
    public void dealDmg(Player player) {
        player.setHealth(player.getHealth() - SINK_HOLE_DMG);
        if (player.getHealth() < 0) {player.setHealth(0);}
        this.hasTriggered = true;
    }
    public boolean getTrigger() {
        return this.hasTriggered;
    }

    public Rectangle getBoxCollider2D() { return this.collidableBoxCollider2D; }


}
