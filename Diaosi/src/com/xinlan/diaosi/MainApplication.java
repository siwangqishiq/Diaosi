package com.xinlan.diaosi;

import com.xinlan.imageloader.cache.disc.naming.Md5FileNameGenerator;
import com.xinlan.imageloader.core.ImageLoader;
import com.xinlan.imageloader.core.ImageLoaderConfiguration;
import com.xinlan.imageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class MainApplication extends Application {
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
	}

	public static void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}

}// end class
