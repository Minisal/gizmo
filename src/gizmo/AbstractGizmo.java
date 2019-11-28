package gizmo;
import window.*;
import physics.Angle;
import physics.Circle;
import physics.LineSegment;

import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class AbstractGizmo {

    // x,y is the [CENTER] of the square, r is the radius (that is, from the center to the left border).
    protected int x;
    protected int y;
    protected int r;

    protected boolean isTrack = false;
    protected boolean isBendTrack = false;
    private int oldDegree;
    protected int degree;

    protected AnimationWindow animationWindow;
    protected List<LineSegment> lines;
    protected List<Circle> corners;
    protected Color color;

    public int getX(){ return x; }
    public int getY() { return y; }
    public int getR(){
        return r;
    }
    public int getDegree(){ return degree; }

    public void setX(int x){ this.x = x; }
    public void setY(int y){ this.y = y; }

    public int getLeft(){ return x-r; }
    public int getRight(){ return x+r; }
    public int getTop(){ return y-r; }
    public int getButtom(){ return y+r; }

    public List<LineSegment>getLines(){ return this.lines; }
    public List<Circle>getCorners(){ return this.corners; }



    public boolean isTracker() {
        return isTrack;
    }
    public boolean isBendTracker(){
        return isBendTrack;
    }

    public AbstractGizmo(AnimationWindow animationWindow) {
        this.isTrack = false;
        this.isBendTrack = false;
        this.degree = 0;
        this.animationWindow = animationWindow;
        this.r = animationWindow.getSizePerUnit() / 2;
        this.oldDegree = -1;
        this.lines = new ArrayList<>();
        this.corners = new ArrayList<>();
        // random position, but no intersect with others.
        Rectangle th;
        while (true) {
            Random random = new Random();
            int x = (random.nextInt(animationWindow.getWindowUnitHeight()) + 1) * animationWindow.getSizePerUnit() - animationWindow.getSizePerUnit() / 2;
            int y = (random.nextInt(animationWindow.getWindowUnitWidth()) + 1) * animationWindow.getSizePerUnit() - animationWindow.getSizePerUnit() / 2;

            // 与其它有交集，删除
            th = new Rectangle(x - r, y - r, r + r, r + r);
            if (!animationWindow.hasCoincidenceWithOthers(th)) break;

        }
        this.x = (int) (th.getX() + (th.getWidth() / 2));
        this.y = (int) (th.getY() + (th.getHeight() / 2));
    }

    public AbstractGizmo(int x, int y, int r, int degree, AnimationWindow animationWindow) {
        this.x = x;
        this.y = y;
        this.isTrack = false;
        this.isBendTrack = false;
        this.degree = degree;
        this.animationWindow = animationWindow;
        this.r = r;
        this.oldDegree = -1;
        this.lines = new ArrayList<>();
        this.corners = new ArrayList<>();
    }


    public void moveLeft(){
        if ( x > r )
            x -= animationWindow.getSizePerUnit();
    }
    public void moveRight(){
        if (x < animationWindow.getWidth()-r  )
            x += animationWindow.getSizePerUnit();
    }
    public void moveUp(){
        if ( y > r )
            y -= animationWindow.getSizePerUnit();
    }
    public void moveDown(){
        if ( y < animationWindow.getHeight() -r)
            y += animationWindow.getSizePerUnit();
    }

    public abstract void paint(Graphics g);

    public Rectangle[] boundingBoxes() {
        return new Rectangle[]{new Rectangle(x - r, y - r, 2 * r, 2 * r)};
    }

    public void setIsTrack(boolean isTrack) {
        this.isTrack = isTrack;
    }

    public void setIsBendTrack(boolean isBendTrack){ this.isBendTrack = isBendTrack; }

    public void rotate(int degree) {
        oldDegree = this.degree;
        this.degree += degree;

    }

    public void makeLarger() {
        r += animationWindow.getSizePerUnit();
        if (x > animationWindow.getWidth() - r || x < r
                || y > animationWindow.getHeight() - r || y < r) {
            r -= animationWindow.getSizePerUnit();
        }
    }

    /**
     * @return true if it can make small, (When r<1, it cannot small!)
     */
    public boolean makeSmaller() {
        if (r - animationWindow.getSizePerUnit() <= 0) return false;
        r -= animationWindow.getSizePerUnit();
        return true;
    }

    public void buildMove(int x, int y) {
        this.x = x / AnimationWindow.getSizePerUnit() * AnimationWindow.getSizePerUnit() + AnimationWindow.getSizePerUnit() / 2;
        if (x >= animationWindow.getWidth() - r) {
            this.x = animationWindow.getWidth() - r;
        }
        if (x <= r) {
            this.x = r;
        }
        this.y = y / AnimationWindow.getSizePerUnit() * AnimationWindow.getSizePerUnit() + AnimationWindow.getSizePerUnit() / 2;
        if (y >= animationWindow.getHeight() - r) {
            this.y = animationWindow.getHeight() - r;
        }
        if (y <= r) {
            this.y = r;
        }
    }

    @Override
    public String toString() {
        return " x=" + x + " y=" + y + " r=" + r + " degree" + degree;
    }

    public Angle findAngleByDegree(int degree) {
        if (degree == 0) return Angle.ZERO;
        else if (degree == 45) return Angle.DEG_45;
        else if (degree == 90) return Angle.DEG_90;
        else if (degree == 135) return Angle.DEG_135;
        else if (degree == 180) return Angle.DEG_180;
        else if (degree == 225) return Angle.DEG_225;
        else if (degree == 270) return Angle.DEG_270;
        else if (degree == 315) return Angle.DEG_315;
        else {
            throw new IllegalArgumentException("degree hasn't been implement yet");
        }
    }

}
