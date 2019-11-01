package Project;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Microwave extends Application {
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;

    @Override
    public void start(Stage stage) {
		//Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        Box micro = new Box(400,200,200);
		micro.setTranslateX(400);
		micro.setTranslateY(300);
        /*micro.setTranslateX(bounds.getWidth() /2); //na full screen
        micro.setTranslateY(bounds.getHeight() /2);*/
        //micro.setTranslateZ(-100);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("grey.png")));
        micro.setMaterial(material);

        Rotate rotateX = new Rotate(30, 0, 0, 0, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(20, 0, 0, 0, Rotate.Y_AXIS);
        micro.getTransforms().addAll(rotateX, rotateY);

        PointLight light = new PointLight();
        /*light.setTranslateX(250);
        light.setTranslateY(100);
        light.setTranslateZ(300);*/

        PerspectiveCamera camera = new PerspectiveCamera();
		/*camera.setNearClip(0.1);
		camera.setFarClip(10000.0);*/

        Group root = new Group(micro);

        Scene scene = new Scene(root, 800, 600, true);

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