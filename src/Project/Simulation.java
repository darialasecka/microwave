package Project;

import java.net.URL;
import java.util.logging.Logger;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
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
    private long cookingTime = 0;
    private long lastCookingAction = System.currentTimeMillis();
    private double cookingCycles = 1;
    public boolean isCookingThread = false;




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


		//Rectangle2D bounds = Screen.getPrimary().getVisualBounds();

        ObjModelImporter objModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("mikrofala.obj");
            objModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }

		MeshView micro = objModelImporter.getImport()[0];
        micro.setTranslateX(400);
        micro.setTranslateY(301);
        micro.setTranslateZ(-1107);

        ObjModelImporter knobModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("pokretlo.obj");
            knobModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        MeshView knob = knobModelImporter.getImport()[0];

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
		table.setTranslateY(306.2);
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

        Box glass = new Box(4, 3, 0.1);
        glass.setTranslateX(399.6);
        glass.setTranslateY(299.8);
        glass.setTranslateZ(-1108.4);
        PhongMaterial transparent = new PhongMaterial();
        transparent.setDiffuseColor(new Color(1, 0.65,0.1,0.3));
        glass.setMaterial(transparent);
        glass.setVisible(false);

        micro.setTranslateX(400);
        micro.setTranslateY(301);
        micro.setTranslateZ(-1107);


        knob.setTranslateX(400);
        knob.setTranslateY(301);
        knob.setTranslateZ(-1106.9);
        //PhongMaterial material = new PhongMaterial();
        //material.setDiffuseMap(new Image(getClass().getResourceAsStream("grey.png")));
        //micro.setMaterial(material);


        MeshView doors = doorModelImporter.getImport()[0];
        doors.setTranslateX(400);
        doors.setTranslateY(301);
        doors.setTranslateZ(-1107);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/grey.jpg")));
        micro.setMaterial(material);
        doors.setMaterial(material);

        ObjModelImporter cabinetModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("podwojna_szafka.obj");
            cabinetModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        MeshView cabinet = cabinetModelImporter.getImport()[0];
        scale = new Scale(1.4,1.4,1.4);
        cabinet.setTranslateX(415.8);
        cabinet.setTranslateY(311.5);
        cabinet.setTranslateZ(-1050.5);
        cabinet.getTransforms().addAll(scale);
        cabinet.setMaterial(wood);

        ObjModelImporter cabinet2ModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("podwojna_szafka.obj");
            cabinet2ModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        MeshView cabinet2 = cabinet2ModelImporter.getImport()[0];
        cabinet2.setTranslateX(380.8);
        cabinet2.setTranslateY(311.5);
        cabinet2.setTranslateZ(-1050.5);
        cabinet2.getTransforms().addAll(scale);
        cabinet2.setMaterial(wood);

        ObjModelImporter drawersModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("z_szufladami.obj");
            drawersModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        MeshView drawers = drawersModelImporter.getImport()[0];
        drawers.setTranslateX(428.5);
        drawers.setTranslateY(311.5);
        drawers.setTranslateZ(-1050.5);
        drawers.getTransforms().addAll(scale);
        drawers.setMaterial(wood);

        ObjModelImporter drawers2ModelImporter = new ObjModelImporter(); //muszę to zrobić jeszce rez jak chcę mieć 2
        try {
            URL url = this.getClass().getResource("z_szufladami.obj");
            drawers2ModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        MeshView drawers2 = drawers2ModelImporter.getImport()[0];
        drawers2.setTranslateX(393.3);
        drawers2.setTranslateY(311.5);
        drawers2.setTranslateZ(-1050.5);
        drawers2.getTransforms().addAll(scale);
        drawers2.setMaterial(wood);

        ObjModelImporter dishwasherModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("zmywarka.obj");
            dishwasherModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        MeshView dishwasher = dishwasherModelImporter.getImport()[0];
        dishwasher.setTranslateX(401.7);
        dishwasher.setTranslateY(311.5);
        dishwasher.setTranslateZ(-1049.5);
        dishwasher.getTransforms().addAll(scale);
        dishwasher.setMaterial(wood);

        ObjModelImporter refrigeratorModelImporter = new ObjModelImporter();
        try {
            URL url = this.getClass().getResource("lodowka.obj");
            refrigeratorModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }
        MeshView refrigerator = refrigeratorModelImporter.getImport()[0];
        refrigerator.setTranslateX(437.5);
        refrigerator.setTranslateY(311.5);
        refrigerator.setTranslateZ(-1050.5);
        refrigerator.getTransforms().addAll(scale);
        refrigerator.setMaterial(wood);

        Rotate rotateX = new Rotate(0, 400, 300.5, -1107, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, 400, 300.5, -1107, Rotate.Y_AXIS);


        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case SPACE:                 //opening microwave doors
                    RotateTransition rt = new RotateTransition(Duration.millis(200), doors);
                    TranslateTransition tt = new TranslateTransition(Duration.millis(200), doors);
                    if (System.currentTimeMillis() > lastDoorAction + 200 &&
                        System.currentTimeMillis() > lastCookingAction + cookingTime) {
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
                    break;
                case ENTER:                 //cooking
                    if (!areDoorsOpen && !isCookingThread) {
                        cookingTime = 9000;
                        cookingCycles = cookingTime / 9000;
                        CookingTimeHandler animate = new CookingTimeHandler(cookingTime, glass, this);
                        animate.start();
                        lastCookingAction = System.currentTimeMillis();
                        rt = new RotateTransition(Duration.millis(cookingTime), food);
                        rt.setAxis(Rotate.Y_AXIS);
                        rt.setByAngle(360 * cookingCycles);
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
                table,
                glass,
                cabinet,
                cabinet2,
                knob,
                drawers,
                drawers2,
                dishwasher,
                refrigerator);
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
