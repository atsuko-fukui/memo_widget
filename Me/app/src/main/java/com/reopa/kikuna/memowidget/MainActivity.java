package com.reopa.kikuna.memowidget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.reopa.kikuna.memowidget.dao.MemosDao;

public class MainActivity extends AppCompatActivity {

    MemosListFragment mMemosListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // init MainActivity (background image etc.).
        initActivity();

        // Create MemoListFragment Object.
        mMemosListFragment = (MemosListFragment) getFragmentManager().findFragmentById(R.id.memos_list_fragment);

        // Create and set listener FAB.
        FloatingActionButton floatingActionButton =
                (FloatingActionButton) findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewAddDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        Const.ActTapWidget localAction = (Const.ActTapWidget) intent.getSerializableExtra(Const.ACT_TAP_WIDGET);

        if (localAction != null) {
            switch (localAction) {
                case KIND_ADD:
                    viewAddDialog();
                    break;
                case KIND_NORMAL:
                default:
                    break;
            }
        }
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

    /**
     * Initialize MainActivity background Image.
     */
    private void initActivity() {
        setContentView(R.layout.activity_main);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.main_activity);
        layout.setBackgroundResource(R.drawable.bg_cork);
    }

    private void viewAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final EditText editText = new EditText(MainActivity.this);
        editText.setHint(R.string.new_memo_hint);
        builder.setTitle(R.string.new_memo);
        builder.setView(editText);
        builder.setPositiveButton(R.string.save,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MemosDao dao = new MemosDao(MainActivity.this);
                        dao.insert(editText.getText().toString());
                        mMemosListFragment.updateList();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();

    }
}
