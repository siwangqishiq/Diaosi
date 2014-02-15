package com.xinlan.diaosi;

import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.Inflater;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DetailActivity extends FragmentActivity {
	private ProgressBar loadView;// 进度条
	private ViewPager gallery;//
	private LinkedList<String> data;
	private GalleryAdapter galleryAdapter;
	private String requestURL;
	private ImageListTask imageListTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		init();
	}

	private void init() {
		requestURL = getIntent().getStringExtra("url");
		loadView = (ProgressBar) findViewById(R.id.loading);
		gallery = (ViewPager) findViewById(R.id.gallery);
		data = new LinkedList<String>();
		galleryAdapter = new GalleryAdapter(getSupportFragmentManager());
		gallery.setAdapter(galleryAdapter);
		galleryAdapter.notifyDataSetChanged();
		
		if (imageListTask != null) {
			imageListTask.cancel(true);
		}
		imageListTask= new ImageListTask();
		imageListTask.execute(requestURL);
	}

	private final class GalleryAdapter extends FragmentStatePagerAdapter {
		public GalleryAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {
			return ImageFragment.newInstance(data.get(index));
		}

		@Override
		public int getCount() {
			return data.size();
		}
	}// end inner class

	/**
	 * 获取列表数据异步操作
	 * 
	 * @author Administrator
	 * 
	 */
	private final class ImageListTask extends
			AsyncTask<String, Void, LinkedList<String>> {
		@Override
		protected void onPreExecute() {
			loadView.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected LinkedList<String> doInBackground(String... params) {
			try {
				LinkedList<String> ret = new LinkedList<String>();
				Document doc = Jsoup.connect(params[0]).get();
				Elements media = doc.select("[src]");
				for (Element src : media) {
					if (src.tagName().equals("img")) {
						String picURL = src.attr("abs:src");
						if(picURL.contains("jpg")||picURL.contains("jpeg")||picURL.contains("png")){
							ret.add(picURL);
						}//end if
					}
				}// end for

				return ret;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(LinkedList<String> result) {
			super.onPostExecute(result);
			loadView.setVisibility(View.GONE);
			if (result == null) {
				return;
			}
			data.addAll(result);
			galleryAdapter.notifyDataSetChanged();
		}
	}// end inner classd
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (imageListTask != null) {
			imageListTask.cancel(true);
		}
	}
}// end class
