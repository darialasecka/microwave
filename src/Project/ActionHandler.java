package Project;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class ActionHandler extends Thread {

	private double doorPivotX = -2;
	private double doorPivotZ = -2;
	private long lastDoorAction = 0;
	private long lastCookingAction = 0;
	private long lastTimeKnobAction = -2000;
	private long lastPowerKnobAction = -2000;
	private boolean areDoorsOpen = false;
	private double cookingCycles = 1;
	private int powerKnobPos = 0;
	private int angle = 90;
	protected int timeKnobPos = 0;
	protected long cookingTime = 0;
	protected boolean isCookingThread = false;
	protected RotateTransition rt;
	private Box glass;
	private Box food;
	private MeshView timeKnob;


	public ActionHandler(Box glass, Box food, MeshView timeKnob) {
		this.glass = glass;
		this.food = food;
		this.timeKnob = timeKnob;
	}


	public void handleDoorEvent(MeshView doors) {
		rt = new RotateTransition(Duration.millis(200), doors);
		TranslateTransition tt = new TranslateTransition(Duration.millis(200), doors);
		if (System.currentTimeMillis() > lastDoorAction + 200 && System.currentTimeMillis() > lastCookingAction + cookingTime) {
			String pathname;
			if (areDoorsOpen)   //closing
				pathname = "src/sounds/close.mp3";
			else pathname = "src/sounds/open.mp3";
			playSound playOpenCloseSound = new playSound(pathname);
			playOpenCloseSound.start();
			areDoorsOpen = !areDoorsOpen;
			lastDoorAction = System.currentTimeMillis();

			rt.setAxis(Rotate.Y_AXIS);
			rt.setByAngle(angle);
			rt.play();

			tt.setByX(doorPivotX);
			tt.setByZ(doorPivotZ);
			tt.play();

			angle = -angle;
			doorPivotX = -doorPivotX;
			doorPivotZ = -doorPivotZ;
		}
	}

	public void handleCookingEvent(Box glass, Box food, MeshView timeKnob) {
		if (!areDoorsOpen && !isCookingThread && cookingTime > 0 && powerKnobPos > 0 && System.currentTimeMillis() > lastTimeKnobAction + 2000) {
			cookingCycles = cookingTime / 9000.0;
			CookingTimeHandler animate = new CookingTimeHandler(cookingTime, glass, this, timeKnob);
			animate.start();
			lastCookingAction = System.currentTimeMillis();
			rt = new RotateTransition(Duration.millis(cookingTime), food);
			rt.setAxis(Rotate.Y_AXIS);
			rt.setByAngle(360 * cookingCycles);
			rt.play();
		}
	}

	public void handleTimeKnobEvent(MeshView timeKnob) {
		if (System.currentTimeMillis() > lastTimeKnobAction + 2000 && timeKnobPos < 4 && !isCookingThread) {
			cookingTime += 4500;
			lastTimeKnobAction = System.currentTimeMillis();
			rt = new RotateTransition(Duration.millis(2000), timeKnob);
			rt.setAxis(Rotate.Z_AXIS);
			rt.setByAngle(90);
			timeKnobPos++;
			rt.play();
		}
	}

	public void handlePowerKnobEvent(MeshView powerKnob) {
		if (System.currentTimeMillis() > lastPowerKnobAction + 2000 && !isCookingThread) {
			lastPowerKnobAction = System.currentTimeMillis();
			rt = new RotateTransition(Duration.millis(2000), powerKnob);
			rt.setAxis(Rotate.Z_AXIS);
			rt.setByAngle(90);
			powerKnobPos = ++powerKnobPos % 4;
			rt.play();
		}
	}

	public void run() {
		while (true) {
			if (!areDoorsOpen && powerKnobPos != 0 && timeKnobPos != 0 && !isCookingThread) {
				handleCookingEvent(glass, food, timeKnob);
			}
			try {
				this.sleep(3000);
			} catch (Exception e) {}
		}
	}
}
