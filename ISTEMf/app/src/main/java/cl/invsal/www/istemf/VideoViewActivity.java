package cl.invsal.www.istemf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by MarceloUsuario on 03-05-2017.
 */

public class VideoViewActivity extends AppCompatActivity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;

    // Insert your Video URL
    static String VideoURL;
    static String vNumero;
    static String vEmpresa;
    static String vIdCategoria;
    static String vCategoria;
    static String vNumSolo;
    static String vCiclo;
    static String vLetra;
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

        Intent i = new Intent();
        i.putExtra("Numero",vNumero);
        i.putExtra("Empresa",vEmpresa);
        i.putExtra("IdEmpresa",vIdEmpresa);
        i.putExtra("IdCategoria",vIdCategoria);
        i.putExtra("Categoria",vCategoria);
        i.putExtra("Ciclo",vCiclo);
        i.putExtra("Letra",vLetra);
        i.putExtra("NumSolo",vNumSolo);

        setResult(RESULT_OK, i);
        finish();


        finish();
    }
    @Override
    public void onBackPressed (){
        Salir();
    }

}
