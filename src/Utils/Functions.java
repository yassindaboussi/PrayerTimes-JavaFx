/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import com.jfoenix.controls.JFXProgressBar;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import prayertime.PrayerTime;
import prayertime.PrayerTimeController;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author yassin
 */
public class Functions {

    public static Date date;
    public static List<String> result = new ArrayList<>(); // List Times

    public static String TimeFajer;
    public static String TimeDhoher;
    public static String TimeAsar;
    public static String TimeMoghreb;
    public static String Timeieacha;

    public static Long FajrToSeconds;
    public static Long DhohrToSeconds;
    public static Long AsarToSeconds;
    public static Long MoghrebToSeconds;
    public static Long IeachaToSeconds;

    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

    public static MediaPlayer MEDIA_PLAYER;

    private static int OnetimeFajar = 0;
    private static int OnetimeDhoher = 0;
    private static int OnetimeAsar = 0;
    private static int OnetimeMoghreb = 0;
    private static int Onetimeieacha = 0;

    private static String FajerAM;
    private static String DhoherPM;
    private static String AsarPM;
    private static String MoghrebPM;
    private static String iechaPM;

    public static Boolean netIsAvailable() {
        try {
            final URL url = new URL("http://www.google.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static void Oppa() throws IOException, ParseException {
        Document document = null;
        document = Jsoup.connect("").get();
        Elements el = document.select(".cm-string");

        for (Element e : el) {
            System.out.println("ssssssssssssssss" + e.text());
            result.add(e.text());
        }

    }

    public static void InterNetTime(Label txtFajr, Label txtDhohr, Label txtAsar, Label txtMoghreb, Label txtecha) {
        try {
            String url = "https://muslimsalat.com/tunisia.json";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.addRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                //  System.out.println("" + response);
            }
            in.close();
            JSONObject myResponse = new JSONObject(response.toString());

            //  String pageName = myResponse.getJSONObject("pageInfo").getString("pageName");
            JSONArray arr = myResponse.getJSONArray("items");
            for (int i = 0; i < arr.length(); i++) {
                TimeFajer = arr.getJSONObject(i).getString("fajr");
                TimeDhoher = arr.getJSONObject(i).getString("dhuhr");
                TimeAsar = arr.getJSONObject(i).getString("asr");
                TimeMoghreb = arr.getJSONObject(i).getString("maghrib");
                Timeieacha = "0" + arr.getJSONObject(i).getString("isha");
            }

            result.add(0, TimeFajer);
            result.add(1, TimeDhoher);
            result.add(2, TimeAsar);
            result.add(3, TimeMoghreb);
            result.add(4, Timeieacha);
            //System.out.println("Fajer" + result.get(0)); //Show the first element (0)
///////////////////////////////////////////////////////////////////////////
            Calendar calender = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("hh:mm aa");
            Date date;
///////////////////////////////////////////////////////////////////////////  DhoherAM To 24H
            String FajerAMM = "0" + result.get(0);
            String FajerAM2 = FajerAMM.replace("am", "");
            FajerAM = FajerAM2.replaceAll("\\s+", "");
///////////////////////////////////////////////////////////////////////////  DhoherAM To 24H
            String DhoherAM = result.get(1);
            String DhoherPM2 = DhoherAM.replace("pm", "");
            DhoherPM = DhoherPM2.replaceAll("\\s+", "");
///////////////////////////////////////////////////////////////////////////  Asar To 24H
            String AsarAM = result.get(2) + " PM";
            System.out.println("" + AsarAM);
            date = format.parse(AsarAM);
            calender.setTime(date);
             AsarPM = calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE);          
///////////////////////////////////////////////////////////////////////////  Moghreb To 24H
            String MoghrebAM = result.get(3) + " PM";
            date = format.parse(MoghrebAM);
            calender.setTime(date);
            MoghrebPM = calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE);
// System.out.println("MoghrebPM " + MoghrebPM);
///////////////////////////////////////////////////////////////////////////  Iecha To 24H
            String iechaAM = result.get(4) + " PM";
            date = format.parse(iechaAM);
            calender.setTime(date);
            iechaPM = calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE);
// System.out.println("iechaPM " + iechaPM);
//////////////////////////////////////////////////////////////////////////////////////////////////////////
            txtFajr.setText(FajerAM);
            txtDhohr.setText(DhoherPM);
            txtAsar.setText(AsarPM);
            txtMoghreb.setText(MoghrebPM);
            txtecha.setText(iechaPM);
//////////////////////// To Use it in Another Func()
            TimeFajer = FajerAM;
            TimeDhoher = DhoherPM;
            TimeAsar = AsarPM;
            TimeMoghreb = MoghrebPM;
            Timeieacha = iechaPM;
        } catch (IOException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void Playsound() {
        MEDIA_PLAYER = new MediaPlayer(new Media(new File("src/rsc/Adhan.mp3").toURI().toString()));
        MEDIA_PLAYER.play();

        MEDIA_PLAYER.setOnEndOfMedia(() -> {
            System.out.println("Mp3 Is End !");
            OnetimeFajar = 0;
            OnetimeDhoher = 0;
            OnetimeAsar = 0;
            OnetimeMoghreb = 0;
            Onetimeieacha = 0;
        });

    }

    public static void VerifTimeisEqual(Pane Adhan, Label txtAdhanSalat) {

        Timeline timelinee = new Timeline();
        timelinee.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {

                //  timelinee.stop();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String TimeNow = LocalDateTime.now().format(formatter);
                // System.out.println("TimeNow " + TimeNow);
                String DateFajr = "0" + result.get(0);
                if (DateFajr.equals(TimeNow) && OnetimeFajar == 0) { // To check Only One time
                    System.out.println("اذان الفجر");
                    Adhan.setVisible(true);
                    PrayerTime.stage.setIconified(false);
                    txtAdhanSalat.setText("الفجر");
                    txtAdhanSalat.setAlignment(Pos.CENTER);
                    if (PrayerTimeController.countClickedSoundFajar != 1) {
                        Playsound();
                    }
                    OnetimeFajar = 1;
                    Timeline timeline2 = new Timeline(new KeyFrame(Duration.minutes(2), event2 -> {
                        OnetimeFajar = 0;
                        Adhan.setVisible(false);
                    }));
                    timeline2.play();
                }
                if (result.get(2).equals(TimeNow) && OnetimeDhoher == 0) {
                    System.out.println("اذان الضهر");

                    Adhan.setVisible(true);
                    PrayerTime.stage.setIconified(false);
                    txtAdhanSalat.setText("الضهر");
                    txtAdhanSalat.setAlignment(Pos.CENTER);
                    if (PrayerTimeController.countClickedSoundDhoher != 1) {
                        Playsound();
                    }
                    OnetimeDhoher = 1;
                    Timeline timeline2 = new Timeline(new KeyFrame(Duration.minutes(2), event2 -> {
                        OnetimeDhoher = 0;
                        Adhan.setVisible(false);
                    }));
                    timeline2.play();
                }

                if (AsarPM.equals(TimeNow) && OnetimeAsar == 0) {
                    System.out.println("اذان العصر");

                    Adhan.setVisible(true);
                    PrayerTime.stage.setIconified(false);
                    txtAdhanSalat.setText("العصر");
                    txtAdhanSalat.setAlignment(Pos.CENTER);
                    if (PrayerTimeController.countClickedSoundAsar != 1) {
                        Playsound();
                    }
                    OnetimeAsar = 1;
                    Timeline timeline2 = new Timeline(new KeyFrame(Duration.minutes(2), event2 -> {
                        OnetimeAsar = 0;
                        Adhan.setVisible(false);
                    }));
                    timeline2.play();
                }
                if (MoghrebPM.equals(TimeNow) && OnetimeMoghreb == 0) {
                    System.out.println("اذان المغرب");

                    Adhan.setVisible(true);
                    PrayerTime.stage.setIconified(false);
                    txtAdhanSalat.setText("المغرب");
                    txtAdhanSalat.setAlignment(Pos.CENTER);
                    if (PrayerTimeController.countClickedSoundMoghreb != 1) {
                        Playsound();
                    }
                    OnetimeMoghreb = 1;
                    Timeline timeline2 = new Timeline(new KeyFrame(Duration.minutes(2), event2 -> {
                        OnetimeMoghreb = 0;
                        Adhan.setVisible(false);
                    }));
                    timeline2.play();
                }
                if (iechaPM.equals(TimeNow) && Onetimeieacha == 0) {
                    System.out.println("اذان العشاء");

                    Adhan.setVisible(true);
                    PrayerTime.stage.setIconified(false);
                    txtAdhanSalat.setText("العشاء");
                    txtAdhanSalat.setAlignment(Pos.CENTER);
                    if (PrayerTimeController.countClickedSoundieacha != 1) {
                        Playsound();
                    }
                    Onetimeieacha = 1;
                    Timeline timeline2 = new Timeline(new KeyFrame(Duration.minutes(2), event2 -> {
                        Onetimeieacha = 0;
                        Adhan.setVisible(false);
                    }));
                    timeline2.play();
                }
            }
        }));
        // Repeat indefinitely until stop() method is called.
        timelinee.setCycleCount(Animation.INDEFINITE);
        timelinee.setAutoReverse(true);
        timelinee.play();
    }

    public static void initClock(Label timeLabel, Label day) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> { //dd/MM/yyyyy 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            timeLabel.setText(LocalDateTime.now().format(formatter));
            date = new Date();
            String DayNowis = String.valueOf(date.getDay());
            if (DayNowis.equals("0")) {
                day.setText("الأحد");
            }
            if (DayNowis.equals("1")) {
                day.setText("الاثنين");
            }
            if (DayNowis.equals("2")) {
                day.setText("الثلاثاء");
            }
            if (DayNowis.equals("3")) {
                day.setText("الأربعاء");
            }
            if (DayNowis.equals("4")) {
                day.setText("الخميس");
            }
            if (DayNowis.equals("5")) {
                day.setText("الجمعة");
            }
            if (DayNowis.equals("6")) {
                day.setText("السبت");
            }
            day.setAlignment(Pos.CENTER); //center Text
            //day.setTextFill(Color.RED);
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public static void CoverToSeconds() {
        try {
            String FajrWithSec = Functions.TimeFajer + ":" + "00";
            String DhoherWithSec = Functions.TimeDhoher + ":" + "00";
            String AsarWithSec = Functions.TimeAsar + ":" + "00";
            String MoghrebWithSec = Functions.TimeMoghreb + ":" + "00";
            String ieachaWithSec = Functions.Timeieacha + ":" + "00";

            //////////////////////////////////////////////////////////////////////////////////// DateNow To SEC
            String TimeNoww = LocalDateTime.now().format(formatter);
            Date DateTaww = null;
            DateTaww = format.parse(TimeNoww); //obligatoir Format HH:mm:ss
            Long TimeNowSeconds = DateTaww.getTime() / 1000;
            System.out.println("TimeNow in Seconds ==>>>> " + TimeNowSeconds);
            //////////////////////////////////////////////////////////////////////////////////// Fajer To Sec
            Date FajarSec = null;
            FajarSec = format.parse(FajrWithSec); //obligatoir Format HH:mm:ss
            FajrToSeconds = FajarSec.getTime() / 1000;
            System.out.println("Fajer in Seconds ==>>>> " + FajrToSeconds);
            //////////////////////////////////////////////////////////////////////////////////// Dhoher To Sec
            Date DhoherSec = null;
            DhoherSec = format.parse(DhoherWithSec); //obligatoir Format HH:mm:ss
            DhohrToSeconds = DhoherSec.getTime() / 1000;
            System.out.println("Dhoher in Seconds ==>>>> " + DhohrToSeconds);
            //////////////////////////////////////////////////////////////////////////////////// Asar To Sec
            Date AsarSec = null;
            AsarSec = format.parse(AsarWithSec); //obligatoir Format HH:mm:ss
            AsarToSeconds = AsarSec.getTime() / 1000;
            System.out.println("Asar in Seconds ==>>>> " + AsarToSeconds);
            //////////////////////////////////////////////////////////////////////////////////// Moghreb To Sec
            Date MoghrebSec = null;
            MoghrebSec = format.parse(MoghrebWithSec); //obligatoir Format HH:mm:ss
            MoghrebToSeconds = MoghrebSec.getTime() / 1000;
            System.out.println("Moghreb in Seconds ==>>>> " + MoghrebToSeconds);
            //////////////////////////////////////////////////////////////////////////////////// Moghreb To Sec
            Date IeachaSec = null;
            IeachaSec = format.parse(ieachaWithSec); //obligatoir Format HH:mm:ss
            IeachaToSeconds = IeachaSec.getTime() / 1000;
            System.out.println("Ieacha in Seconds ==>>>> " + IeachaToSeconds);
            ///////////////////////////////////////////////////////////////////////////////////
        } catch (ParseException ex) {
            Logger.getLogger(Functions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //    public static void ProgressBar(JFXProgressBar ProgressBar, double duree) {
////        Long l = new Long(14447);
////        double d = l.doubleValue();
////        System.out.println("d" + d);
//        Timeline timeline = new Timeline(
//                new KeyFrame(Duration.ZERO, new KeyValue(ProgressBar.progressProperty(), 0)),
//                new KeyFrame(Duration.seconds(duree), e -> {
//                    // do anything you need here on completion...
//                    //System.out.println("Minute over");
//                    // PaneMainApp.setStyle(RandomGradient());
//                }, new KeyValue(ProgressBar.progressProperty(), 1))
//        );
//        timeline.setCycleCount(Animation.INDEFINITE);
//        timeline.play();
//    }
}
