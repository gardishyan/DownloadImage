package club.aborigen.downpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Downloader extends Thread {
    private final String TAG = "DWN";
    public String address;
    private final DownloaderFeedback listener;

    public Downloader(String address, DownloaderFeedback listener) {
        this.address = address;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "Downloader thread started: " + address);

        try {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            Log.i(TAG, "Trying to connect: " + address);

            connection.connect();
            int contentLength = connection.getContentLength();
            Log.i(TAG, "Connected: " + address + ", expected length: " + contentLength);

            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            Bitmap bmp = BitmapFactory.decodeStream(input);
            Log.i(TAG, "Download finished");

            listener.onBitmapDownloaded(bmp);
        } catch (Exception e) {
            Log.e(TAG, "Downloader error: " + e);
        }
    }

    interface DownloaderFeedback {
        void onBitmapDownloaded(Bitmap bmp);
    }
}
