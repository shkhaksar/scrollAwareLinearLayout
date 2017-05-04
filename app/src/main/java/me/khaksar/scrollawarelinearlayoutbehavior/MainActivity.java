package me.khaksar.scrollawarelinearlayoutbehavior;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.linearlayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.coordinator_layout), "Hello World!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }
}
