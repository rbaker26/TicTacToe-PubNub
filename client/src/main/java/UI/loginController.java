package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

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

        Label windowTitle = new Label("Welcome! Please login!");
        Label windowTitle2 = new Label("New Member: Select 'Create'");
        Label blank = new Label("");

        enterButton = new Button("Login");

        createButton = new Button("Create");

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


        GridPane.setConstraints(screenNameLabel, 0, 3);

        grid.getChildren().add(screenNameLabel);

        GridPane.setConstraints(screenNameField, 1, 3);

        grid.getChildren().add(screenNameField);


        GridPane.setConstraints(nameLabel, 0, 4);

        grid.getChildren().add(nameLabel);

        GridPane.setConstraints(usernameField, 1, 4);

        grid.getChildren().add(usernameField);


        GridPane.setConstraints(passwordLabel, 0, 5);

        grid.getChildren().add(passwordLabel);


        GridPane.setConstraints(passwordField, 1, 5);

        grid.getChildren().add(passwordField);


        GridPane.setConstraints(enterButton, 0, 6);

        grid.getChildren().add(enterButton);

        GridPane.setConstraints(createButton, 1, 6);
        grid.getChildren().add(createButton);


       grid.setStyle("-fx-background-color: linear-gradient(to bottom, #33ccff, #ccffff)");

       enterButton.setStyle("-fx-background-color: #00ccff; -fx-text-fill: white; -fx-font: bold 'impact'; -fx-font: normal bold 15px 'arial'");

       nameLabel.setStyle("-fx-font: normal bold 15px 'impact'; -fx-text-fill: white; -fx-text-outline: black");

       passwordLabel.setStyle("-fx-font: normal bold 15px 'impact'; -fx-text-fill: white; -fx-text-outline: black");

       screenNameLabel.setStyle("-fx-font: normal bold 15px 'impact'; -fx-text-fill: white; -fx-text-outline: black");

       createButton.setStyle("-fx-background-color: #00ccff; -fx-text-fill: white; -fx-font: bold 'impact'; -fx-font: normal bold 15px 'arial'");

       windowTitle.setStyle("-fx-font: normal bold 25px 'arial'; -fx-text-fill: white; -fx-text-stroke: black; -fx-text-stroke-width: 2px ");

       windowTitle2.setStyle("-fx-font: normal bold 15px 'arial'; -fx-text-fill: white ");

      // windowTitle.setFont(Font.font(null, FontWeight.BOLD, 30));

      //  Reflection r = new Reflection();
        //r.setFraction(0.7f);
        //windowTitle.setEffect(r);
        //windowTitle.setTranslateY(400);

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


    public void setUsernameField(TextField usernameField) {

        this.usernameField = usernameField;

    }

    public void setPasswordField(TextField passwordField) {

        this.passwordField = passwordField;
    }

    public void setScreenNameField(TextField screenNameField) {

        this.screenNameField = screenNameField;

    }



}
