package com.example.doantotnghiep.Customer

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doantotnghiep.Adapter.CommentAdapter
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Helper.CustomProgressBar
import com.example.doantotnghiep.Model.*
import com.example.doantotnghiep.R
import com.example.doantotnghiep.Repository.ShareReference
import com.example.doantotnghiep.Repository.Time
import com.example.doantotnghiep.ViewModel.*
import com.example.doantotnghiep.databinding.ActivityDetailProductBinding
import com.example.doantotnghiep.databinding.LayoutRatingBinding
import com.squareup.picasso.Picasso
import es.dmoral.toasty.Toasty
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.text.SimpleDateFormat
import java.util.*

class DetailProductActivity : AppCompatActivity() {
    lateinit var viewBinding : ActivityDetailProductBinding
    lateinit var adapterComment : CommentAdapter
    lateinit var viewModelComment : CommentViewModel
    lateinit var viewModelProduct: ProductViewModel
    lateinit var viewModelRating  : RatingViewModel
    lateinit var viewModelCart     :CartViewModel
    lateinit var viewModelOrder     :OrderViewModel
    lateinit var loadingProductDetail : CustomProgressBar
    lateinit var mProduct : Product
    lateinit var user : User
    var flag = 1 ;
    var listComment = mutableListOf<Comment>()
    var idOrder = Time.convertTimeDate(System.currentTimeMillis())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail_product)
        loadingProductDetail = CustomProgressBar(this)
        viewModelCart = ViewModelProvider(this)[CartViewModel::class.java]
        viewBinding = ActivityDetailProductBinding.inflate(LayoutInflater.from(this))
        viewModelComment = ViewModelProvider(this)[CommentViewModel::class.java]
        viewModelProduct = ViewModelProvider(this)[ProductViewModel::class.java]
        viewModelRating  = ViewModelProvider(this)[RatingViewModel::class.java]
        viewModelOrder   = ViewModelProvider(this)[OrderViewModel::class.java]
        loadingProductDetail.showProgressBar(this)
        setContentView(viewBinding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            if (loadingProductDetail.isShowing())loadingProductDetail.dismissDialog()
        }, 1000)
        user = ShareReference.getUser()
        val bundle = intent.extras
        //Log.d("ID_ORDER = ", idOrder.toString())
        if (bundle != null ) {
            mProduct = bundle.getSerializable("product") as Product
            setSupportActionBar(viewBinding.toolbar)
            supportActionBar?.apply {
                this.setDisplayHomeAsUpEnabled(true)
            }
            Picasso.with(this).load(mProduct.imgUrl).into(viewBinding.imageProduct)
            viewBinding.itemName.text = mProduct.nameProduct
            viewBinding.itemPrice.text = mProduct.price.toInt().toString() + " VND"
            viewBinding.itemDescription.text = mProduct.description.toString()
            viewBinding.numberStar.text= mProduct.rate!!.toString()
            viewBinding.collapsingOrder.title = mProduct.nameProduct
            viewModelProduct.getStatusFavorite(user.id!!,mProduct.id!!, mProduct.idShop!!).observe(this, Observer {
                Log.d(Constanst.log, "status []: ${it} ")
                if (it == 1) {
                    flag = 0
                    viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                }else{
                    flag = 1
                    viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
            })
        }

        viewModelRating.liveDataRating.observe(this, Observer {
            Log.d(Constanst.log, "rate: ${it}")
        })
        viewBinding.btnCart.setOnClickListener {

            viewModelProduct.getNumberProduct(mProduct.id!!,mProduct.idShop!!).observe(this@DetailProductActivity,
            Observer {
                it?.apply {
                    if(it > 0) {
                        var cart = Cart(mProduct.idShop!!, mProduct.nameShop!!,mProduct,0,
                            0,System.currentTimeMillis().toString(),idOrder.toString())
                        viewModelCart.addCartProduct(user.id!!,mProduct.price!!.toInt(), mProduct.id!!, cart ).observe(
                            this@DetailProductActivity,  Observer{
                            Log.d(Constanst.log, "addCartProduct: finshed ")
                        })

                        val number = it - 1;
                        viewModelProduct.editNumberProduct(mProduct.idShop!!, mProduct.id!!, number)
                            .observe(this@DetailProductActivity,
                                Observer {
                                    Log.d("editNumberProduct:", " editNumberProduct Successfully")
                                })
                            Toasty.info(this@DetailProductActivity,"Đã thêm vào giỏ hàng",
                            Toasty.LENGTH_SHORT).show()
                    }else {
                        Toasty.error(this@DetailProductActivity,"This item is empty!",
                        Toasty.LENGTH_SHORT).show()
                    }
                }
            })
        }

        viewBinding.btnRating.setOnClickListener {
            showDialog()
        }
        viewBinding.btnFavorite.setOnClickListener {
//            Toasty.info(this,"Clicked $flag - ${user} - ${mProduct}", Toasty.LENGTH_SHORT).show()
            Log.d(Constanst.log, "Clicked $flag - ${user} - ${mProduct}")
            if (flag == 1) {
                Log.d(Constanst.log, "$flag: True ")
                viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_red_24)
                Log.d(Constanst.log, "$flag: True 1")
                viewModelProduct.addMyFavoriteProduct(user.id!!,mProduct.id!!, mProduct).observe(this, Observer {
                    Log.d(Constanst.log, "addFavoriteProduct: finshed ")

                })
                Log.d(Constanst.log, "$flag: True 2")
                flag = 0
            }else {
                viewBinding.btnFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
                viewModelProduct.removeMyFavoriteProduct(user.id!!, mProduct.id!!).observe(this, Observer {
                    Log.d(Constanst.log, "addFavoriteProduct: finshed ")
                })
                flag = 1
            }
            viewModelProduct.addMyFavoriteProduct(user.id!!,mProduct.id!!,mProduct)
        }
        viewBinding.itemCall.setOnClickListener {
            _ ->
            val intent = Intent(Intent.ACTION_DIAL)
            startActivity(intent)
        }


        adapterComment = CommentAdapter(listComment);
        viewBinding.itemListComment?.apply {
            adapter = adapterComment
            hasFixedSize()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
//            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        viewModelComment = ViewModelProvider(this)[CommentViewModel::class.java]

        viewModelComment.getListCommentLiveData(mProduct.idShop!!, mProduct.id!!);
        viewModelComment.getLiveDataComment().observe(this, Observer {
            if (it != null && it.size > 0){
                listComment.clear()
                listComment.addAll(it)
                adapterComment.notifyDataSetChanged()
            }
        })
        viewModelRating.liveDataRatingAvarage1().observe(this, Observer {
            it?.apply {
                viewBinding.numberStar.text = it.toDouble().toString()
                viewModelProduct.editRatingProduct(mProduct.idShop!!,mProduct.id!!,it.toDouble()).
                observe(this@DetailProductActivity,
                    Observer {
                        Log.d("viewModelRating: ", it.toString())
                    })
            }
        })

    }
    fun showDialog() {
        val dialog = Dialog(this)
        val binding = LayoutRatingBinding.inflate(LayoutInflater.from(this))
        dialog.setContentView(binding.root)

        val window = dialog.window
        if (window == null ) return

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setGravity(Gravity.CENTER)
        dialog.setCancelable(true)


        var numberRate : Float = (0).toFloat()
        binding.itemRate.setOnRatingChangeListener { ratingBar, rating ->
            numberRate = rating
        }
        binding.btnSend.setOnClickListener {
            val feedback = binding.edtFeedback.text.trim()
            Log.d("SEND_COMMENT", "SEND_COMMENT : RATING")
            val rate = Rating(System.currentTimeMillis().toString(), numberRate, user.id!!)
            viewModelRating.addGetRating(mProduct.idShop!!, mProduct.id!!, rate)
            viewModelRating.getRating(mProduct.idShop!!, mProduct.id!!)

            Log.d("SEND_COMMENT", "SEND_COMMENT : RATING1 - length = " + feedback.length + " feedback = " + feedback )
            if(feedback.length > 0 ) {
                Log.d("SEND_COMMENT", feedback.toString())
                val time = Time.convertTimstampToDate()
                val comment =
                    Comment(System.currentTimeMillis().toString(), user.name, feedback.toString(), time)
                viewModelComment.addComment(mProduct.idShop!!,mProduct.id!!, comment.idComment!!, comment)
                    .observe(this, Observer {
                        it?.apply {
                            Log.d("COMMENT", it.toString())
                        }
                    })
            }
            dialog.dismiss()
        }
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()

    }
}