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
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.abren.adapter.LocationSuggestionAdapter
import com.example.abren.viewmodel.UserViewModel
import android.widget.AdapterView

import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.example.abren.adapter.VehicleInformationAdapter
import com.example.abren.models.Location
import com.example.abren.models.MenuItem
import com.example.abren.models.VehicleInformation
import com.example.abren.viewmodel.LocationViewModel
import com.example.abren.viewmodel.VehicleInformationViewModel
import java.io.File
import java.time.Year

private const val PICK_IMAGE_FROM_GALLERY_REQUEST1 = 1
private const val PICK_IMAGE_FROM_GALLERY_REQUEST2 = 2
private const val PICK_IMAGE_FROM_GALLERY_REQUEST3 = 3
private const val PICK_IMAGE_FROM_GALLERY_REQUEST4 = 4

class RegisterForm2Fragment : Fragment() {

    private val userViewModel: UserViewModel by activityViewModels()

    private lateinit var vehicleYearAdapter: VehicleInformationAdapter
    private lateinit var vehicleMakeAdapter: VehicleInformationAdapter
    private lateinit var vehicleModelAdapter: VehicleInformationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vehicleYearAdapter = VehicleInformationAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )

        vehicleMakeAdapter = VehicleInformationAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )

        vehicleModelAdapter = VehicleInformationAdapter(
            this.requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_form2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        makeApiCall("YEARS", vehicleYearAdapter)
        val vehicleYear = view.findViewById<Spinner>(R.id.vehicle_year)
        vehicleYear.adapter = vehicleYearAdapter
        vehicleYear.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                userViewModel.setVehicleYear(vehicleYearAdapter.getObject(position).value)
                makeApiCall(
                    "MAKES",
                    vehicleMakeAdapter,
                    vehicleYearAdapter.getObject(position).value
                )
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        val vehicleMake = view.findViewById<Spinner>(R.id.vehicle_make)
        vehicleMake.adapter = vehicleMakeAdapter
        vehicleMake.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                userViewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                    userViewModel.setVehicleMake(vehicleMakeAdapter.getObject(position).value)
                    user.vehicleInformation?.year?.let {
                        makeApiCall(
                            "MODELS", vehicleModelAdapter,
                            it, vehicleMakeAdapter.getObject(position).value
                        )
                    }
                })
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        val vehicleModel = view.findViewById<Spinner>(R.id.vehicle_model)
        vehicleModel.adapter = vehicleModelAdapter
        vehicleModel.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                userViewModel.setVehicleModel(vehicleModelAdapter.getObject(position).value)
                Toast.makeText(
                    requireContext(),
                    vehicleModelAdapter.getObject(position).value,
                    Toast.LENGTH_LONG
                ).show()

                userViewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                    makeApiCall(
                        "OPTIONS",
                        vehicleModelAdapter,
                        user.vehicleInformation?.year!!,
                        user.vehicleInformation?.make!!,
                        user.vehicleInformation?.model!!
                    )
                })

            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        val licensePlateNumber = view.findViewById<EditText>(R.id.plate_number_edittext)
        val licensePicture = view.findViewById<TextView>(R.id.driving_license_image_textview)
        val ownershipDoc = view.findViewById<TextView>(R.id.ownership_doc_image_textview)
        val insuranceDoc = view.findViewById<TextView>(R.id.insurance_doc_image_textview)
        val vehiclePicture = view.findViewById<TextView>(R.id.vehicle_image_textview)

        licensePicture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_FROM_GALLERY_REQUEST1
            )

        }

        ownershipDoc.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST2)
        }

        insuranceDoc.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST3)
        }

        vehiclePicture.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, PICK_IMAGE_FROM_GALLERY_REQUEST4)
        }

        view.findViewById<Button>(R.id.back_button2).setOnClickListener {
            findNavController().navigate(R.id.action_RegisterForm2Fragment_to_RegisterForm1Fragment)
        }

        view.findViewById<Button>(R.id.continue_button2).setOnClickListener {
            Toast.makeText(requireContext(), "Loading...", Toast.LENGTH_SHORT).show()
            userViewModel.setVehicleLicensePlateNumber(licensePlateNumber.text.toString())

            userViewModel.selectedUser.observe(viewLifecycleOwner, Observer { user ->
                if (user.vehicleInformation?.licensePlateNumber == null || user.vehicleInformation?.year == null || user.vehicleInformation?.make == null || user.vehicleInformation?.model == null || user.vehicleInformation?.kml == null) {
                    Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT)
                        .show()
                } else if (user.vehicleInformation?.licenseUrl == null || user.vehicleInformation?.ownershipDocUrl == null || user.vehicleInformation?.insuranceDocUrl == null || user.vehicleInformation?.vehiclePictureUrl == null) {
                    Toast.makeText(requireContext(), "Please fill all fields.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val licenseUpload = uploadToCloudinary(user.vehicleInformation?.licenseUrl!!)
                    val ownershipDocUpload =
                        uploadToCloudinary(user.vehicleInformation?.ownershipDocUrl!!)
                    val insuranceDocUpload =
                        uploadToCloudinary(user.vehicleInformation?.insuranceDocUrl!!)
                    val vehiclePictureUpload =
                        uploadToCloudinary(user.vehicleInformation?.vehiclePictureUrl!!)
                    licenseUpload.observeForever(Observer { result ->
                        if (result != null) {
                            Log.d("Uploading License Picture", result)
                            userViewModel.setLicenseUrl(result)

                            ownershipDocUpload.observeForever(Observer { result2 ->
                                if (result2 != null) {
                                    Log.d("Uploading Ownership Doc", result2)
                                    userViewModel.setOwnershipDocUrl(result2)

                                    insuranceDocUpload.observeForever(Observer { result3 ->
                                        if (result3 != null) {
                                            Log.d("Uploading Insurance doc", result3)
                                            userViewModel.setInsuranceDocUrl(result3)

                                            vehiclePictureUpload.observeForever(Observer { result4 ->
                                                if (result4 != null) {
                                                    Log.d("Uploading Vehicle Picture", result4)
                                                    userViewModel.setVehiclePictureUrl(result4)
                                                        findNavController().navigate(R.id.action_RegisterForm2Fragment_to_PreferenceFragment)

                                                } else {
                                                    Log.d("Uploading Vehicle Picture", "Problem")
                                                }
                                            })
                                        } else {
                                            Log.d("Uploading Insurance Doc", "Problem")
                                        }
                                    })

                                } else {
                                    Log.d("Uploading Ownership Doc", "Problem")
                                }
                            })

                        } else {
                            Log.d("Uploading License", "Problem")
                        }
                    })
                }
            })
        }
    }

    private fun makeApiCall(
        type: String,
        adapter: VehicleInformationAdapter,
        year: String = "",
        make: String = "",
        model: String = "",
        value: String = ""
    ) {
        val vehicleInformationViewModel =
            ViewModelProvider(this)[VehicleInformationViewModel::class.java]


        when (type) {
            "YEARS" -> {
                vehicleInformationViewModel.fetchYears()

                vehicleInformationViewModel.vehicleYearListLiveData?.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it != null) {
                            adapter.setData(it)
                        } else {
                            Toast.makeText(
                                this.requireContext(),
                                "Something Went Wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            "MAKES" -> {
                vehicleInformationViewModel.fetchMakes(year)

                vehicleInformationViewModel.vehicleMakeListLiveData?.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it != null) {
                            adapter.setData(it)
                        } else {
                            Toast.makeText(
                                this.requireContext(),
                                "Something Went Wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
            "MODELS" -> {
                vehicleInformationViewModel.fetchModels(year, make)

                vehicleInformationViewModel.vehicleModelListLiveData?.observe(
                    viewLifecycleOwner,
                    Observer {
                        if (it != null) {
                            adapter.setData(it)
                        } else {
                            Toast.makeText(
                                this.requireContext(),
                                "Something Went Wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }

            "OPTIONS" -> {
                vehicleInformationViewModel.fetchOptions(year, make, model)

                vehicleInformationViewModel.vehicleOptionListLiveData?.observe(
                    viewLifecycleOwner,
                    Observer {


                        if (it != null) {
                            vehicleInformationViewModel.fetchVehicle(it[0].value)

                            vehicleInformationViewModel.vehicleLiveData?.observe(
                                viewLifecycleOwner,
                                Observer { vehicle ->
                                    if (vehicle != null) {
                                        userViewModel.setKml(vehicle.comb08!! / 2.352)
                                        Toast.makeText(
                                            this.requireContext(),
                                            "KML: ${vehicle.comb08!! / 2.352}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            this.requireContext(),
                                            "Something Went Wrong",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        } else {
                            Toast.makeText(
                                this.requireContext(),
                                "Something Went Wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST1) {
                val uri: Uri = data.data!!
                val filePath = getPathFromURI(requireContext(), uri)
                val chosenFile1 = File(filePath)
                userViewModel.setLicenseUrl(filePath)
                view?.findViewById<TextView>(R.id.driving_license_image_textview)?.text =
                    chosenFile1.nameWithoutExtension
            }

            if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST2) {
                val uri: Uri = data.data!!
                val filePath = getPathFromURI(requireContext(), uri)
                val chosenFile2 = File(filePath)
                userViewModel.setOwnershipDocUrl(filePath)
                view?.findViewById<TextView>(R.id.ownership_doc_image_textview)?.text =
                    chosenFile2.nameWithoutExtension
            }

            if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST3) {
                val uri: Uri = data.data!!
                val filePath = getPathFromURI(requireContext(), uri)
                val chosenFile3 = File(filePath)
                userViewModel.setInsuranceDocUrl(filePath)
                view?.findViewById<TextView>(R.id.insurance_doc_image_textview)?.text =
                    chosenFile3.nameWithoutExtension
            }

            if (requestCode == PICK_IMAGE_FROM_GALLERY_REQUEST4) {
                val uri: Uri = data.data!!
                val filePath = getPathFromURI(requireContext(), uri)
                val chosenFile4 = File(filePath)
                userViewModel.setVehiclePictureUrl(filePath)
                view?.findViewById<TextView>(R.id.vehicle_image_textview)?.text =
                    chosenFile4.nameWithoutExtension
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
                Toast.makeText(
                    requireContext(),
                    resultData["secure_url"] as String,
                    Toast.LENGTH_LONG
                ).show()
                result.value = resultData["secure_url"] as String
            }

            override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {

            }

            override fun onReschedule(requestId: String?, error: ErrorInfo?) {

            }

            override fun onError(requestId: String?, error: ErrorInfo?) {
                Toast.makeText(
                    requireContext(),
                    "Task Not successful$error",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onStart(requestId: String?) {

            }

        }).dispatch()

        return result
    }
}