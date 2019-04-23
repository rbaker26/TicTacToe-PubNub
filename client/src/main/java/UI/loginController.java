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
    private Button enterButton;
    private Button createButton;

    public loginController() {

        Label windowTitle = new Label("Welcome! Please login!");
        Label windowTitle2 = new Label("New member: select Create");


        Label nameLabel = new Label("Username: ");
        usernameField = new TextField();
        usernameField.setPrefColumnCount(10);


        Label passwordLabel = new Label("Password: ");
        passwordField = new TextField();
        passwordField.setPrefColumnCount(10);

        enterButton = new Button("Login");

        createButton = new Button("Create");

        Label blank = new Label("");


        GridPane grid = new GridPane();

        grid.setMinSize(400, 200);

        grid.setPadding(new Insets(10,10,10,10));
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setAlignment(Pos.CENTER);



        GridPane.setConstraints(windowTitle, 0, 0);

        grid.getChildren().add(windowTitle);

        GridPane.setConstraints(windowTitle2, 0, 1);

        grid.getChildren().add(windowTitle2);

        GridPane.setConstraints(blank, 0, 2);
        grid.getChildren().add(blank);


       GridPane.setConstraints(nameLabel, 0, 3);

       grid.getChildren().add(nameLabel);


       GridPane.setConstraints(usernameField, 1, 3);

       grid.getChildren().add(usernameField);


       GridPane.setConstraints(passwordLabel, 0, 4);

       grid.getChildren().add(passwordLabel);


       GridPane.setConstraints(passwordField, 1, 4);

       grid.getChildren().add(passwordField);


       GridPane.setConstraints(enterButton, 0, 5);

       grid.getChildren().add(enterButton);

       GridPane.setConstraints(createButton, 1, 5);
       grid.getChildren().add(createButton);


       //Styling nodes
        enterButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font:normal bold 'impact'");
        createButton.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white; -fx-font:normal bold 'impact'");
        //;

        nameLabel.setStyle("-fx-font: normal bold 15px 'impact'; -fx-text-fill: darkslateblue ");
        passwordLabel.setStyle("-fx-font: normal bold 15px 'impact'; -fx-text-fill: darkslateblue  ");

        windowTitle.setStyle("-fx-font: normal bold 20px 'arial'; -fx-text-fill: darkslateblue ");
        windowTitle2.setStyle("-fx-font: normal bold 15px 'arial'; -fx-text-fill: darkslateblue ");




        //String path = "src/main/resources/img/X.png";

       // grid.setStyle("-fx-background-image: url('path')");

        grid.setStyle("-fx-background-color: #99CCFF;");


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
