package app.football.com.footballapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.helpshift.support.Support;

import app.football.com.footballapp.R;

public class ConversationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Support.showConversation(ConversationActivity.this);
    }
}
