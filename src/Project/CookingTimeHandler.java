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
		playSound startSound = new playSound("src/sounds/startMMMM.mp3");
		startSound.start();
		try {
			CookingTimeHandler.sleep(1800);
			playSound sound = new playSound("src/sounds/MMMM.mp3", true);
			sound.start();
			CookingTimeHandler.sleep(cookingTime - 1800);
			sound.stopSounds();
		} catch (Exception e) {}
		mediaPlayer.play();
		box.setVisible(false);
		sim.isCookingThread = false;
	}
}

class playSound extends Thread {
	private String path;
	private AudioClip mediaPlayer;
	private boolean loop = false;

	public playSound(String path) {
		this.path = path;
		mediaPlayer = new AudioClip(new File(path).toURI().toString());
	}

	public playSound(String path, boolean loop) {
		this.path = path;
		mediaPlayer = new AudioClip(new File(path).toURI().toString());
		mediaPlayer.setCycleCount(100);
		this.loop = loop;
	}

	public void run() {
		mediaPlayer.play();
	}

	public void stopSounds() {
		loop = false;
		mediaPlayer.stop();
	}

}
