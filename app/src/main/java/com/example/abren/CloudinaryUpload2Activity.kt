package com.example.abren

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.android.synthetic.main.activity_cloudinary_upload2.*
import java.io.File

const val PICK_IMAGE_FROM_GALLERY_REQUEST = 6

class CloudinaryUpload2Activity : AppCompatActivity() {

    var config: HashMap<String, String> = HashMap()
    var filePath:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cloudinary_upload2)
        process()

        config["cloud_name"] = "dwuzjlzpz"
        config["api_key"] = "536247193955636"
        config["api_secret"] = "4xZu-9hIQPvnj9pusqfK1UBVGd8"
        MediaManager.init(this, config)
    }

    private fun process() {
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.CAMERA)

        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL)

        val sendButton = findViewById<Button>(R.id.send_button)

        sendButton.setOnClickListener {
            uploadToCloudinary(filePath!!)
        }

        image1_imageView.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY_REQUEST)
        }
    }

//        private fun hasPermissions(context: CloudinaryUpload2Activity, vararg permissions: String): Boolean = permissions.all {
//            getContext()?.let { it1 -> ActivityCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
//        }

        @RequiresApi(Build.VERSION_CODES.Q)
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
                if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST){
                    val uri: Uri = data.data!!
                    image1_imageView.setImageURI(uri)
                    val filepath = getPathFromURI(this,uri)
                    val chosenFile = File(filepath!!)
                    filePath = filepath
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.Q)
        fun getPathFromURI(context: Context, uri: Uri): String? {
            val path: String = uri.path!!
            var realPath: String? = null

            val databaseUri: Uri
            val selection: String?
            val selectionArgs: Array<String>?
            if (path.contains("/document/image:")) { // files selected from "Documents"
                databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                selection = "_id=?"
                selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
            } else { // files selected from all other sources, especially on Samsung devices
                databaseUri = uri
                selection = null
                selectionArgs = null
            }
            try {
                val projection = arrayOf(
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.ORIENTATION,
                    MediaStore.Images.Media.DATE_TAKEN
                ) // some example data you can query
                val cursor = context.contentResolver.query(
                    databaseUri,
                    projection, selection, selectionArgs, null
                )
                if (cursor!!.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(projection[0])
                    realPath = cursor.getString(columnIndex)
                }
                cursor.close()
            } catch (e: Exception) {
                Log.d("get path error " , e.message!!)
            }
            return realPath
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            when(requestCode){
                MY_PERMISSIONS_REQUEST ->
                    if(grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        // permission is granted.  do file related tasks here
                        Log.d("Cloudinary ... ", "User Granted image permission.")
                    }
                    else{
                        Log.d("Cloudinary Upload", "User Denied image permission.")
                    }
                else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }

    private fun uploadToCloudinary(filepath: String) {

        MediaManager.get().upload(filepath).unsigned("vq84h8hh").callback(object : UploadCallback {
            override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                Toast.makeText(applicationContext, "Task successful", Toast.LENGTH_LONG).show()
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {

            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                Toast.makeText(applicationContext,
                    "Task Not successful$error",
                    Toast.LENGTH_LONG).show()
            }

            override fun onStart(requestId: String?) {

                Toast.makeText(applicationContext, "Start", Toast.LENGTH_SHORT).show()
            }
        }).dispatch()
    }
    }

