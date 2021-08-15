package com.example.abren

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.contentValuesOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.models.User
import com.example.abren.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.fragment_register_form1.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

        val phoneNumber = view.findViewById<EditText>(R.id.phone_number)
        val emergencyPhoneNumber = view.findViewById<EditText>(R.id.emergency_phone_number)

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

        view.findViewById<ImageView>(R.id.profile_pic).setOnClickListener{
            selectImage()
        }
        view.findViewById<TextView>(R.id.kebele_id_textView).setOnClickListener{
            selectImage()
        }
        view.findViewById<TextView>(R.id.kebele_id_back_textView).setOnClickListener{
            selectImage()
        }

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
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val selectedImage = data.extras!!["data"] as Bitmap?
                    profile_pic.setImageBitmap(selectedImage)
                }
//                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
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
//                            profile_pic.setImageBitmap(BitmapFactory.decodeFile(picturePath))
//                            cursor.close()
//                        }
//                    }
//                }
            }
        }
    }


}