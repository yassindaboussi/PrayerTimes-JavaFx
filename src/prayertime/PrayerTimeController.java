/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package prayertime;

import Utils.Functions;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXToggleButton;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author yassin
 */
public class PrayerTimeController implements Initializable {

    @FXML
    private StackPane stckDoua;
    @FXML
    private Label txtFajr;
    @FXML
    private Label txtDhohr;
    @FXML
    private Label txtAsar;
    @FXML
    private Label txtMoghreb;
    @FXML
    private Label txtecha;
    @FXML
    private Label txtNextSalat;
    @FXML
    private Label TimeNextSalat;
    @FXML
    private Label timeLabel;
    @FXML
    private Label day;
    @FXML
    private Pane PaneMainApp;
    @FXML
    private Label RestTime;

    @FXML
    private Pane PaneNoConnection;
    @FXML
    private ImageView imgSound1;
    @FXML
    private ImageView imgSound2;
    @FXML
    private ImageView imgSound3;
    @FXML
    private ImageView imgSound4;
    @FXML
    private ImageView imgSound5;
    @FXML
    private Pane Adhan;
    @FXML
    private Label txtAdhanSalat;

    public DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
    private Date NexTsalaTT = null;
    private String connection = "No";
    public static int countClickedSoundFajar = 0;
    public static int countClickedSoundDhoher = 0;
    public static int countClickedSoundAsar = 0;
    public static int countClickedSoundMoghreb = 0;
    public static int countClickedSoundieacha = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        try {
//            //
//
////            Functions.Oppa();
////            
////        } catch (IOException ex) {
////            Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
////        } catch (ParseException ex) {
////            Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
////        }
        Timeline RestTimee = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            Functions.initClock(timeLabel, day);
            // || TimeTaw ==24h
            if (connection == "No") {
                if (Functions.netIsAvailable()) {
                    PaneNoConnection.setVisible(false);
                    System.out.println("True");
                    connection = "Yes";

                    Functions.InterNetTime(txtFajr, txtDhohr, txtAsar, txtMoghreb, txtecha); //Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
                    Functions.CoverToSeconds();//To easy Compare Time
                    TimeOfNexTSalatis();
                    Functions.VerifTimeisEqual(Adhan, txtAdhanSalat);
                    //  Functions.ProgressBar(ProgressBar, dureeProgress());
                } else {
                    PaneNoConnection.setVisible(true);
                    System.out.println("False");
                    connection = "No";
                }
            }
        }), new KeyFrame(Duration.seconds(1)));
        RestTimee.setCycleCount(Animation.INDEFINITE);
        RestTimee.play();
    }

    private void TimeOfNexTSalatis() { // Problem CountDown (After SalaT ieacha) 

        String FajrWithSec = Functions.TimeFajer + ":" + "00";
        String DhoherWithSec = Functions.TimeDhoher + ":" + "00";
        String AsarWithSec = Functions.TimeAsar + ":" + "00";
        String MoghrebWithSec = Functions.TimeMoghreb + ":" + "00";
        String ieachaWithSec = Functions.Timeieacha + ":" + "00";

        Timeline RestTimee = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            String TimeNow = LocalDateTime.now().format(formatter);
            // System.out.println("date "+TimeNow);
            Date TimeTaw = null;
            Date NexTsalaT = null;
            try {
                TimeTaw = format.parse(TimeNow); //obligatoir Format HH:mm:ss
            } catch (ParseException ex) {
                // Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
            }
            Long TimeNowSeconds = TimeTaw.getTime() / 1000; //To Second to easy compare

            if (TimeNowSeconds <= Functions.FajrToSeconds || TimeNowSeconds > Functions.IeachaToSeconds) {
                txtNextSalat.setText("الفجر");
                TimeNextSalat.setText(Functions.TimeFajer);
                try {
                    NexTsalaT = format.parse(FajrWithSec); //obligatoir Format HH:mm:ss
                } catch (ParseException ex) {
                    //Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (TimeNowSeconds > Functions.FajrToSeconds && TimeNowSeconds <= Functions.DhohrToSeconds) {
                txtNextSalat.setText("الضهر");
                TimeNextSalat.setText(Functions.TimeDhoher);
                try {
                    NexTsalaT = format.parse(DhoherWithSec); //obligatoir Format HH:mm:ss
                } catch (ParseException ex) {
                    //Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (TimeNowSeconds > Functions.FajrToSeconds && TimeNowSeconds > Functions.DhohrToSeconds && TimeNowSeconds <= Functions.AsarToSeconds) {
                txtNextSalat.setText("العصر");
                TimeNextSalat.setText(Functions.TimeAsar);
                try {
                    NexTsalaT = format.parse(AsarWithSec); //obligatoir Format HH:mm:ss
                } catch (ParseException ex) {
                    // Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (TimeNowSeconds > Functions.FajrToSeconds && TimeNowSeconds > Functions.DhohrToSeconds && TimeNowSeconds > Functions.AsarToSeconds && TimeNowSeconds <= Functions.MoghrebToSeconds) {
                txtNextSalat.setText("المغرب");
                TimeNextSalat.setText(Functions.TimeMoghreb);
                try {
                    NexTsalaT = format.parse(MoghrebWithSec); //obligatoir Format HH:mm:ss
                } catch (ParseException ex) {
                    // Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (TimeNowSeconds > Functions.FajrToSeconds && TimeNowSeconds > Functions.DhohrToSeconds && TimeNowSeconds > Functions.AsarToSeconds && TimeNowSeconds > Functions.MoghrebToSeconds && TimeNowSeconds <= Functions.IeachaToSeconds) {
                txtNextSalat.setText("العشاء");
                TimeNextSalat.setText(Functions.Timeieacha);
                try {
                    NexTsalaT = format.parse(ieachaWithSec); //obligatoir Format HH:mm:ss
                } catch (ParseException ex) {
                    // Logger.getLogger(PrayerTimeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            long diffpp = NexTsalaT.getTime() - TimeTaw.getTime();

            if (diffpp < 0) { // To Solve The Problem Fajer Countdown befor 00:00 (-sign)
                long difff = (86400000 - TimeTaw.getTime()) + (NexTsalaT.getTime());
//                System.out.println("\n \n");
//                System.out.println("TimeTaw1 " + TimeTaw.getTime());
//                System.out.println("NexTsalaT1 " + NexTsalaT.getTime());
//                System.out.println("difference1 " + difff);
                long diffSecondss = difff / 1000;
                long hourss = diffSecondss / 3600;
                long minutess = (diffSecondss % 3600) / 60;
                long secondss = diffSecondss % 60;
                String timeStringg = String.format("%02d:%02d:%02d", hourss, minutess, secondss); //.replace("-", "")
                RestTime.setText(String.valueOf(timeStringg));
            } else {
                long diff = NexTsalaT.getTime() - TimeTaw.getTime();
                // System.out.println("NexTsalaT2 " + NexTsalaT.getTime());
                long diffSeconds = diff / 1000;
                /////////////////////////////////////////////////////////////////////////////// Convert Long To FOrmat HH:mm:ss
                long hours = diffSeconds / 3600;
                long minutes = (diffSeconds % 3600) / 60;
                long seconds = diffSeconds % 60;
                String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds); //.replace("-", "")
                RestTime.setText(String.valueOf(timeString));
            }

            //String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }), new KeyFrame(Duration.seconds(1)));
        RestTimee.setCycleCount(Animation.INDEFINITE);
        RestTimee.play();

    }

    @FXML
    private void SoundFajerClicked(MouseEvent event) throws FileNotFoundException {
        countClickedSoundFajar++;
        if (countClickedSoundFajar == 1) {
            InputStream stream = new FileInputStream("src/rsc/NoSound.png");
            Image image = new Image(stream);
            imgSound1.setImage(image);
        }
        if (countClickedSoundFajar == 2) {
            InputStream stream = new FileInputStream("src/rsc/Sound.png");
            Image image = new Image(stream);
            imgSound1.setImage(image);
            countClickedSoundFajar = 0;
        }
    }

    @FXML
    private void SoundDhoherClicked(MouseEvent event) throws FileNotFoundException {
        countClickedSoundDhoher++;
        if (countClickedSoundDhoher == 1) {
            InputStream stream = new FileInputStream("src/rsc/NoSound.png");
            Image image = new Image(stream);
            imgSound2.setImage(image);
        }
        if (countClickedSoundDhoher == 2) {
            InputStream stream = new FileInputStream("src/rsc/Sound.png");
            Image image = new Image(stream);
            imgSound2.setImage(image);
            countClickedSoundDhoher = 0;
        }
    }

    @FXML
    private void SoundAserClicked(MouseEvent event) throws FileNotFoundException {
        countClickedSoundAsar++;
        if (countClickedSoundAsar == 1) {
            InputStream stream = new FileInputStream("src/rsc/NoSound.png");
            Image image = new Image(stream);
            imgSound3.setImage(image);
        }
        if (countClickedSoundAsar == 2) {
            InputStream stream = new FileInputStream("src/rsc/Sound.png");
            Image image = new Image(stream);
            imgSound3.setImage(image);
            countClickedSoundAsar = 0;
        }
    }

    @FXML
    private void SoundMaghrebClicked(MouseEvent event) throws FileNotFoundException {
        countClickedSoundMoghreb++;
        if (countClickedSoundMoghreb == 1) {
            InputStream stream = new FileInputStream("src/rsc/NoSound.png");
            Image image = new Image(stream);
            imgSound4.setImage(image);
        }
        if (countClickedSoundMoghreb == 2) {
            InputStream stream = new FileInputStream("src/rsc/Sound.png");
            Image image = new Image(stream);
            imgSound4.setImage(image);
            countClickedSoundMoghreb = 0;
        }
    }

    @FXML
    private void SoundieachaClicked(MouseEvent event) throws FileNotFoundException {
        countClickedSoundieacha++;
        if (countClickedSoundieacha == 1) {
            InputStream stream = new FileInputStream("src/rsc/NoSound.png");
            Image image = new Image(stream);
            imgSound5.setImage(image);
        }
        if (countClickedSoundieacha == 2) {
            InputStream stream = new FileInputStream("src/rsc/Sound.png");
            Image image = new Image(stream);
            imgSound5.setImage(image);
            countClickedSoundieacha = 0;
        }
    }

    @FXML
    private void Close(MouseEvent event) {
        //System.exit(0);
        PrayerTime.stage.setIconified(true);
    }

    @FXML
    private void MuteClick(MouseEvent event) { 
        if(Functions.MEDIA_PLAYER!=null)
        {
        Functions.MEDIA_PLAYER.stop();
        }
    }

}
