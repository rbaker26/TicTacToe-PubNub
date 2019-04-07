package UI;

import javafx.stage.Stage;

/**
 * Implementors are expected to be able to have a scene to apply.
 */
public interface ISceneController {
    /**
     * Applies the scene asynchronously. This only needs to be called
     * when activating the scene from a separate thread.
     * @param targetStage Stage to put the scene on.
     */
    void applySceneAsync(Stage targetStage);

    /**
     * Applies the scene asynchronously. This only needs to be called
     * when activating the scene from a separate thread.
     * @param targetStage Stage to put the scene on.
     * @param alsoShow If true, targetStage.show() will be called.
     */
    void applySceneAsync(Stage targetStage, boolean alsoShow);


    /**
     * Applies the scene now, on the current thread. Will crash if
     * called from a thread other than the main one. See
     * applySceneAsync.
     * @param targetStage Stage to put the scene on.
     */
    void applyScene(Stage targetStage);
}
