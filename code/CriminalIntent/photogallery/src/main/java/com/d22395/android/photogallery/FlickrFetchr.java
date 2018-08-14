package com.d22395.android.photogallery;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FlickrFetchr {

    private static final String TAG = "FlickrFetchr";

//    private static final String API_KEY = "REPLACE_ME_WITH_A_REAL_KEY";
    private static final String FETCH_RECENTS_MRTHOD = "tag1";
    private static final String SEARCH_METHOD = "tag1";
    private static final Uri ENDPOINT = Uri.parse(
            "http://image.baidu.com/channel/listjson")
            .buildUpon()
            .appendQueryParameter("pn", "0")
            .appendQueryParameter("rn", "50")
            .appendQueryParameter("tag1", "美女")
            .appendQueryParameter("tag2", "全部")
            .appendQueryParameter("ie", "utf8")
            .build();

    /*
        从指定URL获取原始数据并返回一个字节流数组
     */
    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
    /*
        将getUrlBytes（String） 方法返回的结果转换为String
        创建一个线程来运行这个方法
     */
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchRecentPhotos() {
        String url = buildUrl(FETCH_RECENTS_MRTHOD, null);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query) {
        String url = buildUrl(SEARCH_METHOD, query);
        return downloadGalleryItems(url);
    }

//    public List<GalleryItem> fetchItems() {
    private List<GalleryItem> downloadGalleryItems(String url) {
        List<GalleryItem> items = new ArrayList<>();

        try {
//            String url = Uri.parse(
//                    "http://image.baidu.com/channel/listjson")
//                    .buildUpon()
//                    .appendQueryParameter("pn", "0")
//                    .appendQueryParameter("rn", "20")
//                    .appendQueryParameter("tag1", "美女")
//                    .appendQueryParameter("tag2", "全部")
//                    .appendQueryParameter("ie", "utf8")
//                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONObject jsonBody = new JSONObject(jsonString);
            parseItems(items, jsonBody);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            Log.e(TAG, "Failed to fetch items", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return items;
    }

    /*
        添加创建URL的辅助方法
     */
    private String buildUrl (String method, String query) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon()
                .appendQueryParameter("tag1", method);

        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("tag1", query);
        }
        return uriBuilder.build().toString();
    }

    // 解析Flickr图片
    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

//        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = jsonBody.getJSONArray("data");
        Log.d(TAG, "parseItems: " + photoJsonArray.length());
        for (int i = 0; i < photoJsonArray.length(); i++) {

            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);
            if (!photoJsonObject.has("id")) {
                continue;
            }
            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
//            item.setCaption(photoJsonObject.getString("image_uri"));
            item.setUrl(photoJsonObject.getString("image_url"));


//            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
    }

}

