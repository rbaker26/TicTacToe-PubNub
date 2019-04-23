package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;


public class loginController extends AbstractSceneController{

    private TextField usernameField;
    private TextField passwordField;
    private Button enterButton;
    private Button createButton;

    public loginController() {


        Label nameLabel = new Label("Username: ");
        usernameField = new TextField();
        usernameField.setPrefColumnCount(10);


        Label passwordLabel = new Label("Password: ");
        passwordField = new TextField();
        passwordField.setPrefColumnCount(10);

        enterButton = new Button("Login");

        createButton = new Button("Create");

        GridPane grid = new GridPane();

        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);


       GridPane.setConstraints(nameLabel, 0, 0);

       grid.getChildren().add(nameLabel);


       GridPane.setConstraints(usernameField, 1, 0);

       grid.getChildren().add(usernameField);


       GridPane.setConstraints(passwordLabel, 0, 1);

       grid.getChildren().add(passwordLabel);


       GridPane.setConstraints(passwordField, 1, 1);

       grid.getChildren().add(passwordField);


       GridPane.setConstraints(enterButton, 0, 2);

       grid.getChildren().add(enterButton);

       GridPane.setConstraints(createButton, 1, 2);
       grid.getChildren().add(createButton);


       setRoot(grid);

    }

    public Button getEnterButton() {
        return enterButton;
    }

    public Button getCreateButton() {
        return createButton;
    }

    public String getUsernameField() {
        return usernameField.getText();
    }

    public String getPasswordField() {
        return passwordField.getText();
    }


}
