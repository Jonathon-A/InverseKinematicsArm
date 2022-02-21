package inversekinematicarm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;

public class InverseKinematicArm {

    public static void main(String[] args) {
        JFrame window = new JFrame("Inverse Kinematic Arm");
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setSize(818, 847);
        window.setLayout(null);
        window.getContentPane().setBackground(Color.DARK_GRAY);
        window.setVisible(true);
        //window.repaint();

        ArrayList<Segment> Segments = new ArrayList<>();

        int TotalLength = 350;
        int SegmentCount = 4;
        int SegmentSize = 4;
        int SegmentJointSize = 6;
        boolean ProportionalSegment = true;
        boolean ProportionalJoints = true;
        Point2D.Double base = new Point2D.Double(400, 400);

        for (int i = 0; i < SegmentCount; i++) {
            Segments.add(new Segment(TotalLength / SegmentCount, 0, 0, 0));
        }

        JLabel Display = new JLabel() {
            @Override
            public void paintComponent(final Graphics g) {
                final Graphics2D gx = (Graphics2D) g;

                gx.setColor(Color.white);
                //ANTIALIASING
                gx.addRenderingHints(new RenderingHints(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON));

                //render segments
                for (int i = 0; i < Segments.size(); i++) {

                    int x1 = (int) Math.round(Segments.get(i).getStart().x);
                    int y1 = (int) Math.round(Segments.get(i).getStart().y);
                    int x2 = (int) Math.round(Segments.get(i).getEnd().x);
                    int y2 = (int) Math.round(Segments.get(i).getEnd().y);

                    int Size = SegmentSize;

                    if (ProportionalSegment) {
                        Size += i;
                    }

                    gx.setStroke(new BasicStroke(Size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

                    gx.drawLine(x1, y1, x2, y2);

                    int JointSize = SegmentJointSize;

                    if (ProportionalJoints) {
                        JointSize += i;
                    }

                    //  gx.drawOval(x2 - JointSize / 2, y2 - JointSize / 2, JointSize, JointSize);
                    gx.fillOval(x2 - JointSize / 2, y2 - JointSize / 2, JointSize, JointSize);

                }

            }
        };

        Display.setSize(800, 800);
        Display.setLayout(null);
        Display.setBorder(BorderFactory.createLineBorder(Color.white, 3));
        window.add(Display);

        int PrevX = 0;
        int PrevY = 0;

        

        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(InverseKinematicArm.class.getName()).log(Level.SEVERE, null, ex);
            }
            //first segment points at mouse
            if (Display.getMousePosition() != null) {

                Segment seg = Segments.get(0);

                try {
                    double x = Display.getMousePosition().x;
                    double y = Display.getMousePosition().y;

                    seg.Folow(x, y);

                    seg.CalculateEnd();

                    PrevX = Display.getMousePosition().x;
                    PrevY = Display.getMousePosition().y;
                } catch (Exception e) {
                    seg.Folow(PrevX, PrevY);

                    seg.CalculateEnd();

                    System.out.println(e);
                }
                //othe segments follow using inverse kinematics
                for (int i = 1; i < Segments.size(); i++) {
                    Segments.get(i).Folow(Segments.get(i - 1).getStart().x, Segments.get(i - 1).getStart().y);
                    Segments.get(i).CalculateEnd();
                }

                //shift all segments to attach to base
                Segments.get(Segments.size() - 1).SetStart(base);
                Segments.get(Segments.size() - 1).CalculateEnd();

                for (int i = Segments.size() - 2; i >= 0; i--) {
                    Segments.get(i).SetStart(Segments.get(i + 1).getEnd());
                    Segments.get(i).CalculateEnd();
                }

                Display.repaint();
            }

        }

    }

}
