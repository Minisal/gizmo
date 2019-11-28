package window;
import gizmo.*;
import physics.Circle;
import physics.Geometry;
import physics.LineSegment;
import physics.Vect;
import java.awt.*;

public class BouncingBall {

    // private final static double VELOCITY_STEP = 0.5f;
    private double x = 10.0f;
    private double y = 10.0f;
    private int radius = 6;
    private double gravity;
    private Vect velocity;
    private Vect friction;

    private Color color = new Color(255, 0, 0);

    // Keep track of the animation window that will be drawing this ball.
    private AnimationWindow win;


    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }
    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
    }

    public double getLeft() {
        return x-radius;
    }
    public double getRight() {
        return x+radius;
    }
    public double getTop() {
        return y-radius;
    }
    public double getButtom() {
        return y+radius;
    }


    public int getRadius() {
        return radius;
    }
    public Vect getVect(){
        return new Vect(this.x, this.y);
    }

    public void setVelocity(Vect velocity) {
        this.velocity = velocity;
    }



    /**
     * Constructor.
     * @param win Animation window that will be drawing this ball.
     */
    public BouncingBall(AnimationWindow win) {
        this.win = win;
        this.velocity=new Vect(5.0,0.0);
        gravity=5;
        friction=new Vect(1.0,1.0);
    }

    public void remove(){
        this.x = -100;
        this.y = -100;
        this.velocity=new Vect(5.0,0.0);
        win.setMode(false);
    }

    public void move(){
        double vx = this.velocity.x();
        double vy = this.velocity.y();
        this.velocity=new Vect(vx,vy);
    }


    public void inHorizontalTrack(){
        double vx = this.velocity.x();
        this.velocity=new Vect(vx,0.0);
    }

    public void inVerticalTrack(){
        double vy = this.velocity.y();
        this.velocity = new Vect(0.0, vy);
    }

    public void inInflectionTrack(int degree){
        double vx = this.velocity.x();
        double vy = this.velocity.y();
        double v = vx+vy;
        if ( degree == 0 || degree == 180) // right
            this.velocity = new Vect(vy, vx);
        else if (degree == 90 || degree == 270) // buttom
            this.velocity = new Vect(-vy, vx);
    }

    /**
     * @modifies this
     * @effects Moves the ball according to its velocity.  Reflections off
     * walls cause the ball to change direction.
     */
    public void move(int inteval) {
        double intevals=inteval*1.0f/100;
        x +=  velocity.x() * intevals;
        y +=  velocity.y() * intevals;

        double newVx = velocity.x()  ;
        double newVy = velocity.y();
        // encounter the left boundary
        if (x <= radius) {
            x = radius;
            newVx=-newVx;
        }
        // encounter the right boundary
        if (x >= win.getWidth() - radius) {
            x = win.getWidth() - radius;
            newVx=-newVx;
        }

        // encounter the top boundary
        if (y <= radius) {
            y = radius;
            newVy=-newVy;
        }
        // encounter the buttom boundary
        if (y >= win.getHeight() - radius) {
            y = win.getHeight() - radius;
            newVy=-newVy;
        }
        newVx = newVx  *friction.x();
        newVy = (newVy+ gravity*intevals )*friction.y();
        if(newVx==0) newVx++;
        if(newVy==0) newVy++;
        this.velocity = new Vect(newVx, newVy);
    }

    public CollisionInfo detectCollision(java.util.List<AbstractGizmo> gizmoList) {
        Circle c=new Circle(x,y,radius);
        //only line collision, fill others here!
        for (AbstractGizmo gizmo : gizmoList) {
            for (LineSegment lineSegment : gizmo.getLines()) {
                if (Geometry.timeUntilWallCollision(lineSegment,c,velocity)<=0.5f) {
                    return new CollisionInfo(gizmo, lineSegment);
                }
            }

            for(Circle circle : gizmo.getCorners()){
                if(Geometry.timeUntilCircleCollision(circle,c,velocity)<=0.5f){
                    return new CollisionInfo(gizmo, circle);
                }
            }
        }
        return null;
    }
    public void dealCollision(CollisionInfo collisionInfo) {
        AbstractGizmo abstractGizmo = collisionInfo.getGizmo();
        Vect newVect;
        LineSegment lineSegment;
        Circle circle;
        if(collisionInfo.getLineSegment() != null)
        {
            lineSegment = collisionInfo.getLineSegment();
            // Geometry.timeUntilWallCollision();
            newVect = Geometry.reflectWall(lineSegment, this.velocity);
            this.setVelocity(newVect);
        }

        else if(collisionInfo.getCircle() != null)
        {
            circle = collisionInfo.getCircle();
            newVect = Geometry.reflectCircle(circle.getCenter(), this.getVect(), this.velocity);
            this.setVelocity(newVect);
        }
    }

    /**
     * @modifies the Graphics object <g>.
     * @effects paints a circle on <g> reflecting the current position
     * of the ball.
     * @param g Graphics context to be used for drawing.
     */
    public void paint(Graphics g) {
        Rectangle clipRect = g.getClipBounds();
        if (clipRect.intersects(this.boundingBox())) {
            g.setColor(color);
            g.fillOval((int)x - radius, (int)y - radius, radius + radius, radius
                    + radius);
        }
    }

    /**
     * @return the smallest rectangle that completely covers the current
     * position of the ball.
     */
    public Rectangle boundingBox() {
        // a Rectangle is the x,y for the upper left corner and then the width and height
        return new Rectangle((int)x - radius - 1, (int)y - radius - 1, radius + radius + 2,
                radius + radius + 2);
    }


    public void resetPosition() {
        x=0;
        y=0;
        velocity=new Vect(5.0,0.0);
    }


}
