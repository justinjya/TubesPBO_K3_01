package src.main.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import src.entities.handlers.KeyHandler;
import src.items.interactables.*;
import src.main.Consts;
import src.world.World;

public class SelectionBox {
    // Selection Box Attributes
    private static int selectedBox = 0; // Boxes starting from 0 to 4
    private static int selectedBoxX = 203;
    private static int selectedBoxY = 479;
    private static int selectedBoxWidth = Consts.SCALED_TILE + 8;
    private static int selectedBoxHeight = Consts.SCALED_TILE + 8;
    private static int boxStep = 81;

    private static void moveSelectedBox(String direction) {
        switch (direction)  {
            case "left":
                selectedBox--;
                if (selectedBox < 0) {
                    selectedBox = 4;
                    selectedBoxX += boxStep * 4;
                }
                else {
                    selectedBoxX -= boxStep;
                }
                break;
            case "right":
                selectedBox++;
                if (selectedBox > 4) {
                    selectedBox = 0;
                    selectedBoxX -= boxStep * 4;
                }
                else {
                    selectedBoxX += boxStep;
                }
                break;
            default:
                break;
        }
    }

    // TO - DO !!! : Integrate with Store
    private static void boxEntered(UserInterface ui) {
        ui.tab();

        Sim currentSim = ui.getCurrentSim();
        World world = ui.getWorld();

        if (currentSim.isBusy()) return;

        switch (selectedBox) {
            case 0:
                NonActiveActions.editRoom(currentSim.getCurrentRoom());
                break;
            case 1:
                UpgradeActions.addRoom(currentSim.getCurrentRoom(), "Second Room");
                break;
            case 2:
                // This is just a test
                currentSim.getCurrentRoom().addObject(new Aquarium(3, 3,time));
                break;
            case 3:
                AddSimAction.addSim(ui, world);
                break;
            case 4:
                ActiveActions.visitAnotherSim(ui);
                break;
            default:
                break;
        }
    }
    
    public static void update(UserInterface ui) {
        if (ui.isTabbed()) {
            // Change selected box based on key input
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
                moveSelectedBox("left");
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
                moveSelectedBox("right");
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
                boxEntered(ui);
            }
        }
    }

    public static void draw(Graphics2D g, UserInterface ui) {
        g.setColor(Color.GRAY);
        
        g.fillRect(207, 483, Consts.SCALED_TILE, Consts.SCALED_TILE);
        g.fillRect(288, 483, Consts.SCALED_TILE, Consts.SCALED_TILE);
        g.fillRect(369, 483, Consts.SCALED_TILE, Consts.SCALED_TILE);
        g.fillRect(450, 483, Consts.SCALED_TILE, Consts.SCALED_TILE);
        g.fillRect(531, 483, Consts.SCALED_TILE, Consts.SCALED_TILE);

        // Draw selected box
        if (ui.isTabbed()) {
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(selectedBoxX, selectedBoxY, selectedBoxWidth, selectedBoxHeight);
            drawSelectedBoxText(g);
        }
    }

    private static void drawSelectedBoxText(Graphics2D g) {
        Font font = new Font("Arial", Font.PLAIN, 12);

        g.setFont(font);
        switch (selectedBox) {
            case 0:
                g.drawString("Edit Room", Consts.CENTER_X - 18, Consts.CENTER_Y + 172);
                break;
            case 1:
                g.drawString("Upgrade House", Consts.CENTER_X - 38, Consts.CENTER_Y + 172);
                break;
            case 2:
                g.drawString("Add Object", Consts.CENTER_X - 22, Consts.CENTER_Y + 172);
                break;
            case 3:
                g.drawString("Add Sims", Consts.CENTER_X - 20, Consts.CENTER_Y + 172);
                break;
            case 4:
                g.drawString("Visit Another Sim", Consts.CENTER_X - 38, Consts.CENTER_Y + 172);
                break;
            default:
                g.drawString("Lorem Ipsum", Consts.CENTER_X - 28, Consts.CENTER_Y + 172);
                break;
        }
    }
}
