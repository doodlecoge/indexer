package com.ajaxw.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.ajaxw.util.Helper;

public class CheckCodeDet {
	private BufferedImage[] numArr = null;

	public CheckCodeDet() throws IOException {
		numArr = new BufferedImage[10];
		train();
	}

	private void train() throws IOException {
		String[] ss = "2718,4547,6109,6376".split(",");

		for (String s : ss) {
			InputStream is = CheckCodeDet.class
					.getResourceAsStream("/com/ajaxw/res/" + s.trim() + ".jpg");
			BufferedImage bi = ImageIO.read(is);

			for (int i = 0; i < 4; i++) {
				int n = Integer.parseInt(s.substring(i, i + 1));
				// if (numArr[n] == null)
				numArr[n] = bi.getSubimage(i * 10, 0, 10, 10);
			}
		}

		for (int i = 0; i < 10; i++) {
			numArr[i] = numberize(numArr[i]);
		}

		// for (int i = 0; i < 10; i++) {
		// for (int j = 0; j < 10; j++) {
		// for (int k = 0; k < 10; k++) {
		// if (numArr[i].getRGB(k, j) == Color.black.getRGB()) {
		// System.out.print(".");
		// } else
		// System.out.print(" ");
		// }
		// System.out.println();
		// }
		// }
	}

	private BufferedImage numberize(BufferedImage img) {
		img = Helper.convert2Gray(img, null);

		int w = img.getWidth();
		int h = img.getHeight();

		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				Color c = new Color(img.getRGB(j, i));

				if (c.getRed() > 150)
					img.setRGB(j, i, Color.white.getRGB());
				else
					img.setRGB(j, i, Color.black.getRGB());
			}
		}

		return img;
	}

	public String detect(BufferedImage img) {
		List<BufferedImage> lst = split(img);
		String ret = "";
		for (BufferedImage bi : lst) {
			ret += getNum(bi);
		}

		return ret;
	}

	private List<BufferedImage> split(BufferedImage img) {
		List<BufferedImage> lst = new ArrayList<BufferedImage>();

		for (int i = 0; i < 4; i++) {
			lst.add(numberize(img.getSubimage(i * 10, 0, 10, 10)));
		}

		return lst;
	}

	private boolean equal(BufferedImage img1, BufferedImage img2) {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (img1.getRGB(i, j) != img2.getRGB(i, j))
					return false;
			}
		}
		return true;
	}

	private int getNum(BufferedImage img) {
		for (int i = 0; i < 10; i++) {
			if (equal(img, numArr[i]))
				return i;
		}

		return -1;
	}
}
