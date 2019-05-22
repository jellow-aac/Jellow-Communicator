package com.dsource.idc.jellowintl;

import android.app.Activity;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public final  class ThumbnailListener implements
        YouTubeThumbnailView.OnInitializedListener,
        YouTubeThumbnailLoader.OnThumbnailLoadedListener {
    public static final String JELLOW_INFO_VIDEO_ID = "5LXDcPBYCyA";
    public static final String VISUAL_ACCESS_VIDEO_ID = "QDU1Qp-u2Zs";
    public static final String SWITCH_ACCESS_VIDEO_ID = "OJiOC0Wkvlk";
    private Activity mAct;

    public ThumbnailListener(Activity activity){
        mAct = activity;
    }

    @Override
    public void onInitializationSuccess(
            YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        loader.setOnThumbnailLoadedListener(this);
        if(view.equals(mAct.findViewById(R.id.thumbnailVisualAccess)))
            loader.setVideo(VISUAL_ACCESS_VIDEO_ID);
        else if (view.equals(mAct.findViewById(R.id.thumbnailSwitchAccess)))
            loader.setVideo(SWITCH_ACCESS_VIDEO_ID);
        else
            loader.setVideo(JELLOW_INFO_VIDEO_ID);
    }

    @Override
    public void onInitializationFailure(
            YouTubeThumbnailView view, YouTubeInitializationResult loader) {
        view.setImageResource(R.drawable.no_thumbnail);
    }

    @Override
    public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
    }

    @Override
    public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
        view.setImageResource(R.drawable.no_thumbnail);
    }
}
