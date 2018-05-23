package com.example.ndh.reminder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddNoteActivity extends AppCompatActivity {
    private boolean isAlarm = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);

        LinearLayout layout = (LinearLayout) findViewById(R.id.addNoteLayout);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });

        Switch alarmBtn = (Switch) findViewById(R.id.alarm);
        alarmBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    isAlarm = true;
                else
                    isAlarm = false;
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onSaveButtonClick(View view) {
        adddNote();
        finish();
    }

    private void adddNote() {
        EditText title = (EditText) findViewById(R.id.editTextTitle);
        EditText content = (EditText) findViewById(R.id.editTextMemo);

        EditText date = (EditText) findViewById(R.id.editTextDate);
        EditText month = (EditText) findViewById(R.id.editTextMonth);
        EditText year = (EditText) findViewById(R.id.editTextYear);

        EditText hour = (EditText) findViewById(R.id.editTextHour);
        EditText min = (EditText) findViewById(R.id.editTextMin);


        String sTitle = title.getText().toString();
        String sContent = content.getText().toString();

        int iDate = Integer.parseInt(date.getText().toString());
        int iMon = Integer.parseInt(month.getText().toString()) - 1;
        int iYear = Integer.parseInt(year.getText().toString()) - 1900;

        int iHour = Integer.parseInt(hour.getText().toString());
        int iMin = Integer.parseInt(min.getText().toString());

        Date deadline = new Date(iYear, iMon, iDate, iHour, iMin, 0);


        Note note = new Note();
        note.setText(sTitle);
        note.setComment(sContent);
        note.setDate(deadline);

        if (isAlarm)
            note.setType(NoteType.ALARM);
        else
            note.setType(NoteType.TEXT);

        MainActivity.getNoteDao().insert(note);
        Log.d("DaoExample", "Inserted new note, ID: " + note.getId() + note.getDate() + "  " + note.getType());

        if (isAlarm){
            Alarm temp = new Alarm();
            temp.create(this, note.getDate(), note.getText());
            MainActivity.schedule.put(note.getId(), temp);
        }


        MainActivity.updateNotes();
    }
}