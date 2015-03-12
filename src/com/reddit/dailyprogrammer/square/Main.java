package com.reddit.dailyprogrammer.square;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glViewport;

public class Main {

    public static class Square {

        int x, y, side;
        float red, green, blue, alpha;

        public Square(int x, int y, int side, float red, float green, float blue, float alpha) {
            this.x = x;
            this.y = y;
            this.side = side;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

    }

    private static List<Square> squares = new ArrayList<>();

    public static void main(String[] args) {

        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();

        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (!Display.isCloseRequested()) {
            initGL();
            pollInput();
            renderSquares();
            Display.update();
        }

        Display.destroy();
    }

    private static void initGL() {
        setupCanvas();
        enableBlending();
    }

    private static void enableBlending() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    private static void setupCanvas() {
        glViewport(0, 0, Display.getWidth(), Display.getHeight());
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 0, 600, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    private static void pollInput() {
        while (Mouse.next()) {
            if (Mouse.getEventButtonState()) {
                if (Mouse.getEventButton() == 0) {
                    int x = Mouse.getEventX();
                    int y = Mouse.getEventY();
                    generateSquare(x, y);
                }
            } else if (Mouse.getEventButton() == 1) {
                if (!squares.isEmpty()) {
                    System.out.println("Removing!");
                    squares.remove(squares.size() - 1);
                }
            } else if (Mouse.getEventButton() == 2) {
                System.out.println("Clearing!");
                squares.clear();
            }
        }
    }

    private static void generateSquare(int x, int y) {
        Random random = new Random(x * y);
        int side = random.nextInt(299);
        side++;
        float red = random.nextFloat();
        float green = random.nextFloat();
        float blue = random.nextFloat();
        float alpha = random.nextFloat();

        System.out.println(
                "Generating: (" + x + "," + y + ")@" + side + "[" + red + "," + green + "," + blue + "," + alpha + "]");

        squares.add(new Square(x, y, side, red, green, blue, alpha));
    }

    private static void renderSquares() {
        squares.forEach(s -> renderSquare(s.x, s.y, s.side, s.red, s.green, s.blue, s.alpha));
    }

    private static void renderSquare(int x, int y, int side, float red, float green, float blue, float alpha) {
        GL11.glPushMatrix();
        {
            GL11.glColor4f(red, green, blue, alpha);

            int halfSide = side / 2;

            // draw quad
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glVertex2f(x - halfSide, y - halfSide);
                GL11.glVertex2f(x + halfSide, y - halfSide);
                GL11.glVertex2f(x + halfSide, y + halfSide);
                GL11.glVertex2f(x - halfSide, y + halfSide);
            }
            GL11.glEnd();
        }
        GL11.glPopMatrix();
    }
}
