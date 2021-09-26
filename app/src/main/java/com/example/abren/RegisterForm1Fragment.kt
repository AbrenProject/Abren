package com.example.abren

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_register_form1.*
import java.io.File
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.abren.models.VehicleInformation


const val MY_PERMISSIONS_REQUEST = 100
private const val PICK_IMAGE_FROM_GALLERY_REQUEST1 = 1
private const val PICK_IMAGE_FROM_GALLERY_REQUEST2 = 2
private const val PICK_IMAGE_FROM_GALLERY_REQUEST3 = 3

class RegisterForm1Fragment : Fragment() {

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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


        view.findViewById<Button>(R.id.driverPrevButton).setOnClickListener {
            findNavController().navigate(R.id.action_RegisterForm1Fragment_to_RegisterFragment)
        }

        // call registerUser network call when continue button clicked for rider
        view.findViewById<Button>(R.id.driverNextButton).setOnClickListener {
            Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
            viewModel.setPhoneNumber("251" + phoneNumber.text.toString())
            viewModel.setEmergencyPhoneNumber("251" + emergencyPhoneNumber.text.toString())
            viewModel.setPassword("1234")

            //TODO: Set the images urls

            viewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                if(user.profilePictureUrl == null || user.idCardUrl == null || user.idCardBackUrl == null || user.phoneNumber == null || user.emergencyPhoneNumber == null){
                    Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show()
                }else if(user.phoneNumber!!.length != 12){
                    Toast.makeText(requireContext(), "Invalid Input for Phone Number.", Toast.LENGTH_SHORT).show()
                }else if(user.emergencyPhoneNumber!!.length != 12){
                    Toast.makeText(requireContext(), "Invalid Input for Emergency Phone Number.", Toast.LENGTH_SHORT).show()
                }else {
                    val profileUpload =  uploadToCloudinary(user.profilePictureUrl!!)
                    val idUpload =  uploadToCloudinary(user.idCardUrl!!)
                    val idBackUpload =  uploadToCloudinary(user.idCardBackUrl!!)
                    profileUpload.observeForever(Observer { result ->
                        if(result != null){
                            Log.d("Uploading Profile Picture", result)
                            user.profilePictureUrl = result

                            idUpload.observeForever(Observer { result2 ->
                                if(result2 != null){
                                    Log.d("Uploading Id", result2)
                                    user.idCardUrl = result2

                                    idBackUpload.observeForever(Observer { result3 ->
                                        if(result3 != null){
                                            Log.d("Uploading Id Back", result3)
                                            user.idCardBackUrl = result3

                                            if (user.role == "DRIVER") {
                                                user.vehicleInformation = VehicleInformation()
                                                findNavController().navigate(R.id.action_RegisterForm1Fragment_to_RegisterForm2Fragment)
                                            } else {
                                                findNavController().navigate(R.id.action_RegisterForm1Fragment_to_PreferenceFragment)
                                            }

                                        }else {
                                            Log.d("Uploading Profile Picture", "Problem")
                                        }
                                    })

                                }else {
                                    Log.d("Uploading Id", "Problem")
                                }
                            })

                        }else {
                            Log.d("Uploading Id Back", "Problem")
                        }
                    })
                }
            })
        }

        //intent to select image from gallery
        profilePicture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_FROM_GALLERY_REQUEST1
            )

        }

        idCardPicture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST2)
        }

        idCardBackPicture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST3)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST1){
                val uri: Uri = data.data!!
                profile_pic_imageView.setImageURI(uri)
                val filePath = getPathFromURI(requireContext(), uri)
                viewModel.setProfilePictureUrl(filePath)
            }

            if(requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST2){
                val uri:Uri = data.data!!
                val filePath = getPathFromURI(requireContext(),uri)
                val chosenFile1 = File(filePath)
                viewModel.setIdCardUrl(filePath)
                view?.findViewById<TextView>(R.id.kebele_id_textView)?.text = chosenFile1.nameWithoutExtension
            }

            if(requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST3){
                val uri:Uri = data.data!!
                val filePath = getPathFromURI(requireContext(),uri)
                val chosenFile2 = File(filePath)
                viewModel.setIdCardBackUrl(filePath)
                view?.findViewById<TextView>(R.id.kebele_id_back_textView)?.text = chosenFile2.nameWithoutExtension
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun getPathFromURI(context: Context, uri: Uri): String {
        val path: String = uri.path!!
        var realPath = ""

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
            Log.d("zeze get path error ", e.message!!)
        }
        return realPath
    }

    private fun hasPermissions(
        context: RegisterForm1Fragment,
        vararg permissions: String
    ): Boolean = permissions.all {
        getContext()?.let { it1 ->
            ActivityCompat.checkSelfPermission(
                it1,
                it
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST ->
                if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission is granted.  do file related tasks here
                    Log.d("RegisterForm1TAG", "User Granted image permission.")
                } else {
                    Log.d("RegisterForm1TAG", "User Denied image permission.")
                }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun uploadToCloudinary(filepath: String): MutableLiveData<String> {
        val result = MutableLiveData<String>()
        MediaManager.get().upload(filepath).callback(object : UploadCallback {

            override fun onSuccess(requestId: String, resultData: MutableMap<Any?, Any?>) {
                Toast.makeText(requireContext(), resultData["secure_url"] as String, Toast.LENGTH_LONG).show()
                result.value = resultData["secure_url"] as String
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {

            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                Toast.makeText(requireContext(),
                    "Task Not successful$error",
                    Toast.LENGTH_LONG).show()
            }

            override fun onStart(requestId: String?) {

            }

        }).dispatch()

        return result
    }

}