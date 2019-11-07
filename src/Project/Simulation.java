package Project;

import java.net.URL;
import java.util.logging.Logger;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Simulation extends Application {
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private int angle = 90;
    private double doorPivotX = -2;
    private double doorPivotZ = -2;
    private long lastDoorAction = System.currentTimeMillis();
    private boolean areDoorsOpen = false;



    @Override
    public void start(Stage stage) {
        Box[] background = new Box[6];
        for (int i = 0; i < 6; i++) {
            background[i] = new Box(2000, 1400, 1);
            PhongMaterial image = new PhongMaterial();
            image.setDiffuseMap(new Image(getClass().getResourceAsStream("/background_image.jpg")));
            background[i].setMaterial(image);
            background[i].setTranslateY(400);

        }

        background[0].setTranslateX(400);

        background[1].setTranslateX(400);
        background[1].setTranslateZ(-2200);

        background[2].setTranslateZ(-1105);
        background[2].setTranslateX(-400);
        background[2].setRotationAxis(Rotate.Y_AXIS);
        background[2].setRotate(90);

        background[3].setTranslateZ(-1105);
        background[3].setRotationAxis(Rotate.Y_AXIS);
        background[3].setRotate(-90);
        background[3].setTranslateX(800);

        background[4].setTranslateZ(-1105);         //4 - sufit
        background[4].setTranslateX(800);
        background[4].setTranslateY(-2000);
        background[4].setRotationAxis(Rotate.X_AXIS);
        background[4].setRotate(90);
        PhongMaterial ceiling = new PhongMaterial();
        ceiling.setDiffuseMap(new Image(getClass().getResourceAsStream("/sufit.jpg")));
        background[4].setMaterial(ceiling);

        background[5].setTranslateZ(-1105);         //5 - podłoga
        background[5].setTranslateX(400);
        background[5].setRotationAxis(Rotate.X_AXIS);
        background[5].setRotate(-90);
        background[5].setTranslateY(1000);
        PhongMaterial floor = new PhongMaterial();
        floor.setDiffuseMap(new Image(getClass().getResourceAsStream("/panele.jpg")));
        background[5].setMaterial(floor);


		Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        ObjModelImporter objModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("mikrofala.obj");
            objModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }

		MeshView micro = objModelImporter.getImport()[0];
		PhongMaterial mat = new PhongMaterial();

        ObjModelImporter doorModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("drzwi.obj");
            doorModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        ObjModelImporter plateModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("tacka.obj");
            plateModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }

        MeshView plate = plateModelImporter.getImport()[0];
        plate.setTranslateX(400);
        plate.setTranslateY(301);
        plate.setTranslateZ(-1107);
        PhongMaterial platePattern = new PhongMaterial();
        platePattern.setDiffuseMap(new Image(getClass().getResourceAsStream("/plate.jpg")));
        plate.setMaterial(platePattern);

		ObjModelImporter tableModelImporter = new ObjModelImporter();
		try {
			URL url = this.getClass().getResource("stolik.obj");
			tableModelImporter.read(url);
		} catch (ImportException ie) {
			Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
		}

		PhongMaterial wood = new PhongMaterial();
		wood.setDiffuseMap(new Image(getClass().getResourceAsStream("/wood.jpg")));
		MeshView table = tableModelImporter.getImport()[0];
		Scale scale = new Scale(10,10,10);
		table.setTranslateX(400);
		table.setTranslateY(306.2);//- góra, + dół
		table.setTranslateZ(-1107);
		table.getTransforms().addAll(scale);
		table.setMaterial(wood);

        Box food = new Box(0.6, 0.6, 0.6);
        food.setTranslateX(399.6);
        food.setTranslateY(300.5);
        food.setTranslateZ(-1107.1);
        PhongMaterial melon = new PhongMaterial();
        melon.setDiffuseMap(new Image(getClass().getResourceAsStream("/melon.png")));
        food.setMaterial(melon);


        micro.setTranslateX(400);
        micro.setTranslateY(301);
        micro.setTranslateZ(-1107);

        PhongMaterial material = new PhongMaterial();
        //material.setDiffuseMap(new Image(getClass().getResourceAsStream("grey.png")));
        //micro.setMaterial(material);


        MeshView doors = doorModelImporter.getImport()[0];
        doors.setTranslateX(400);
        doors.setTranslateY(301);
        doors.setTranslateZ(-1107);


        Rotate rotateX = new Rotate(0, 400, 300.5, -1107, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, 400, 300.5, -1107, Rotate.Y_AXIS);


        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case SPACE:
                    areDoorsOpen = !areDoorsOpen;
                    RotateTransition rt = new RotateTransition(Duration.millis(200), doors);
                    TranslateTransition tt = new TranslateTransition(Duration.millis(200), doors);
                    if (System.currentTimeMillis() > lastDoorAction + 200) {
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
                    } else areDoorsOpen = true;
                    break;
                case ENTER:
                    if (!areDoorsOpen) {
                        rt = new RotateTransition(Duration.millis(9000), food);
                        rt.setAxis(Rotate.Y_AXIS);
                        rt.setByAngle(360);
                        rt.play();
                    }
                    break;
            }
        });

        PointLight light = new PointLight();
        light.setTranslateX(400);
        light.setTranslateY(280);
        light.setTranslateZ(-1115);

        PointLight light2 = new PointLight(Color.web("#262626"));//ciemny szary tylko lepszy xD //4F4F4F
        light2.setTranslateX(350);
        light2.setTranslateY(290);
        light2.setTranslateZ(-2000);

        PerspectiveCamera camera = new PerspectiveCamera();
		camera.setNearClip(0.001);
		camera.setFarClip(100.0);

        Group root = new Group(micro,
                doors,
                plate,
                light,
                light2,
                food,
                background[0],
                background[1],
                background[2],
                background[3],
                background[4],
                background[5],
                table);
        Scene scene = new Scene(root, 800, 600, true);
        stage.setResizable(false);

        camera.getTransforms().addAll(rotateX, rotateY);

        scene.setCamera(camera);

        scene.setOnMousePressed(me -> {
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            rotateX.setAngle(rotateX.getAngle()-(mousePosY - mouseOldY));
            rotateY.setAngle(rotateY.getAngle()+(mousePosX - mouseOldX));
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
        });

        stage.setScene(scene);
        stage.setTitle("Microwave Simulator");
        stage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }

}