package src.entities.interactables;

import java.awt.image.BufferedImage;

import src.assets.ImageLoader;
import src.entities.sim.Sim;
import src.main.Consts;
import src.main.GameTime;

public class Television extends Interactables{
    // Attributes
    private int price = 100;
    private int duration = 10; // TO BE DETERMINED

    private BufferedImage icon;
    private BufferedImage[] images;

    public Television(int x, int y) {
        super (
            "Television",
            "watch the television",
            0,
            x,
            y,
            2,
            1
        );

        setPrice(price);
        setDuration(duration);

        this.icon = ImageLoader.loadTelevisionIcon();
        this.images = ImageLoader.loadTelevision();
    }

    @Override
    public BufferedImage getIcon() {
        return icon;
    }

    @Override
    public BufferedImage getImage() {
        return images[getImageIndex()];
    }

    @Override
    public void interact(Sim sim){
        Thread watch = new Thread(){
            @Override
            public void run(){
                sim.setStatus("Watching the TV");
                changeOccupiedState();
                Thread t = GameTime.startDecrementTimeRemaining(Consts.ONE_SECOND * duration);
                
                while (t.isAlive()) {
                    continue;
                }

                changeOccupiedState();
                sim.resetStatus();
                sim.setMood(sim.getMood() + 10);
                sim.setHealth(sim.getHealth() - 10);
                sim.setHunger(sim.getHunger() - 5);
        }
    };
    watch.start();
    }
}
