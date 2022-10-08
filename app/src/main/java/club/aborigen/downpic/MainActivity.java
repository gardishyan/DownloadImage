package club.aborigen.downpic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Downloader.DownloaderFeedback {

    private Downloader downloader;
    private ImageView pictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictureView = findViewById(R.id.picture);
        TextView starter1 = findViewById(R.id.starter_1);
        starter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloader = new Downloader(getString(R.string.image_link_1),MainActivity.this);
                downloader.start();
            }
        });
        TextView starter2 = findViewById(R.id.starter_2);
        starter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloader = new Downloader(getString(R.string.image_link_2),MainActivity.this);
                downloader.start();
            }
        });
        TextView starter3 = findViewById(R.id.starter_3);
        starter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloader = new Downloader(getString(R.string.image_link_3),MainActivity.this);
                downloader.start();
            }
        });
    }

    @Override
    public void onBitmapDownloaded(Bitmap bmp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pictureView.setImageBitmap(bmp);
            }
        });
    }
}