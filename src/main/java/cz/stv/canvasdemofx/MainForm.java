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
import javafx.scene.text.Text;
import javafx.util.Duration;


public class MainForm implements Initializable {


  private EventHandler<ActionEvent> timingHandler = new EventHandler<ActionEvent>() {

    boolean victroryscrean = true;

    /**
     * volá metodu draw pokud není dosaženos skóre 13 u jednoho ze dvou hráčů
     * pokud jeden hráč dosáhne skóre 13 tak se vypíše v místě, kde se nachází skóre výtěze
     * */
    @Override
    public void handle(ActionEvent event) {

      if (playertwoscore == 13 || playeronescore == 13) {
        if (playertwoscore == 13 && victroryscrean == true) {
          scoreboard.setText("Game over Player 2 wins.");
          victroryscrean = false;
        } else if(victroryscrean == true){
          victroryscrean = false;
          scoreboard.setText("Game over Player 1 wins.");
        }

      } else {

        draw();
      }
    }
  };

  private Timeline timer;

  @FXML
  private Canvas canvas;

  @FXML
  public Text scoreboard;

  /**
   * po každých 5 milisekundách zavolá hande
   * */
  @Override
  public void initialize(URL url, ResourceBundle rb) {

    Duration duration = Duration.millis(5);
    KeyFrame keyFrame = new KeyFrame (duration, timingHandler);

    timer = new Timeline(keyFrame);
    timer.setCycleCount(Timeline.INDEFINITE);
    timer.play();
  }

  double platformone = 260.0;     //nastavování parametrů plošin
  double platformtwo = 260.0;

  boolean upone = false;          //rozhodování zda je daná klávesa ztlačena nebo ne
  boolean uptwo = false;
  boolean downone = false;
  boolean downtwo = false;

  double ballX = 500.0;       //nastavení parametrů míčku
  double ballY = 300.0;
  double ballbouncewalls = 1.0;
  double ballbouncepalatforms = 1.0;

  int playeronescore = 0;    //skóre jednotlivých hráčů
  int playertwoscore = 0;

  /**
   * stále překresluje kanvaz
   * pohynuje s plošinamy
   * */
  @FXML
  private void draw() {

    /*
     * pohybuje s plošinaky podle toho, jaká klávesa je stlačená
     * */
    if (uptwo == true) {
      if (platformtwo > 31) {
        platformtwo -= 1.0;
      } else {
        platformtwo += 2.0;
        uptwo = false;
      }

    }

    if (downtwo == true) {
      if (519 > platformtwo){
        platformtwo += 1.0;
      } else {
        platformtwo -= 2.0;
        downtwo = false;
      }

    }

    if (upone == true) {
      if (platformone > 31){
        platformone -= 1.0;
      } else {
        platformone += 2.0;
        upone = false;
      }

    }

    if (downone == true) {
      if (519 > platformone){
        platformone += 1.0;
      } else {
        platformone -= 2.0;
        downone = false;
      }
    }

    /*
    * zjištůje jestli nedošlo ke kolizy
    * */
    bouncePlatform();
    bounceWall();

    /*
    * Nová lokace míče
    * */
    ballX = ballX - ballbouncepalatforms;
    ballY = ballY - ballbouncewalls;

    /*
    * volání metod na počítání score
    * */
    playerOneScore();
    playerTwoScore();

    /*
    * vykterlování míče a plošin a resetování kanvazu
    * */
    drawDelate();

    drawBorder();
    drawBall(ballX, ballY);
    drawPlatformFirst(platformone);
    drawPlatformSec(platformtwo);

  }

  /**
   * překreslí celí kanvaz na bílou
   * */
  private void drawDelate() {
    
    GraphicsContext gc = canvas.getGraphicsContext2D();
    
    gc.setFill(Color.WHITESMOKE);
    gc.fillRect(0, 0, 1000, 1000);
    
  }

  /**
   * nakreslí první plošinu
   * */
  private void drawPlatformFirst(double platform) {

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(5);
    gc.strokeLine(10, platform, 10, platform + 80);
  }

  /**
   * nakreslí druhou plošinu
   * */
  private void drawPlatformSec(double platform) {

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(5);
    gc.strokeLine(990, platform, 990, platform + 80);
  }

  /**
   * nakreslí malý šedivý obdélník co jakoždo pozadí k liště
   * */
  private void drawBorder() {

    GraphicsContext gc = canvas.getGraphicsContext2D();

    gc.setFill(Color.LIGHTGRAY);
    gc.fillRect(0, 0, 1000, 30);
  }

  /**
   * nakreslí míč
   * */
  private void drawBall(double xPoz,double yPoz) {

    GraphicsContext gc = canvas.getGraphicsContext2D();
    gc.setStroke(Color.BLACK);
    gc.setLineWidth(8);
    gc.strokeLine(xPoz, yPoz, xPoz, yPoz);
  }

  /**
   * určuje kdy dostane druhý hráč bod a nastaví souřadnice míče do středu kanvazu
   * */
  public void playerTwoScore(){
    if (ballX == 1){
      playertwoscore++;
      ballX = 500.0;
      ballY = 300.0;
      ballbouncepalatforms = ballbouncepalatforms * -1;
      scoreboard.setText(playeronescore + " - " +playertwoscore);
    }
  }

  /**
   * určuje kdy dostane první hráč bod a nastaví souřadnice míče do středu kanvazu
   * */
  public void playerOneScore(){
    if (ballX == 999){
      playeronescore++;
      ballX = 500.0;
      ballY = 300.0;
      ballbouncepalatforms = ballbouncepalatforms * -1;
      scoreboard.setText(playeronescore + " - " +playertwoscore);
    }
  }

  /**
   * otáčí pohyb míče pohed dojde ke kolizi s pločinou
   * */
  public void bouncePlatform() {
    if (ballX == 16) {
      if (ballY > platformone && ballY < platformone + 80) {
        ballbouncepalatforms = ballbouncepalatforms * -1;
      }
    } else if (ballX == 983){
      if (ballY > platformtwo && ballY < platformtwo + 80) {
        ballbouncepalatforms = ballbouncepalatforms * -1;
      }
    }
  }

  /**
   * otáčí pohyb míče pohed dojde ke kolizi se zdí
   * */
  public void bounceWall() {

    if (ballY < 34 || ballY > 599){
      ballbouncewalls = ballbouncewalls * -1;
    }
  }

  /**
   * zkoumá jaká klávesa byla stlačená - nedokáže číst dvě klávesy na jednou
   * */
  @FXML
  public void onKeyPressed(KeyEvent event) {

    KeyCode key = event.getCode();

    if (key == KeyCode.O) {
      if (platformtwo > 1){
        uptwo = true;
      }
    } else if (key == KeyCode.L) {
      if (519 > platformtwo){
        downtwo = true;
      }
    } else if (key == KeyCode.W) {
      if (platformone > 1){
        upone = true;
      }
    }else if (key == KeyCode.S) {
      if (519 > platformone){
        downone = true;
      }
    }
  }

  /**
   * zkoumá jaká klávesa byla uvolněna - nedokáže číst dvě klávesy na jednou
   * */
  @FXML
  public void onKeyReleased(KeyEvent event) {

    KeyCode key = event.getCode();

    if (key == KeyCode.O) {
      if (platformtwo > 1){
        uptwo = false;
      }
    } else if (key == KeyCode.L) {
      if (519 > platformtwo){
        downtwo = false;
      }
    } else if (key == KeyCode.W) {
      if (platformone > 1){
        upone = false;
      }
    } else if (key == KeyCode.S) {
      if (519 > platformone){
        downone = false;
      }
    }
  }
}
