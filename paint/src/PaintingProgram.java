import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class PaintingProgram extends JFrame {
    private DrawCanvas canvas;
    private Color currentColor = Color.BLACK;
    private int brushSize = 5;

    public PaintingProgram() {
        setTitle("Java Painting Program");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize canvas and tool panel
        canvas = new DrawCanvas();
        JPanel toolPanel = createToolPanel();

        // Add components to the frame
        add(canvas, BorderLayout.CENTER);
        add(toolPanel, BorderLayout.SOUTH);
    }

    // Create a tool panel with color circles, eraser, and brush size slider
    private JPanel createToolPanel() {
        JPanel panel = new JPanel();
    
        // Predefined color circle buttons
        Color[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW};
        for (Color color : colors) {
            JButton colorButton = new CircleButton(color);
            colorButton.addActionListener(e -> currentColor = color);
            panel.add(colorButton);
        }
    
        // Custom multicolored circle button
        Color[] gradientColors = {Color.BLUE, Color.YELLOW, Color.GREEN};
        JButton customColorButton = new GradientCircleButton(gradientColors);
        customColorButton.addActionListener(e -> openColorChooser());
        panel.add(customColorButton);
    
        // Eraser circle button
        JButton eraserButton = new CircleImageButton("eraser.png");
        eraserButton.addActionListener(e -> currentColor = Color.WHITE);
        panel.add(eraserButton);
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> canvas.clearCanvas());
        panel.add(clearButton);
    
        // Brush size slider
        JSlider brushSlider = new JSlider(1, 20, 5);
        brushSlider.setMajorTickSpacing(5);
        brushSlider.setPaintTicks(true);
        brushSlider.setPaintLabels(true);
        brushSlider.addChangeListener(e -> brushSize = brushSlider.getValue());
        panel.add(new JLabel("Brush Size:"));
        panel.add(brushSlider);
    
        return panel;
    }
    // displays a circle with the eraser in it
    private static class CircleImageButton extends JButton {
        private final Image image;

        public CircleImageButton(String imagePath) {
            // Load the image from the given path
            ImageIcon icon = new ImageIcon(imagePath);
            this.image = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);

            setPreferredSize(new Dimension(40, 40));  // Size of the circle button
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);  // Remove the default button appearance
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the circular button background
            g2d.setColor(Color.LIGHT_GRAY);  
            g2d.fillOval(0, 0, getWidth(), getHeight());

            // Draw the image inside the circle
            g2d.drawImage(image, 5, 5, null);
        }
    }
    
    // Method to open the color picker dialog
    private void openColorChooser() {
        Color selectedColor = JColorChooser.showDialog(this, "Select a Custom Color", currentColor);
        if (selectedColor != null) {
            currentColor = selectedColor;
        }
    }
    

    // JPanel for the drawing canvas
    private class DrawCanvas extends JPanel {
        private List<ColoredPoint> points = new ArrayList<>();

        public DrawCanvas() {
            setBackground(Color.WHITE);
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    points.add(new ColoredPoint(e.getX(), e.getY(), currentColor, brushSize));
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (ColoredPoint point : points) {
                g.setColor(point.color);
                g.fillOval(point.x, point.y, point.size, point.size);
            }
        }

        public void clearCanvas() {
            points.clear();
            repaint();
        }
    }

    // Displays a colored circle
    private static class CircleButton extends JButton {
        private final Color color;

        public CircleButton(Color color) {
            this.color = color;
            setPreferredSize(new Dimension(30, 30));
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false); // Remove default button appearance
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.fillOval(5, 5, getWidth() - 10, getHeight() - 10);
        }
    }
    // Circular gradient with multiple colors
    private static class GradientCircleButton extends JButton {
        private final Color[] colors;

        public GradientCircleButton(Color[] colors) {
            this.colors = colors;
            setPreferredSize(new Dimension(30, 30));  // Size of the circular button
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false); // Remove default button appearance
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Define gradient colors and positions for the radial gradient
            float[] fractions = {0f, 0.5f, 1f}; // Define the positions of the colors
            RadialGradientPaint gradient = new RadialGradientPaint(
                    new Point(getWidth() / 2, getHeight() / 2), // Center of the gradient
                    getWidth() / 2f, // Radius of the gradient
                    fractions, colors 
            );

            // Set the gradient paint and draw the circle
            g2d.setPaint(gradient);
            g2d.fillOval(0, 0, getWidth(), getHeight());
        }
    }

    // class to store a point with its color and size
    private static class ColoredPoint {
        int x, y, size;
        Color color;

        public ColoredPoint(int x, int y, Color color, int size) {
            this.x = x;
            this.y = y;
            this.color = color;
            this.size = size;
        }
    }

    // Main method to run the program
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PaintingProgram app = new PaintingProgram();
            app.setVisible(true);
        });
    }
}
