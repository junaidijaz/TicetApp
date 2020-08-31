package au.net.tech.app.ui.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import au.net.tech.app.R
import au.net.tech.app.baseclasses.BaseFragment
import au.net.tech.app.getTrimmedText
import au.net.tech.app.models.DepartmentDto
import au.net.tech.app.models.PrioritiesDto
import au.net.tech.app.networking.Networking
import au.net.tech.app.setCustomAnimation
import com.bigkoo.pickerview.MyOptionsPickerView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_add_ticket_image.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.*


class AddImageTicketFragment : BaseFragment() {
    private var subscribers = CompositeDisposable()
    val TAG = "AddImageTicketFragment"
    var mContext : Context? = null

    private var departmentPicker: MyOptionsPickerView<String>? = null
    private var prioritiesPickerView: MyOptionsPickerView<String>? = null

    private var selectedPriorityId = ""
    private var selectedDepartmentId = ""

    private lateinit var mView: View
    private var selectedImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_ticket_image, container, false)


        mContext = activity
        applyListeners()
        getDataFromServer()
        return mView
    }

    private fun applyListeners() {
        mView.etEnterSubject.setCustomAnimation(mView.dvdSubject)
        mView.etMessage.setCustomAnimation(mView.dvdMessage)

        mView.tvRetryDep.setOnClickListener {
            getDepartments()
        }

        mView.tvRetryPriority.setOnClickListener {
            getPriorities()
        }

        mView.btnPickImage.setOnClickListener {
            pickImageFromGallery()
        }

        mView.ivTicket.setOnClickListener {
            pickImageFromGallery()
        }

        mView.btnOpenTicket.setOnClickListener {
            if (isFormValid())
                openTicket()
        }

        mView.etPriority.setOnClickListener {
            prioritiesPickerView?.show()
        }

        mView.etDepartment.setOnClickListener {
            departmentPicker?.show()
        }
    }


    private fun getDataFromServer() {
        getPriorities()
        getDepartments()
    }


    private fun setDepartmentsPickerView(departments: ArrayList<DepartmentDto>) {

        departmentPicker = MyOptionsPickerView(mContext)
        val items = ArrayList<String>()
        departments.forEach {
            items.add(it.name ?: "")
        }

        departmentPicker?.setPicker(items)
        departmentPicker?.setTitle("")
        departmentPicker?.setCyclic(false)
        departmentPicker?.setSelectOptions(0)
        departmentPicker?.setOnoptionsSelectListener { options1, option2, options3 ->
            selectedDepartmentId = departments[options1].id ?: ""
            mView.etDepartment.text = departments[options1].name
        }
    }

    private fun setPrioritiesPickerView(priorities: ArrayList<PrioritiesDto>) {

        prioritiesPickerView = MyOptionsPickerView(mContext)
        val items = ArrayList<String>()
        priorities.forEach {
            items.add(it.name ?: "")
        }

        prioritiesPickerView?.setPicker(items)
        prioritiesPickerView?.setTitle("")
        prioritiesPickerView?.setCyclic(false)
        prioritiesPickerView?.setSelectOptions(0)
        prioritiesPickerView?.setOnoptionsSelectListener { options1, option2, options3 ->
            selectedPriorityId = priorities[options1].id ?: ""
            mView.etPriority.text = priorities[options1].name
        }
    }


    private fun getDepartments() {
        mView.tvRetryDep.visibility = View.GONE
        mView.pbDepartment.visibility = View.VISIBLE

        subscribers.add(
            Networking.create().getDepartments()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    mView.pbDepartment.visibility = View.GONE
                    setDepartmentsPickerView(result)
                },
                { error ->
                    mView.tvRetryDep.visibility = View.GONE
                    mView.pbDepartment.visibility = View.GONE

                }
            )
        )
    }


    private fun getPriorities() {

        mView.tvRetryPriority.visibility = View.GONE
        mView.pbPriorities.visibility = View.VISIBLE

        subscribers.add(Networking.create().getPriorities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    mView.pbPriorities.visibility = View.GONE
                    setPrioritiesPickerView(result)
                },
                { error ->
                    mView.tvRetryPriority.visibility = View.VISIBLE
                    mView.pbPriorities.visibility = View.GONE
                }
            )
        )
    }


    private fun openTicket() {
        showWaitingDialog()

        Log.d(TAG, "openTicket: $selectedDepartmentId")
        Log.d(TAG, "openTicket: $selectedPriorityId")


        val attachment: MultipartBody.Part = buildImageBodyPart(selectedImage!!)
//        subscribers.add(
//            Networking.create().openTicketWithImage(
//                department = selectedDepartmentId,
//                priority = selectedPriorityId,
//                message = mView.etMessage.getTrimmedText(),
//                subject = mView.etEnterSubject.getTrimmedText(),
//                contactid = "",
//                attachment = attachment
//            )
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(
//                    { result ->
//                        dismissWaitingDialog()
//                        Toast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT).show()
//                        resetFields()
//                    },
//                    { error ->
//                        dismissWaitingDialog()
//                        Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                )
//        )
    }

    private fun resetFields() {
        mView.etEnterSubject.requestFocus()
        mView.etEnterSubject.setText("")
        mView.etDepartment.text = ""
        mView.etPriority.text = ""
        mView.etMessage.setText("")
        selectedImage = null
        mView.ivTicket.visibility = View.GONE
    }


    private fun isFormValid(): Boolean {

        if (mView.etEnterSubject.getTrimmedText() == "") {
            mView.etEnterSubject.error = "Please enter subject"
            return false
        }
        if (mView.etDepartment.getTrimmedText() == "") {
            mView.etDepartment.error = "Please select department"
            return false
        }
        if (mView.etPriority.getTrimmedText() == "") {
            mView.etPriority.error = "Please select priority"
            return false
        }

        if (selectedImage == null) {
            Toast.makeText(requireContext(), "Please select image", Toast.LENGTH_SHORT).show()
            return false
        }
        if (mView.etMessage.getTrimmedText() == "") {
            mView.etMessage.error = "Please enter some message"
            return false
        }
        return true

    }

    private fun buildImageBodyPart(bitmap: Bitmap): MultipartBody.Part {
        val leftImageFile = convertBitmapToFile("attachment", bitmap)
        val reqFile = RequestBody.create(MediaType.parse("image/*"), leftImageFile)
        return MultipartBody.Part.createFormData("attachment", leftImageFile.name, reqFile)
    }


    @Suppress("SameParameterValue")
    private fun convertBitmapToFile(fileName: String, bitmap: Bitmap): File {
        //create a file to write bitmap data
        val file = File(requireActivity().cacheDir, fileName)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: ")

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {

            val pickedImage = data.data as Uri
            selectedImage = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, pickedImage)
            } else {
                val source =
                    ImageDecoder.createSource(requireActivity().contentResolver, pickedImage)
                ImageDecoder.decodeBitmap(source)
            }
            mView.ivTicket.visibility = View.VISIBLE
            mView.ivTicket.setImageBitmap(selectedImage)

        }

    }

}