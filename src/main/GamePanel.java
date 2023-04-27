package src.main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import src.assets.ImageLoader;
import src.entities.*;
import src.entities.handlers.KeyHandler;
import src.world.Room;
import src.Menu.MenuButton;

// ini notes aja
// x + 12 pas dikiri 6x6 grid
// y - 38 pas diatas 6x6 grid
// y + 283 pas dibawah 6x6 grid

public class GamePanel extends JPanel implements Runnable {
    private GameTime time;
    private Sim sim;
    private Room room;
    private UserInterface ui;
    private MenuButton menu;

    public GamePanel() {
        setPreferredSize(new Dimension(Consts.WIDTH, Consts.HEIGHT));
        setBackground(new Color(44, 39, 35));

        // Create game time
        time = new GameTime(1, 720, 720);

        // Create room
        room = new Room("First Room", time);
        
        // Create sim
        sim = new Sim("Justin", Consts.CENTER_X + 80, Consts.CENTER_Y, room);
        
        // Create user interface
        ui = new UserInterface(sim, time);

        // Create a KeyAdapter and add it as a key listener to the panel
        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                KeyHandler.keyPressed(e.getKeyCode());
                KeyHandler.keyBinds(sim, ui);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                KeyHandler.keyReleased(e.getKeyCode());
            }
        };
        addKeyListener(keyAdapter);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        requestFocusInWindow();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        int fps = 0;
        double time = 0;
        final double TARGET_FPS = 60.0;
        final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        double accumulator = 0.0;
        while (true) {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1000000000.0;
            lastTime = now;
            accumulator += deltaTime;
            while (accumulator >= OPTIMAL_TIME / 1000000000.0) {
                update();
                accumulator -= OPTIMAL_TIME / 1000000000.0;
            }
            repaint();
            fps++;
            time += deltaTime;
            if (time > 1.0) {
                System.out.println("Game Window - FPS: " + fps);
                fps = 0;
                time = 0;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        sim.update();

        sim.getCurrentRoom().update();

        ui.update();
    }

    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        // ONLY FOR DEBUGGING
        // ui.drawMockup(g2);

        // // Draw room
        sim.getCurrentRoom().draw(g2);

        // // Draw sim
        sim.draw(g2);

        // // Draw UI
        ui.draw(g2);

        // g2.drawImage(ImageLoader.testSimColor(), Consts.CENTER_X, Consts.CENTER_Y, Consts.SCALED_TILE * 4, Consts.SCALED_TILE * 4, null);

        // To free resources
        g2.dispose();

        // Draw Menu
        //menu.loadImages();
    }
}