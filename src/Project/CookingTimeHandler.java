package Project;

import javafx.animation.RotateTransition;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.io.File;

public class CookingTimeHandler extends Thread {
	private long cookingTime;
	private Box box;
	private ActionHandler sim;
	private AudioClip mediaPlayer;
	protected MeshView knob;

	public CookingTimeHandler(long cookingTime, Box box, ActionHandler sim, MeshView knob) {
		this.cookingTime = cookingTime;
		this.box = box;
		this.sim = sim;
		mediaPlayer = new AudioClip(new File("src/sounds/bell.mp3").toURI().toString());
		this.knob = knob;
	}

	public void run() {
		sim.isCookingThread = true;
		box.setVisible(true);
		playSound startSound = new playSound("src/sounds/startMMMM.mp3");
		startSound.start();
		try {
			returnKnobToCorrectPosition knobThread = new returnKnobToCorrectPosition(sim, knob);
			knobThread.start();
			CookingTimeHandler.sleep(1800);
			playSound sound = new playSound("src/sounds/MMMM.mp3", true);
			sound.start();
			CookingTimeHandler.sleep(cookingTime - 1800);
			sound.stopSounds();
		} catch (Exception e) {}
		mediaPlayer.play();
		box.setVisible(false);
		sim.isCookingThread = false;
		sim.cookingTime = 0;
		sim.timeKnobPos = 0;
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

class returnKnobToCorrectPosition extends Thread {
	ActionHandler sim;
	MeshView timeKnob;

	public returnKnobToCorrectPosition(ActionHandler sim, MeshView timeKnob) {
		this.sim = sim;
		this.timeKnob = timeKnob;
	}

	public void run() {
		sim.rt = new RotateTransition(Duration.millis(sim.cookingTime), timeKnob);
		sim.rt.setAxis(Rotate.Z_AXIS);
		sim.rt.setByAngle(-90 * sim.timeKnobPos);
		sim.timeKnobPos = 0;
		sim.rt.play();
	}
}
