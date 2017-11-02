package rubado.printeasy.activities;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubado.printeasy.Pojos.DocumentsPojo;
import rubado.printeasy.PrintEasyApplication;
import rubado.printeasy.R;
import rubado.printeasy.adapters.FileRowAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mFileLists;
    private PrintEasyApplication mApp;
    private FileRowAdapter sessionsAdapter;
    private ProgressBar mProgressBar;
    private CoordinatorLayout mContainerMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApp = (PrintEasyApplication) getApplication();

        mProgressBar =(ProgressBar) findViewById(R.id.progress_main);
        mContainerMain = (CoordinatorLayout) findViewById(R.id.containerMain);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton addFile = (FloatingActionButton) findViewById(R.id.addNewFile);
        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE};
                if (!hasPermissions(MainActivity.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions( MainActivity.this, PERMISSIONS, 0);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(Intent.createChooser(intent, "Open folder"), 0);

                }

            }
        });

        mFileLists = (ListView) findViewById(R.id.listViewMain);

        getFiles();

        mFileLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, "Open folder"), 0);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void getFiles() {
        Call<DocumentsPojo> documentsCall = mApp.getAPI().documentsCall();

        documentsCall.enqueue(new Callback<DocumentsPojo>() {
            @Override
            public void onResponse(Call<DocumentsPojo> call, Response<DocumentsPojo> response) {
                hideProgressBar();
                if (response.code() >= 400 && response.code() <= 500) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();

                        }
                    });
                } else if (response.isSuccessful()) {
                    DocumentsPojo documentsPojo = response.body();
                    sessionsAdapter = new FileRowAdapter(MainActivity.this, getApplication(), documentsPojo.getDocuments());
                    mFileLists.setAdapter(sessionsAdapter);


                } else {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<DocumentsPojo> call, Throwable t) {
                hideProgressBar();
                Log.e("MainActivity", t.getMessage());

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    showProgressBar();
                    Uri uri = data.getData();
                    // Get the path
                    String path = getPathFromURI(MainActivity.this,uri);

                    File fileToUpload = new File(path);
                    sendFile(fileToUpload, uri);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mContainerMain.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
        mContainerMain.setVisibility(View.VISIBLE);
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathFromURI(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private void sendFile(File fileToUpload, Uri uri) {

        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(uri)),
                        fileToUpload
                );

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", fileToUpload.getName(), requestFile);

        Call<Void> userCall = mApp.getAPI().upload(body);

        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                hideProgressBar();
                if (response.code() >= 400 && response.code() <= 500) {
                    Log.e("Login", response.code() + " " + response.message());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();

                        }
                    });
                } else if (response.isSuccessful()) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mApp, "Archivo subido exitosamente", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    sessionsAdapter.notifyDataSetChanged();

                } else {
                    Log.e("LoginActivity", "Mensaje: " + response.code());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                hideProgressBar();
                Log.e("Login Activity", t.getMessage());

            }

        });
    }

    public void deleteDocument(String id) {
        showProgressBar();
        Log.e("Document id ", id);
        Call<Void> deleteFileCall = mApp.getAPI().deleteFileCall(id);

        deleteFileCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                hideProgressBar();
                if (response.code() >= 400 && response.code() <= 500) {
                    Log.e("MainActivity", response.code() + " " + response.message());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.delete_file_error), Toast.LENGTH_LONG).show();

                        }
                    });
                } else if (response.isSuccessful()) {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(mApp, "Archivo eliminado exitosamente", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    sessionsAdapter.notifyDataSetChanged();

                } else {
                    Log.e("MainActivity", "Mensaje: " + response.code());
                    MainActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.delete_file_error), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                hideProgressBar();
                Log.e("MainActivity", t.getMessage());

            }

        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent i;
        if (id == R.id.nav_printed) {
            i = new Intent(MainActivity.this, AlreadyPrintedActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_payments) {
            i = new Intent(MainActivity.this, Payments.class);
            startActivity(i);
        } else if (id == R.id.nav_close_session) {
            i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
