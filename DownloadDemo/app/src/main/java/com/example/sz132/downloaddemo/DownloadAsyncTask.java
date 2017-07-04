package com.example.sz132.downloaddemo;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sz132 on 2017/7/3.
 */

public class DownloadAsyncTask extends AsyncTask<String, Integer, Integer> {

    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPause = false;
    private int lastProgress;

    public DownloadAsyncTask(DownloadListener listener) {
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream inputStream = null;
        RandomAccessFile saveFile = null;
        File file = null;
        try {
            long downloadLength = 0;//记录已经下载的文件长度
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory, fileName);
            if (file.exists()){
                downloadLength = file.length();
            }
            //得到要下载的文件的总长度
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0){
                return TYPE_FAILED;
            }else if (contentLength == downloadLength){
                return TYPE_SUCCESS;
            }
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    //okhttp的断点下载
                    .addHeader("RANGE", "bytes="+downloadLength+"-")
                    .url(downloadUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            if (response != null){
                //输入
                inputStream = response.body().byteStream();
                //输出
                saveFile = new RandomAccessFile(file, "rw");
                //跳过已经下载的字节
                saveFile.seek(downloadLength);
                byte[] bytes = new byte[1024];
                int total = 0;
                int length;
                while ((length = inputStream.read(bytes)) != -1){
                    if (isPause){
                        return TYPE_PAUSED;
                    }else if (isCanceled){
                        return TYPE_CANCELED;
                    }else {
                        total += length;
                        saveFile.write(bytes);
                        //计算已下载的百分比
                        int progress = (int) ((total+downloadLength) * 100/contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null){
                    inputStream.close();
                }
                if (saveFile != null){
                    saveFile.close();
                }
                if (isCanceled && file != null){
                    file.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (values[0] >= lastProgress){
            listener.onProgress(values[0]);
            lastProgress = values[0];
        }
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            default:
                break;
        }
    }

    public void pauseDownload(){
        isPause = true;
    }

    public void canceledDownload(){
        isCanceled = true;
    }

    /**
     * 得到下载的文件长度
     * @param downloadUrl
     * @return
     * @throws IOException
     */
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = okHttpClient.newCall(request).execute();
        if (response != null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }

}
