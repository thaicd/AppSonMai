package com.example.doantotnghiep.Repository

import kotlinx.coroutines.tasks.await
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.doantotnghiep.Helper.Constanst
import com.example.doantotnghiep.Model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.Exception
import com.example.doantotnghiep.Model.ResponseProduct as ResponseProduct1

class Repository {

//    lateinit var  = MutableLiveData()
    var listUsers = mutableListOf<User>()
    fun updateRegisFirebase(user: User) {

        val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Accounts").child(user.id!!)
        ref.setValue(user)
    }
    suspend fun getListCart(idUser : String ) : List<Cart> {
        val ref = FirebaseDatabase.getInstance().
                getReference("Carts").child(idUser)

        val res : List<Cart> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Cart::class.java)!!
        }
        return res
    }
    suspend fun getMyFavoriteProduct(idUser : String ) : List<MyFavoriteProduct> {
        val ref = FirebaseDatabase.getInstance()
            .getReference("MyFavoriteProduct").child(idUser)

        val res : List<MyFavoriteProduct> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(MyFavoriteProduct::class.java)!!
        }

        return res

    }
    fun checkLoginAccount(uid : String , livedata: MutableLiveData<User>) {
        val ref = FirebaseDatabase.getInstance().getReference("Accounts").child(uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0 != null){
                    val user : User? = p0.getValue(User::class.java)
                    livedata.postValue(user)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                livedata.postValue(null)
            }

        })
    }

    suspend fun addProductToFirebase( id: String ,product: Product)  {
        val ref = FirebaseDatabase.getInstance().getReference("Products").child(id)
        try{
            ref.setValue(product).await()
        }catch (e: Exception){
        }
    }
    suspend fun getListUsersCoroutine() : List<User> {
        val ref = FirebaseDatabase.getInstance().getReference("Accounts")
        val res : List<User> = ref.get().await().children.map {
            dataSnapshot ->   dataSnapshot.getValue(User::class.java)!!
        }
        return res
    }
    suspend fun addCategory(cat : Category) {
        val ref = FirebaseDatabase.getInstance().getReference("Category").child(cat.id!!)
        ref.setValue(cat).await()
    }
    suspend fun getListCategoryFirebase () : List<Category>{
        val ref = FirebaseDatabase.getInstance().getReference("Category")
        val res : List<Category> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Category::class.java)!!
        }
        return res
    }
    suspend fun checkExistCategoryFirebase(idCategory : String ) : Boolean {
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        val res : List<Product> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Product::class.java) !!
        }
        val tmp = res.size
//        Log.d(Constanst.log
//            , "${tmp} san pham ")
        if ( tmp == 0 ){
            Log.d(Constanst.log
                , " Vao false ")
            return false
        }
//        Log.d(Constanst.log
//            , "Chua Vao ")
        for (d in res ) {
//            Log.d(Constanst.log
//                , "Vao ")
//            Log.d(Constanst.log
//                , "${d} ")
            if(d?.idCategory.equals(idCategory)) return true
        }
        return false
    }
    suspend fun getListMyFavoriteProduct(idUser : String ) : List<MyFavoriteProduct> {

        val ref = FirebaseDatabase.getInstance().
                  getReference("MyProductFavorite").child(idUser)

        val res : List<MyFavoriteProduct> = ref.get().await().children.map {
            dataSnapshot ->  dataSnapshot.getValue(MyFavoriteProduct::class.java)!!
        }
        Log.d(Constanst.log, "res :  ${res.size}" )
        return  res ;
    }
     fun addRating( idProduct: String , rate : Rating ,  livedata: MutableLiveData<Float>) {
        var f = false
        val ref = FirebaseDatabase.getInstance().getReference("Ratings").child(idProduct).child(rate.idUser!!)
        ref.setValue(rate)
         val ref1 = FirebaseDatabase.getInstance().getReference("Ratings").child(idProduct)
         val res = ref1.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {

                 if (snapshot != null && snapshot.children.count() > 0) {
                     var tmpRating = (0).toFloat()
                     var number = 0
                     for (d in snapshot.children) {
                        tmpRating += d.getValue(Rating::class.java)?.rateNumber?.toFloat()!!
                        number+= 1
                     }
                    livedata.postValue(tmpRating/number)
                 }
             }

             override fun onCancelled(error: DatabaseError) {
             }

         })

    }
    fun getRating( idProduct: String, liveData: MutableLiveData<Float>) {
        val ref1 = FirebaseDatabase.getInstance().getReference("Ratings").child(idProduct)
        val res = ref1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot != null && snapshot.children.count() > 0) {
                    var tmpRating = (0).toFloat()
                    var number = 0
                    for (d in snapshot.children) {
                        tmpRating += d.getValue(Rating::class.java)?.rateNumber?.toFloat()!!
                        number+= 1
                    }
                    liveData.postValue(tmpRating/number)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    suspend fun addFavoriteProduct(idUser: String , idProduct: String, prod : Product) : Boolean {
        var f = false
        Log.d(Constanst.log, "addFavoriteProduct: before ")

        val ref = FirebaseDatabase.getInstance().getReference("MyProductFavorite").child(idUser)
            .child(idProduct)
        Log.d(Constanst.log, "addFavoriteProduct: before 1")
        try {
            val value = MyFavoriteProduct(System.currentTimeMillis().toString(), prod, 1)
            ref.setValue(value).await()
            Log.d(Constanst.log, "addFavoriteProduct: before 2")

            f = true
        }catch (e : Exception) {
            f = false
        }
        Log.d(Constanst.log, "addFavoriteProduct: $f")
        return f

    }
    suspend fun removeFavoriteProduct(idUser: String, idProduct: String) : Boolean{
        var f = false
        val ref = FirebaseDatabase.getInstance().getReference("MyProductFavorite").child(idUser)
            .child(idProduct)
        try {
            ref.removeValue().await()
            f = true
        }catch (e : Exception) {
            f = false
        }
        return f
    }
    suspend fun getStatusFavoriteProduct(idUser: String, idProduct: String) : Int {
        val ref = FirebaseDatabase.getInstance().getReference("MyProductFavorite").child(idUser)
            .child(idProduct)

        val res = ref.get().await().getValue(MyFavoriteProduct::class.java)


        if (res == null ) return 0
        Log.d(Constanst.log, "status: ${res} ")
        return res.likes

    }
    suspend fun addComment(idProduct : String , idComment : String, comment : Comment)  :Int {
        var f = 0 ;
        val ref = FirebaseDatabase.getInstance().getReference("Comments")
            .child(idProduct).child(idComment)

        try {
            val res = ref.setValue(comment).await() ;
            f = 1;
        }catch (e : Exception) {
            f = 0;
        }
        Log.d(Constanst.log, "add comment : ${f} ")

        return f ;
    }
    suspend fun getCommentProduct(idProduct : String ) : List<Comment> {
        val ref = FirebaseDatabase.getInstance().getReference("Comments").child(idProduct)

        val res : List<Comment> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Comment::class.java)!!
        }
        Log.d(Constanst.log, "res :  ${res.size}" )
        return  res ;
    }
    suspend fun getCartProduct(idUser : String, idProduct : String) : List<Cart> {
        val ref = FirebaseDatabase.getInstance().getReference("Carts")
                                  .child(idUser)
        val res : List<Cart> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Cart::class.java)!!
        }
        Log.d(Constanst.log, "res :  ${res.size}" )
        return  res ;
    }
    suspend fun getListOrders(idUser : String) : List<Order> {
        val ref = FirebaseDatabase.getInstance().
                                   getReference("Orders").
                                   child(idUser)
        val res : List<Order> = ref.get().await().children.map {
                dataSnapshot -> dataSnapshot.getValue(Order::class.java)!!
        }
        Log.d(Constanst.log, "res :  ${res.size}" )
        return  res ;
    }
    suspend fun  getListCommentProduct(id : String ): List<Comment> {
        val ref = FirebaseDatabase.getInstance().getReference("Comments").child(id)
        var res : List<Comment> = mutableListOf()
            res =  ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Comment::class.java)!!
        }
        return  res ;
    }
    suspend fun getListProductCoroutine(): List<Product> {
        Log.d(Constanst.log, "List Product")
        val ref = FirebaseDatabase.getInstance().getReference("Products")
        val res: List<Product> = ref.get().await().children.map {
            dataSnapshot -> dataSnapshot.getValue(Product::class.java)!!
        }
        Log.d(Constanst.log, "${res}: ")
        return res
    }
    suspend fun removeProduct(id: String) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(id)
        var f = true
        try {
            refs.removeValue().await()

        }catch (e : Exception) {
            f = false
        }
        return f
    }
    suspend fun addProduct(p : Product) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(p.id!!)
        var f = true
        try {
            refs.setValue(p).await()

        }catch (e : Exception) {
            f = false
        }
        return f
    }
    suspend fun editProduct(id : String, p : Product) : Boolean {
        val refs = FirebaseDatabase.getInstance().getReference("Products").child(id)
        var f = true
        try {
            refs.setValue(p).await()

        }catch (e : Exception) {
            f = false
        }
        return f
    }

    fun getListUsers(liveData : MutableLiveData<MutableList<User>>) {
//        listUsers.clear()
        val ref = FirebaseDatabase.getInstance().getReference("Accounts")
        ref.addValueEventListener(object  : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0 != null && p0.children != null) {
                    listUsers.clear()
                    for(data in p0.children) {
                        val user : User? = data.getValue(User::class.java)
                        listUsers.add(user!!)
                    }
                    liveData.postValue(listUsers)
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                liveData.postValue(mutableListOf())
            }

        })
    }

    fun RegisterUser(user : User, regisLiveData : MutableLiveData<FirebaseUser>) {

        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(user.email!!, user.password!!)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult>{
                override fun onComplete(p0: Task<AuthResult>) {
//                    Log.d("onComplete: ", "VAO + ${p0.isSuccessful}  ${p0.exception}")
                    if (p0.isSuccessful) {
                        if (firebaseAuth.currentUser != null) {
                            user.id = firebaseAuth.currentUser!!.uid
                            updateRegisFirebase(user)
//                            Log.d("onComplete: ", "VAO 1")
                            regisLiveData.postValue(firebaseAuth.currentUser)
                        }
                    }
                }

            })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(p0: Exception) {
                    Log.d("onFailure: ", "fail")
                    regisLiveData.postValue(null)
                }
            })
    }
    fun Login(strUser : String , strPass : String , livedata : MutableLiveData<FirebaseUser>) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(strUser, strPass)
            .addOnSuccessListener  (object : OnSuccessListener<AuthResult>{
                override fun onSuccess(p0: AuthResult?) {
                    livedata.postValue(FirebaseAuth.getInstance().currentUser)
                }

            })
            .addOnFailureListener(object : OnFailureListener{
                override fun onFailure(p0: Exception) {
                    livedata.postValue(null)
                }

            })
    }
    companion object {
        var instance : Repository ? = null

        fun getRepository() : Repository {
            if (instance == null ) {
                instance = Repository()
            }
            return instance!!
        }

    }

}