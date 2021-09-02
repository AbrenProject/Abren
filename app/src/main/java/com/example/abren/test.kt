package com.example.abren

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

internal class test {
//    fun getPath(uri: Uri?): String? {
//        // just some safety built in
//        if (uri == null) {
//            // TODO perform some logging or show user feedback
//            return null
//        }
//        // try to retrieve the image from the media store first
//        // this will only work for images selected from gallery
//        val projection = arrayOf<String>(MediaStore.Images.Media.DATA)
//        val cursor: Cursor? = context.contentResolver?.query(uri,projection,null,null,null)
//        if (cursor != null) {
//            val column_index = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//            cursor.moveToFirst()
//            val path = cursor.getString(column_index)
//            cursor.close()
//            return path
//        }
//        // this is our fallback here
//        return uri.path
//    }
}