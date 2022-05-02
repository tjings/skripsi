package umn.ac.id.skripsijosh.ui.profile.uploadimage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import umn.ac.id.skripsijosh.base.BaseActivity
import umn.ac.id.skripsijosh.databinding.ActivityUploadImageBinding
import umn.ac.id.skripsijosh.pojo.UserData
import umn.ac.id.skripsijosh.utils.Util
import java.io.IOException

class UploadImageActivity : BaseActivity(), UploadImageView {

    private lateinit var binding : ActivityUploadImageBinding
    private lateinit var presenter: UploadImagePresenter
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = UploadImagePresenter(this)

        binding.btnChose.setOnClickListener { launchGallery() }
        binding.btnUpload.setOnClickListener {
            if(Util.isNotNull(filePath.toString())) {
                presenter.uploadImage(
                    filePath!!,
                    userData = UserData(
                        displayName = sharedPreferences.getString("display_name", ""),
                        gender = sharedPreferences.getString("gender", ""),
                        bday = sharedPreferences.getString("bday", ""),
                        height = sharedPreferences.getString("height", ""),
                        weight = sharedPreferences.getString("weight", ""),
                        displayPic = sharedPreferences.getString("display_pic", ""),
                        isBiodataDone = sharedPreferences.getString("is_biodata_done", "").toBoolean()
                    )
                )
            } else {
                Toast.makeText(this, "Please Upload an Image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                binding.ivPreview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onUploadImageComplete() {
        finish()
    }

    override fun startLoading() {
        showLoadingProgress()
    }

    override fun stopLoading() {
        dismissLoading()
    }

    override fun showError(message: String) {}

    override fun showEmpty() {}
}