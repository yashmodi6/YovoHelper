package com.example.yovohelper.views.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.yovohelper.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CardView yt = findViewById(R.id.yt); // Replace with your actual CardView ID
        yt.setOnClickListener(v -> {
            // Intent to navigate to the next activity
            Intent intent = new Intent(MainActivity.this, YoutubeActivity.class);
            startActivity(intent);
        });
        CardView wp = findViewById(R.id.wp); // Replace with your actual CardView ID
        wp.setOnClickListener(v -> {
            // Intent to navigate to the next activity
            Intent intent = new Intent(MainActivity.this, WhatsAppActivity.class);
            startActivity(intent);
        });
        CardView ig = findViewById(R.id.ig); // Replace with your actual CardView ID
        ig.setOnClickListener(v -> {
            // Intent to navigate to the next activity
            Intent intent = new Intent(MainActivity.this, InstagramActivity.class);
            startActivity(intent);
        });
        CardView fb = findViewById(R.id.fb); // Replace with your actual CardView ID
        fb.setOnClickListener(v -> {
            // Intent to navigate to the next activity
            Intent intent = new Intent(MainActivity.this, FacebookActivity.class);
            startActivity(intent);
        });
    }
}