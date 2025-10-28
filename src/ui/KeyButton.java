package ui;


import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;
import static utilz.Constants.UI.KeyButton.*;

public class KeyButton {
	private int xPos, yPos, rowIndex, index;
	private int height, width;
	private BufferedImage[] imgs;
	private boolean keyPressed;

	public KeyButton(int xPos, int yPos, int rowIndex) {
	  	this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.height = K_HEIGHT;
		this.width = K_WIDTH;
		loadImgs();
	}

	public KeyButton(int xPos, int yPos, int rowIndex, int height, int width, String fileName){
		this.xPos = xPos;
		this.yPos = yPos;
		this.rowIndex = rowIndex;
		this.height = height;
		this.width = width;
		imgs = new BufferedImage[2];
		BufferedImage temp = LoadSave.GetSpriteAtlas(fileName);
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = temp.getSubimage(i * width, rowIndex * height, width, height);
		}
	}
	private void loadImgs() {
		imgs = new BufferedImage[2];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.KeyButton);
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = temp.getSubimage(i * width, rowIndex * height, width, height);
		}
	}

	public void draw(Graphics g) {
		g.drawImage(imgs[index], xPos, yPos, width * 3, height * 3 , null);
	}

	public void update() {
		index = 0;
		if (keyPressed) index = 1;
	}

	public void setKeyPressed(boolean keyPressed) {
		this.keyPressed = keyPressed;
	}


	public void resetBools() {
		keyPressed = false;
	}
}
