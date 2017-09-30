package be.snipper.thrillcorder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import be.snipper.thrillcorder.AccSampler.XaccSampler;
import be.snipper.thrillcorder.AccSampler.YaccSampler;
import be.snipper.thrillcorder.AccSampler.ZaccSampler;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ThrillCorder--";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;
    private ArrayList<SamplerPlot> samplerPlots;
    private ArrayList<SamplerSaver> samplerSavers;
    private int samplingPeriod = 10;
    //private SensorSampler soundSampler;
    private SensorSampler xAccSampler;
    private SensorSampler yAccSampler;
    private SensorSampler zAccSampler;
    private boolean saveSamples = false;
    private File xCsvFile;
    private File yCsvFile;
    private File zCsvFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final long startTime = System.currentTimeMillis();


        samplerPlots = new ArrayList<>();
        xAccSampler = new XaccSampler(this);
        yAccSampler = new YaccSampler(this);
        zAccSampler = new ZaccSampler(this);
        //soundSampler = new SoundSampler(this);
        samplerPlots.add(new SamplerPlot(xAccSampler, (LineChart) findViewById(R.id.xAccChart), "X"));
        samplerPlots.add(new SamplerPlot(yAccSampler, (LineChart) findViewById(R.id.yAccChart), "Y"));
        samplerPlots.add(new SamplerPlot(zAccSampler, (LineChart) findViewById(R.id.zAccChart), "Z"));
        //samplerPlots.add(new SamplerPlot(soundSampler, (LineChart) findViewById(R.id.soundChart), "Sound"));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            final Button recordButton = (Button) findViewById(R.id.recButton);
            recordButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final View.OnClickListener previousOnClick = this;
                    recordButton.setText("Stop recording");
                    initializeFile();
                    if (xCsvFile != null && yCsvFile != null && zCsvFile != null) Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
                    else Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                    recordButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            saveSamples = false;
                            samplerSavers.clear();
                            recordButton.setText("Start recording");
                            recordButton.setOnClickListener(previousOnClick);
                            Toast.makeText(MainActivity.this, "Saved recording in " + xCsvFile.getParent(), Toast.LENGTH_SHORT).show();
                            MediaScannerHelper mediaScannerHelper = new MediaScannerHelper();
                            mediaScannerHelper.addFile(xCsvFile.getAbsolutePath(), MainActivity.this);
                            mediaScannerHelper.addFile(yCsvFile.getAbsolutePath(), MainActivity.this);
                            mediaScannerHelper.addFile(zCsvFile.getAbsolutePath(), MainActivity.this);
                        }
                    });
                }
            });
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                long now = System.currentTimeMillis();
                double time = ((double)(now - startTime))/1000.0;
                for (int i = 0; i < samplerPlots.size(); i++) {
                    samplerPlots.get(i).plot(time);
                    if (saveSamples) samplerSavers.get(i).save(time);
                }
                handler.postDelayed(this, samplingPeriod);
            }
        }, samplingPeriod);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public File getCsvStorageDir(String csvName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), csvName);
        if (!file.mkdirs()) {
            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    initializeFile();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void initializeFile() {
        samplerSavers = new ArrayList<>();
        File dir = getCsvStorageDir("ThrillCorder");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String currentDateandTime = sdf.format(new Date());
        String xFilename = "TC_" + currentDateandTime + "-X.csv";
        String yFilename = "TC_" + currentDateandTime + "-Y.csv";
        String zFilename = "TC_" + currentDateandTime + "-Z.csv";
        xCsvFile = getFile(xFilename, dir);
        yCsvFile = getFile(yFilename, dir);
        zCsvFile = getFile(zFilename, dir);
        if (xCsvFile != null && yCsvFile != null && zCsvFile != null) {
            samplerSavers.add(new SamplerSaver(xAccSampler, xCsvFile));
            samplerSavers.add(new SamplerSaver(yAccSampler, yCsvFile));
            samplerSavers.add(new SamplerSaver(zAccSampler, zCsvFile));
            //samplerSavers.add(new SamplerSaver(this, soundSampler, csvFile, "sound"));
            saveSamples = true;
        }
    }

    private File getFile(String filename, File directory) {
        File file = null;
        boolean isWritable = isExternalStorageWritable();
        if(isWritable){
            // Get the directory for the user's public pictures directory.
            if(directory.exists() || directory.mkdirs()) {
                file = new File(directory, filename);
            }
            try {
                if (file != null && !file.createNewFile()) {
//                    Log.e(TAG, "Directory not created");
                } else if(file != null) {
                    // File is created. Set the permissions so that files can be accessed from a PC.
                    file.setWritable(true, false);
                    file.setReadable(true, false);
                    MediaScannerHelper mediaScannerHelper = new MediaScannerHelper();
                    mediaScannerHelper.addFile(file.getAbsolutePath(), this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
