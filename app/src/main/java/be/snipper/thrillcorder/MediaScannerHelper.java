package be.snipper.thrillcorder;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

public class MediaScannerHelper implements MediaScannerConnectionClient {

    public void addFile(String filename, Context context)
    {
        String [] paths = new String[1];
        paths[0] = filename;
        MediaScannerConnection.scanFile(context, paths, null, this);
    }

    public void onMediaScannerConnected() {
    }

    @Override
    public void onScanCompleted(String s, Uri uri) {

    }
}