package com.xinlan.diaosi;

import com.xinlan.imageloader.core.DisplayImageOptions;
import com.xinlan.imageloader.core.ImageLoader;
import com.xinlan.imageloader.core.assist.FailReason;
import com.xinlan.imageloader.core.display.FadeInBitmapDisplayer;
import com.xinlan.imageloader.core.listener.ImageLoadingListener;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImageFragment extends Fragment {
	private String mImageUrl;
	private ProgressBar progress;
	private ImageView imageView;
	private View mMain;
	private DisplayImageOptions options;

	public static ImageFragment newInstance(String imagURL) {
		final ImageFragment f = new ImageFragment();
		f.mImageUrl = imagURL;
		return f;
	}

	public ImageFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		options = new DisplayImageOptions.Builder().displayer(
				new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMain = inflater
				.inflate(R.layout.fragment_image_item, container, false);
		progress = (ProgressBar) mMain.findViewById(R.id.loading);
		imageView = (ImageView) mMain.findViewById(R.id.image);
		return mMain;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ImageLoader.getInstance().displayImage(mImageUrl, imageView,options,
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						progress.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						progress.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}// end class
