package com.jhu.utils;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.jhu.azureapi.ApiUtil;

public class GUIHelper {

	public static void displayImage(String imageUrlOrFileName) {
		try {
			if (ApiUtil.isHttpUrl(imageUrlOrFileName)) {
				displayImage(new URL(imageUrlOrFileName).openStream());
			} else {
				displayImage(new FileInputStream(new File(imageUrlOrFileName)));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	// Displays the given input stream as an image.
	public static void displayImage(InputStream inputStream) {
		try {
			BufferedImage bufferedImage = ImageIO.read(inputStream);
			ImageIcon imageIcon = new ImageIcon(bufferedImage);

			int w = imageIcon.getIconWidth();
			int h = imageIcon.getIconHeight() + 50;

			JLabel jLabel = new JLabel();
			jLabel.setIcon(imageIcon);

			JFrame jFrame = new JFrame();
			jFrame.setLayout(new FlowLayout());
			jFrame.setSize(w, h);
			jFrame.setLocation((int) (Math.random() * 300), (int) (Math.random() * 300));

			jFrame.add(jLabel);
			jFrame.setVisible(true);
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
