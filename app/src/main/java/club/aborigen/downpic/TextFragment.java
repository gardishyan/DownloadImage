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
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

public class TextFragment extends Fragment {
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
                UUID wid = TextDownloader.start(getActivity(), getString(R.string.text_link));
                WorkManager.getInstance(getActivity()).getWorkInfoByIdLiveData(wid)
                        .observe(getActivity(), new Observer<WorkInfo>() {
                            @Override
                            public void onChanged(WorkInfo workInfo) {
                                switch (workInfo.getState()) {
                                    case RUNNING:
                                        Data pd = workInfo.getProgress();
                                        progressView.setProgress(pd.getInt("progress", 0));
                                        break;
                                    case SUCCEEDED:
                                        Data od = workInfo.getOutputData();
                                        documentView.setText(od.getString("text"));
                                        infoView.setText(getString(R.string.download_size,
                                                od.getInt("length", 0), od.getLong("elapsed", 0)));

                                        break;
                                }
                            }
                        });
            }
        });

        return root;
    }
}
