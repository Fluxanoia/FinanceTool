package flux.hk.main;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public abstract class FileReader {

	public static ArrayList<String> read(String path) {

		File file = new File(getPath() + path + ".txt");
		ArrayList<String> strings = new ArrayList<String>();

		try {
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				strings.add(sc.nextLine());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			return null;
		}

		return strings;

	}

	public static String read(String path, String key) {

		String value = null;
		String[] split = null;
		ArrayList<String> strings = read(path);

		for (String s : strings) {
			split = s.split(": ");
			if (split[0].equals(key)) {
				value = split[1];
			}
		}

		return value;

	}

	public static void edit(String path, String key, String value) {

		int count = 0;
		int line = -1;
		File file = new File(getPath() + path + ".txt");
		String[] split = null;
		ArrayList<String> strings = read(path);

		for (String s : strings) {
			split = s.split(" ");
			if (split[0].equals(key + ":")) {
				line = count;
			}
			count++;
		}

		split = strings.get(line).split(" ");
		strings.set(line, split[0] + " " + value);

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			for (String s : strings) {

				bw.write(s);
				bw.newLine();

			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void setFile(String origin, String tochange) {

		File file = new File(getPath() + tochange + ".txt");
		ArrayList<String> strings = read(origin);

		FileWriter fw = null;
		BufferedWriter bw = null;

		try {
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			for (String s : strings) {
				bw.write(s);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String getPath() {
		try {
			return new File(FileReader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
			        .getParentFile().getPath();
		} catch (URISyntaxException e) {
			return "NOT FOUND";
		}
	}

	public static File getFile(String path) {
		return new File(getPath() + path);
	}

	public static void fileOverwrite(String path, ArrayList<String> strings) {

		File file = new File(getPath() + path + ".txt");

		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			for (String ss : strings) {
				bw.write(ss);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static boolean createFile(String path, ArrayList<String> strings) {

		File file = new File(getPath() + path + ".txt");

		if (!file.exists()) {

			FileWriter fw = null;
			BufferedWriter bw = null;
			try {
				fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				for (String ss : strings) {
					bw.write(ss);
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			return false;
		}

	}
	
	public static BufferedImage loadImage(String path) {
		BufferedImage i = null;
		try {
			i = ImageIO.read(new File(FileReader.getPath() + path));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}
	
	public static Font getFont(float pt, String type) {
		File file = null;
		Font f = null;
		try {
			file = new File(FileReader.getPath() + type);
			f = Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return f.deriveFont(pt);
	}
	
}
