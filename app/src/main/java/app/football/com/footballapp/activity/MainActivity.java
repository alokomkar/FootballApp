package app.football.com.footballapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.helpshift.support.ApiConfig;
import com.helpshift.support.Support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import app.football.com.footballapp.R;
import app.football.com.footballapp.adapters.TeamRecyclerAdapter;
import app.football.com.footballapp.model.FootballTeam;
import app.football.com.footballapp.utils.ServiceGenerator;
import app.football.com.footballapp.utils.interfaces.GetScoresInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private ImageView downloadImageView;
    private RecyclerView teamsRecyclerview;
    private ArrayList<FootballTeam> footballTeams;
    private TeamRecyclerAdapter teamRecyclerAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadImageView = (ImageView) findViewById(R.id.downloadImageView);
        teamsRecyclerview = (RecyclerView) findViewById(R.id.teamsRecyclerview);
        downloadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadImageView.setVisibility(View.GONE);
                initInterface();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == R.id.action_chat ) {
            startChat();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startChat() {
        if( !isNetworkAvailable() ) {
            Toast.makeText(MainActivity.this, "Internet connection unavailable", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiConfig.Builder configBuilder = new ApiConfig.Builder();
        configBuilder.setEnableFullPrivacy(true);
        configBuilder.setEnableChat(true);
        configBuilder.setEnableContactUs(1);
        configBuilder.setRequireEmail(false);
        Support.showConversation(MainActivity.this, configBuilder.build());
    }

    private void displayProgressDialog() {
        if( progressDialog == null ) {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Downloading...");
        }
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if( progressDialog != null && progressDialog.isShowing() ) {
            progressDialog.dismiss();
        }
    }

    public boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            boolean connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return false;
    }

    private void initInterface() {

        if( !isNetworkAvailable() ) {
            Toast.makeText(MainActivity.this, "Internet connection unavailable", Toast.LENGTH_SHORT).show();
            return;
        }

        displayProgressDialog();
        GetScoresInterface getScoresInterface = ServiceGenerator.createService(GetScoresInterface.class);
        Call<JsonObject> call = getScoresInterface.getFootballScores("FootballScoresData.json");
        call.enqueue( new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if( response != null ) {
                    Log.d("FootballScores", response.body().toString());
                    parseFootballScores( response.body() );
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("FootballScores", "Failed : " + t.getMessage());
                t.printStackTrace();
                dismissProgressDialog();
                Toast.makeText(MainActivity.this, "Unable to fetch data", Toast.LENGTH_LONG ).show();
                downloadImageView.setVisibility(View.VISIBLE);
            }
        } );
    }

    private void parseFootballScores(JsonObject jsonObject) {
        footballTeams = new ArrayList<>();
        for( Map.Entry<String, JsonElement> map : jsonObject.entrySet() ) {
            FootballTeam footballTeam = FootballTeam.createObject( map );
            footballTeams.add(footballTeam);
        }
        //Sort teams as per total score and GD
        Collections.sort(footballTeams, new Comparator<FootballTeam>() {
            @Override
            public int compare(FootballTeam rhs, FootballTeam lhs) {
                int result = lhs.getTotalPoints() - rhs.getTotalPoints();
                if( result != 0 )
                    return result;
                else
                   result = lhs.getGD() - rhs.getGD();
                return result;
            }
        });

        setupAdapter();
    }

    private void setupAdapter() {
        if( teamRecyclerAdapter == null ) {
            teamRecyclerAdapter = new TeamRecyclerAdapter(footballTeams);
            teamsRecyclerview.setLayoutManager( new LinearLayoutManager(MainActivity.this));
            teamsRecyclerview.setAdapter(teamRecyclerAdapter);
            downloadImageView.setVisibility(View.GONE);
        }
        else {
            teamRecyclerAdapter.notifyDataSetChanged();
            downloadImageView.setVisibility(View.GONE);
        }
        if( footballTeams == null || footballTeams.size() == 0 ) {
            Toast.makeText(MainActivity.this, "Unable to fetch data", Toast.LENGTH_LONG ).show();
            downloadImageView.setVisibility(View.VISIBLE);
        }
        else {
            downloadImageView.setVisibility(View.GONE);
        }
        dismissProgressDialog();
        showSuccessDialog();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if( dialog != null && dialog.isShowing() )
                    dialog.dismiss();
            }
        }, 2000);
    }


}
