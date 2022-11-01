package inversekinematicarm;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class InverseKinematicArm {

    private static JLabel Display;
    private static ArrayList<Segment> Segments;

    private static int TotalLength = 350;
    private static int SegmentCount = 4;
    private static int SegmentSize = 4;
    private static int SegmentJointSize = 6;
    private static boolean ProportionalSegments = false;
    private static boolean ProportionalJoints = false;
    private static Point2D.Double base = new Point2D.Double(400, 400);

    public static void main(String[] args) {
        JFrame window = new JFrame("Inverse Kinematic Arm");
        window.setDefaultCloseOperation(EXIT_ON_CLOSE);
        window.setSize(816, 905);
        window.setLayout(null);
        window.getContentPane().setBackground(Color.DARK_GRAY);

        Segments = new ArrayList<>();

        Display = new JLabel() {
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

                    if (ProportionalSegments) {
                        Size += i;
                    }

                    gx.setStroke(new BasicStroke(Size, BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));

                    gx.drawLine(x1, y1, x2, y2);

                    int JointSize = SegmentJointSize;

                    if (ProportionalJoints) {
                        JointSize += i;
                    }

                    gx.fillOval(x2 - JointSize / 2, y2 - JointSize / 2, JointSize, JointSize);

                }
                int JointSize = SegmentJointSize;
                if (ProportionalJoints) {
                    JointSize += Segments.size();
                }

                gx.fillOval((int) base.getX() - JointSize / 2, (int) base.getY() - JointSize / 2, JointSize, JointSize);
            }
        };

        Display.setSize(800, 800);
        Display.setLayout(null);
        Display.setBorder(BorderFactory.createLineBorder(Color.white, 3));
        window.add(Display);

        SpinnerModel sizeModal = new SpinnerNumberModel(350, 0, 1000, 1);
        JLabel sizeLabel = new JLabel("0 <= Length <= 1000");
        sizeLabel.setBounds(10, 800, 120, 30);
        sizeLabel.setForeground(Color.WHITE);
        window.add(sizeLabel);
        JSpinner sizeSpinner = new JSpinner(sizeModal);
        sizeSpinner.setBounds(10, 825, 120, 30);
        window.add(sizeSpinner);
        sizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                TotalLength = Integer.valueOf(sizeSpinner.getValue().toString());
                generateSegments();
            }
        });

        SpinnerModel SegmentCountModal = new SpinnerNumberModel(4, 1, 1000, 1);
        JLabel SegmentCountLabel = new JLabel("1 <= Segment Count <= 1000");
        SegmentCountLabel.setBounds(140, 800, 170, 30);
        SegmentCountLabel.setForeground(Color.WHITE);
        window.add(SegmentCountLabel);
        JSpinner SegmentCountSpinner = new JSpinner(SegmentCountModal);
        SegmentCountSpinner.setBounds(140, 825, 170, 30);
        window.add(SegmentCountSpinner);
        SegmentCountSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SegmentCount = Integer.valueOf(SegmentCountSpinner.getValue().toString());
                generateSegments();
            }
        });

        SpinnerModel SegmentSizeModal = new SpinnerNumberModel(4, 0, 1000, 1);
        JLabel SegmentSizeLabel = new JLabel("0 <=  Segment Size <= 1000");
        SegmentSizeLabel.setBounds(320, 800, 160, 30);
        SegmentSizeLabel.setForeground(Color.WHITE);
        window.add(SegmentSizeLabel);
        JSpinner SegmentSizeSpinner = new JSpinner(SegmentSizeModal);
        SegmentSizeSpinner.setBounds(320, 825, 160, 30);
        window.add(SegmentSizeSpinner);
        SegmentSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SegmentSize = Integer.valueOf(SegmentSizeSpinner.getValue().toString());
                generateSegments();
            }
        });

        JLabel ProportionalSegmentLabel = new JLabel("     ∝");
        ProportionalSegmentLabel.setBounds(490, 800, 45, 30);
        ProportionalSegmentLabel.setForeground(Color.WHITE);
        window.add(ProportionalSegmentLabel);
        JButton ProportionalSegmentButton = new JButton("N");
        ProportionalSegmentButton.setBounds(490, 825, 45, 30);
        window.add(ProportionalSegmentButton);
        ProportionalSegmentButton.addActionListener((final ActionEvent ae) -> {
            ProportionalSegmentButton.setText(ProportionalSegmentButton.getText().equals("Y") ? "N" : "Y");
            ProportionalSegments = !ProportionalSegments;
        });

        SpinnerModel SegmentJointSizeModal = new SpinnerNumberModel(6, 0, 1000, 1);
        JLabel SegmentJointSizeLabel = new JLabel("0 <=  Segment Joint Size <= 1000");
        SegmentJointSizeLabel.setBounds(545, 800, 190, 30);
        SegmentJointSizeLabel.setForeground(Color.WHITE);
        window.add(SegmentJointSizeLabel);
        JSpinner SegmentJointSizeSpinner = new JSpinner(SegmentJointSizeModal);
        SegmentJointSizeSpinner.setBounds(545, 825, 190, 30);
        window.add(SegmentJointSizeSpinner);
        SegmentJointSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                SegmentJointSize = Integer.valueOf(SegmentJointSizeSpinner.getValue().toString());
                generateSegments();
            }
        });

        JLabel ProportionalJointsLabel = new JLabel("     ∝");
        ProportionalJointsLabel.setBounds(745, 800, 45, 30);
        ProportionalJointsLabel.setForeground(Color.WHITE);
        window.add(ProportionalJointsLabel);
        JButton ProportionalJointsButton = new JButton("N");
        ProportionalJointsButton.setBounds(745, 825, 45, 30);
        window.add(ProportionalJointsButton);
        ProportionalJointsButton.addActionListener((final ActionEvent ae) -> {
            ProportionalJointsButton.setText(ProportionalJointsButton.getText().equals("Y") ? "N" : "Y");
            ProportionalJoints = !ProportionalJoints;
        });

        generateSegments();

        window.setVisible(true);

        StartSimulation();

    }

    private static void generateSegments() {
        Segments.clear();
        for (int i = 0; i < SegmentCount; i++) {
            Segments.add(new Segment(Math.floor((double) TotalLength / (double) SegmentCount), 0, 0, 0));
        }
        Display.repaint();
    }

    private static void StartSimulation() {

        int PrevX = 0;
        int PrevY = 0;

        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(InverseKinematicArm.class.getName()).log(Level.SEVERE, null, ex);
            }

            int x = PrevX;
            int y = PrevY;

            //first segment points at mouse
            if (Display.getMousePosition() != null) {
                try {
                    x = Display.getMousePosition().x;
                    y = Display.getMousePosition().y;

                    PrevX = x;
                    PrevY = y;
                } catch (Exception e) {

                }
            }

            try {
                Segment seg = Segments.get(0);

                seg.Folow(x, y);

                seg.CalculateEnd();

                //other segments follow using inverse kinematics
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
            } catch (Exception e) {

            }

        }
    }

}
