package cz.stv.canvasdemofx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class MainForm implements Initializable
{



  private EventHandler<ActionEvent> timingHandler = new EventHandler<ActionEvent>() {

    @Override
    public void handle(ActionEvent event)
    {
      draw();           //stále volá draw
    }
  };

  private Timeline timer; //k dělání animací
  
  @FXML
  private Canvas canvas;


  KeyCode keyb = KeyCode.KP_UP; //event.getCode(); keyb je nastaveno na šipku nahotu

  @Override
  public void initialize(URL url, ResourceBundle rb) {      // Z projektu z analogovích hodin. Díky initalize se vše stále vykresluje dokola (časová osa).

    Duration duration = Duration.millis(50);
    KeyFrame keyFrame = new KeyFrame (duration, timingHandler);
    
    timer = new Timeline(keyFrame);
    timer.setCycleCount(Timeline.INDEFINITE);
    timer.play();
  }

  double platformone = 260.0;     //pozice platform
  double platformtwo = 260.0;

  @FXML
  private void draw() {        //draw kreslí canvas

    double width = canvas.getWidth();         //nastavování parametrů
    double height = canvas.getHeight();

    double ballX = width / 2.0;
    double ballY = height / 2.0;





                                        //program na ukázání fungčnosti onKeyPressed
    if (keyb == KeyCode.KP_UP) {
      if (platformtwo > 1){
        platformtwo -= 3.0;
      } else {
        keyb = KeyCode.KP_DOWN;
      }
    } else if (keyb == KeyCode.KP_DOWN) {
      if (519 > platformtwo){
        platformtwo += 3.0;
      } else {
        keyb = KeyCode.W;
      }
    } else if (keyb == KeyCode.W) {
      if (platformone > 1){
        platformone -= 3.0;
      } else {
        keyb = KeyCode.S;
      }
    }else if (keyb == KeyCode.S) {
      if (519 > platformone){
        platformone += 3.0;
      }
    }



                        //vykterlování
    drawDelate();

    drawBall(ballX, ballY);
    drawPlatformFirst(platformone);
    drawPlatformSec(platformtwo);

  }
  
  private void drawDelate() {   //Změní celý canvas na bílí
    
    GraphicsContext gc = canvas.getGraphicsContext2D();
    
    gc.setFill(Color.WHITESMOKE);
    gc.fillRect(1, 1, 1000, 1000);
    
  }

  private void drawPlatformFirst(double platform) {  //nakreslí 1. platformu

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(5);
    gc.strokeLine(10, platform, 10, platform + 80);
  }

  private void drawPlatformSec(double platform) { //nakreslí 2. platformu

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(5);
    gc.strokeLine(990, platform, 990, platform + 80);
  }

  private void drawBall(double xPoz,double yPoz) { //nakreslí míč

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(8);
    gc.strokeLine(xPoz, yPoz, xPoz, yPoz);
  }

  @FXML
  public void onKeyPressed(KeyEvent event) { // Nefunkční z důvodu špatného skenu klávesy (Klávesa není nikdy naskenována v xml??)

    KeyCode key = event.getCode();  // Keyboard code for the pressed key.

    if (key == KeyCode.KP_UP) {         //Jednotlivé prvky, které se třídí
      if (platformtwo > 1){
        platformtwo -= 3.0;
      }
    } else if (key == KeyCode.KP_DOWN) {
      if (519 > platformtwo){
        platformtwo += 3.0;
      }
    } else if (key == KeyCode.W) {
      if (platformone > 1){
        platformone -= 3.0;
      }
    }else if (key == KeyCode.S) {
      if (519 > platformone){
        platformone += 3.0;
      }
    }
  }
}
