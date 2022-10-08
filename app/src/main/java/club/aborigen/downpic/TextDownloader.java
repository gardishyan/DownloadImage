package club.aborigen.downpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class TextDownloader extends Thread {
    private final String TAG = "DWN";
    public String address;
    private final TextDownloaderFeedback listener;

    public TextDownloader(String address, TextDownloaderFeedback listener) {
        this.address = address;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        Log.i(TAG, "Downloader thread started: " + address);

        try {
            long ms1 = System.currentTimeMillis();
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            Log.i(TAG, "Trying to connect: " + address);

            connection.connect();
            int contentLength = connection.getContentLength();
            Log.i(TAG, "Connected: " + address + ", expected length: " + contentLength);

            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            StringBuffer output = new StringBuffer();

            byte[] chunk = new byte[1024];
            int count = input.read(chunk);
            while (count > 0) {
                output.append(new String(chunk, StandardCharsets.UTF_8));
                count = input.read(chunk);
                listener.onTextProgress((output.length() * 100) / contentLength);
                Thread.sleep(100); 
            }

            Log.i(TAG, "Download finished: " + output.length());

            listener.onTextDownloaded(output.toString(), contentLength, System.currentTimeMillis() - ms1);
        } catch (Exception e) {
            Log.e(TAG, "Downloader error: " + e);
        }
    }

    interface TextDownloaderFeedback {
        void onTextProgress(int progress);
        void onTextDownloaded(String str, int size, long elapsed);
    }
}
