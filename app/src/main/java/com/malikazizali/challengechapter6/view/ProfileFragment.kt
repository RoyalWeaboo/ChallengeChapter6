package com.malikazizali.challengechapter6.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentHomeBinding
import com.malikazizali.challengechapter6.databinding.FragmentProfileBinding
import com.malikazizali.challengechapter6.viewmodel.BlurViewModelFactory
import com.malikazizali.challengechapter6.viewmodel.ProfilePictureViewModel
import com.malikazizali.challengechapter6.viewmodel.UserViewModel
import com.malikazizali.challengechapter6.worker.OUTPUT_PATH
import com.malikazizali.challengechapter6.worker.writeBitmapToFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ProfileFragment : Fragment() {
    lateinit var binding : FragmentProfileBinding
    lateinit var userViewModel : UserViewModel
    lateinit var namaLengkap : String
    lateinit var username : String
    lateinit var password : String
    private val blurViewModel: ProfilePictureViewModel by viewModels { BlurViewModelFactory(requireActivity().application) }
    private var currLang = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileProgressBar.visibility = View.GONE
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        setBlurredProfileImage()
        getSavedData()

        binding.ivEdit.setOnClickListener {
            enableEditMode()
            binding.btnEdit.visibility = View.VISIBLE
            binding.btnEdit.setOnClickListener {
                binding.profileProgressBar.visibility = View.VISIBLE
                val newNamaLengkap = binding.etNamaLengkap.text.toString()
                val newUsername = binding.etUsername.text.toString()
                val newPassword = binding.etPassword.text.toString()
                userViewModel.editData(
                    newNamaLengkap,
                    newUsername,
                    newPassword,
                    "true"
                )
                disableEditMode()
                binding.btnEdit.visibility = View.GONE
            }
            getSavedData()
        }

        binding.ivProfile.setOnClickListener {
            checkingPermissions()
        }

        binding.arrowBack.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_homeFragment)
        }

        binding.changeLanguage.setOnClickListener {
            getLocale()
            showChangeLanguageDialogue(currLang)
        }

        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(context?.getString(R.string.dialog_logout_title))
            builder.setMessage(context?.getString(R.string.dialog_logout_content))
            builder.setIcon(R.drawable.ic_baseline_language_24_black)
            builder.setPositiveButton(context?.getString(R.string.dialog_yes)) { dialog, _ ->
                userViewModel.editSession("false")
                Toast.makeText(requireActivity(), context?.getString(R.string.success_logout), Toast.LENGTH_SHORT).show()
                Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment)
            }
            builder.setNegativeButton(context?.getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

    }

    private fun getSavedData() {
        userViewModel.dataUser.observe(requireActivity()) {
            namaLengkap = it.namaLengkap
            username = it.username
            password = it.password
        }

        binding.etNamaLengkap.setText(namaLengkap)
        binding.etUsername.setText(username)
        binding.etPassword.setText(password)
    }

    fun enableEditMode() {
        binding.etNamaLengkap.isEnabled = true
        binding.etUsername.isEnabled = true
        binding.password.isEnabled = true
    }

    fun disableEditMode() {
        binding.etNamaLengkap.isEnabled = false
        binding.etUsername.isEnabled = false
        binding.password.isEnabled = false
    }

    fun setLocale(lang: String) {

        val locale = Locale(lang)
        Locale.setDefault(locale)

        val resources = context?.resources

        val configuration = resources?.configuration
        configuration?.locale = locale
        configuration?.setLayoutDirection(locale)

        resources?.updateConfiguration(configuration, resources.displayMetrics)
    }

    fun getLocale() {
        val lang = resources.getConfiguration().locale.getLanguage()
        currLang = lang
    }

    fun showChangeLanguageDialogue(currLang : String){
        if (currLang == "in") {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(context?.getString(R.string.dialog_cl_title))
            builder.setMessage(context?.getString(R.string.dialog_cl_content))
            builder.setIcon(R.drawable.ic_baseline_language_24_black)
            builder.setPositiveButton(context?.getString(R.string.dialog_yes)) { dialog, _ ->
                setLocale("en")
                dialog.dismiss()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
            builder.setNegativeButton(context?.getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        } else if (currLang == "en") {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(context?.getString(R.string.dialog_cl_title))
            builder.setMessage(context?.getString(R.string.dialog_cl_content))
            builder.setIcon(R.drawable.ic_baseline_language_24_black)
            builder.setPositiveButton(context?.getString(R.string.dialog_yes)) { dialog, _ ->
                setLocale("in")
                dialog.dismiss()
                startActivity(Intent(requireActivity(), MainActivity::class.java))
            }
            builder.setNegativeButton(context?.getString(R.string.dialog_no)) { dialog, _ ->
                dialog.dismiss()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }

    //handling profile picture
    private fun checkingPermissions() {
        if (isGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                97,
            )
        ) {
            chooseImageDialog()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }


    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(context?.getString(R.string.dialog_perm_title))
            .setMessage(context?.getString(R.string.dialog_perm_content))
            .setPositiveButton(
                context?.getString(R.string.dialog_yes)
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(context?.getString(R.string.dialog_no)) { dialog, _ -> dialog.cancel() }
            .show()
    }
    private fun chooseImageDialog() {
        AlertDialog.Builder(requireActivity())
            .setMessage(context?.getString(R.string.choose_picture))
            .setPositiveButton("Gallery") { _, _ -> openGallery() }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    // Gallery
    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            blurViewModel.setImageUri(result!!)
            saveProfileImage(result)
            setBlurredProfileImage()
        }

    private fun setBlurredProfileImage(){
        val img = BitmapFactory.decodeFile(requireActivity().applicationContext.filesDir.path + File.separator +"blur_filter_outputs"+ File.separator +"Blurred-Image.png")
        if(img!=null){
            binding.ivProfile.setImageBitmap(img)
        }else{
            Toast.makeText(requireActivity(), context?.getString(R.string.profile_picture_empty), Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveProfileImage(uri : Uri){
        val resolver = requireActivity().applicationContext.contentResolver
        val picture = BitmapFactory.decodeStream(
            resolver.openInputStream(Uri.parse(uri.toString())))
        writeBitmapToFile(requireActivity(), picture)
        blurViewModel.applyBlur()
    }

    //    camera
    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }
    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        binding.ivProfile.setImageBitmap(bitmap)
    }

}
