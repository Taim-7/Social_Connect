package Classes;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private final BufferedImage image;
    private double scale;

    private int dragStartX;
    private int dragStartY;

    public ImagePanel(BufferedImage img) {
        this.image = img;

        // initial fit to 600px width
        this.scale = 600.0 / img.getWidth();

        updateSize();

        // Ctrl + Scroll = Zoom
        addMouseWheelListener(e -> {
            if (e.isControlDown()) {

                double zoomFactor = (e.getWheelRotation() < 0) ? 1.1 : 0.9;
                scale *= zoomFactor;

                scale = Math.max(0.1, Math.min(scale, 10));

                updateSize();
                repaint();

                e.consume(); // stop scroll
            }
        });

        // Drag to pan
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                dragStartX = e.getX();
                dragStartY = e.getY();
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {

                JViewport viewport = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, ImagePanel.this);

                if (viewport != null) {

                    Point viewPos = viewport.getViewPosition();

                    int dx = dragStartX - e.getX();
                    int dy = dragStartY - e.getY();

                    viewPos.translate(dx, dy);

                    scrollRectToVisible(new Rectangle(viewPos, viewport.getSize()));
                }
            }
        });
    }

    private void updateSize() {
        setPreferredSize(new Dimension((int) (image.getWidth() * scale), (int) (image.getHeight() * scale)));

        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = (int) (image.getWidth() * scale);
        int h = (int) (image.getHeight() * scale);

        g2.drawImage(image, 0, 0, w, h, null);
    }
}