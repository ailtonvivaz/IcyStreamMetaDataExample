package com.amicly.icycastmetadata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    IcyStreamMeta streamMeta;
    MetadataTask2 metadataTask2;
    String title_artist;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.main_activity_text_view);


        String streamUrl = "http://kut1.streamguys.net/kut1.aac";

        streamMeta = new IcyStreamMeta();
        try {
            streamMeta.setStreamUrl(new URL(streamUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        metadataTask2 =new MetadataTask2();
        try {
            metadataTask2.execute(new URL(streamUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Timer timer = new Timer();
        MyTimerTask task = new MyTimerTask();
        timer.schedule(task,100, 10000);


    }


    protected class MetadataTask2 extends AsyncTask<URL, Void, IcyStreamMeta>
    {
        @Override
        protected IcyStreamMeta doInBackground(URL... urls)
        {
            try
            {
                streamMeta.refreshMeta();
                Log.e("Retrieving MetaData","Refreshed Metadata");
            }
            catch (IOException e)
            {
                Log.e(MetadataTask2.class.toString(), e.getMessage());
            }
            return streamMeta;
        }

        @Override
        protected void onPostExecute(IcyStreamMeta result)
        {
            try
            {
                title_artist=streamMeta.getStreamTitle();
                Log.e("Retrieved title_artist", title_artist);
                if(title_artist.length()>0)
                {
                    textView.setText(title_artist);
                }
            }
            catch (IOException e)
            {
                Log.e(MetadataTask2.class.toString(), e.getMessage());
            }
        }
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            try {
                streamMeta.refreshMeta();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                String title_artist=streamMeta.getStreamTitle();
                Log.i("ARTIST TITLE", title_artist);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
