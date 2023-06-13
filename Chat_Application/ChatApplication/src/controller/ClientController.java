package controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;

import java.nio.charset.StandardCharsets;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;

import java.io.*;
import java.net.Socket;

public class ClientController extends Thread{

    public AnchorPane chatscreenAnconepane;
    public TextField msgTextfield;
    public Button btnEmojis;
    public ScrollPane scrollPane;
    public VBox vboxMsg;
    public Button btnback;
    public Button btncam;
    public AnchorPane emojiAnchorpane;
    public ImageView emoji2;
    public ImageView emoji7;
    public ImageView emoji8;
    public ImageView emoji1;
    public ImageView emoji3;
    public ImageView emoji6;
    public ImageView emoji4;
    public ImageView emoji5;
    public Button btnsend;
    Socket socket;
    BufferedReader bufferedReader;
    BufferedWriter bufferedWriter;
    public static String username;

    public HBox hBox;
    public VBox vBox;
    public ImageView imageView;
    public Image image;
    public FileChooser fileChooser;
    public File file;
    public Text text;
    public TextFlow textFlow;
    public PrintWriter printWriter;


    String[] emojiPath = new String[8];
    public FontAwesomeIconView imgEmoji;

    public ClientController() {
    }

    public void initialize() {
        try {
            this.socket = new Socket("localhost", 3000);
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedWriter.write(username);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
            new Thread(() -> {
                String msg;
                try {
                    while (true) {
                        msg = bufferedReader.readLine();
                        String[] tokens = msg.split(" ");
                        String cmd = tokens[0];
                        StringBuilder stringBuilder = new StringBuilder();
                        for (int i = 1; i < tokens.length; i++) {
                            stringBuilder.append(tokens[i]);
                        }
                        if (msg.startsWith("IMG")) {
                            String replace = msg.replace("IMG", "");
                            String[] split = replace.split("=");


                            hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 5, 5, 5));

                            Text text1 = new Text(split[0] + " :");
                            TextFlow textFlow1 = new TextFlow(text1);
                            textFlow1.setStyle("-fx-font-weight: bold;" + "-fx-background-color:#121212;");
                            textFlow1.setPadding(new Insets(5, 10, 5, 10));
                            text1.setFill(Color.color(1, 1, 1, 1));


                            imageView = new ImageView();
                            imageView.setImage(new Image(new File(split[1]).toURI().toString()));
                            imageView.setFitWidth(100);
                            imageView.setPreserveRatio(true);

                            hBox.getChildren().add(textFlow1);
                            hBox.getChildren().add(imageView);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    vboxMsg.getChildren().add(hBox);
                                }
                            });
                        }else {
                            hBox = new HBox();
                            hBox.setAlignment(Pos.CENTER_LEFT);
                            hBox.setPadding(new Insets(5, 10, 5, 5));

                            text=new Text( msg);
                            textFlow=new TextFlow(text);

                            textFlow.setStyle("-fx-font-weight: bold;"  + "-fx-background-color: #333232;"+ "-fx-background-radius:5px");
                            textFlow.setPadding(new Insets(5, 10, 5, 10));
                            text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 17));
                            text.setFill(Color.color(1, 1, 1, 1));
                            hBox.getChildren().add(textFlow);

                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    vboxMsg.getChildren().add(hBox);
                                }
                            });
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void btnEmojisOnAction(ActionEvent actionEvent) {
        if (emojiAnchorpane.isVisible()) {
            emojiAnchorpane.setVisible(false);
        } else {
            emojiAnchorpane.setVisible(true);
        }

    }

    public void btnbackonAction(ActionEvent actionEvent) {

    }

    public void btncamOnAction(ActionEvent actionEvent) throws IOException {

        fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Image");
        file = fileChooser.showOpenDialog(null);

        bufferedWriter.write("IMG" + username + " =" + file.getPath());
        bufferedWriter.newLine();
        bufferedWriter.flush();

        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(10, 10, 10, 10));

         text = new Text("you    " + '\n');
         textFlow = new TextFlow(text);

        imageView = new ImageView();
        imageView.setImage(new Image(new File(file.getPath()).toURI().toString()));
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        hBox.getChildren().add(textFlow);
        hBox.getChildren().add(imageView);

        vboxMsg.getChildren().add(hBox);
        bufferedWriter.write(username + " : " + imageView);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        msgTextfield.clear();
    }

    public void btnsendOnAction(ActionEvent actionEvent) throws NullPointerException, IOException {

        String msg = msgTextfield.getText();
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(5, 5, 5, 10));
        text = new Text("you    " + '\n' + '\t' +  msg);
        textFlow = new TextFlow(text);

        textFlow.setStyle("-fx-font-weight: bold;" + "-fx-background-color: #eb3464;" + "-fx-background-radius:5px");
        textFlow.setPadding(new Insets(5, 10, 5, 10));
        text.setFill(Color.color(1, 1, 1, 1));
        text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 17));
        hBox.getChildren().add(textFlow);
        vboxMsg.getChildren().add(hBox);

        bufferedWriter.write(username + " : " + msg);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        msgTextfield.clear();
    }

    public void emojiOnAnction(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView emoji = (ImageView) mouseEvent.getSource();
            switch (emoji.getId()) {
                case "emoji1":
                    msgTextfield.appendText(new String("😍".getBytes("UTF-8"), "UTF-8"));
                    break;
                case "emoji2":
                    msgTextfield.appendText(new String("😴".getBytes("UTF-8"), "UTF-8"));
                    break;
                case "emoji3":
                    msgTextfield.appendText(new String("😉".getBytes("UTF-8"), "UTF-8"));
                    break;
                case "emoji4":
                    msgTextfield.appendText(new String("😂".getBytes("UTF-8"), "UTF-8"));
                    break;
                case "emoji5":
                    msgTextfield.appendText(new String("❤️".getBytes("UTF-8"), "UTF-8"));
                    break;
                case "emoji6":
                    msgTextfield.appendText(new String("😡".getBytes("UTF-8"), "UTF-8"));
                    break;
                case "emoji7":
                    msgTextfield.appendText(new String("😇".getBytes("UTF-8"), "UTF-8"));
                    break;
                case "emoji8":
                    msgTextfield.appendText(new String("🤔".getBytes("UTF-8"), "UTF-8"));
                    break;
            }
        }
    }


}
