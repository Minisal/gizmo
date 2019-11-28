package gizmo;
import physics.*;
import window.*;

import java.awt.*;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.Graphics;

public class AbsorberGizmo extends AbstractGizmo {

    public AbsorberGizmo(AnimationWindow animationWindow) {
        super(animationWindow);
        makeCircle();
    }
    public AbsorberGizmo(int x, int y, int r, int degree, AnimationWindow animationWindow) {
        super(x,y,r,degree,animationWindow);
        makeCircle();
    }
    public void makeCircle(){
        lines.clear();
        corners.clear();
        while(degree>=360) {
            degree -= 360;
        }
        Angle angle=findAngleByDegree(degree);
        Vect center=new Vect(new Point(x,y));
        corners.add(Geometry.rotateAround( new Circle(x,y,r),center,angle));
    }



    @Override
    public void paint(Graphics g) {

        Graphics2D g2d=(Graphics2D)g;
        g2d.setColor(Color.GRAY);
        makeCircle();
        for(LineSegment lineSegment:lines){
            Line2D.Double d=lineSegment.toLine2D();
            g2d.draw(d);
        }
        for(Circle corner:corners){
            Ellipse2D d= corner.toEllipse2D();
            Ellipse2D.Double dd=new Ellipse2D.Double(d.getX(),d.getY(),d.getHeight(),d.getWidth());
            g2d.fill(dd);
        }

    }
}
