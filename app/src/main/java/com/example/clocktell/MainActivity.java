package com.example.clocktell;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech mTTS;
    private Button btnTellTime;
    private Button btnTellDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(new Locale("hi_IN"));

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not Supported!");
                    } else {
                        btnTellTime.setEnabled(true);
                        btnTellDate.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization Failed!");
                }
            }
        });

        btnTellTime = findViewById(R.id.tellTime);
        btnTellDate = findViewById(R.id.tellDate);
        btnTellTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakTime();
            }
        });
        btnTellDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speakDate();
            }
        });
    }

    private void speakTime() {
        SimpleDateFormat sdfTime = new SimpleDateFormat("h:mm");
        String currentTime = sdfTime.format(new Date());
        String[] timeNodes = currentTime.split(":");
        String timeMsg = "समय है " + timeNodes[0] + " बजकर " + timeNodes[1] + " मिनट";

        mTTS.setPitch(1.2f);
        mTTS.setSpeechRate(0.5f);
        mTTS.speak(timeMsg, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void speakDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy:MM:dd");
        String currentDate = sdfDate.format(new Date());
        String[] dateNodes = currentDate.split(":");
        Calendar date = Calendar.getInstance();
        String dayToday = android.text.format.DateFormat.format("EEEE", date).toString();
        HashMap<String, String> dayName = new HashMap<>();
        dataValue(dayName);
        String day = dayToday;
        if (dayName.containsKey(dayToday)) {
            day = dayName.get(dayToday);
        }


        String dateMsg = "आज का दिनांक है " + day + " " + dateNodes[2] + " तारिख " + Integer.parseInt(dateNodes[1]) + " महीना " + dateNodes[0];

        mTTS.setPitch(1.2f);
        mTTS.setSpeechRate(0.5f);
        mTTS.speak(dateMsg, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void dataValue(HashMap<String, String> dayName) {
        dayName.put("Sunday", "रविवार");
        dayName.put("Monday", "सोमवार");
        dayName.put("Tuesday", "मंगलवार");
        dayName.put("Wednesday", "बुधवार");
        dayName.put("Thursday", "वृहस्पतिवार");
        dayName.put("Friday", "शुक्रवार");
        dayName.put("Saturday", "शनिवार");
    }

    @Override
    protected void onDestroy() {
        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }
}