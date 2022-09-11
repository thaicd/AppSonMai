package com.example.doantotnghiep.Repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.doantotnghiep.Model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.lang.Exception
import kotlin.math.log

class Repository {

//    lateinit var  = MutableLiveData()
    var listUsers = mutableListOf<User>()
    fun updateRegisFirebase(user: User) {

        val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference("Accounts").child(user.id!!)
        ref.setValue(user)
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