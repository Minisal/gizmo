package gizmo;
import window.*;
import physics.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class BendTrackGizmo extends AbstractGizmo {


    public BendTrackGizmo(AnimationWindow animationWindow){
        super(animationWindow);
        color = Color.BLACK;
        this.setIsBendTrack(true);
        makeSquare();
    }

    public BendTrackGizmo(int x, int y, int r, int degree, boolean crashMove, AnimationWindow animationWindow){
        super(x,y,r,degree,crashMove,animationWindow);
        color=Color.BLACK;
        this.setIsBendTrack(true);
        makeSquare();
    }
    public void makeSquare(){
        lines.clear();
        corners.clear();
        while(degree>=360) degree-=360;
        Angle angle = findAngleByDegree(degree);
        Vect center = new Vect(new Point(x,y));
        int t = r/2; // halfUnit
        lines.add(Geometry.rotateAround( new LineSegment(x-t,y-r,x+t,y-r),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x+t,y-r,x+t,y-t),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x+t,y-t,x+r,y-t),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x+r,y-t,x+r,y+t),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x+r,y+t,x-t,y+t),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x-t,y+t,x-t,y-r),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x-t,y-r,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+t,y-r,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+t,y-t,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+r,y-t,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+r,y+t,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x-t,y+t,0),center,angle));
    }

    @Override
    public Rectangle[] boundingBoxes() {
        Rectangle[] rectangles=new Rectangle[2];
        rectangles[0]=new Rectangle(x-r/2,y-r,r,r/2);
        rectangles[1]=new Rectangle(x-r/2,y-r/2,r+r/2,r);
        return rectangles;
    }

    @Override
    public void paint(Graphics g) {

        Rectangle clipRect = g.getClipBounds();
        Graphics2D g2d=(Graphics2D)g;
        g2d.setColor(color);
        makeSquare();
        for(LineSegment lineSegment:lines){
            Line2D.Double d=lineSegment.toLine2D();
            g2d.draw(d);
        }
        for(Circle corner:corners){
            Ellipse2D d= corner.toEllipse2D();
            g2d.draw(d);
        }
        int t = r/2; // halfUnit
        Polygon polygon = new Polygon(new int[] { x-t,x+t,x+t,x+r,x+r,x-t }, new int[] { y-r,y-r,y-t,y-t,y+t,y+t }, 6);
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(degree), x, y);
        Shape transformed = transform.createTransformedShape(polygon);
        g2d.fill(transformed);
    }
}

