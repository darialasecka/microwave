package Project;

import java.net.URL;
import java.util.logging.Logger;
import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
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

public class Simulation extends Application {
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;

    private double height = 520;
    private double depth = -700;

    ObjModelImporter objModelImporter = new ObjModelImporter();
    ObjModelImporter knobModelImporter = new ObjModelImporter();
    ObjModelImporter timeKnobModelImporter = new ObjModelImporter();
    ObjModelImporter doorModelImporter = new ObjModelImporter();
    ObjModelImporter plateModelImporter = new ObjModelImporter();
    ObjModelImporter tableModelImporter = new ObjModelImporter();
    ObjModelImporter cabinetModelImporter = new ObjModelImporter();
    ObjModelImporter dishwasherModelImporter = new ObjModelImporter();
    ObjModelImporter drawersModelImporter = new ObjModelImporter();
    ObjModelImporter drawers2ModelImporter = new ObjModelImporter();
    ObjModelImporter refrigeratorModelImporter = new ObjModelImporter();

    @Override
    public void start(Stage stage) {

        Box background[] = spawnRoom();

        try {
            URL url = this.getClass().getResource("/objects3D/mikrofala.obj");
            objModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/pokretlo.obj");
            knobModelImporter.read(url);
            timeKnobModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/drzwi.obj");
            doorModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/tacka.obj");
            plateModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/podwojna_szafka.obj");
            cabinetModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/z_szufladami.obj");
            drawersModelImporter.read(url);
            drawers2ModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/stolik.obj");
            tableModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/zmywarka.obj");
            dishwasherModelImporter.read(url);
            url = this.getClass().getResource("/objects3D/lodowka.obj");
            refrigeratorModelImporter.read(url);
        } catch (ImportException ie) {
            Logger.getLogger(getClass().getName()).severe("Could not load file: " + ie.getMessage());
        }

        //Importing microwave elements
        MeshView micro = objModelImporter.getImport()[0];
        MeshView doors = doorModelImporter.getImport()[0];
        MeshView knob = knobModelImporter.getImport()[0];
        MeshView timeKnob = timeKnobModelImporter.getImport()[0];
        MeshView plate = plateModelImporter.getImport()[0];

        //Importing furniture
        MeshView cabinet = cabinetModelImporter.getImport()[0];
        MeshView table = tableModelImporter.getImport()[0];
        MeshView drawers = drawersModelImporter.getImport()[0];
        MeshView drawers2 = drawers2ModelImporter.getImport()[0];
        MeshView dishwasher = dishwasherModelImporter.getImport()[0];
        MeshView refrigerator = refrigeratorModelImporter.getImport()[0];

        //Textures:
        PhongMaterial melon = new PhongMaterial();
        melon.setDiffuseMap(new Image(getClass().getResourceAsStream("/textures/melon.png")));

        PhongMaterial transparent = new PhongMaterial();
        transparent.setDiffuseColor(new Color(1, 0.65,0.1,0.3));

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseMap(new Image(getClass().getResourceAsStream("/textures/grey.jpg")));

        PhongMaterial platePattern = new PhongMaterial();
        platePattern.setDiffuseMap(new Image(getClass().getResourceAsStream("/textures/plate.jpg")));

        PhongMaterial wood = new PhongMaterial();
        wood.setDiffuseMap(new Image(getClass().getResourceAsStream("/textures/wood.jpg")));

        //Other shapes
        Box food = new Box(0.6, 0.6, 0.6);
        food.setTranslateX(399.6);
        food.setTranslateY(300.5);
        food.setTranslateZ(-1107.1);
        food.setMaterial(melon);

        Box glass = new Box(4, 3, 0.1);
        glass.setTranslateX(399.6);
        glass.setTranslateY(299.8);
        glass.setTranslateZ(-1108.4);
        glass.setMaterial(transparent);
        glass.setVisible(false);

        //Light
        PointLight light = new PointLight();
        light.setTranslateX(400);
        light.setTranslateY(280);
        light.setTranslateZ(-1115);

        PointLight light2 = new PointLight(Color.web("#262626"));
        light2.setTranslateX(350);
        light2.setTranslateY(290);
        light2.setTranslateZ(-2000);

        //Rotates
        Rotate rotateX = new Rotate(0, 400, 300.5, -1107, Rotate.X_AXIS);
        Rotate rotateY = new Rotate(0, 400, 300.5, -1107, Rotate.Y_AXIS);

        //Camera
        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setNearClip(0.001);
        camera.setFarClip(100.0);
        camera.getTransforms().addAll(rotateX, rotateY);

        //Custom parts of microwave
        micro.setTranslateX(400);
        micro.setTranslateY(301);
        micro.setTranslateZ(-1107);
        micro.setMaterial(material);

        doors.setTranslateX(400);
        doors.setTranslateY(301);
        doors.setTranslateZ(-1107);
        doors.setMaterial(material);

        knob.setTranslateX(400);
        knob.setTranslateY(301);
        knob.setTranslateZ(-1106.9);

        timeKnob.setTranslateX(400);
        timeKnob.setTranslateZ(-1106.9);
        timeKnob.setTranslateY(knob.getTranslateY() + 0.79);

        plate.setTranslateX(400);
        plate.setTranslateY(301);
        plate.setTranslateZ(-1107);
        plate.setMaterial(platePattern);


        //Custom furniture
        Scale scale = new Scale(10,10,10);
        table.setTranslateX(400);
        table.setTranslateY(306.2);
        table.setTranslateZ(-1107);
        table.getTransforms().addAll(scale);
        table.setMaterial(wood);

        scale = new Scale(15,15,15);
        cabinet.setTranslateX(310);
        cabinet.setTranslateY(height);
        cabinet.setTranslateZ(depth);
        cabinet.getTransforms().addAll(scale);
        cabinet.setMaterial(wood);

        drawers.setTranslateX(444.1);
        drawers.setTranslateY(height);
        drawers.setTranslateZ(depth);
        drawers.getTransforms().addAll(scale);
        drawers.setMaterial(wood);

        drawers2.setTranslateX(530);
        drawers2.setTranslateY(height);
        drawers2.setTranslateZ(depth);
        drawers2.getTransforms().addAll(scale);
        drawers2.setMaterial(wood);

        dishwasher.setTranslateX(170);
        dishwasher.setTranslateY(height);
        dishwasher.setTranslateZ(-690);
        dishwasher.getTransforms().addAll(scale);
        dishwasher.setMaterial(wood);

        refrigerator.setTranslateX(72);
        refrigerator.setTranslateY(height);
        refrigerator.setTranslateZ(depth);
        refrigerator.getTransforms().addAll(scale);
        refrigerator.setMaterial(wood);

        //Microwave's logic
        ActionHandler ah = new ActionHandler(glass, food, timeKnob);
        ah.start();

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
                knob,
                timeKnob,
                cabinet,
                drawers,
                drawers2,
                dishwasher,
                refrigerator);
        Scene scene = new Scene(root, 800, 600, true);
        //stage.setResizable(false);
        stage.setOnCloseRequest(ev -> System.exit(0));
        scene.setCamera(camera);


        /*ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                double scaleX = scene.getWidth() / 800;
                root.setScaleX(scaleX);
                double scaleY = scene.getHeight() / 600;
                root.setScaleY(scaleY);
                scene.setRoot(root);
                System.out.println(scaleX + ", " + scaleY);
        };
        stage.widthProperty().addListener(stageSizeListener);
        stage.heightProperty().addListener(stageSizeListener);*/

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

        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case SPACE:
                    ah.handleDoorEvent(doors);
                    break;
                case Q:
                    ah.handleTimeKnobEvent(timeKnob);
                    break;
                case W:
                    ah.handlePowerKnobEvent(knob);
                    break;
            }
        });

        doors.setOnMouseClicked(event -> {
            ah.handleDoorEvent(doors);
        });

        timeKnob.setOnMouseClicked(event -> {
            ah.handleTimeKnobEvent(timeKnob);
        });

        knob.setOnMouseClicked(event -> {
            ah.handlePowerKnobEvent(knob);
        });

        stage.setScene(scene);
        stage.setTitle("Microwave Simulator");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    private Box[] spawnRoom() {
        Box[] background = new Box[6];
        for (int i = 0; i < 6; i++) {
            background[i] = new Box(2200, 1700, 1);
            PhongMaterial image = new PhongMaterial();
            image.setDiffuseMap(new Image(getClass().getResourceAsStream("/textures/background_image.jpg")));
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
        background[4].setTranslateX(400);
        background[4].setTranslateY(-550);
        background[4].setRotationAxis(Rotate.X_AXIS);
        background[4].setRotate(90);
        background[4].setWidth(2500);
        background[4].setHeight(2700);
        PhongMaterial ceiling = new PhongMaterial();
        ceiling.setDiffuseMap(new Image(getClass().getResourceAsStream("/textures/sufit.jpg")));
        background[4].setMaterial(ceiling);

        background[5].setTranslateZ(-1105);         //5 - podłoga
        background[5].setTranslateX(400);
        background[5].setRotationAxis(Rotate.X_AXIS);
        background[5].setRotate(-90);
        background[5].setTranslateY(750);//1000
        background[5].setWidth(2500);
        background[5].setHeight(2200);
        PhongMaterial floor = new PhongMaterial();
        floor.setDiffuseMap(new Image(getClass().getResourceAsStream("/textures/panele.jpg")));
        background[5].setMaterial(floor);

        return background;
    }

}
