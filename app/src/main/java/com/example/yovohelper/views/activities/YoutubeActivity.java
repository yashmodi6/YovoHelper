package com.example.yovohelper.views.activities;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.example.yovohelper.databinding.ActivityYoutubeBinding;
import com.faltenreich.skeletonlayout.Skeleton;
import com.faltenreich.skeletonlayout.SkeletonLayoutUtils;

import org.json.JSONObject;

public class YoutubeActivity extends AppCompatActivity {

    ActivityYoutubeBinding binding;
    Skeleton skeleton;
    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Chaquopy if it hasn't been started
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }

        binding = ActivityYoutubeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoUrl = binding.UrlET.getText().toString();
                if (videoUrl.isEmpty()) {
                    binding.urlLayout.setError("Please paste video URL!");
                    return;
                }

                binding.videoCardView.setVisibility(View.VISIBLE);
                skeleton = SkeletonLayoutUtils.createSkeleton(binding.linearLayout);
                skeleton.showSkeleton();

                new Thread(() -> {
                    try {
                        Python py = Python.getInstance();
                        PyObject result = py.getModule("main").callAttr("get_video_info", videoUrl);
                        String jsonString = result.toString().trim();

                        // Parse the JSON response
                        JSONObject videoInfo = new JSONObject(jsonString);
                        String title = videoInfo.optString("title", "N/A");
                        String uploader = videoInfo.optString("uploader", "N/A");
                        int duration = videoInfo.optInt("duration", 0);
                        int viewCount = videoInfo.optInt("view_count", 0);
                        int likeCount = videoInfo.optInt("like_count", 0);
                        String thumbnail = videoInfo.optString("thumbnail", "N/A");
//                        String uploadDate = videoInfo.optString("upload_date", "N/A");


                        // Log retrieved values
                        Log.d("YActivity", "Title: " + title);
                        Log.d("YActivity", "Uploader: " + uploader);
                        Log.d("YActivity", "Duration: " + duration);
                        Log.d("YActivity", "View Count: " + viewCount);
                        Log.d("YActivity", "Like Count: " + likeCount);
                        Log.d("YActivity", "Thumbnail: " + thumbnail);
//                      Log.d("YActivity", "Description: " + description);

                        // Update UI on the main thread
                        runOnUiThread(() -> {
                            binding.videoTitle.setText(title);
                            binding.videoUploader.setText(String.format("Uploaded By: %s", uploader));
                            binding.videoDuration.setText(String.format("Duration: %s seconds", duration));
                            binding.videoViewCount.setText(String.format("Views: %s", viewCount));
                            binding.videoLikeCount.setText(String.format("Likes: %s", likeCount));
//                            binding.videoDescription.setText(description);

                            // Load thumbnail
                            if (!thumbnail.isEmpty() && !thumbnail.equals("N/A")) {
                                Glide.with(getApplicationContext())
                                        .load(thumbnail)
                                        .transform(new CenterCrop(), new RoundedCorners(32))
                                        .into(binding.videoThumbnail);
                            }

                            skeleton.showOriginal();
                            binding.download.setVisibility(View.VISIBLE);
                        });
                    } catch (Exception e) {
                        Log.e("YActivity", "Exception occurred while processing video details", e);
                        runOnUiThread(() -> {
                            Toast.makeText(YoutubeActivity.this, "Error processing video details", Toast.LENGTH_SHORT).show();
                            skeleton.showOriginal();
                        });
                    }
                }).start();


            }
        });


        binding.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String videoUrl = binding.UrlET.getText().toString();
                new Thread(() -> {
                    Python py = Python.getInstance();
                    PyObject downloadUrl = py.getModule("main").callAttr("get_download_url", videoUrl);
                    Log.d("YActivity", "Download URL: " + downloadUrl);
                    downloadVideo(downloadUrl.toString(), "DownloadedVideo");

                    runOnUiThread(() -> {
                        Toast.makeText(YoutubeActivity.this, "Downloading video..." , Toast.LENGTH_SHORT).show();
                    });
                }).start();
            }
        });
    }

    private void downloadVideo(String videoUrl, String title) {
        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(videoUrl);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Downloading video" );
        request.setDescription("Downloading video from " + videoUrl);
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".mp4");

        downloadManager.enqueue(request);
    }
}
