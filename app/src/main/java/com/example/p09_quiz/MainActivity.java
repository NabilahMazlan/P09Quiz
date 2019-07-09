package com.example.p09_quiz;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {
    EditText et;
    Button btn, btnRead, btnCoordinates;
    TextView tv;
    String folderLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.buttonSave);
        btnRead = findViewById(R.id.buttonRead);
        btnCoordinates = findViewById(R.id.buttonCoordinates);
        et = findViewById(R.id.editTextLocation);
        tv = findViewById(R.id.textViewLocation);

        folderLocation = Environment.getExternalStorageDirectory().getAbsolutePath() + "/quiz";

        if(checkPremission() == true){
            File folder = new File(folderLocation);
            if(folder.exists() == false){
                boolean result = folder.mkdir();
                if (result == true){
                    Log.d("File Read/Write", "Folder created");
                    Toast.makeText(MainActivity.this, "Folder created", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Folder creation failed", Toast.LENGTH_SHORT).show();

                }
            }
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{
                    File file = new File(folderLocation, "quiz.txt");
                    FileWriter writer = new FileWriter(file, false);
                    writer.write( et.getText() + " \n");
                    writer.flush();
                    writer.close();

                    Toast.makeText(MainActivity.this, "Write Successful", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Failed to write", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File target = new File(folderLocation, "quiz.txt");
                if(target.exists() == true){
                    String data = "";
                    try{
                        FileReader reader = new FileReader(target);
                        BufferedReader br = new BufferedReader(reader);

                        String line = br.readLine();
                        while(line != null){
                            data += line + "\n";
                            line = br.readLine();

                        }

                        br.close();
                        reader.close();
                    }catch(Exception e){
                        Toast.makeText(MainActivity.this, "Failed to read!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    Log.d("Content", data);
                    tv.setText(data);
                }
            }
        });

        btnCoordinates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] splitString = tv.getText().toString().split(", ");
                Intent i = new Intent(MainActivity.this, SecondActivity.class);
                i.putExtra("lat", String.valueOf(splitString[0]));
                i.putExtra("lng", String.valueOf(splitString[1]));
                startActivity(i);


            }
        });
    }

    private boolean checkPremission(){
        int permissionCheck_WRITE = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionCheck_READ = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        if(permissionCheck_READ == PermissionChecker.PERMISSION_GRANTED && permissionCheck_WRITE == PermissionChecker.PERMISSION_GRANTED){
            return true;
        }else{
            return  false;
        }
    }
}
