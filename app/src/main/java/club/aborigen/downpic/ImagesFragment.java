package club.aborigen.downpic;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImagesFragment extends Fragment  implements Downloader.DownloaderFeedback {
    private Downloader downloader;
    private ImageView pictureView;
    private TextView infoView;
    private ProgressBar progressView;

    public ImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_images, container, false);
        pictureView = root.findViewById(R.id.picture);
        infoView = root.findViewById(R.id.info);
        progressView = root.findViewById(R.id.progress);

        TextView starter1 = root.findViewById(R.id.starter_1);
        starter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloader = new Downloader(getString(R.string.image_link_1),ImagesFragment.this);
                downloader.start();
            }
        });
        TextView starter2 = root.findViewById(R.id.starter_2);
        starter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloader = new Downloader(getString(R.string.image_link_2),ImagesFragment.this);
                downloader.start();
            }
        });
        TextView starter3 = root.findViewById(R.id.starter_3);
        starter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloader = new Downloader(getString(R.string.image_link_3),ImagesFragment.this);
                downloader.start();
            }
        });

        return root;
    }

    @Override
    public void onBitmapProgress(int progress) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setProgress(progress);
            }
        });
    }

    @Override
    public void onBitmapDownloaded(Bitmap bmp, int size, long elapsed) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pictureView.setImageBitmap(bmp);
                infoView.setText(getString(R.string.download_size, size, elapsed));
            }
        });
    }
}
