package com.fortysevendeg.mvessel.test.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.fortysevendeg.mvessel.test.R;
import com.fortysevendeg.mvessel.test.db.ContactsCursorLoader;
import com.fortysevendeg.mvessel.test.db.ContactsOpenHelper;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {

    ContactsOpenHelper contactsOpenHelper;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contactsOpenHelper = new ContactsOpenHelper(this);
        contactsOpenHelper.open();

        setContentView(R.layout.sample_main);

        listView = (ListView) findViewById(R.id.list_view);

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onDestroy() {
        contactsOpenHelper.close();
        contactsOpenHelper = null;
        super.onDestroy();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ContactsCursorLoader(this, contactsOpenHelper.getDatabase());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        ContactsAdapter adapter = new ContactsAdapter(this, data);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    static class ContactsAdapter extends CursorAdapter {

        private final int nameColumn;
        private final int ageColumn;

        public ContactsAdapter(Context context, Cursor c) {
            super(context, c, false);
            nameColumn = c.getColumnIndex(ContactsOpenHelper.C_NAME);
            ageColumn = c.getColumnIndex(ContactsOpenHelper.C_AGE);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(String.format("%s (%d)", cursor.getString(nameColumn), cursor.getInt(ageColumn)));
        }
    }
}
