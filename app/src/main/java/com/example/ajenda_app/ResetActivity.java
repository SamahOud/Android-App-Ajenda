package com.example.ajenda_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DatabaseHelper.DatabaseHelper;

public class ResetActivity extends AppCompatActivity {
    private TextView username;
    private EditText pass, repass;
    private Button confirm;
    // **************************************
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        username = (TextView) findViewById(R.id.username_resetText);
        // **************************************
        pass = (EditText) findViewById(R.id.password_reset);
        repass = (EditText) findViewById(R.id.repassword_reset);
        // **************************************
        confirm = (Button) findViewById(R.id.btnConfirm);
        // **************************************
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        username.setText(intent.getStringExtra("username"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
                TextView text = (TextView) layout.findViewById(R.id.text);
                CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
                final Toast toast = new Toast(getApplicationContext());

                String user = username.getText().toString();
                String password = pass.getText().toString();
                String repassword = repass.getText().toString();

                if (password.equals(repassword)) {
                    Boolean checkPasswordUpdate = databaseHelper.updatePassword(user, password);
                    if (checkPasswordUpdate == true) {
                        text.setText(R.string.password_updated_successfully);
                        lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_teal_500));
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);

                    } else {
                        text.setText(R.string.password_not_updated);
                        lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                        toast.setDuration(Toast.LENGTH_LONG);
                        toast.setView(layout);
                        toast.show();
                    }
                }
                else {
                    text.setText(R.string.password_not_matching);
                    lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });
    }
}