import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Signupcontroller
{
    @FXML
    Button Submit;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    TextField ServerIP;
    @FXML
    TextField PortNo;
    private Socket socket;
    LoginWindow lw;
    Stage window;

    public void Sign() throws IOException
    {
        socket = new Socket(ServerIP.getText(), Integer.parseInt(PortNo.getText()));
        System.out.println("Connected to server");
        Signupclass data = new Signupclass(username.getText(),password.getText());
        ObjectOutputStream oos=new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(data);
        oos.flush();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = "jdbc:mysql://127.0.0.1:3306/Chat_App";
        try {
            Connection connection = DriverManager.getConnection(url, "root", "password");
            String q="CREATE TABLE `Local"+username.getText()+"Chats` (\n" +
                    "  `Sender` varchar(15) NOT NULL,\n" +
                    "  `Receiver` varchar(15) NOT NULL,\n" +
                    "  `Message` text NOT NULL,\n" +
                    "  `SentTime` timestamp NULL DEFAULT NULL,\n" +
                    "  `ReceivedTime` timestamp NULL DEFAULT NULL,\n" +
                    "  `SeenTime` timestamp NULL DEFAULT NULL\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
            PreparedStatement ps=connection.prepareStatement(q);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            lw=new LoginWindow();
            lw.start(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
