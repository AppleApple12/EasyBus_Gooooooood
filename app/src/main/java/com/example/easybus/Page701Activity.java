package com.example.easybus;
/*注意事項影片*/
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class Page701Activity extends AppCompatActivity {
    VideoView videoView;
    MediaController mc;
    String url = Urls.url1+"/LoginRegister/video/one.mp4";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page701);
        //隱藏title bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //跳選擇頁


        videoView=findViewById(R.id.videoView);
        mc = new MediaController(this);
        //播放video
        new Page701Activity.fetchData1().execute();

        //影片播放時發生錯誤時觸發的方法
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i("通知","播放中出現錯誤");
                return false;
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Intent it = new Intent(Page701Activity.this,Page70101Activity.class);
                startActivity(it);
            }
        });

    }
    public class fetchData1 extends AsyncTask<Void,Void,Void> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Page701Activity.this);
            pd.setCancelable(false);
            pd.setMessage("加載中...請稍等!");
            pd.setProgress(0);
            pd.show();
        }
        protected void onPostExecute(Void aVoid) {

                mc.setAnchorView(videoView);
                // Get the URL from String VideoURL
                Uri video = Uri.parse(url);
                videoView.setMediaController(mc);
                videoView.setVideoURI(video);

                videoView.requestFocus();
                pd.dismiss();
                videoView.start();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
