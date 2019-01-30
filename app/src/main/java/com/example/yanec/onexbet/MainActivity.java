package com.example.yanec.onexbet;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yanec.onexbet.Adapters.RecyclerAdapter;
import com.example.yanec.onexbet.DownloadApp.Downloader;
import com.example.yanec.onexbet.InternetClasses.Response;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements OnDownload, OnTryAgain, EasyPermissions.PermissionCallbacks {

    private static int REQUEST_CODE=1;
    private static final int WRITE_REQUEST_CODE = 300;
    private static final int READ_REQUEST_CODE = 42;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String url;
    private EditText editTextUrl;
    RecyclerView rvMatches;
    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadManager downloadManager = new DownloadManager(this);
        downloadManager.downloadInformation(this, this);

       initialize();


        final List<String> installedPackages = getInstalledAppsPackageNameList();
        String packageNamefavorite = "com.betinvest.favorit_sport_com_ua";


        // Check facebook is installed in the device or not
        if(installedPackages.contains(packageNamefavorite)){

            CustomDialog customDialog = new CustomDialog(MainActivity.this);
            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.show();
        }else {
            File app = new File(Environment.getExternalStorageDirectory() + "/Downlaod/run.apk");
            if (app.exists() == true){
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile( getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", new File(Environment.getExternalStorageDirectory().toString() + "/Download/run.apk"));
                    intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    Uri apkUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().toString() + "/Download/run.apk"));
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else
                DownloadInit();

        }

    }
    // Custom method to get all installed apps package name list
    protected List<String> getInstalledAppsPackageNameList(){
        // Initialize a new intent
        Intent intent = new Intent(Intent.ACTION_MAIN,null);

        // Set intent category
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // Set intent flags
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        // Initialize a new list of resolve info
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent,0);

        // Initialize a new list of package name
        List<String> packageNameList = new ArrayList<>();

        for(ResolveInfo resolveInfo: resolveInfoList){

            // Get the activity info from resolve info
            ActivityInfo activityInfo = resolveInfo.activityInfo;

            // Get the package name from activity info's application info
            // Add the package name to the list
            packageNameList.add(activityInfo.applicationInfo.packageName);
        }

        // Return the package name list
        return packageNameList;
    }


    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission to read was granted!");
                return true;
            } else {

                Log.v(TAG,"Permission to read was revoked1");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission to read was granted! AUTO");
            return true;
        }
    }

    public  boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission to write was granted");
                return true;
            } else {

                Log.v(TAG,"Permission to write was revoked!");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission to write was granted! AUTO");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                Log.d(TAG, "External storage2");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    url = "https://favoritsport.onelink.me/829449105?pid=8&af_sub1=835&af_sub2=AffiliateId=185";
                    Downloader downloader = new Downloader(this, Uri.parse(url));
                }else{

                }
                break;

            case 3:
                Log.d(TAG, "External storage1");
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
                    //resume tasks needing this permission
                    url = "https://favoritsport.onelink.me/829449105?pid=8&af_sub1=835&af_sub2=AffiliateId=185";
                    Downloader downloader = new Downloader(this, Uri.parse(url));
                }else{

                }
                break;
        }
    }
    public void DownloadInit(){
        if (isReadStoragePermissionGranted() && isWriteStoragePermissionGranted()){
            url = "https://favoritsport.onelink.me/829449105?pid=8&af_sub1=835&af_sub2=AffiliateId=185";
            Downloader downloader = new Downloader(this, Uri.parse(url));
        }

        /*
        //Check if SD card is present or not
        Log.d("myLogs: ", "START0");
        if (CheckSdCard.isSDCardPresent() ||  1 == 1) {
            Log.d("myLogs: ", "START1");
            //check if app has permission to write to the external storage.
            if (EasyPermissions.hasPermissions(MainActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) && 2 == 1) {
                //Get the URL entered https://utka.su/O4jOG
                url = "https://favoritsport.onelink.me/829449105?pid=8&af_sub1=835&af_sub2=AffiliateId=185";
                Log.d("myLogs: ", "START2");
                new DownloadFile().execute(url);
                Log.d("myLogs: ", "END1");
            } else {

                //If permission is not present request for the same.
                Toast.makeText(getApplicationContext(),
                        "No permissions!", Toast.LENGTH_LONG).show();


                Log.d("myLogs: ", "START2");
                new DownloadFile().execute(url);
                EasyPermissions.requestPermissions(MainActivity.this, getString(R.string.write_file), WRITE_REQUEST_CODE, Manifest.permission.READ_EXTERNAL_STORAGE);

            }


        } else {
            Toast.makeText(getApplicationContext(),
                    "SD Card not found", Toast.LENGTH_LONG).show();

        }
*/
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void initialize(){
        rvMatches = findViewById(R.id.rvMatches);
    }

    private void setAdapter(List<Response> response){

        RecyclerAdapter adapterCountries = new RecyclerAdapter(deleteRepeats(response));

        rvMatches.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvMatches.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMatches.setLayoutManager(llm);

        rvMatches.setAdapter(adapterCountries);
    }

    private List<Response> deleteRepeats(List<Response> responses){
        Set<Response> set = new HashSet<>(responses);
        return new ArrayList(set);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        LayoutInflater inflater;
        View view;
        AlertDialog.Builder alertBuilder;

        switch (id) {
            case Constants.DIALOG_DOWNLOAD:
                inflater = LayoutInflater.from(this);

                view = inflater.inflate(R.layout.download_dialog, null);
                alertBuilder = new AlertDialog.Builder(this);

                alertBuilder.setView(view);

                alertBuilder.setCancelable(false);
                return alertBuilder.create();
        }
        return null;
    }

    @Override
    public void onSuccessDownload(List<Response> response) {
        setAdapter(response);
    }

    @Override
    public void onStartDownload() {
        showDialog(Constants.DIALOG_DOWNLOAD);
    }

    @Override
    public void onFinishDownload() {
        removeDialog(Constants.DIALOG_DOWNLOAD);
    }

    public void tryAgain() {
        DownloadManager downloadManager = new DownloadManager(this);
        downloadManager.downloadInformation(this, this);
    }

    @Override
    public void retry() {
        tryAgain();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**

     */
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this);
    }
*/
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //Download the file once permission is granted
        url = "https://favoritsport.onelink.me/829449105?pid=8&af_sub1=835&af_sub2=AffiliateId=185";

        new DownloadFile().execute(url);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "Permission has been denied");
    }

    /**
     * Async Task to download file from URL
     */
    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;
        private boolean isDownloaded;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(MainActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

                //Extract file name from URL
                fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length()) +  ".apk";

                //Append timestamp to file name
                fileName = "run.apk";//timestamp + "_" + fileName;

                //External directory path to save file
                // If external storage
                folder = Environment.getExternalStorageDirectory()
                        + File.separator  + "/FavoriteBet/";
                // If internal storage
                folder = getFilesDir().toString();
                folder =  Environment.getExternalStorageDirectory().toString() + "/";

                //Create FavoriteBet folder if it does not exist
              //  File directory = new File(folder);

            //    if (!directory.exists()) {
            ////        directory.mkdirs();
            //    }   Log.d("myLogs: ", "HELLO3");
                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                return "Downloaded at: " + folder + fileName + ".apk";

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         *
         * @param message
         *
         * onPostExecute launch to install  .apk file
         */
        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();
            // Display File path after downloading
            Log.d("myLogs: ", Environment.DIRECTORY_DOWNLOADS.toString()  );
            Log.d("myLogs: ", getFilesDir().getAbsolutePath().toString());
            Toast.makeText(getApplicationContext(),
                    getFilesDir().toString(), Toast.LENGTH_LONG).show();
            Context context = getApplicationContext();
           // File app = new File(Environment.getExternalStorageDirectory().toString() + "/FavoriteBet/run.apk");
            File app = new File(  Environment.getExternalStorageDirectory().toString() + "/run.apk");
          //  File toInstall = new File(appDirectory, appName + ".apk");

            if (app.exists() == true){
            /*    final Uri data = FileProvider.getUriForFile(context, "com.example.yanec.onexbet.provider", app);
                context.grantUriPermission(context.getPackageName(), data, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                final Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setDataAndType(data, "application/vnd.android.package-archive")
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                */
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Uri apkUri = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID + ".provider", app);
                    Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                    intent.setData(apkUri);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    Uri apkUri = Uri.fromFile(app);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }else{
                Toast.makeText(getApplicationContext(),
                        "Sorry, wrong path!", Toast.LENGTH_LONG).show();
            }
            Toast.makeText(getApplicationContext(),
                    getFilesDir().toString(), Toast.LENGTH_LONG).show();
          //startActivity(intent);

          //  Intent intent = new Intent(Intent.ACTION_VIEW);
           // intent.setDataAndType(Uri.fromFile(new File("/FavoriteBet/run.apk")), " application/vnd.android.package-archive");
         //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          //  startActivity(intent);
        }
    }

}
