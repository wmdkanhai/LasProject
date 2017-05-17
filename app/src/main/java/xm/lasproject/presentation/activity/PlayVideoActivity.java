package xm.lasproject.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import xm.lasproject.R;

public class PlayVideoActivity extends AppCompatActivity {

    @BindView(R.id.vv_videoPlay)
    VideoView mVvVideoPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String data = intent.getStringExtra("data");

        mVvVideoPlay.setVideoPath(data);
        MediaController controller = new MediaController(this);
        mVvVideoPlay.setMediaController(controller);
        mVvVideoPlay.start();
    }
}
