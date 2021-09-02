package com.example.abren

import android.R.attr
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_register_form1.*
import okhttp3.RequestBody


private const val MY_PERMISSIONS_REQUEST = 100
private const val PICK_IMAGE_FROM_GALLERY_REQUEST = 1

class RegisterForm1Fragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_form1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()
        // The request code used in ActivityCompat.requestPermissions()
// and returned in the Activity's onRequestPermissionsResult()
        val PERMISSION_ALL = 1
        val PERMISSIONS = arrayOf(
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.CAMERA
        )

        if (!hasPermissions(this, *PERMISSIONS)) {
            ActivityCompat.requestPermissions(context as Activity, PERMISSIONS, PERMISSION_ALL)
        }

//        if(context?.let
//                ContextCompat.checkSelfPermission(
//                    it,
//                    Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(
//                        context as Activity,
//                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE },
//                        MY_PERMISSIONS_REQUEST);
//
//                }

        val phoneNumber = view.findViewById<EditText>(R.id.phone_number)
        val emergencyPhoneNumber = view.findViewById<EditText>(R.id.emergency_phone_number)
        val profilePicture = view.findViewById<ImageView>(R.id.profile_pic_imageView)
        val idCardPicture = view.findViewById<TextView>(R.id.kebele_id_textView)
        val idCardBackPicture = view.findViewById<TextView>(R.id.kebele_id_back_textView)

        view.findViewById<Button>(R.id.back_button1).setOnClickListener{
            findNavController().navigate(R.id.action_RegisterForm1Fragment_to_RegisterFragment)
        }

        view.findViewById<Button>(R.id.continue_button1).setOnClickListener{
            viewModel.setPhoneNumber(phoneNumber.text.toString())
            viewModel.setEmergencyPhoneNumber(emergencyPhoneNumber.text.toString())
            viewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                if(user.role == "DRIVER"){
                    findNavController().navigate(R.id.action_RegisterForm1Fragment_to_RegisterForm2Fragment)
                }else{
                    findNavController().navigate(R.id.action_RegisterForm1Fragment_to_PreferenceFragment)
                }
            })
        }

        view.findViewById<ImageView>(R.id.profile_pic_imageView).setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY_REQUEST)

        }
        view.findViewById<TextView>(R.id.kebele_id_textView).setOnClickListener{
            selectImage()
        }
        view.findViewById<TextView>(R.id.kebele_id_back_textView).setOnClickListener{
            selectImage()
        }

    }

    fun hasPermissions(context: RegisterForm1Fragment, vararg permissions: String): Boolean = permissions.all {
        getContext()?.let { it1 -> ActivityCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
    }

    fun selectImage(){
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose your profile picture")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Take Photo") {
                val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(takePicture,0)
                //ActivityCompat.startActivityForResult(takePicture, 0)
            } else if (options[item] == "Choose from Gallery") {
                val pickPhoto =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(pickPhoto,1)
//                ActivityCompat.startActivityForResult(pickPhoto, 1)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val uri: Uri = data.data!!
            //uploadFile(uri)

            profile_pic_imageView.setImageURI(uri)
           // profile_pic_imageView.setImageBitmap(BitmapFactory.decodeFile(getPath(uri)))

        }
    }

    fun getPath(uri: Uri?): String? {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        val projection = arrayOf<String>(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context?.contentResolver?.query(uri,projection,null,null,null)
        if (cursor != null) {
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val moveToFirst = cursor.moveToFirst()
            val path = cursor.getString(column_index)
            cursor.close()
            return path
        }
        // this is our fallback here
        return uri.path
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
                    Log.d("RegisterForm1TAG", "User Granted image permission.")
                }
                else{
                    Log.d("RegisterForm1TAG", "User Denied image permission.")
                }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }




//    private fun uploadFile(fileUri: Uri) {
//        // create upload service client
//        val service: FileUploadService =
//            ServiceGenerator.createService(FileUploadService::class.java)
//
//        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
//        // use the FileUtils to get the actual file by uri
//        val file: File = FileUtils.getFile(RegisterForm1Fragment(), fileUri)
//
//        // create RequestBody instance from file
//        val requestFile: Request = create(
//            MediaType.parse(getContentResolver().getType(fileUri)),
//            file
//        )
//
//        // MultipartBody.Part is used to send also the actual file name
//        val body: Part = createFormData.createFormData("picture", file.getName(), requestFile)
//
//        // add another part within the multipart request
//        val descriptionString = "hello, this is description speaking"
//        val description = RequestBody.create(
//            MultipartBody.FORM, descriptionString
//        )
//
//        // finally, execute the request
//        val call: Call<ResponseBody> = service.upload(description, body)
//        call.enqueue(object : Callback<ResponseBody?>() {
//            fun onResponse(
//                call: Call<ResponseBody?>?,
//                response: Response<ResponseBody?>?
//            ) {
//                Log.v("Upload", "success")
//            }
//
//            fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
//                t.message?.let { Log.e("Upload error:", it) }
//            }
//        })
//    }


}