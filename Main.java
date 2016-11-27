package flux.hk.main;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import flux.hk.state.StateManager;

@SuppressWarnings("serial")
public class Main extends JPanel {

	public static final int WIDTH = 1280, HEIGHT = 720;
	public static final int UPS = 30;
	
	private StateManager sm = new StateManager();
	
	private Listener listener;
	private JFrame jf;
	private BufferedImage image;
	private Graphics2D g;
	
	private boolean running = false;

	public static void main(String[] args) {
		new Main();
	}

	public Main() {

		listener = new Listener(this);

		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.addKeyListener(listener);
		this.addMouseListener(listener);
		this.addMouseWheelListener(listener);
		this.addMouseMotionListener(listener);
		this.setFocusable(true);
		this.requestFocus();

		jf = new JFrame("HackKings!");
		jf.setContentPane(this);
		jf.setResizable(false);
		
		ImageIcon ico = null;
		ico = new ImageIcon(FileReader.loadImage("/assets/splash/icon.png"));
		jf.setIconImage(ico.getImage());
		
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setUndecorated(true);
		jf.pack();
		jf.setVisible(true);

		init();
		run();
		
	}
	
	private void init() {
		
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = (Graphics2D) image.getGraphics();
		
	}
	
	private void run() {
		
		running = true;
	
		long nstart = System.nanoTime();
		long mstart = System.currentTimeMillis();
		long delta = (long) (Math.pow(10, 9) / UPS);
		
		int frames = 0, updates = 0;
		
		while (running) {
			
			if (System.nanoTime() - nstart >= delta) {
				nstart += delta;
				update();
				updates++;
			}
			
			draw();
			frames++;
			
			if (System.currentTimeMillis() - mstart >= 1000) {
				System.out.println("Frames: " + frames + " | Updates: " + updates);
				frames = 0;
				updates = 0;
				mstart += 1000;
			}
			
		}
		
	}
	
	long lastTime = System.nanoTime();
	long lastTimer = System.currentTimeMillis();
	double nsPerTick = 1000000000 / UPS;
	double delta = 0;
	int frames = 0, ticks = 0;

	private void update() {
		sm.update();
		jf.setLocation(sm.getWindowX(), sm.getWindowY());
	}

	private void draw() {
		
		boolean success = false;
		
		if (sm.draw(g)) {
			success = true;
		}
		
		if (success) {
			drawToScreen();
		}
		
	}
	
	private void drawToScreen() {
		((Graphics2D) this.getGraphics()).drawImage(image, new AffineTransform(), null);
	}
	
	public void keyPressed(KeyEvent e) {
		sm.keyPressed(e);
	}

	public void keyReleased(KeyEvent e) {
		sm.keyReleased(e);
	}

	public void keyTyped(KeyEvent e) {
		sm.keyTyped(e);
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		sm.mouseWheelMoved(e);
	}

	public void mouseDragged(MouseEvent e) {
		sm.mouseDragged(e);
	}

	public void mouseMoved(MouseEvent e) {
		sm.mouseMoved(e);
	}

	public void mouseClicked(MouseEvent e) {
		sm.mouseClicked(e);
	}

	public void mouseEntered(MouseEvent e) {
		sm.mouseEntered(e);
	}

	public void mouseExited(MouseEvent e) {
		sm.mouseExited(e);
	}

	public void mousePressed(MouseEvent e) {
		sm.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e) {
		sm.mouseReleased(e);
	}
	
}
