package rubado.printeasy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubado.printeasy.Pojos.DocumentsPojo;
import rubado.printeasy.PrintEasyApplication;
import rubado.printeasy.R;
import rubado.printeasy.adapters.FileRowAdapter;

public class AlreadyPrintedActivity extends AppCompatActivity {

    private ListView mFileLists;
    private PrintEasyApplication mApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_printed);
        mApp = (PrintEasyApplication) getApplication();

        mFileLists = (ListView) findViewById(R.id.listView);

        getFiles();
    }

    public void getFiles() {
        Call<DocumentsPojo> userCall = mApp.getAPI().historyCall();

        userCall.enqueue(new Callback<DocumentsPojo>() {
            @Override
            public void onResponse(Call<DocumentsPojo> call, Response<DocumentsPojo> response) {
                if (response.code() >= 400 && response.code() <= 500) {
                    Log.e("Login", response.code() + " " + response.message());
                    AlreadyPrintedActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AlreadyPrintedActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();

                        }
                    });
                } else if (response.isSuccessful()) {
                    DocumentsPojo documentsPojo = response.body();
                    FileRowAdapter sessionsAdapter = new FileRowAdapter(AlreadyPrintedActivity.this, getApplication(), documentsPojo.getDocuments());
                    mFileLists.setAdapter(sessionsAdapter);


                } else {
                    Log.e("LoginActivity", "Mensaje: " + response.code());
                    AlreadyPrintedActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(AlreadyPrintedActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DocumentsPojo> call, Throwable t) {
                Log.e("Login Activity", t.getMessage());

            }

        });
    }
}
