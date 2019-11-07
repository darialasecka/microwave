package Project;

import javafx.scene.shape.Box;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class CookingTimeHandler extends Thread {
	private long cookingTime;
	private Box box;

	public CookingTimeHandler(long cookingTime, Box box) {
		this.cookingTime = cookingTime;
		this.box = box;
	}

	public void run() {
		box.setVisible(true);
		try {
			CookingTimeHandler.sleep(cookingTime);
		} catch (Exception e) {}
		Media sound = new Media(new File("src/bell.mp3").toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
		box.setVisible(false);
	}
}
