import java.awt.*;
import javax.swing.*;


public class TrafficLightSimulation implements Runnable {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new TrafficLightSimulation());
    }

    private JFrame frame;
    private TrafficLightModel model;
    private Animation animation;
    private DrawingPanel drawingPanel;
    private ControlPanel controlPanel;

    public TrafficLightSimulation() {
        model = new TrafficLightModel();
    }

    @Override
    public void run() {
        frame = new JFrame("Traffic Signal Control System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(18, 18, 18));

        drawingPanel = new DrawingPanel(model);
        controlPanel = new ControlPanel(model, this);

        frame.add(drawingPanel, BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.EAST);

        frame.setSize(1200, 720);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /* ================= CONTROL ================= */

    public void startSimulation() {
        if (animation == null) {
            animation = new Animation(model, this);
            new Thread(animation).start();
        } else {
            animation.resumeSimulation(); // RESUME after stop
        }
    }

    public void stopSimulation() {
        if (animation != null) {
            animation.pauseSimulation();
            model.setLightOn(-1);
            updateTimer(0);
            controlPanel.setStatusNormal();
            repaintUI();
        }
    }

    public void updateTimer(int seconds) {
        controlPanel.setTimer(seconds);
    }

    public void repaintUI() {
        drawingPanel.repaint();
    }
}

/* ================= ENUM ================= */

enum TrafficDensity { LOW, MEDIUM, HIGH }

/* ================= MODEL ================= */

class TrafficLightModel {

    private TrafficLight[] lights;
    private TrafficDensity density = TrafficDensity.MEDIUM;
    private boolean emergency = false;

    public TrafficLightModel() {
        lights = new TrafficLight[]{
                new TrafficLight(Color.RED, 30),
                new TrafficLight(Color.YELLOW, 10),
                new TrafficLight(Color.GREEN, 20)
        };
    }

    public void setLightOn(int index) {
        for (TrafficLight l : lights) l.setOn(false);
        if (index >= 0) lights[index].setOn(true);
    }

    public boolean isLightOn(int index) {
        return lights[index].isOn();
    }

    public Color getColor(int index) {
        return lights[index].getColor();
    }

    public int getDelay(int index) {
        if (index == 2) {
            switch (density) {
                case LOW: return 15;
                case HIGH: return 50;
                default: return 30;
            }
        }
        return lights[index].getDelay();
    }

    public void setDensity(TrafficDensity d) {
        density = d;
    }

    public void toggleEmergency() {
        emergency = !emergency;
    }

    public boolean isEmergency() {
        return emergency;
    }
}

/* ================= ENTITY ================= */

class TrafficLight {
    private boolean on;
    private int delay;
    private final Color color;

    public TrafficLight(Color c, int d) {
        color = c;
        delay = d;
    }

    public boolean isOn() { return on; }
    public void setOn(boolean v) { on = v; }
    public int getDelay() { return delay; }
    public Color getColor() { return color; }
}

/* ================= THREAD ================= */

class Animation implements Runnable {

    private TrafficLightModel model;
    private TrafficLightSimulation frame;

    private boolean paused = false;
    private boolean blink = true;
    private int index = 2;

    public Animation(TrafficLightModel model, TrafficLightSimulation frame) {
        this.model = model;
        this.frame = frame;
    }

    @Override
    public void run() {
        while (true) {

            if (paused) {
                sleep(300);
                continue;
            }

            if (model.isEmergency()) {
                if (blink) model.setLightOn(2);
                else model.setLightOn(-1);

                blink = !blink;
                frame.updateTimer(0);
                frame.repaintUI();
                sleep(500);
                continue;
            }

            model.setLightOn(index);
            frame.repaintUI();

            int time = model.getDelay(index);
            while (time > 0 && !paused && !model.isEmergency()) {
                frame.updateTimer(time);
                sleep(1000);
                time--;
            }

            index = (index == 0) ? 2 : index - 1;
        }
    }

    public void pauseSimulation() {
        paused = true;
    }

    public void resumeSimulation() {
        paused = false;
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (Exception ignored) {}
    }
}

/* ================= VIEW: SIGNAL ================= */

class DrawingPanel extends JPanel {

    private TrafficLightModel model;

    public DrawingPanel(TrafficLightModel model) {
        this.model = model;
        setBackground(new Color(20, 20, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int boxW = 220, boxH = 540;
        int x = (getWidth() - boxW) / 2;
        int y = 60;

        g2.setColor(Color.BLACK);
        g2.fillRoundRect(x, y, boxW, boxH, 40, 40);

        int cx = x + boxW / 2;
        int cy = y + 110;
        int r = 50;

        for (int i = 0; i < 3; i++) {
            g2.setColor(model.isLightOn(i)
                    ? model.getColor(i)
                    : model.getColor(i).darker().darker());
            g2.fillOval(cx - r, cy - r, r * 2, r * 2);
            cy += 160;
        }
    }
}

/* ================= VIEW: CONTROL PANEL ================= */

class ControlPanel extends JPanel {

    private JLabel timerLabel;
    private JLabel statusLabel;

    public ControlPanel(TrafficLightModel model, TrafficLightSimulation frame) {

        setPreferredSize(new Dimension(300, 0));
        setBackground(new Color(25, 25, 25));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        JLabel title = new JLabel("CONTROL PANEL");
        title.setForeground(Color.LIGHT_GRAY);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel(" ", SwingConstants.CENTER);
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setForeground(Color.GREEN);
        timerLabel.setFont(new Font("Consolas", Font.BOLD, 42));
        timerLabel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        timerLabel.setMaximumSize(new Dimension(220, 80));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("NORMAL MODE");
        statusLabel.setForeground(Color.GRAY);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JComboBox<TrafficDensity> densityBox =
                new JComboBox<>(TrafficDensity.values());
        densityBox.setMaximumSize(new Dimension(220, 38));
        densityBox.setBackground(new Color(40, 40, 40));
        densityBox.setForeground(Color.WHITE);

        RoundedButton start =
                new RoundedButton("START", new Color(39, 174, 96));
        RoundedButton stop =
                new RoundedButton("STOP", new Color(192, 57, 43));
        RoundedButton emergency =
                new RoundedButton("EMERGENCY", new Color(243, 156, 18));

        start.addActionListener(e -> frame.startSimulation());
        stop.addActionListener(e -> frame.stopSimulation());
        emergency.addActionListener(e -> {
            model.toggleEmergency();
            if (model.isEmergency()) {
                statusLabel.setText("🚨 EMERGENCY MODE ACTIVE");
                statusLabel.setForeground(Color.RED);
            } else {
                statusLabel.setText("✔ EMERGENCY MODE ENDED");
                statusLabel.setForeground(Color.GREEN);
            }
        });

        densityBox.addActionListener(e ->
                model.setDensity((TrafficDensity) densityBox.getSelectedItem()));

        add(title);
        add(Box.createVerticalStrut(20));
        add(timerLabel);
        add(Box.createVerticalStrut(10));
        add(statusLabel);
        add(Box.createVerticalStrut(30));
        add(label("Traffic Density"));
        add(Box.createVerticalStrut(8));
        add(densityBox);
        add(Box.createVerticalStrut(40));
        add(start);
        add(Box.createVerticalStrut(12));
        add(stop);
        add(Box.createVerticalStrut(12));
        add(emergency);
    }

    private JLabel label(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(Color.GRAY);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    public void setTimer(int seconds) {
        timerLabel.setText(seconds > 0 ? seconds + " s" : " ");
    }

    public void setStatusNormal() {
        statusLabel.setText("NORMAL MODE");
        statusLabel.setForeground(Color.GRAY);
    }
}

/* ================= ROUNDED BUTTON ================= */

class RoundedButton extends JButton {

    private int radius = 22;

    public RoundedButton(String text, Color bg) {
        super(text);
        setBackground(bg);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setMaximumSize(new Dimension(220, 44));
        setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(getModel().isPressed()
                ? getBackground().darker()
                : getBackground());

        g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                radius, radius);
        super.paintComponent(g2);
        g2.dispose();
    }
}
