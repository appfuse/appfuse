package org.appfuse.ui;

import java.awt.*;

/**
 * <p> This program is open software. It is licensed using the Apache Software
 * Foundation, version 2.0 January 2004
 * </p>
 * <a
 * href="mailto:dlwhitehurst@gmail.com">dlwhitehurst@gmail.com</a>
 *
 * @author David L Whitehurst
 */
public final class SplashScreen extends Canvas {
    public int left;

    public SplashScreen() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        setBackground(Color.white);

        final Font font = new Font("Arial", Font.BOLD, 10);
        setFont(font);
        fm = getFontMetrics(font);

        image = getToolkit().getImage(getClass().getResource("/META-INF/splash.jpg"));
        final MediaTracker tracker = new MediaTracker(this);
        tracker.addImage(image, 0);

        try {
            tracker.waitForAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        win = new Window(new Frame());

        final Dimension screen = getToolkit().getScreenSize();
        final Dimension size = new Dimension(image.getWidth(this) + 2,
                image.getHeight(this) + 2 + PROGRESS_HEIGHT);
        win.setSize(size);

        win.setLayout(new BorderLayout());
        win.add(BorderLayout.CENTER, this);
        win.setLocation((screen.width - size.width) / 2,
                (screen.height - size.height) / 2);
        win.validate();
        win.setVisible(true);

    }

    public final void dispose() {
        win.dispose();
    }

    public final synchronized void advance() {
        progress++;
        repaint();

        // wait for it to be painted to ensure progress is updated
        // continuously
        try {
            wait();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }

    public final void update(final Graphics g) {
        paint(g);
    }

    public final synchronized void paint(final Graphics g) {
        final Dimension size = getSize();

        if (offscreenImg == null) {
            offscreenImg = createImage(size.width, size.height);
            offscreenGfx = offscreenImg.getGraphics();
            offscreenGfx.setFont(getFont());
        }

        offscreenGfx.setColor(Color.blue);
        offscreenGfx.drawRect(0, 0, size.width - 1, size.height - 1);

        offscreenGfx.drawImage(image, 1, 1, this);
        offscreenGfx.setColor(new Color(100, 100, 255));

        offscreenGfx.fillRect(1, image.getHeight(this) + 1,
                ((win.getWidth() - 2) * progress) / 6, PROGRESS_HEIGHT);

        g.drawImage(offscreenImg, 0, 0, this);

        notify();
    }

    // private members
    private final FontMetrics fm;
    private final Window win;
    private final Image image;
    private Image offscreenImg;
    private Graphics offscreenGfx;
    private int progress;
    private static final int PROGRESS_HEIGHT = 20;

}
