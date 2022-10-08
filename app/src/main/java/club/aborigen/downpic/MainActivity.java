package club.aborigen.downpic;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Downloader.DownloaderFeedback {

    private Downloader downloader;
    private ImageView pictureView;
    private TextView infoView;
    private ProgressBar progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pictureView = findViewById(R.id.picture);
        infoView = findViewById(R.id.info);
        progressView = findViewById(R.id.progress);

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
    public void onBitmapProgress(int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setProgress(progress);
            }
        });
    }

    @Override
    public void onBitmapDownloaded(Bitmap bmp, int size, long elapsed) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pictureView.setImageBitmap(bmp);
                infoView.setText(getString(R.string.download_size, size, elapsed));
            }
        });
    }
}