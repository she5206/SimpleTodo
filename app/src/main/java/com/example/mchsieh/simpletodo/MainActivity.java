package com.example.mchsieh.simpletodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {
    public ArrayList<String> items;
    public ArrayAdapter<String> arrayAdapter;
    public ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        items = new ArrayList<String>();
        readItems();
        setContentView(R.layout.activity_main);
        lvItems = (ListView) findViewById(R.id.lvItem);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        lvItems.setAdapter(arrayAdapter);

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                i.putExtra("item", items.get(position));
                i.putExtra("index", position);
                startActivityForResult(i, REQUEST_CODE);
            }
        });
        //items.add("first");
        //items.add("second");
        setupListViewListener();
    }

    private void readItems(){
        File filePath = getFilesDir();
        File todoFile = new File(filePath,"todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch(IOException e) {
            items = new ArrayList<String>();
        }
    }

    private void writeItems() {
        File filePath = getFilesDir();
        File todoFile = new File(filePath, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        items.remove(position);
                        arrayAdapter.notifyDataSetChanged();
                        writeItems();
                        return true;
                    }
                }

        );
    }
    private void setupListViewOnClickListener() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddBtnClicked(View v){
        EditText editText = (EditText) findViewById(R.id.editText);
        String newItem = editText.getText().toString();
        arrayAdapter.add(newItem);
        editText.setText("");
        writeItems();
    }
    private final int REQUEST_CODE = 20;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            String item = data.getExtras().getString("item");
            int index = data.getExtras().getInt("index", 0);
            items.set(index, item);
            arrayAdapter.notifyDataSetChanged();
            // Toast the name to display temporarily on screen
            Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
            writeItems();
        }
    }
}
