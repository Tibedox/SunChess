package ru.myitschool.sunchess.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import ru.myitschool.sunchess.ChessGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = ChessGame.SCR_WIDTH;
		config.height = ChessGame.SCR_HEIGHT;
		//config.backgroundFPS = 10;
		new LwjglApplication(new ChessGame(), config);
	}
}
