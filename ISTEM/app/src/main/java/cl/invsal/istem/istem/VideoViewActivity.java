package cl.invsal.istem.istem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by MarceloUsuario on 03-01-2017.
 */

public class VideoViewActivity extends Activity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;

    // Insert your Video URL
    static String VideoURL;
    static String vNumero;
    static String vEmpresa;
    static String vIdCategoria;
    static String vIdEmpresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the layout from video_main.xml
        setContentView(R.layout.videoview_main);
        // Find your VideoView in your video_main.xml layout
        videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask

        // Create a progressbar
        pDialog = new ProgressDialog(VideoViewActivity.this);
        // Set progressbar title
        pDialog.setTitle("Cargando video");
        // Set progressbar message
        pDialog.setMessage("Cargando...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(
                    VideoViewActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            pDialog.dismiss();
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });

    }
    public void fnAbrirNumero(View view){
        Salir();
    }
    private void Salir(){
        ActVisNumero.vNumero = vNumero;
        ActVisNumero.vEmpresa = vEmpresa;
        ActVisNumero.vIdEmpresa = vIdEmpresa;
        Intent i = new Intent(this, ActVisNumero.class);
        ActVisNumero.vIdCategoria = vIdCategoria;
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed (){
        Salir();
    }

}
