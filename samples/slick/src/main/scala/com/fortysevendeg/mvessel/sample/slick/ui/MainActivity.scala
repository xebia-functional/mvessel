package com.fortysevendeg.mvessel.sample.slick.ui

import java.util.concurrent.Executor

import android.app.Activity
import android.content.Context
import android.os.{Looper, Handler, AsyncTask, Bundle}
import android.support.v7.widget.{LinearLayoutManager, RecyclerView}
import android.view._
import android.widget.TextView
import com.fortysevendeg.mvessel.sample.slick.db.SlickDatabaseHelper
import com.fortysevendeg.mvessel.sample.slick.repository.{Value, Sample, Repository}
import com.fortysevendeg.mvessel.sample.slick.{R, TR, TypedFindView}

import scala.concurrent.{Future, ExecutionContext}

import scala.util.Random

class MainActivity
  extends Activity
  with TypedFindView {

  implicit lazy val Pool = ExecutionContext.fromExecutor(AsyncTask.THREAD_POOL_EXECUTOR)

  // UI thread executor
  lazy val Ui = ExecutionContext.fromExecutor(new Executor {
    private val handler = new Handler(Looper.getMainLooper)

    override def execute(command: Runnable) = handler.post(command)
  })

  private[this] lazy val repository = new Repository(new SlickDatabaseHelper(this))

  private[this] lazy val recyclerView: RecyclerView = findView(TR.recycler_view)

  private[this] val adapter: SampleAdapter = new SampleAdapter()

  protected override def onCreate(savedInstanceState: Bundle) = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.sample_main)
    recyclerView.setHasFixedSize(true)
    recyclerView.setLayoutManager(new LinearLayoutManager(this))
    recyclerView.setAdapter(adapter)
    loadList() map (addItems(_)(Ui))
  }

  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater.inflate(R.menu.sample_main, menu)
    super.onCreateOptionsMenu(menu)
  }

  override def onOptionsItemSelected(item: MenuItem): Boolean = item.getItemId match {
    case R.id.add =>
      createItem() map (item => addItems(Seq(item))(Ui))
      true
    case _ =>
      super.onOptionsItemSelected(item)
  }

  private[this] def createItem()(implicit context: ExecutionContext) = {
    val random = Random.nextInt(100)
    repository.insertSample(Sample(
      title = s"Title $random",
      description = s"Description $random",
      map = Map("key1" -> "val1", "key2" -> "val2"),
      value = Value(value = "Value")))
  }

  private[this] def loadList()(implicit context: ExecutionContext) = repository.fetchAllSample()

  private[this] def addItems(sampleSeq: Seq[Sample])(implicit context: ExecutionContext) =
    Future(adapter.addItems(sampleSeq))

}

class SampleViewHolder(view: View) extends RecyclerView.ViewHolder(view) {

  private[this] val title = view.findViewById(android.R.id.text1).asInstanceOf[TextView]

  private[this] val description = view.findViewById(android.R.id.text2).asInstanceOf[TextView]

  def bind(sample: Sample): Unit = {
    title.setText(sample.title)
    description.setText(sample.description)
  }

}

class SampleAdapter(var sampleSeq: Seq[Sample] = Seq.empty) extends RecyclerView.Adapter[SampleViewHolder] {

  override def getItemCount: Int = sampleSeq.size

  override def onCreateViewHolder(parent: ViewGroup, position: Int): SampleViewHolder =
    parent.getContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) match {
      case inflater: LayoutInflater =>
        val v = inflater.inflate(android.R.layout.simple_list_item_2, parent, false)
        new SampleViewHolder(v)
      case _ =>
        throw new IllegalStateException("Can't get a layout reference")
    }

  override def onBindViewHolder(vh: SampleViewHolder, position: Int): Unit = vh.bind(sampleSeq(position))

  def addItems(seq: Seq[Sample]) = {
    sampleSeq = sampleSeq ++ seq
    val count = seq.size
    notifyItemRangeInserted(sampleSeq.size - count, count)
  }

}