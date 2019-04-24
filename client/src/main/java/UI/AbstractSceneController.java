package UI;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * This is a slightly more concrete version of ISceneController, providing
 * both functions. Inheritors should call setRoot before applyScene gets
 * called.
 */
abstract public class AbstractSceneController implements ISceneController {

    //private Scene masterScene;
    private Parent root;

    @Override
    public void applySceneAsync(Stage targetStage) {
        applySceneAsync(targetStage, false);
    }

    @Override
    public void applySceneAsync(Stage targetStage, boolean alsoShow) {
        Platform.runLater(() -> {
            applyScene(targetStage);

            if(alsoShow) {
                targetStage.show();
            }
        });
    }

    @Override
    public void applyScene(Stage targetStage) {

        Scene oldScene = targetStage.getScene();

        // Attempt to get the width of the old stage.
        // This is our fallback values.
        double stageWidth = targetStage.getMinWidth();
        double stageHeight = targetStage.getMinHeight();

        // However, it's preferable to use the old scene's
        // width and height. So we'll try to get that.
        // (Uh... why is it preferable?)
        if(oldScene != null) {
            stageWidth = oldScene.getWidth();
            stageHeight = oldScene.getHeight();
        }

        Parent root = getRoot();

        // We will need to make sure the window isn't smaller than the
        // minimum values.
        /*
        double targetWidth = Math.max(root.minWidth(stageHeight), stageWidth);
        double targetHeight = Math.max(root.minHeight(stageWidth), stageHeight);
         */
        double targetWidth = stageWidth;
        double targetHeight = stageHeight;

        System.out.println("Width: " + root.minWidth(stageHeight) + ", " + stageWidth);
        System.out.println("Height: " + root.minHeight(stageWidth) + ", " + stageHeight);

        // Now we can finally apply the size stuff that we've figured out.

        // This is needed and it is dumb...

        Scene newScene;
        if(oldScene == null) {
            newScene = new Scene(root, targetWidth, targetHeight);
        }
        else {
            newScene = oldScene;
            newScene.setRoot(root);
        }
        targetStage.setScene(newScene);

        //targetStage.setWidth(targetWidth);
        //targetStage.setHeight(targetHeight);

        root.resize(targetWidth, targetHeight);    // This is needed so that layouts update correctly

    }


    public Parent getRoot() {
        return root;
    }

    public void setRoot(Parent root) {
        this.root = root;
    }

    //region Object overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSceneController that = (AbstractSceneController) o;
        return Objects.equals(getRoot(), that.getRoot());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoot());
    }

    @Override
    public String toString() {
        return "AbstractSceneController{" +
                "masterScene=" + root +
                '}';
    }
    //endregion
}
