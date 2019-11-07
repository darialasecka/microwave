package Project;

import javafx.scene.media.AudioClip;
import javafx.scene.shape.Box;
import java.io.File;

public class CookingTimeHandler extends Thread {
	private long cookingTime;
	private Box box;
	private Simulation sim;
	private AudioClip mediaPlayer;

	public CookingTimeHandler(long cookingTime, Box box, Simulation sim) {
		this.cookingTime = cookingTime;
		this.box = box;
		this.sim = sim;
		mediaPlayer = new AudioClip(new File("src/sounds/bell.mp3").toURI().toString());
	}

	public void run() {
		sim.isCookingThread = true;
		box.setVisible(true);
		try {
			CookingTimeHandler.sleep(cookingTime);
		} catch (Exception e) {}

		mediaPlayer.play();
		box.setVisible(false);
		sim.isCookingThread = false;
	}
}

class playSound extends Thread {
	private String path;
	private AudioClip mediaPlayer;

	public playSound(String path) {
		this.path = path;
		mediaPlayer = new AudioClip(new File(path).toURI().toString());
	}

	public void run() {
		mediaPlayer.play();
	}

}
