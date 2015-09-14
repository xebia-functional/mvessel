package com.fortysevendeg.mvessel.sample.scala.ui

import android.app.Activity
import android.app.LoaderManager
import android.content.Context
import android.content.Loader
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ListView
import android.widget.TextView
import com.fortysevendeg.mvessel.sample.scala.{R, TR, TypedFindView}
import com.fortysevendeg.mvessel.sample.scala.db.{ContactsCursorLoader, ContactsOpenHelper}

class MainActivity
  extends Activity with LoaderManager.LoaderCallbacks[Cursor]
  with TypedFindView {

  private[this] var contactsOpenHelper: Option[ContactsOpenHelper] = None
  private[this] var listView: ListView = null

  protected override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    contactsOpenHelper = Some(new ContactsOpenHelper(this))
    setContentView(R.layout.sample_main)
    listView = findView(TR.list_view)
    getLoaderManager.restartLoader(0, null, this)
  }

  protected override def onDestroy() = {
    contactsOpenHelper foreach (_.close())
    contactsOpenHelper = None
    super.onDestroy()
  }

  def onCreateLoader(id: Int, args: Bundle): Loader[Cursor] = {
    contactsOpenHelper map { helper =>
      new ContactsCursorLoader(this, helper.database)
    } getOrElse (throw new IllegalStateException("Database not open"))
  }

  def onLoadFinished(loader: Loader[Cursor], data: Cursor) = {
    val adapter: ContactsAdapter = new ContactsAdapter(this, data)
    listView.setAdapter(adapter)
  }

  def onLoaderReset(loader: Loader[Cursor]) = {}
}

class ContactsAdapter(context: Context, c: Cursor)
  extends CursorAdapter(context, c, false) {

  val nameColumn: Int = c.getColumnIndex(ContactsOpenHelper.C_NAME)
  val ageColumn: Int = c.getColumnIndex(ContactsOpenHelper.C_AGE)

  def newView(context: Context, cursor: Cursor, parent: ViewGroup): View = {
    val inflater: LayoutInflater = parent.getContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater]
    inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
  }

  def bindView(view: View, context: Context, cursor: Cursor) {
    val textView: TextView = view.findViewById(android.R.id.text1).asInstanceOf[TextView]
    textView.setText(s"${cursor.getString(nameColumn)} (${cursor.getInt(ageColumn)})")
  }
}