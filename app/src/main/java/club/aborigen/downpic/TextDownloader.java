package club.aborigen.downpic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class TextDownloader extends Worker {
    public final String TAG = "DWN";

    public TextDownloader(Context context, WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            long ms1 = System.currentTimeMillis();
            String address = getInputData().getString("address");
            Log.i(TAG, "Downloader worker started: " + address);

            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            Log.i(TAG, "Trying to connect: " + address);

            connection.connect();
            int contentLength = connection.getContentLength();
            Log.i(TAG, "Connected: " + address + ", expected length: " + contentLength);

            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            StringBuffer output = new StringBuffer();

            byte[] chunk = new byte[4];
            int count = input.read(chunk);
            while (count > 0) {
                output.append(new String(chunk, StandardCharsets.UTF_8));
                count = input.read(chunk);

                Data pd = new Data.Builder()
                        .putInt("progress", (output.length() * 100) / contentLength)
                        .build();
                setProgressAsync(pd);

                Thread.sleep(100);
            }

            Log.i(TAG, "Download finished: " + output.length());

            Data od = new Data.Builder()
                    .putString("text", output.toString())
                    .putInt("length", contentLength)
                    .putLong("elapsed", System.currentTimeMillis() - ms1)
                    .build();
            return Worker.Result.success(od);
        } catch (Exception e) {
            Log.e(TAG, "Downloader error: " + e);
            return Worker.Result.failure();
        }

    }

    static UUID start(Context context, String address) {
        Data data = new Data.Builder()
                .putString("address", address)
                .build();
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(TextDownloader.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(context).enqueue(work);
        return work.getId();
    }
}
