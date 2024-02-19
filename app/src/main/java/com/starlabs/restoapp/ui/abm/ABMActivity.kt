package com.starlabs.restoapp.ui.abm

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.squareup.picasso.Picasso
import com.starlabs.restoapp.R
import com.starlabs.restoapp.databinding.ActivityAbmBinding
import com.starlabs.restoapp.helpers.LoadingDialog
import com.starlabs.restoapp.model.MenuItem
import com.starlabs.restoapp.ui.extentions.createImageFile
import com.starlabs.restoapp.ui.extentions.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class ABMActivity : AppCompatActivity() {

    private val viewModel by viewModels<ABMViewModel>()
    private lateinit var binding: ActivityAbmBinding
    private var photoURI: Uri?= null
    private var loadingDialog = LoadingDialog()
    private val name = System.currentTimeMillis().toString()

    companion object {
        const val CAMARA_REQUEST = 100
        fun start(activity: Activity, id: String?=null){
            val intent = Intent(activity, ABMActivity::class.java)
            intent.putExtra("id", id)
            activity.startActivity(intent)
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { imageUri ->
            photoURI = imageUri
            showImage(imageUri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAbmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.init()
        checkIntents()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.menuItemLiveData.observe(this) { handleProduct(it) }
        viewModel.saveProductLiveData.observe(this) { handleSavedProduct(it) }
        viewModel.updatedProductLiveData.observe(this) { handleUpdateProduct(it) }
        viewModel.imageProductLiveData.observe(this) { handleSavedImage(it) }
    }

    private fun handleSavedImage(unit: Unit?) {
        unit?.let { createAndSave() }
            ?:run {
                loadingDialog.dismiss()
                showToast(this, getString(R.string.error_saved))
            }
    }

    private fun handleUpdateProduct(product: Unit?) {
        product
            ?.let { finish() }
            ?:run {
                loadingDialog.dismiss()
                showToast(this, getString(R.string.error_update)) }
    }

    private fun handleSavedProduct(product: MenuItem?) {
        product
            ?.let { finish() }
            ?:run {
                loadingDialog.dismiss()
                showToast(this, getString(R.string.error_saved)) }
    }

    private fun saveImage() {
        viewModel.uri
            ?.let { viewModel.saveImage(name, it) }
            ?:run { showToast(this, getString(R.string.error_saved)) }
    }

    private fun handleProduct(menuItem: MenuItem?) {

        menuItem
            ?.let { setProduct(it) }
            ?:run { showToast(this, getString(R.string.error_prod)) }
    }

    private fun setProduct(menuItem: MenuItem) {
        with(binding){
            nameTextView.hint = menuItem.titulo
            descriptionTextView.hint = menuItem.descripcion
            priceText.hint = "$ ${menuItem.precio}"
            menuItem.imagen?.let {
                if (it.isNotEmpty()) showImage(Uri.parse(it))
            }
        }
    }
    private fun checkIntents() {
        val id = intent.getStringExtra("id")
        if (id.isNullOrEmpty()) showAddLayout() else showEditLayout(id)
        setButtons()
    }

    private fun setButtons() {
        with(binding){
            camaraImage.setOnClickListener { dispatchTakePictureIntent() }
            galleryImage.setOnClickListener { getContent.launch("image/*") }
            sendButton.setOnClickListener { validateAndSave() }
        }
    }

    private fun validateAndSave() {
        viewModel.menuItemLiveData.value
            ?.let { checkEditProduct(it) }
            ?:run { checkNewProduct() }
    }
    private fun checkNewProduct() {
        when {
            binding.nameTextView.text.isNullOrBlank() -> binding.nameTextView.hint = getString(R.string.insert_text)
            binding.descriptionTextView.text.isNullOrBlank() -> binding.descriptionTextView.hint = getString(R.string.insert_text)
            binding.priceText.text.isNullOrBlank() -> binding.priceText.hint = getString(R.string.insert_precio)
            else -> saveImage()
        }
    }

    private fun createAndSave() {
        loadingDialog.show(supportFragmentManager)
        val newProduct = MenuItem(
            titulo = binding.nameTextView.text.toString(),
            descripcion = binding.descriptionTextView.text.toString(),
            precio = binding.priceText.text.toString().toDouble(),
            tipo = binding.tipoTextView.text.toString()
        )
        viewModel.saveMenuItem(newProduct)
    }

    private fun checkEditProduct(menuItem: MenuItem) {
        loadingDialog.show(supportFragmentManager)
        val image = menuItem.imagen
        val modifiedItems = MenuItem(
            titulo = getBinding(binding.nameTextView),
            descripcion = getBinding(binding.descriptionTextView),
            precio = getBinding(binding.priceText).toDouble()
        )
        if (!image.isNullOrEmpty()) modifiedItems.imagen = menuItem.imagen
        viewModel.updateMenuItem(modifiedItems)
    }

    private fun getBinding(hint: AppCompatEditText): String{
        return if (hint.text.isNullOrBlank()) hint.hint.toString() else hint.text.toString()
    }
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile(name)
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    photoURI = FileProvider.getUriForFile(
                        this,
                        "com.example.android.fileprovider",
                        it
                    )
                    viewModel.uri = photoURI
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, CAMARA_REQUEST)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CANCELED) return
        when(requestCode){
            CAMARA_REQUEST -> showImage(photoURI!!)
        }
    }

    private fun showEditLayout(id: String) {
        loadingDialog.show(supportFragmentManager)
        viewModel.getMenuItem(id)
    }

    private fun showAddLayout() {
        binding.sendButton.visibility = View.GONE
    }


    private fun showImage(uri: Uri) {
        viewModel.uri = uri
        binding.sendButton.visibility = View.VISIBLE
        Picasso.get().load(uri).into(binding.image)
    }
}