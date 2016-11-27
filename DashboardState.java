package flux.hk.state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

import flux.hk.main.FileReader;
import flux.hk.main.Main;
import flux.hk.util.ButtonManager;
import flux.hk.util.LineChart;

public class DashboardState extends State {

	private int slide = Main.HEIGHT;
	private float slideVel = 1.0f;

	private Font font, smallfont;

	private boolean popWindow = false;
	private ButtonManager popbm = new ButtonManager();
	private String[][] popList;
	private String popInput = "", popErr = "";
	private int[] popIndexes = new int[] { -1, -1 };

	private HashMap<Double, String> revenues = new HashMap<Double, String>();
	private HashMap<Double, String> fcosts = new HashMap<Double, String>();
	private HashMap<Double, String> rcosts = new HashMap<Double, String>();

	private int quantity;

	private double heldValue;

	private ButtonManager bm = new ButtonManager();

	public DashboardState() {
		bm.addButton(new Rectangle(20, 20, 600, 50), "Input expected monthly revenue", new Color(4, 125, 255, 255), 0);
		bm.addButton(new Rectangle(20, 90, 600, 50), "Input monthly fixed costs", new Color(4, 125, 255, 255), 1);
		bm.addButton(new Rectangle(20, 160, 600, 50), "Input expected running costs", new Color(4, 125, 255, 255), 2);
		bm.addButton(new Rectangle(20, 230, 600, 50), "Input quantity", new Color(4, 125, 255, 255), 3);

		bm.addButton(new Rectangle(20, 440, 600, 50), "Read from file", new Color(4, 125, 255, 255), 96);
		bm.addButton(new Rectangle(20, 510, 600, 50), "Create graph", new Color(4, 125, 255, 255), 97);
		bm.addButton(new Rectangle(20, 580, 600, 50), "Save to text file", new Color(4, 125, 255, 255), 98);
		bm.addButton(new Rectangle(20, 650, 600, 50), "Clear all", new Color(4, 125, 255, 255), 99);

		popbm.addButton(new Rectangle(1020, 560, 150, 50), "Next", new Color(25, 150, 255), 100);
		popbm.addButton(new Rectangle(110, 560, 150, 50), "Exit", new Color(25, 150, 255), 101);

		popList = new String[5][2];
		popList[0][0] = "Input a revenue value:";
		popList[0][1] = "Input a tag for this revenue value:";

		popList[1][0] = "Input a value for fixed monthly costs:";
		popList[1][1] = "Input a tag for this value:";

		popList[2][0] = "Input a value for the expected running costs:";
		popList[2][1] = "Input a tag for this value:";

		popList[3][1] = "Input a quantity:";

		popList[4][1] = "Input a file name (excluding .txt):";

		font = FileReader.getFont(48, "/assets/font/font.TTF");
		smallfont = FileReader.getFont(32, "/assets/font/font.TTF");

	}

	public void update() {

		if (slide > 0) {
			slide -= 10 * slideVel;
			slideVel += 0.1f;
			if (slide < 0) {
				slide = 0;
			}
			newDraw = true;
		}

		bm.update();
		popbm.update();
		ArrayList<Integer> fs = new ArrayList<Integer>();
		fs.addAll(bm.popFunctions());
		fs.addAll(popbm.popFunctions());
		for (Integer i : fs) {
			function(i);
		}

		if (bm.getNewDraw()) {
			newDraw = true;
			bm.resetNewDraw();
		}

		if (popbm.getNewDraw()) {
			newDraw = true;
			popbm.resetNewDraw();
		}
	}

	public void function(int func) {

		switch (func) {

		case 0:
			popWindow = true;
			popIndexes[0] = 0;
			popIndexes[1] = 0;
			newDraw = true;
			break;

		case 1:
			popWindow = true;
			popIndexes[0] = 1;
			popIndexes[1] = 0;
			newDraw = true;
			break;

		case 2:
			popWindow = true;
			popIndexes[0] = 2;
			popIndexes[1] = 0;
			newDraw = true;
			break;

		case 3:
			popWindow = true;
			popIndexes[0] = 3;
			popIndexes[1] = 1;
			newDraw = true;
			break;

		case 96:
			popWindow = true;
			popIndexes[0] = 4;
			popIndexes[1] = 1;
			newDraw = true;
			break;

		case 97:
			createGraph();
			break;

		case 98:
			ArrayList<String> text = new ArrayList<String>();

			int reven = 0, costs = 0;

			text.add("~Revenues");
			Object[] rv = revenues.keySet().toArray();
			Collection<String> strings = revenues.values();
			for (int i = 0; i < rv.length; i++) {
				text.add("£" + ((int) Double.parseDouble((rv[i].toString()))) + " | " + strings.toArray()[i]);
				reven += Double.valueOf(rv[i].toString());
			}
			text.add("");

			text.add("~Fixed Costs");
			Object[] fc = fcosts.keySet().toArray();
			strings = fcosts.values();
			for (int i = 0; i < fc.length; i++) {
				text.add("£" + ((int) Double.parseDouble((fc[i].toString()))) + " | " + strings.toArray()[i]);
				double c = Double.valueOf(fc[i].toString());
				costs += c;
			}
			text.add("");

			text.add("~Running Costs");
			Object[] rc = fcosts.keySet().toArray();
			strings = rcosts.values();
			for (int i = 0; i < rc.length; i++) {
				text.add("£" + ((int) Double.parseDouble((rc[i].toString()))) + " | " + strings.toArray()[i]);
				double c = Double.valueOf(rc[i].toString());
				costs += c;
			}
			text.add("");

			text.add("~Quantity");
			text.add(quantity + " units");
			text.add("");

			text.add("~Total Costs");
			text.add("£" + costs);
			text.add("");

			double np = reven - costs;
			text.add("~Net Profit");
			text.add("£" + (reven - costs));
			text.add("");

			if (np < 0) {
				text.add("Your net profit is negative, consider new ways of saving, investing or reducing costs.");
			} else if (np == 0) {
				text.add("Your business is breaking even, consider ways of reducing costs further.");
			} else {
				text.add("Your business is profiting, consider investment oppoutinities to maximise this profit.");
			}

			FileReader.createFile("/Balance Sheet", text);

			break;

		case 99:
			clearValues();
			break;

		case 100:
			pushPopWindow();
			break;

		case 101:
			exitPopWindow();
			break;

		}

	}

	public void draw(Graphics2D g) {
		BufferedImage i = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gg = (Graphics2D) i.getGraphics();

		gg.setColor(new Color(240, 240, 240));
		gg.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);

		bm.draw(gg);

		gg.setColor(new Color(4, 125, 255));
		gg.fillRect(680, 20, 560, 680);

		gg.setColor(Color.WHITE);
		gg.setFont(smallfont);

		int revenue = 0;
		Object[] rv = revenues.keySet().toArray();
		for (int ii = 0; ii < rv.length; ii++) {
			revenue += Double.valueOf(rv[ii].toString());
		}
		gg.drawString("Gross revenue: £" + ((int) revenue), 690, 60);

		int fcost = 0;
		Object[] fv = fcosts.keySet().toArray();
		for (int ii = 0; ii < fv.length; ii++) {
			fcost += Double.valueOf(fv[ii].toString());
		}
		gg.drawString("Fixed costs: £" + fcost, 690, 110);

		int rcost = 0;
		Object[] rcv = rcosts.keySet().toArray();
		for (int ii = 0; ii < rcv.length; ii++) {
			rcost += Double.valueOf(rcv[ii].toString());
		}

		gg.drawString("Quantity: " + quantity, 690, 160);

		gg.drawString("Running costs: £" + rcost, 690, 210);

		gg.drawString("Total costs: £" + (rcost + fcost), 690, 260);

		gg.drawString("Net profit: £" + (revenue - (rcost + fcost)), 690, 310);

		if (popWindow) {
			float[] matrix = new float[90];
			for (int ii = 0; ii < 90; ii++)
				matrix[ii] = 8.0f / 90.0f;
			BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, matrix));
			g.drawImage(i, op, 0, slide);

			g.setColor(new Color(4, 125, 255));
			g.fillRect(100, 100, 1080, 520);

			g.setColor(Color.WHITE);
			g.setFont(font);
			String s = popList[popIndexes[0]][popIndexes[1]];
			if (s != null) {
				g.drawString(s, (Main.WIDTH / 2) - (g.getFontMetrics().stringWidth(s) / 2), 175);
			}

			s = popInput;
			if (popIndexes[1] == 0) {
				s = "£" + s;
			}
			g.drawString(s, (Main.WIDTH / 2) - (g.getFontMetrics().stringWidth(popInput) / 2), 300);

			s = popErr;
			g.setColor(Color.RED);
			g.drawString(s, (Main.WIDTH / 2) - (g.getFontMetrics().stringWidth(s) / 2), 400);

			popbm.draw(g);

		} else {
			g.drawImage(i, null, 0, slide);
		}

		newDraw = false;
	}

	public void readFile(String path) {

		ArrayList<String> strs = FileReader.read("/" + path);
		if (strs == null) {
			return;
		}
		int val = -1;
		String[] split;

		clearValues();

		for (String s : strs) {
			if (s.equals("")) {
				val = -1;
			}
			if (s.equalsIgnoreCase("~Revenues")) {
				val = 0;
			} else if (s.equalsIgnoreCase("~Fixed Costs")) {
				val = 1;
			} else if (s.equalsIgnoreCase("~Running Costs")) {
				val = 2;
			} else if (s.equalsIgnoreCase("~Quantity")) {
				val = 3;
			} else {
				switch (val) {
				case 0:
					split = s.split(" ");
					if (split.length == 2) {
						try {
							Double.valueOf(split[0].substring(1));
							revenues.put(Double.parseDouble(split[0].substring(1)), "");
						} catch (Exception e) {
						}
					}
					if (split.length == 3) {
						try {
							Double.valueOf(split[0].substring(1));
							revenues.put(Double.parseDouble(split[0].substring(1)), "");
						} catch (Exception e) {
						}
					}
					break;
				case 1:
					split = s.split(" ");
					if (split.length == 2) {
						try {
							Double.valueOf(split[0].substring(1));
							fcosts.put(Double.parseDouble(split[0].substring(1)), "");
						} catch (Exception e) {
						}
					}
					if (split.length == 3) {
						try {
							Double.valueOf(split[0].substring(1));
							fcosts.put(Double.parseDouble(split[0].substring(1)), "");
						} catch (Exception e) {
						}
					}
					break;
				case 2:
					split = s.split(" ");
					if (split.length == 2) {
						try {
							Double.valueOf(split[0].substring(1));
							rcosts.put(Double.parseDouble(split[0].substring(1)), "");
						} catch (Exception e) {
						}
					}
					if (split.length == 3) {
						try {
							Double.valueOf(split[0].substring(1));
							rcosts.put(Double.parseDouble(split[0].substring(1)), "");
						} catch (Exception e) {
						}
					}
					break;
				case 3:
					split = s.split(" ");
					if (split.length > 0) {
						quantity = Integer.parseInt(split[0]);
					}
					break;
				}
			}
		}

		newDraw = true;

	}

	public void createGraph() {
		int fcost = 0;
		Object[] fv = fcosts.keySet().toArray();
		for (int ii = 0; ii < fv.length; ii++) {
			fcost += Double.valueOf(fv[ii].toString());
		}
		final XYSeries fc = new XYSeries("Fixed Costs");
		fc.add(0, fcost);
		fc.add(quantity, fcost);

		int rev = 0;
		Object[] rv = revenues.keySet().toArray();
		for (int ii = 0; ii < rv.length; ii++) {
			rev += Double.valueOf(rv[ii].toString());
		}
		final XYSeries revenue = new XYSeries("Revenue");
		revenue.add(0, 0);
		revenue.add(quantity, rev);

		int rcost = 0;
		Object[] rcv = rcosts.keySet().toArray();
		for (int ii = 0; ii < rcv.length; ii++) {
			rcost += Double.valueOf(rcv[ii].toString());
		}
		final XYSeries costs = new XYSeries("Total Costs");
		costs.add(0, fcost);
		costs.add(quantity, rcost + fcost);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(costs);
		dataset.addSeries(revenue);
		dataset.addSeries(fc);

		LineChart chart = new LineChart("Breakeven Chart",
				"A point where revenue and total costs lines cross is your breakeven point. If your revenue and costs do not cross, then you are at a loss!",
				"Quantity", "£", dataset);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (popWindow) {
			int kc = e.getKeyCode();
			if (kc == KeyEvent.VK_BACK_SPACE) {
				if (popInput.length() > 0) {
					popInput = popInput.substring(0, popInput.length() - 1);
					newDraw = true;
				}
			}
			if (kc == KeyEvent.VK_SPACE) {
				popInput += " ";
				newDraw = true;
			}
			if (kc == KeyEvent.VK_ESCAPE) {
				exitPopWindow();
			}
			if (kc == KeyEvent.VK_ENTER) {
				pushPopWindow();
			}
			if (kc >= 48 && kc <= 90 && popInput.length() < 25) {
				if (e.isShiftDown()) {
					popInput += Character.toString(e.getKeyChar()).toUpperCase();
				} else {
					popInput += Character.toString(e.getKeyChar()).toLowerCase();
				}
				newDraw = true;
			}
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		bm.mouseMoved(e);
		popbm.mouseMoved(e);
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		if (popWindow) {
			popbm.mousePressed(e);
		} else {
			bm.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (popWindow) {
			popbm.mouseReleased(e);
		} else {
			bm.mouseReleased(e);
		}
	}

	public void pushPopWindow() {
		if (popIndexes[1] == 0) {
			if (popInput.matches("[0-9]+")) {
				popErr = "";
				newDraw = true;
			} else {
				popErr = "Input a numerical value!";
				newDraw = true;
				return;
			}
			heldValue = Integer.valueOf(popInput);
			popInput = "";
			newDraw = true;
		} else if (popIndexes[1] == 1) {

			switch (popIndexes[0]) {
			case 0:
				revenues.put(heldValue, popInput);
				break;
			case 1:
				fcosts.put(heldValue, popInput);
				break;
			case 2:
				rcosts.put(heldValue, popInput);
				break;
			case 3:
				if (popInput.matches("[0-9]+")) {
					popErr = "";
					newDraw = true;
				} else {
					popErr = "Input a numerical value!";
					newDraw = true;
					return;
				}
				quantity = Integer.valueOf(popInput);
				popWindow = false;
				newDraw = true;
				break;
			case 4:
				readFile(popInput);
				popWindow = false;
				newDraw = true;
				break;
			}

			popInput = "";
			newDraw = true;
		}

		popIndexes[1] += 1;
		if (popIndexes[1] >= popList[popIndexes[0]].length) {
			popIndexes[1] = 0;
		}
		newDraw = true;
	}

	public void clearValues() {
		revenues.clear();
		fcosts.clear();
		rcosts.clear();
		quantity = 0;
	}

	public void exitPopWindow() {
		popInput = "";
		popErr = "";
		popWindow = false;
		newDraw = true;
	}

}