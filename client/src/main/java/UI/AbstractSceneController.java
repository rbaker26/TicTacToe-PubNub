package UI;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This is a slightly more concrete version of ISceneController, providing
 * both functions, and rather asking just for a function to provide a
 * scene.
 */
abstract public class AbstractSceneController implements ISceneController {

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
     * applySceneAsync. Note that this should be saved as part of
     * the scene, rather than rebuilt every time it is called.
     * @return The scene which may be staged.
     */
    abstract protected Scene getMasterScene();
}
