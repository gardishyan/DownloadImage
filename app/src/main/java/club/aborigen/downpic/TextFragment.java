package club.aborigen.downpic;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class TextFragment extends Fragment implements TextDownloader.TextDownloaderFeedback {
    private TextDownloader downloader;
    private TextView documentView;
    private TextView infoView;
    private ProgressBar progressView;

    public TextFragment() {
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
        View root = inflater.inflate(R.layout.fragment_text, container, false);
        documentView = root.findViewById(R.id.document);
        infoView = root.findViewById(R.id.info);
        progressView = root.findViewById(R.id.progress);

        TextView starter = root.findViewById(R.id.starter);
        starter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloader = new TextDownloader(getString(R.string.text_link), TextFragment.this);
                downloader.start();
            }
        });

        return root;
    }

    @Override
    public void onTextProgress(int progress) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressView.setProgress(progress);
            }
        });
    }

    @Override
    public void onTextDownloaded(String str, int size, long elapsed) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                documentView.setText(str);
                infoView.setText(getString(R.string.download_size, size, elapsed));
            }
        });
    }
}
