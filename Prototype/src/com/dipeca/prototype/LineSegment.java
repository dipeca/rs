package com.dipeca.prototype;

import android.graphics.Point;

public class LineSegment {

	private int m;
	private int b;
	private int x1;
	private int y1;
	private int x2;
	private int y2;

	private int x3;
	private int y3;

	private boolean drawn = false;

	public boolean isDrawn() {
		return drawn;
	}

	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}

	public int getX3() {
		return x3;
	}

	public void setX3(int x3) {
		this.x3 = x3;
	}

	public int getY3() {
		return y3;
	}

	public void setY3(int y3) {
		this.y3 = y3;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public int getM() {
		return m;
	}

	public void setM(int m) {
		this.m = m;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	// set the line equation from 2 pair of points
	public void setLineEquationFromPoints(int x1, int y1, int x2, int y2) {
		// for a line with no slope, horizontal
		if (y2 == y1) {
			m = 0;
			b = x1;

			setY3(y2);
			setX3(Math.min(x1, x2) + 10);
		} else if (x2 == x1) { // for a line with no slope on the vertical
			m = 0;
			b = y1;

			setY3(Math.min(y1, y2) + 10);
			setX3(x1);
		} else { // for any other line
			Point[] ps = { new Point(x1, y1), new Point(x2, y2) };
			m = (ps[1].y - ps[0].y) / (ps[1].x - ps[0].x);
			b = ps[1].y - m * ps[1].x;

			int i = Math.min(x1, x2);
			while (i < Math.max(x1, x2)) {
				i++;
				if (m * i + b >= 0) {
					setY3(m * i + b);
					setX3(i);
					break;
				}
			}
			
			if(getY3() == 0){
				System.out.println("Damm");
			}
			// setY3(m*10+b);
			// setX3(10);
		}

		setX1(x1);
		setY1(y1);

		setX2(x2);
		setY2(y2);

	}

	@Override
	public String toString() {
		return "line x1 = " + getX1() + " y1 = " + getY1() + " x2 = " + getX2()
				+ " y2 = " + getY2();
	}

}
