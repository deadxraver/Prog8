package gui;

import gui.frames.LogRegFrame;

import java.util.Locale;


public class GUIMain {

	public static Locale currentLocale = new Locale("ru");

	public static void main(String... args) {
		LogRegFrame logRegFrame = new LogRegFrame();
		logRegFrame.setVisible(true);
	}

}
