package src.assets;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import src.main.Consts;

public class ImageLoader {
    private static BufferedImage scaleImage(BufferedImage image, int width, int height) {
        BufferedImage scaledImage = new BufferedImage(Consts.SCALED_TILE * width, Consts.SCALED_TILE * height, image.getType());
        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(image, 0, 0, Consts.SCALED_TILE * width, Consts.SCALED_TILE * height, null);
        g.dispose();
        return scaledImage;
    }

    public static BufferedImage readImage(String folder, String fileName, int width, int height) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File("./src/assets/" + folder + "/" + fileName + ".png"));
            image = scaleImage(image, width, height);
            return image;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // read image with no scaling
    public static BufferedImage readImage(String folder, String fileName) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File("./src/assets/" + folder + "/" + fileName + ".png"));
            return image;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage rotate90Clockwise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage rotated = new BufferedImage(height, width, image.getType());
    
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotated.setRGB(height - 1 - y, x, image.getRGB(x, y));
            }
        }
        return rotated;
    }
    

    public static BufferedImage[] loadSim() {
        BufferedImage[] images = new BufferedImage[12];
        images[0] = readImage("sim", "idle_down", 1, 1);
        images[1] = readImage("sim", "idle_up", 1, 1);
        images[2] = readImage("sim", "idle_left", 1, 1);
        images[3] = readImage("sim", "idle_right", 1, 1);
        images[4] = readImage("sim", "walk_down_1", 1, 1);
        images[5] = readImage("sim", "walk_down_2", 1, 1);
        images[6] = readImage("sim", "walk_up_1", 1, 1);
        images[7] = readImage("sim", "walk_up_2", 1, 1);
        images[8] = readImage("sim", "walk_left_1", 1, 1);
        images[9] = readImage("sim", "walk_left_2", 1, 1);
        images[10] = readImage("sim", "walk_right_1", 1, 1);
        images[11] = readImage("sim", "walk_right_2", 1, 1);
        return images;
    }

    public static BufferedImage[] loadBeds() {
        BufferedImage[] images = new BufferedImage[6];
        images[0] = readImage("beds", "bed_idle", 4, 1);
        images[1] = readImage("beds", "bed_occupied", 4, 1);
        return images;
    }

    public static BufferedImage loadWood() {
        BufferedImage image = readImage("tiles", "wood", 1, 1);
        return image;
    }

    public static BufferedImage[] loadItemsIcon() {
        BufferedImage[] images = new BufferedImage[12];
        images[0] = readImage("inventory", "bed_single");
        images[1] = readImage("inventory", "bed_queen");
        images[2] = readImage("inventory", "bed_king");
        images[3] = readImage("inventory", "trash_bin");
        images[4] = readImage("inventory", "gas_stove");
        // images[5] = readImage("inventory", "electric_stove");
        // images[6] = readImage("inventory", "table_and_chair");
        // images[7] = readImage("inventory", "clock");
        // images[8] = readImage("inventory", "television");
        // images[9] = readImage("inventory", "shover");
        // images[10] = readImage("inventory", "aquarium");
        // images[11] = readImage("inventory", "trash_bin");
        // images[12] = readImage("inventory", "rice");
        // images[13] = readImage("inventory", "potato");
        // images[14] = readImage("inventory", "chicken");
        // images[15] = readImage("inventory", "meat");
        // images[16] = readImage("inventory", "carrot");
        // images[17] = readImage("inventory", "spinach");
        // images[18] = readImage("inventory", "peanuts");
        // images[19] = readImage("inventory", "milk");
        // images[20] = readImage("inventory", "chicken_and_rice");
        // images[21] = readImage("inventory", "curry_and_rice");
        // images[22] = readImage("inventory", "peanut_and_milk");
        // images[23] = readImage("inventory", "cut_vegetables");
        // images[24] = readImage("inventory", "steak");
        return images;
    }


    public static BufferedImage loadMockup() {
        try {
            BufferedImage image = ImageIO.read(new File("./src/assets/mockup/gameLayoutMockup.png"));
            return image;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage[] loadDoor() {
        BufferedImage[] images = new BufferedImage[4];
        images[0] = readImage("tiles", "door", 1, 1);
        images[1] = rotate90Clockwise(images[0]);
        images[2] = rotate90Clockwise(images[1]);
        images[3] = rotate90Clockwise(images[2]);

        return images;
    }
}
