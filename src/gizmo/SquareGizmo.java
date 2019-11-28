package gizmo;
import window.*;
import physics.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class SquareGizmo extends AbstractGizmo {

    public SquareGizmo(AnimationWindow animationWindow) {
        super(animationWindow);
        color=Color.ORANGE;
        makeSquare();
    }
    public SquareGizmo(int x, int y, int r, int degree, AnimationWindow animationWindow) {
        super(x,y,r,degree,animationWindow);
        color=Color.ORANGE;
        makeSquare();
    }
    public void makeSquare(){
        lines.clear();
        corners.clear();
        while(degree>=360) degree-=360;
        Angle angle=findAngleByDegree(degree);
        Vect center=new Vect(new Point(x,y));
        lines.add(Geometry.rotateAround( new LineSegment(x-r,y-r,x+r,y-r),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x-r,y-r,x-r,y+r),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x-r,y+r,x+r,y+r),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x+r,y-r,x+r,y+r),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x-r,y-r,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x-r,y+r,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+r,y+r,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+r,y-r,0),center,angle));
    }



    @Override
    public void paint(Graphics g) {

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
        Polygon polygon = new Polygon(new int[] { x-r, x-r, x+r, x+r }, new int[] { y-r, y+r, y+r, y-r }, 4);
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(degree), polygon.getBounds().getCenterX(),
                polygon.getBounds().getCenterY());
        Shape transformed = transform.createTransformedShape(polygon);
        g2d.fill(transformed);
    }
}
