//required imports
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.*;


public class layout extends Application
{
    // Textfield for inputting id.
    private TextField id;
    // PasswordField for inputting password.
    private PasswordField password;
    // label
    private Label label;
    // salt for adding into the password for unique  encryption.
    private static String salt = Passwords.getSalt(30);

    // abstract method
    @Override
    public void start(Stage primaryStage){
        // Setting the title.
        primaryStage.setTitle("Password Encryptor");
        // Pane.
        GridPane pane = new GridPane();
        // Setting the size of the pane.
        pane.setMinSize(400, 200);
        // Setting the inner margins or padding.
        pane.setPadding(new Insets(10, 10, 10, 10));
        // Setting the vertical distance of elements from the pane.
        pane.setVgap(5);
        // Setting the horizontal distance of elements from the pane.
        pane.setHgap(5);
        // Aligning the elements to the centre.
        pane.setAlignment(Pos.CENTER);
        // Label.
        label = new Label("");
        // Sign up button.
        Button button = new Button("Sign up");
        // Setting the action for the button when it is pressed.
        button.setOnAction( actionEvent -> {
            // Handling the exception
            try {
                signUp();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });

        Button button1 = new Button("Log in");

        button1.setOnAction(actionEvent ->
        {
            try {
                logIn();
            }
           catch (Exception e)
           {
               e.printStackTrace();
           }
        });

        // Initializing the TextField.
        id = new TextField();
        //  Setting the prompt text.
        id.setPromptText("Enter the id");
        // Initializing the PasswordField.
        password = new PasswordField();
        password.setPromptText("Enter the passsword");
        // Text.
        Text text1 = new Text("Id:");
        Text text2 = new Text("Password:");

        // Adding  elements to the pane with their positions.
        pane.add(text1, 0, 0);
        pane.add(id, 1, 0);
        pane.add(text2, 0, 1);
        pane.add(password, 1, 1);
        pane.add(button, 0, 2);
        pane.add(button1,  1, 2);
        pane.add(label, 0,4);

        // Creating a scene.
        Scene scene = new Scene(pane);
        // Setting the scene to the stage.
        primaryStage.setScene(scene);
        primaryStage.show();
        // Making the stage non-resizable
        primaryStage.setResizable(false);
    }

    public static void main(String[] args)
    {
        // This method launches the Javafx application.
        launch(args);
    }

    private void signUp() throws Exception
    {
        // Checking if the input in the fields are invalid.
        if(this.id.getText().trim().equals("") || this.password.getText().equals(""))
        {
            label.setTextFill(Color.RED);
            label.setText("All fields are mandotary!");
        }
        else {
            label.setText("");
            label.setTextFill(Color.BLACK);
            // Getting the inputted id from the textfield.
            String id = this.id.getText();
            // Getting the inputted password from the textfield.
            String password = this.password.getText();
            // Name of the file.
            String name = "Credentials";
            // Object for writing data into the file.
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(name, true)));
            // writing id.
            pw.print(id + " ");
            // writing password and salt value.
            pw.println(Passwords.generateSecurePassword(password, salt) + " " + salt + " ");
            pw.close();
            // Clearing the fields
            this.id.clear();
            this.password.clear();
            label.setText("Credentials successfully saved.");
        }
    }

    // This is the method for matching the inputted credentials to those saved in the file.
    private void logIn() throws Exception
    {
        if(this.id.getText().trim().equals("") || this.password.getText().equals(""))
        {
            label.setTextFill(Color.RED);
            label.setText("All fields are mandotary!");
        }
        else {
            label.setText("");
            label.setTextFill(Color.BLACK);
            // Object for reading the credentials from the files.
            BufferedReader bufferedReader = new BufferedReader(new FileReader("Credentials"));
            String s;
            // Getting the inputted values from the fields.
            String id = this.id.getText();
            String password = this.password.getText();
            // Checking all the credentials.
            while ((s = bufferedReader.readLine()) != null) {
                // Note: String builder is more efficient to use in the place of String somne mutation is needed in it's value.
                StringBuilder word = new StringBuilder();
                // String array
                String[] passwordInfo = new String[3];
                int c = 0;
                // for loop for separating the id, password and the salt.
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        passwordInfo[c++] = word.toString();
                        word.setLength(0);
                    } else {
                        word.append(s.charAt(i));
                    }
                }

                // Function for checking if the provided password and the password already saved in the file is the same.
                boolean passwordMatched = Passwords.verifyUserPassword(password, passwordInfo[1], passwordInfo[2]);
                // if the id and the password matches then then telling the user "Login successful."
                if (id.equals(passwordInfo[0]) && passwordMatched) {
                    label.setText("Login successful.");
                }
                // if the id and the password did not match then telling the user "Incorrect id or paassword."
                else {
                    label.setText("Incorrect id or paassword.");
                }
            }
            this.id.clear();
            this.password.clear();
        }
    }
}
