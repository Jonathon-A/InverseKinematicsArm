

package inversekinematicarm;

import java.awt.geom.Point2D;



public class Segment {

    private final double length;
    private double angle;
    private Point2D.Double Start;
    private Point2D.Double End;

    public Segment(double length, double angle, double x, double y) {
        this.length = length;
        this.angle = angle;
        this.Start = new Point2D.Double(x, y);
        End = new Point2D.Double(x, y);
    }
    
    public void CalculateEnd(){
        double DeltaX = length * Math.cos(angle);
        double DeltaY = length * Math.sin(angle);
        End.setLocation(Start.x + DeltaX, Start.y + DeltaY);
        
    }
    
    public void SetStart( Point2D.Double start){
        Start.setLocation(start.x, start.y);
    }
    
    public void Folow(double TargetX, double TargetY){
        
        double x = TargetX - Start.x;
        double y = TargetY - Start.y;
        
        angle = Math.atan2(y, x);
        
        //get unit vector
        //times by length
        
        double Size = Math.sqrt(x * x + y * y);
        
        x *= -1;
        y *= -1;
        
        x = x/Size * length;
        y = y/Size * length;
        
        Start.setLocation(TargetX + x, TargetY + y);
    }

    public Point2D.Double getStart() {
        return Start;
    }

    public Point2D.Double getEnd() {
        return End;
    }

    
    
}
