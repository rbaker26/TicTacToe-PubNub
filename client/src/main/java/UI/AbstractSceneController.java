package UI;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * This is a slightly more concrete version of ISceneController, providing
 * both functions. The only requirement is that masterScene is initialized
 * by calling setMasterScene.
 */
abstract public class AbstractSceneController implements ISceneController {

    private Scene masterScene;

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
        targetStage.setScene(getMasterScene());
    }

    /**
     * Gets the scene to be applied using applyScene and
     * applySceneAsync.
     * @return The scene which may be staged.
     */
    public Scene getMasterScene() {
        return masterScene;
    }

    protected void setMasterScene(Scene masterScene) {
        this.masterScene = masterScene;
    }

    //region Object overrides
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractSceneController that = (AbstractSceneController) o;
        return Objects.equals(getMasterScene(), that.getMasterScene());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMasterScene());
    }

    @Override
    public String toString() {
        return "AbstractSceneController{" +
                "masterScene=" + masterScene +
                '}';
    }
    //endregion
}
