package com.example.ndh.reminder;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import org.greenrobot.greendao.query.Query;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static NoteDao noteDao;
    private static Query<Note> notesQuery;
    private static NotesAdapter notesAdapter;
    public static Map<Long, Alarm> schedule = new Map<Long, Alarm>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(Object key) {
            return false;
        }

        @Override
        public boolean containsValue(Object value) {
            return false;
        }

        @Override
        public Alarm get(Object key) {
            return null;
        }

        @Override
        public Alarm put(Long key, Alarm value) {
            return null;
        }

        @Override
        public Alarm remove(Object key) {
            return null;
        }

        @Override
        public void putAll(@NonNull Map<? extends Long, ? extends Alarm> m) {

        }

        @Override
        public void clear() {

        }

        @NonNull
        @Override
        public Set<Long> keySet() {
            return null;
        }

        @NonNull
        @Override
        public Collection<Alarm> values() {
            return null;
        }

        @NonNull
        @Override
        public Set<Entry<Long, Alarm>> entrySet() {
            return null;
        }
    };

    public static NoteDao getNoteDao(){
        return noteDao;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        setUpViews();

        // get the note DAO
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        noteDao = daoSession.getNoteDao();

        // query all notes, sorted by date
        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Date).build();
        updateNotes();
    }

    public static void updateNotes() {
        List<Note> notes = notesQuery.list();
        notesAdapter.setNotes(notes);
    }

    protected void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewNotes);
        //noinspection ConstantConditions
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        notesAdapter = new NotesAdapter(noteClickListener);
        recyclerView.setAdapter(notesAdapter);
    }

    NotesAdapter.NoteClickListener noteClickListener = new NotesAdapter.NoteClickListener() {
        @Override
        public void onNoteClick(int position) {
            Note note = notesAdapter.getNote(position);
            Long noteId = note.getId();

            noteDao.deleteByKey(noteId);
            Log.d("DaoExample", "Deleted note, ID: " + noteId);

            Alarm a = schedule.get(noteId);
            if (a != null) {
                a.cancel();
                schedule.remove(noteId);
                Log.d("alarm", "removed");
            }

            updateNotes();
        }
    };

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}