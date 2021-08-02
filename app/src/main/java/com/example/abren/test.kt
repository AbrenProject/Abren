package com.example.abren

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore


class test {
//    protected fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode != RESULT_CANCELED) {
//            when (requestCode) {
//                0 -> if (resultCode == RESULT_OK && data != null) {
//                    val selectedImage = data.extras!!["data"] as Bitmap?
//                    imageView.setImageBitmap(selectedImage)
//                }
//                1 -> if (resultCode == RESULT_OK && data != null) {
//                    val selectedImage: Uri? = data.data
//                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
//                    if (selectedImage != null) {
//                        val cursor: Cursor = getContentResolver().query(
//                            selectedImage,
//                            filePathColumn, null, null, null
//                        )
//                        if (cursor != null) {
//                            cursor.moveToFirst()
//                            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
//                            val picturePath: String = cursor.getString(columnIndex)
//                            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath))
//                            cursor.close()
//                        }
//                    }
//                }
//            }
//        }
//    }
}