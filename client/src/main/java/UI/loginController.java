package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class loginController extends AbstractSceneController{

    private TextField usernameField;
    private TextField passwordField;
    private TextField screenNameField;
    private Button enterButton;
    private Button createButton;

    public loginController() {

        Label screenNameLabel = new Label("Screen Name: ");
        screenNameField = new TextField();
        screenNameField.setPrefColumnCount(10);

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

        GridPane.setConstraints(screenNameLabel, 0, 0);
        grid.getChildren().add(screenNameLabel);

        GridPane.setConstraints(screenNameField, 1, 0);
        grid.getChildren().add(screenNameField);

       GridPane.setConstraints(nameLabel, 0, 1);

       grid.getChildren().add(nameLabel);


       GridPane.setConstraints(usernameField, 1, 1);

       grid.getChildren().add(usernameField);


       GridPane.setConstraints(passwordLabel, 0, 2);

       grid.getChildren().add(passwordLabel);


       GridPane.setConstraints(passwordField, 1, 2);

       grid.getChildren().add(passwordField);


       GridPane.setConstraints(enterButton, 0, 3);

       grid.getChildren().add(enterButton);

       GridPane.setConstraints(createButton, 1, 3);
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
public String getScreenNameField() {
        return  screenNameField.getText();
}



}
