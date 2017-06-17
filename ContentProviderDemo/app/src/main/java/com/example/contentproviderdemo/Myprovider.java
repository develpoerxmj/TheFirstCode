package com.example.contentproviderdemo;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

/**
 * Created by sz132 on 2017/6/12.
 */

public class Myprovider extends ContentProvider {

    public static final int STUDENT_DIR = 1;
    public static final int STUDENT_ITEM = 2;

    public static final String AUTHORITY = "com.example.contentproviderdemo";

    private SQLiteDatabase liteDatabase;

    private static UriMatcher matcher;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "Student", STUDENT_DIR);
        matcher.addURI(AUTHORITY, "Student/#", STUDENT_ITEM);
    }

    @Override
    public boolean onCreate() {
        liteDatabase = LitePal.getDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        switch (matcher.match(uri)){
            case STUDENT_DIR:
                return liteDatabase.query("Student", projection, selection, selectionArgs, null, null, sortOrder);
            case STUDENT_ITEM:
                String id = uri.getPathSegments().get(1);
                return liteDatabase.query("Student", projection, "id = ?", new String[]{id}, null, null, sortOrder);
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (matcher.match(uri)){
            case STUDENT_DIR:
                return "vnd.android.cursor.dir/vnd."+AUTHORITY+".Student";
            case STUDENT_ITEM:
                return "vnd.android.cursor.item/vnd."+AUTHORITY+".Student";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (matcher.match(uri)){
            case STUDENT_DIR:
            case STUDENT_ITEM:
                long id = liteDatabase.insert("Student", null, values);
                return Uri.parse("content://"+AUTHORITY+"/Student/"+id);
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (matcher.match(uri)){
            case STUDENT_DIR:
                return liteDatabase.delete("Student", selection, selectionArgs);
            case STUDENT_ITEM:
                String id = uri.getPathSegments().get(1);
                return liteDatabase.delete("Student", "id = ?", new String[]{id});
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (matcher.match(uri)){
            case STUDENT_DIR:
                return liteDatabase.update("Student", values, selection, selectionArgs);
            case STUDENT_ITEM:
                String id = uri.getPathSegments().get(1);
                return liteDatabase.update("Student", values, "id = ?", new String[]{id});
        }
        return 0;
    }
}
