package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.entities.handlers.KeyHandler;

import src.assets.ImageLoader;
import src.entities.sim.Inventory;
import src.entities.sim.Sim;
import src.main.Consts;
import src.main.GameTime;
import src.items.foods.BakedFood;
import src.items.Item;
import src.main.ui.UserInterface;

public class Stove extends Interactables{
    // Types of stove
    private static String[] names = {
        "Gas Stove",
        "Electric Stove"
    };
    private static int[] width = {
        2,
        1
    };
    private static int[] height = {
        1,
        1
    };
    private static int[] prices = {
        100,
        200
    };
    
    // Images of stove
    BufferedImage[] images = new BufferedImage[4];
    BufferedImage[] icons = new BufferedImage[2]; 

    // Attributes
    private int price;
    private int duration;

    // CONSTRUCTOR
    public Stove(int x, int y, int imageIndex) {
        super (
            names[imageIndex],
            "cook",
            imageIndex,
            x,
            y,
            width[imageIndex],
            height[imageIndex]
        );

        this.price = prices[imageIndex];

        // Load the images and icons of the stoves
        this.images = ImageLoader.loadStoves();
        this.icons = ImageLoader.loadStovesIcons(); 
    }

    // IMPLEMENTATION OF ABSTRACT METHODS
    @Override
    public BufferedImage getIcon() {
        return images[getImageIndex()];
    }

    @Override
    public BufferedImage getImage() {
        return images[getImageIndex()];
    }

    @Override
    public void interact(Sim sim) {
        // call ActiveAction.cook, but dont know how to insert the parameter required by cook since interact can only accept sim
        // my view: 
        // changeOccupied(sim);
        // ActiveAction.cook(bakedfood, null, sim, getTime()); but again how to insert bakedfood :v
        // changeOccupied(sim);
        Thread cooking = new Thread() {
            @Override
            public void run() {
                try {
                    UserInterface.changeIsViewingRecipes();
                    sim.changeIsBusyState();
                    int slotColumn = 0;
                    int slotRow = 0;
                    // choose the food to be cooked
                    BakedFood bakedFood = null;
                    while (bakedFood == null) {
                        if (KeyHandler.isKeyPressed(KeyHandler.KEY_A)) {
                            if (slotColumn > 0) {
                                slotColumn--;
                                UserInterface.setSlotColumn(slotColumn);
                            }
                        }
                        if (KeyHandler.isKeyPressed(KeyHandler.KEY_S)) {
                            if (slotRow < 1) {
                                if (slotColumn < 2) {
                                    slotRow++;
                                    UserInterface.setSlotRow(slotRow);
                                }
                                
                            }
                        }
                        if (KeyHandler.isKeyPressed(KeyHandler.KEY_D)) {
                            if (slotColumn < 2) {
                                if (!(slotRow == 1 && slotColumn == 1)) {
                                    slotColumn++;
                                    UserInterface.setSlotColumn(slotColumn);
                                }
                            }
                        }
                        if (KeyHandler.isKeyPressed(KeyHandler.KEY_W)) {
                            if (slotRow > 0 && slotRow < 2) {
                                slotRow--;
                                UserInterface.setSlotRow(slotRow);
                            }
                        }
                        if(KeyHandler.isKeyPressed(KeyHandler.KEY_ENTER)) {
                            if (slotRow == 0 && slotColumn == 0) {
                                bakedFood = new BakedFood(0);
                            }
                            else if (slotRow == 0 && slotColumn == 1) {
                                bakedFood = new BakedFood(1);
                            }
                            else if (slotRow == 0 && slotColumn == 2) {
                                bakedFood = new BakedFood(2);
                            }
                            else if (slotRow == 1 && slotColumn == 0) {
                                bakedFood = new BakedFood(3);
                            }
                            else if (slotRow == 1 && slotColumn == 1) {
                                bakedFood = new BakedFood(4);
                            }
                        }
                    }
                    UserInterface.changeIsViewingRecipes();
                    

                    // check available ingredients first
                    boolean isAllIngredientAvailable = true;
                    for (String ingredient : bakedFood.getIngredients()) {
                        boolean isIngredientAvailable = false;
                        for (Item rawfood : sim.getInventory().getMapOfItems().keySet()) {
                            if (ingredient.equals(rawfood.getName())) {
                                isIngredientAvailable = true;
                                break;
                            }
                        }  
                        if (!isIngredientAvailable) {
                            isAllIngredientAvailable = false;
                            break;
                        }
                    }
                    if (isAllIngredientAvailable) {
                        changeOccupiedState();
                        
                        double cookDuration = bakedFood.getHungerPoint() * 1.5;
                        GameTime.startDecrementTimeRemaining((int) cookDuration);
                        sim.setStatus("Cooking");

                        Thread.sleep((int) cookDuration*Consts.THREAD_ONE_SECOND);
                        
                        changeOccupiedState();
                        sim.resetStatus();
                        sim.setMood(sim.getMood() + 10);
                        // must add code to add to inventory
                        sim.getInventory().addItem(bakedFood);
                        sim.changeIsBusyState();
                    }
                    else {
                        UserInterface.changeIsAbleToCook();
                        while (UserInterface.getIsAbleToCook() == false){
                            
                            if (KeyHandler.isKeyPressed(KeyHandler.KEY_ESCAPE)) {
                                UserInterface.changeIsAbleToCook();
                                sim.changeIsBusyState();
                            }
                        }
                    }
                    
                    
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        cooking.start();
    } 
}
