package club.aborigen.downpic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
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
            long ms1 = System.currentTimeMillis();
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            Log.i(TAG, "Trying to connect: " + address);

            connection.connect();
            int contentLength = connection.getContentLength();
            Log.i(TAG, "Connected: " + address + ", expected length: " + contentLength);

            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            ByteArrayOutputStream output = new ByteArrayOutputStream(contentLength);

            byte[] chunk = new byte[1024];
            int count = input.read(chunk);
            while (count > 0) {
                output.write(chunk, 0, count);
                count = input.read(chunk);
                listener.onBitmapProgress((output.size() * 100) / contentLength);
                Thread.sleep(100); 
            }

            Bitmap bmp = BitmapFactory.decodeByteArray(output.toByteArray(), 0, output.size());
            Log.i(TAG, "Download finished: " + output.size());

            listener.onBitmapDownloaded(bmp, contentLength, System.currentTimeMillis() - ms1);
        } catch (Exception e) {
            Log.e(TAG, "Downloader error: " + e);
        }
    }

    interface DownloaderFeedback {
        void onBitmapProgress(int progress);
        void onBitmapDownloaded(Bitmap bmp, int size, long elapsed);
    }
}
