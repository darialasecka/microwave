package Project;

import java.net.URL;
import java.util.logging.Logger;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Simulation extends Application {
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;

    @Override
    public void start(Stage stage) {
        Box background = new Box(950, 700, 1);
        background.setTranslateX(400);
        background.setTranslateY(250);
        PhongMaterial image = new PhongMaterial();
        image.setDiffuseMap(new Image(getClass().getResourceAsStream("/background_image.png")));
        background.setMaterial(image);

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
        platePattern.setDiffuseMap(new Image(getClass().getResourceAsStream("/plate.png")));
        plate.setMaterial(platePattern);

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

        Rotate rotateX = new Rotate(30, 0, 0, 0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(20, 0, 0, 0, Rotate.Y_AXIS);
        micro.getTransforms().addAll(rotateX, rotateY);
        doors.getTransforms().addAll(rotateX, rotateY);
        plate.getTransforms().addAll(rotateX, rotateY);


        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            int angle = 90;
            switch (event.getCode()) {
                case SPACE:
                    RotateTransition rt = new RotateTransition(Duration.millis(200), doors);
                    rt.setAxis(Rotate.Y_AXIS);
                    rt.setByAngle(angle);
                    rt.play();
                    angle = 90;
                    break;
                case ENTER:
                    rt = new RotateTransition(Duration.millis(9000), plate);
                    rt.setAxis(Rotate.Y_AXIS);
                    rt.setByAngle(360);
                    rt.play();
                    angle = 90;
                    break;
            }
        });

        PointLight light = new PointLight();
        light.setTranslateX(400);
        light.setTranslateY(290);
        light.setTranslateZ(-1115);

        PerspectiveCamera camera = new PerspectiveCamera();
		camera.setNearClip(0.001);
		camera.setFarClip(100.0);

        Group root = new Group(micro, doors, plate, light, background);

        Scene scene = new Scene(root, 800, 600, true);
        stage.setResizable(false);
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