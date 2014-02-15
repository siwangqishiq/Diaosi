package com.xinlan.diaosi;

import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.Inflater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {
	public static final String URL = "http://we.99btgongchang.org/00/0402.html";

	private ProgressBar loadView;// 进度条
	private ListView listView;// 列表栏
	private GetDataTask mTask;
	private LinkedList<Item> data;
	protected ListAdapter mListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();
	}

	private void init() {
		loadView = (ProgressBar) findViewById(R.id.loading);
		listView = (ListView) findViewById(R.id.list);
		data = new LinkedList<Item>();
		mListAdapter = new ListAdapter();
		listView.setAdapter(mListAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent it = new Intent(MainActivity.this, DetailActivity.class);
				System.out.println("--->"+data.get(position).getContent());
				it.putExtra("url", data.get(position).getUrl());
				MainActivity.this.startActivity(it);
			}
		});

		if (mTask != null) {
			mTask.cancel(true);
		}
		mTask = new GetDataTask();
		mTask.execute(1);
	}

	/**
	 * 获取列表数据异步操作
	 * 
	 * @author Administrator
	 * 
	 */
	private final class GetDataTask extends
			AsyncTask<Integer, Void, LinkedList<Item>> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.INVISIBLE);
		}

		@Override
		protected LinkedList<Item> doInBackground(Integer... params) {
			try {
				Document doc = Jsoup.connect(URL).get();
				Elements links = doc.select("a[href]");
				LinkedList<Item> retData = new LinkedList<Item>();

				for (Element link : links) {
					String href = link.attr("abs:href");
					String content = link.text();
					if (href.startsWith("http://we.99btgongchang.org/p2p")
							&& (content.contains("日本") || content
									.contains("美女"))) {
						Item addItem = new Item();
						addItem.setUrl(href);
						addItem.setContent(content);
						retData.add(addItem);
					}
				}// end each for

				return retData;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(LinkedList<Item> result) {
			super.onPostExecute(result);
			if (result == null) {
				loadView.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				return;
			}

			data.addAll(result);
			mListAdapter.notifyDataSetChanged();
			loadView.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
		}
	}// end inner class

	private final class ListAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private ViewHolder holder;

		public ListAdapter() {
			inflater = LayoutInflater.from(MainActivity.this);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return (long) position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.item, null);
				holder.textView = (TextView) convertView
						.findViewById(R.id.content);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			Item item = data.get(position);
			holder.textView.setText(item.getContent());
			return convertView;
		}
	}

	static class ViewHolder {
		TextView textView;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTask != null) {
			mTask.cancel(true);
		}
	}
}// end class
