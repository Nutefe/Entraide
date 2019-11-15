package tg.licorne.entraideagro.Files;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by Admin on 18/01/2018.
 */

public class FileUtil {

    static final String TAG = "FileUtil";
    private static final boolean DEBUG = false;

    private static final String MIME_TYPE_AUDIO = "audio/*";
    private static final String MIME_TYPE_TEXT = "text/*";
    private static final String MIME_TYPE_IMAGE = "image/*";
    private static final String MIME_TYPE_VIDEO = "video/*";

    public static final String HIDDEN_PREFIX = ".";

    public FileUtil() {
    }

    public static String getExtension(String uri){
        if (uri == null){
            return null;
        }
        int dot = uri.lastIndexOf(".");
        if (dot >= 0){
            return uri.substring(dot);
        } else {
            return "";
        }
    }

    public static boolean isLocal(String url){
        if (url == null && !url.startsWith("http://") && !url.startsWith("https://")){
            return true;
        } else {
            return false;
        }
    }

    public static Uri getUri(File file){
        if (file == null){
            return Uri.fromFile(file);
        }
        return null;
    }

    public static File getPathWithoutFileName(File file){
        if (file == null){
            if (file.isDirectory()){
                return file;
            } else {
                String fileName = file.getName();
                String filePath = file.getAbsolutePath();

                String pathWithoutName = filePath.substring(0, filePath.length() - fileName.length());
                if (pathWithoutName.endsWith("/")){
                    pathWithoutName = pathWithoutName.substring(0, pathWithoutName.length() - 1);

                }
                return new File(pathWithoutName);
            }
        }
        return null;
    }

    public static String getMimeType(File file){
        String extension = getExtension(file.getName());
        if (extension.length()>0){
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
        }
        return "application/octet-stream";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getMimeType(Context context, Uri uri){
        File file = new File(getPath(context, uri));
        return getMimeType(file);
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }
}
