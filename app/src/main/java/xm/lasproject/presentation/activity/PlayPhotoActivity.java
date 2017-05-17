package xm.lasproject.presentation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import xm.lasproject.R;

public class PlayPhotoActivity extends AppCompatActivity {

    @BindView(R.id.imageView)
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_photo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Glide.with(this).load(data).into(mImageView);
    }
}
