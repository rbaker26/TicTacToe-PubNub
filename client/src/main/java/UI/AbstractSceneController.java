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
        double width;
        double heigth;

        if(oldScene != null) {
            width = oldScene.getWidth();
            heigth = oldScene.getHeight();
        }
        else {
            width = targetStage.getMinWidth();
            heigth = targetStage.getMinHeight();
        }

        Scene newScene = new Scene(getRoot(), width, heigth);
        targetStage.setScene(newScene);

        getRoot().resize(width, heigth);    // This is needed so that layouts update correctly
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
