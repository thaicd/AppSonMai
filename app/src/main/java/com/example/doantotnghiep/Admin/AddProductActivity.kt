package com.example.doantotnghiep.Admin

import android.graphics.Color
import android.opengl.Visibility
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.Model.Category
import com.example.doantotnghiep.Model.Product
import com.example.doantotnghiep.R
import com.example.doantotnghiep.ViewModel.CategoryViewModel
import com.example.doantotnghiep.ViewModel.ProductViewModel
import com.example.doantotnghiep.databinding.ActivityAddProductBinding
import com.skydoves.powerspinner.PowerSpinnerView
import com.skydoves.powerspinner.SpinnerAnimation
import com.skydoves.powerspinner.SpinnerGravity
import com.skydoves.powerspinner.createPowerSpinnerView
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty

class AddProductActivity : AppCompatActivity() {
    private lateinit var viewBinding : ActivityAddProductBinding
    lateinit var viewModel : ProductViewModel
    lateinit var viewModelCat : CategoryViewModel
    lateinit var listCategory : List<Category>
    lateinit var progress : CustomProgressBar
    var mIndex : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityAddProductBinding.inflate(LayoutInflater.from(this))
        setContentView(viewBinding.root)

        progress = CustomProgressBar(this)

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModelCat = ViewModelProvider(this)[CategoryViewModel::class.java]

        supportActionBar?.setTitle("Add Product")
        supportActionBar?.setDisplayShowHomeEnabled(true)

        viewModelCat.getListCategory().observe(this, Observer {
            if (it != null)
            {
                listCategory = it
                val listString = mutableListOf<String>()

                for (data in it ) {
                    listString.add(data.id+". "+data.name)
                }
                viewBinding.spinnerData.setItems(listString)
                viewBinding.spinnerData.show() // show the spinner popup.

                viewBinding.spinnerData.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
                    mIndex = newIndex
                    Log.d(Constanst.log, "${oldItem} | ${oldIndex} | ${newIndex} | ${newText} ")
                }
            }
        })

        viewBinding.addProBtn.setOnClickListener { _ ->
            val nameP = viewBinding.nameOutlinedTextField.editText?.text.toString().trim()
            val priceP = viewBinding.priceOutlinedTextField.editText?.text.toString().trim()
            val numP  = viewBinding.mrpOutlinedTextField.editText?.text.toString().trim()
            val description = viewBinding.descOutlinedTextField.editText?.text.toString().trim()
            val linkPath = viewBinding.addProImagesRv.text.toString().trim()
            Log.d(Constanst.log, "${nameP} - ${priceP} - ${numP} - ${description} ")
            var f= true
            if(nameP.length == 0){
                Toast.makeText(this, "Chưa nhập tên sản phẩm",Toast.LENGTH_SHORT).show()
                f = false
            }
            else if (priceP.length == 0) {
                Toast.makeText(this, "Chưa nhập giá sản phẩm",Toast.LENGTH_SHORT).show()
                f = false

            }else if(numP.length == 0) {
                Toast.makeText(this, "Chưa nhập số lượng sản phẩm",Toast.LENGTH_SHORT).show()
                f = false


            }else if(description.length == 0 ) {
                Toast.makeText(this, "Chưa nhập mô tả sản phẩm",Toast.LENGTH_SHORT).show()
                f = false

            }else if(linkPath.length == 0) {
                Toast.makeText(this, "Chưa chọn ảnh cho  sản phẩm",Toast.LENGTH_SHORT).show()
                f = false

            } else if(mIndex == -1  ) {
                f = false
            }
            if (f == true) {

                progress.showProgressBar(this)
                viewModel.addData(Product(System.currentTimeMillis().toString(), listCategory.get(mIndex).id,nameP,priceP.toDouble(),
                    numP.toInt(),description,0.0, linkPath,1)).observe(this, object :Observer<Boolean>{
                    override fun onChanged(t: Boolean?) {
                        if (t == true) {
                            mIndex = -1 ;
                            viewBinding.nameOutlinedTextField.editText?.setText("")
                            viewBinding.priceOutlinedTextField.editText?.setText("")
                            viewBinding.mrpOutlinedTextField.editText?.setText("")
                            viewBinding.descOutlinedTextField.editText?.setText("")
                            viewBinding.addProImagesRv.setText("")
                            viewBinding.addImageProduct.visibility= View.GONE
                            Toast.makeText(this@AddProductActivity,
                                "Them sản phẩm thành công", Toast.LENGTH_SHORT).show()
                        }else {
                            Toast.makeText(this@AddProductActivity,
                                "Them sản phẩm không thành công", Toast.LENGTH_SHORT).show()
                        }

                        progress.dismissDialog()
                    }

                })
            }
        }
        viewBinding.addProImagesBtn.setOnClickListener{
            _->
            viewBinding.addImageProduct.visibility = View.VISIBLE
            val linkPath = viewBinding.addProImagesRv.text.toString().trim()

            if (linkPath.length == 0 ) {
                Toasty.error(this@AddProductActivity, "Chua nhap link anh",Toasty.LENGTH_SHORT).show()
            }else {
                Picasso.with(this).load(linkPath.toString()).into(viewBinding.addImageProduct)
            }
        }
        //

    }
}