package com.russ.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class OvalBorder implements Border {
	protected int ovalWidth = 6;

	protected int ovalHeight = 6;

	protected Color topColor = Color.DARK_GRAY;

	protected Color bottomColor = Color.BLACK;

	public OvalBorder() {
		ovalWidth = 6;
		ovalHeight = 6;
	}

	public OvalBorder(Color topColor, Color bottomColor) {

		this.topColor = topColor;
		this.bottomColor = bottomColor;
	}

	public OvalBorder(int w, int h) {
		ovalWidth = w;
		ovalHeight = h;
	}

	public OvalBorder(int w, int h, Color topColor, Color bottomColor) {
		ovalWidth = w;
		ovalHeight = h;
		this.topColor = topColor;
		this.bottomColor = bottomColor;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(ovalHeight, ovalWidth, ovalHeight, ovalWidth);
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		width--;
		height--;

		g.setColor(topColor);
		g.drawLine(x, y + height - ovalHeight, x, y + ovalHeight);
		g.drawArc(x, y, 2 * ovalWidth, 2 * ovalHeight, 180, -90);
		g.drawLine(x + ovalWidth, y, x + width - ovalWidth, y);
		g.drawArc(x + width - 2 * ovalWidth, y, 2 * ovalWidth, 2 * ovalHeight, 90, -90);

		g.setColor(bottomColor);
		g.drawLine(x + width, y + ovalHeight, x + width, y + height - ovalHeight);
		g.drawArc(x + width - 2 * ovalWidth, y + height - 2 * ovalHeight, 2 * ovalWidth, 2 * ovalHeight, 0, -90);
		g.drawLine(x + ovalWidth, y + height, x + width - ovalWidth, y + height);
		g.drawArc(x, y + height - 2 * ovalHeight, 2 * ovalWidth, 2 * ovalHeight, -90, -90);
	}

}
