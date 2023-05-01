package src.world;

import java.awt.event.KeyEvent;

import src.entities.sim.Sim;
import src.main.Consts;
import src.main.GameLoader;
import src.main.GameTime;
import src.main.KeyHandler;
import src.main.panels.CreateSimPanel;
import src.main.panels.GamePanel;
import src.main.ui.UserInterface;

public class Cursor {
    // Location inside of the world
    private int x;
    private int y;
    private World world;
    private boolean gridMovement = false;

    // Constructor
    public Cursor(int x, int y, World world){
        this.x = x;
        this.y = y;
        this.world = world;
    }

    // Getter and setter
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getGridX() {
        return x / Consts.TILE_SIZE;
    }

    public int getGridY() {
        return y / Consts.TILE_SIZE;
    }

    // Others
    public boolean isAboveHouse() {
        int x = getGridX();
        int y = getGridY();
        boolean isOccupied = false; // Initialize the status of occupation in newHouse location

        if (world.getMap(x, y) == 1) {
            isOccupied = true;
        }
        return isOccupied;
    }

    private boolean isMovingDiagonally() {
        if ((KeyHandler.isKeyDown(KeyHandler.KEY_W) && KeyHandler.isKeyDown(KeyHandler.KEY_A)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_W) && KeyHandler.isKeyDown(KeyHandler.KEY_D)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_S) && KeyHandler.isKeyDown(KeyHandler.KEY_A)) ||
            (KeyHandler.isKeyDown(KeyHandler.KEY_S) && KeyHandler.isKeyDown(KeyHandler.KEY_D))) {
            return true;
        }
        return false;
    }

    public void move(){
        int upperX = (Consts.TILE_SIZE * 64) - 14;
        int upperY = (Consts.TILE_SIZE * 64) - 14;
        int newX = x;
        int newY = y;
        int speed = 5;
        int initialSpeed = speed;

        if (KeyHandler.isKeyPressed(KeyEvent.VK_SHIFT)) {
            gridMovement = !gridMovement;
        }

        if (gridMovement) {
            upperX = (Consts.TILE_SIZE * 64);
            upperY = (Consts.TILE_SIZE * 64);
            // Move the cursor to the nearest grid
            newX -= (newX % Consts.TILE_SIZE);
            newY -= (newY % Consts.TILE_SIZE);

            if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
                newX -= Consts.TILE_SIZE;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
                newX += Consts.TILE_SIZE;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
                newY -= Consts.TILE_SIZE;
            }
            if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
                newY += Consts.TILE_SIZE;  
            }
        }
        else {
            if (isMovingDiagonally()) {
                speed *= 0.707;
            }
            
            if (KeyHandler.isKeyDown(KeyHandler.KEY_A)) {
                newX -= speed;
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_D)) {
                newX += speed;
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_W)) {
                newY -= speed;
            }
            if (KeyHandler.isKeyDown(KeyHandler.KEY_S)) {
                newY += speed;  
            }
            speed = initialSpeed;
        }

        if ((newX >= 0 && newX < upperX) && (newY >= 0 && newY < upperY)) {
            x = newX;
            y = newY;
        }
    }

    public void enterPressed(UserInterface ui) {
        if (world.isAdding()) {
            if (isAboveHouse()) return;
            
            if (!isAboveHouse()) {
                world.addHouse(GameLoader.roomName);
                CreateSimPanel.simName = "";
                CreateSimPanel.simName = "";
            }
        }
        enterHouse(ui);
    }

    private void enterHouse(UserInterface ui) {
        int x = getGridX();
        int y = getGridY();
        Sim currentSim;
        House houseToVisit, currentHouse;
        Room roomToVisit;

        try {
            currentSim = ui.getCurrentSim();
            currentHouse = currentSim.getCurrentHouse();
            houseToVisit = world.getHouse(x, y);
            roomToVisit = houseToVisit.getRoomWhenEntered();

            if (currentHouse == houseToVisit) {
                ui.changeIsViewingWorldState();
                System.out.println("You cannot enter a house you're already in!");
                return;
            }
    
            if (isAboveHouse()) {
                if (world.isAdding()) {
                    int newSim = world.getListOfSim().size() - 1;
        
                    currentSim = world.getSim(newSim);
                    currentSim.changeIsBusyState();
                    ui.setCurrentSim(currentSim);
                    world.changeIsAddingState();
                }
                else {
                    int x1 = currentHouse.getX(); int x2 = houseToVisit.getX();
                    int y1 = currentHouse.getY(); int y2 = houseToVisit.getY();
                    double deltaXsquared = Math.pow((double) (x2 - x1), 2);
                    double deltaYsquared = Math.pow((double) (y2 - y1), 2);
                    int duration = (int) Math.sqrt(deltaXsquared + deltaYsquared);

                    GameTime.decreaseTimeRemaining(duration);
                }

                currentSim.setCurrentHouse(houseToVisit);
                currentSim.setCurrentRoom(roomToVisit);
                ui.changeIsViewingWorldState();
            }

            if (GamePanel.isCurrentState("Placing a new house")) {
                GamePanel.gameState = "Playing";
            }
        }
        catch (Exception e) {System.out.println("Unexpected Error!");}
    }
}
