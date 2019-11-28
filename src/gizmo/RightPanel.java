package gizmo;
import window.*;
import physics.*;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class RightPanel extends AbstractGizmo{
    public RightPanel(AnimationWindow animationWindow) {
        super(animationWindow);
        color= Color.YELLOW;
        r = r*3;
        if(x>animationWindow.getWidth()-r||x<r)
            x = animationWindow.getWidth()/2;
        if(y>animationWindow.getHeight()-r||y<r)
            y = animationWindow.getHeight()/2;
        makeSquare();
    }
    public RightPanel(int x, int y, int r, int degree, AnimationWindow animationWindow) {
        super(x,y,r,degree,animationWindow);
        color=Color.YELLOW;
        makeSquare();
    }
    public void makeSquare(){
        lines.clear();
        corners.clear();
        while(degree>=360) degree-=360;
        Angle angle=findAngleByDegree(degree);
        Vect center=new Vect(new Point(x,y));
        int t = animationWindow.getSizePerUnit() / 2;
        lines.add(Geometry.rotateAround( new LineSegment(x-r,y-t/2,x+r,y-t/2),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x-r,y-t/2,x-r,y+t/2),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x-r,y+t/2,x+r,y+t/2),center,angle));
        lines.add(Geometry.rotateAround( new LineSegment(x+r,y-t/2,x+r,y+t/2),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x-r,y-t/2,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x-r,y+t/2,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+r,y+t/2,0),center,angle));
        corners.add(Geometry.rotateAround( new Circle(x+r,y-t/2,0),center,angle));
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
        int t = animationWindow.getSizePerUnit() / 2;
        Polygon polygon = new Polygon(new int[] { x-r, x-r, x+r, x+r }, new int[] { y-t/2, y+t/2, y+t/2, y-t/2 }, 4);
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(degree), polygon.getBounds().getCenterX(),
                polygon.getBounds().getCenterY());
        Shape transformed = transform.createTransformedShape(polygon);
        g2d.fill(transformed);
    }

    @Override
    public Rectangle[] boundingBoxes() {
        Rectangle[] rectangles=new Rectangle[1];
        int t = animationWindow.getSizePerUnit() / 2;
        rectangles[0]=new Rectangle(x-r,y-t/2,2*r,2*t);
        return rectangles;
    }
}

