package ir.nabzi.samplevideobrowser.ui.util;

import android.content.Context;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import ir.nabzi.samplevideobrowser.R;

public class CacheDataSourceFactory implements DataSource.Factory {
    private static CacheDataSourceFactory instance;
    private final Context context;
    private final DefaultDataSourceFactory defaultDatasourceFactory;
    private final long maxFileSize, maxCacheSize;

    public static CacheDataSourceFactory getInstance(Context context, long maxCacheSize, long maxFileSize){
        if (instance==null)
            instance  = new CacheDataSourceFactory(context,maxCacheSize,maxFileSize);
        return instance;
    }

    private CacheDataSourceFactory(Context context, long maxCacheSize, long maxFileSize) {
        super();
        this.context = context;
        this.maxCacheSize = maxCacheSize;
        this.maxFileSize = maxFileSize;
        String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        defaultDatasourceFactory = new DefaultDataSourceFactory(this.context,
                bandwidthMeter,
                new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter));
    }

    private static SimpleCache simpleCache;
    private static SimpleCache getSimpleCache(Context context, Long maxCacheSize){
        if (simpleCache==null) {
            LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
            simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), evictor);
        }
        return simpleCache;
    }
    @NotNull
    @Override
    public DataSource createDataSource() {
        simpleCache = getSimpleCache(context,maxCacheSize);
        return new CacheDataSource(simpleCache, defaultDatasourceFactory.createDataSource(),
                new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
    }
}