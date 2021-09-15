package com.example.abren

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_register_form1.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


private const val MY_PERMISSIONS_REQUEST = 100
private const val PICK_IMAGE_FROM_GALLERY_REQUEST1 = 1
private const val PICK_IMAGE_FROM_GALLERY_REQUEST2 = 2
private const val PICK_IMAGE_FROM_GALLERY_REQUEST3 = 3

class RegisterForm1Fragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()
    private var profileMultipartImage:MultipartBody.Part? = null
    private var idCardMultipartImage:MultipartBody.Part? = null
    private var idCardBackMultipartImage:MultipartBody.Part? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register_form1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

        val phoneNumber = view.findViewById<EditText>(R.id.phone_number)
        val emergencyPhoneNumber = view.findViewById<EditText>(R.id.emergency_phone_number)
        val profilePicture = view.findViewById<ImageView>(R.id.profile_pic_imageView)
        val idCardPicture = view.findViewById<TextView>(R.id.kebele_id_textView)
        val idCardBackPicture = view.findViewById<TextView>(R.id.kebele_id_back_textView)
        val pass = viewModel.setPasssword()

        //convert to requestBody
        val requestPhoneNumber:RequestBody = phoneNumber.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestemergPhoneno:RequestBody = emergencyPhoneNumber.text.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestPass:RequestBody = pass.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())


        view.findViewById<Button>(R.id.back_button1).setOnClickListener{
            findNavController().navigate(R.id.action_RegisterForm1Fragment_to_RegisterFragment)
        }

        // call registerUser network call when continue button clicked for rider
        view.findViewById<Button>(R.id.continue_button1).setOnClickListener{
            viewModel.setPhoneNumber(phoneNumber.text.toString())
            viewModel.setEmergencyPhoneNumber(emergencyPhoneNumber.text.toString())

            viewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                if(user.role == "DRIVER"){
                    findNavController().navigate(R.id.action_RegisterForm1Fragment_to_RegisterForm2Fragment)
                }else{
//                    Log.d("idCardMultiPart Image from user...",user.profilePictureUrl!!)
//                    val requestRole:RequestBody = user.role.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
//                    val x = viewModel.registerUser(profileMultipartImage!!,
//                        idCardMultipartImage!!,
//                        idCardBackMultipartImage!!,
//                        requestPhoneNumber,
//                        requestemergPhoneno,
//                        requestRole,
//                        requestPass)
//
//                    Log.d("Check user ",user!!.toString())
                   findNavController().navigate(R.id.action_RegisterForm1Fragment_to_PreferenceFragment)
                }
            })
        }

        //intent to select image from gallery
        profilePicture.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_FROM_GALLERY_REQUEST1)

        }
        idCardPicture.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST2)
        }
        idCardBackPicture.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST3)
        }

    }

    private fun hasPermissions(context: RegisterForm1Fragment, vararg permissions: String): Boolean = permissions.all {
        getContext()?.let { it1 -> ActivityCompat.checkSelfPermission(it1, it) } == PackageManager.PERMISSION_GRANTED
    }

    //get path from uri and file then convert it to multiPart and set the multipart image to viewModel
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST1){
                val uri: Uri = data.data!!
                profile_pic_imageView.setImageURI(uri)
                val filePath = getPathFromURI(requireContext(),uri)
                val chosenFile = File(filePath!!)
                val requestProfilePic:RequestBody = chosenFile.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                profileMultipartImage = MultipartBody.Part.createFormData("profilePicture",chosenFile.name, requestProfilePic)
//                val profileRequestBody = createRequestBody(chosenFile)
//                profileMultipartImage = createPart(chosenFile,profileRequestBody)
                viewModel.setProfilePicture(profileMultipartImage)
                Log.d("pathhhhh",profileMultipartImage.toString())
//                 profile_pic_imageView.setImageBitmap(BitmapFactory.decodeFile(getPathFromURI(requireContext(),uri)))
            }
            if(requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST2){
                val uri:Uri = data.data!!
                val filePath = getPathFromURI(requireContext(),uri)
                val chosenFile1 = File(filePath!!)
                val requstIdcard:RequestBody = chosenFile1.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                idCardMultipartImage = MultipartBody.Part.createFormData("idCardPicture",chosenFile1.name ,requstIdcard)
//                idCardMultipartImage = MultipartBody.Part.createFormData("file",chosenFile1.name,chosenFile1.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
//                val idCardRequestBody = createRequestBody(chosenFile1)
//                idCardMultipartImage = createPart(chosenFile1,idCardRequestBody)
                viewModel.setIdCardPicture(idCardMultipartImage)
                view?.findViewById<TextView>(R.id.kebele_id_textView)?.text = chosenFile1.nameWithoutExtension
                Log.d("id card Multipart image",idCardMultipartImage.toString())
            }
            if(requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST3){
                val uri:Uri = data.data!!
                val filePath = getPathFromURI(requireContext(),uri)
                val chosenFile2 = File(filePath!!)
                val requestIdCardBack:RequestBody = chosenFile2.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
                idCardBackMultipartImage = MultipartBody.Part.createFormData("idCardBackPicture",chosenFile2.name, requestIdCardBack)
//
//                idCardBackMultipartImage = MultipartBody.Part.createFormData("file",chosenFile2.name, chosenFile2.asRequestBody("multipart/form-data".toMediaTypeOrNull()))
//                val idCardbackRequestBody = createRequestBody(chosenFile2)
//                idCardBackMultipartImage = createPart(chosenFile2,idCardbackRequestBody)
                viewModel.setIdCardBackPicture(idCardBackMultipartImage)
                view?.findViewById<TextView>(R.id.kebele_id_back_textView)?.text = chosenFile2.nameWithoutExtension
                Log.d("id card back Multipart image",idCardBackMultipartImage.toString())
            }
        }
    }


    private fun createFile(realPath: String): File {
        return File(realPath)
    }

    private fun createPart(file: File, requestBody: RequestBody): MultipartBody.Part {
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
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
            Log.d("zeze get path error " , e.message!!)
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
                    Log.d("RegisterForm1TAG", "User Granted image permission.")
                }
                else{
                    Log.d("RegisterForm1TAG", "User Denied image permission.")
                }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

}