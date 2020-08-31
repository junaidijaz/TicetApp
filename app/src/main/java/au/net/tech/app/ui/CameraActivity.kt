package au.net.tech.app.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import au.net.tech.app.AppSharedPrefs
import au.net.tech.app.R
import au.net.tech.app.Utils
import au.net.tech.app.baseclasses.BaseActivity
import au.net.tech.app.getTrimmedText
import au.net.tech.app.models.Company
import au.net.tech.app.networking.Networking
import com.bigkoo.pickerview.MyOptionsPickerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.fragment_add_ticket.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList


class CameraActivity : BaseActivity() {
    private var subscribers = CompositeDisposable()
    private lateinit var mPhotoEditor: PhotoEditor

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var selectedContactId: String = ""

    private var selectedImage: Bitmap? = null

    private var contactIdPickerView: MyOptionsPickerView<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }


        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()

        applyListeners()

        setImageEditor()

        getCompanies()
    }

    private fun setImageEditor() {
        mPhotoEditor = PhotoEditor.Builder(this, ivEditSS)
            .setPinchTextScalable(false)
            .build()

        ivEditSS.source.scaleType = ImageView.ScaleType.FIT_XY
        mPhotoEditor.brushColor = ContextCompat.getColor(this, R.color.colorRed)
        mPhotoEditor.brushSize = 10f


    }

    private fun applyListeners() {
        ivCapture.setOnClickListener {
            takePhoto()
        }

        ivSwitchToCam.setOnClickListener {
            showEditView(null)
        }

        ivPickImageFromGall.setOnClickListener {
            pickImageFromGallery()
        }
        btnOpenTicket.setOnClickListener {

            if (Utils.isStaff()) {
                if (contactIdPickerView == null) {
                    Toast.makeText(this, "Please select customer first", Toast.LENGTH_SHORT)
                        .show()
                    getCompanies()
                    return@setOnClickListener
                }

                contactIdPickerView?.show()

            } else {
                openTicket()
            }
        }
        btnRetryContact.setOnClickListener {
            getCompanies()
        }


    }

    fun showEditView(image: Bitmap?) {
        clEditImage.visibility = if (image != null) View.VISIBLE else View.GONE

        if (image != null) {
            ivEditSS.source.setImageBitmap(image)
        } else {
            mPhotoEditor.clearAllViews()
        }

    }

    private val PICK_IMAGE = 1
    private fun pickImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        val pickIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"

        val chooserIntent = Intent.createChooser(intent, "Select Image")
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, pickIntent)


        startActivityForResult(chooserIntent, PICK_IMAGE)
    }


    private fun takePhoto() {

        pbImage.visibility = View.VISIBLE

        Log.d(TAG, "takePhoto: ")
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time-stamped output file to hold the image
        val photoFile = File(
            outputDirectory,
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.US
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        Log.d(TAG, "takePhoto: ")
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    pbImage.visibility = View.GONE
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    pbImage.visibility = View.GONE
                    val myBitmap = BitmapFactory.decodeFile(photoFile.absolutePath)


                    val matrix = Matrix()
                    matrix.postRotate(90f)
                    val rotated = Bitmap.createBitmap(
                        myBitmap,
                        0,
                        0,
                        myBitmap.width,
                        myBitmap.height,
                        matrix,
                        true
                    )
                    showEditView(rotated)
                }
            })
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.createSurfaceProvider())
                }

            imageCapture = ImageCapture.Builder()
                .build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))

    }


    private fun openTicket() {

        if (!isFormValid())
            return

        showWaitingDialog()

        selectedImage = loadBitmapFromView(ivEditSS)

        val attachment: MultipartBody.Part = buildImageBodyPart(selectedImage!!)


        subscribers.add(
            Networking.create().openTicketWithImage(
                department = RequestBody.create(MediaType.parse("text"), "1"),
                priority = RequestBody.create(MediaType.parse("text"), "1"),
                message = RequestBody.create(MediaType.parse("text"), "its image token"),
                subject = RequestBody.create(MediaType.parse("text"), etSubject.getTrimmedText()),
                contactid = RequestBody.create(
                    MediaType.parse(selectedContactId),
                    etSubject.getTrimmedText()
                ),
                attachment = attachment
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        dismissWaitingDialog()
                        Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                        if (result.ticket_group_id != null) {
                            showEditView(null)
                            openChatFragment(result.ticket_group_id!!, result.ticket_id!!)
                        } else showEditView(null)
                    },
                    { error ->
                        dismissWaitingDialog()
                        Toast.makeText(this, "You don't have permissions to open ticket", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
        )
    }


    private fun getCompanies() {

        if (Utils.isStaff()) {
            pbContactId.visibility = View.VISIBLE
            btnRetryContact.visibility = View.GONE

            subscribers.add(Networking.create().getCompanies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { result ->
                        pbContactId.visibility = View.GONE
                        btnRetryContact.visibility = View.GONE
                        setContactIds(result)
                    },
                    { error ->
                        pbContactId.visibility = View.GONE
                        btnRetryContact.visibility = View.VISIBLE
                    }
                ))

        } else {
            pbContactId.visibility = View.GONE
        }

    }

    private fun openChatFragment(ticketId: String, gId: String) {

        if (AppSharedPrefs.getUser()?.mesibo_id == null)
            return

        startActivity(Intent(this, ChatActivity::class.java).apply {
            putExtra("gid", gId)
            putExtra("tittle", ticketId)
        })

    }

    private fun setContactIds(contactIds: ArrayList<Company>) {

        contactIdPickerView = MyOptionsPickerView(this)
        val items = ArrayList<String>()
        contactIds.forEach {
            items.add(it.company ?: "")
        }

        contactIdPickerView?.setPicker(items)
        contactIdPickerView?.setTitle("Please select company")
        contactIdPickerView?.setCyclic(false)
        contactIdPickerView?.setSelectOptions(0)
        contactIdPickerView?.setOnoptionsSelectListener { options1, option2, options3 ->
            selectedContactId = contactIds[options1].userid ?: ""

            openTicket()

        }
    }


    private fun isFormValid(): Boolean {

        if (etSubject.getTrimmedText() == "") {
            Toast.makeText(this, "Please Enter Subject", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun loadBitmapFromView(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.draw(c)
        return b
    }

    private fun buildImageBodyPart(bitmap: Bitmap): MultipartBody.Part {
        val leftImageFile = convertBitmapToFile("attachment", bitmap)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), leftImageFile)
        return MultipartBody.Part.createFormData("attachment", leftImageFile.name, reqFile)
    }


    @Suppress("SameParameterValue")
    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(cacheDir, fileName)
        file.createNewFile()

        //Convert bitmap to byte array
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos)
        val bitMapData = bos.toByteArray()

        //write the bytes in file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        try {
            fos?.write(bitMapData)
            fos?.flush()
            fos?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: ")

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            val pickedImage = data.data as Uri
            val selectedImage = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(this.contentResolver, pickedImage)
            } else {
                val source =
                    ImageDecoder.createSource(this.contentResolver, pickedImage)
                ImageDecoder.decodeBitmap(source)
            }

            showEditView(selectedImage)

        }

    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}