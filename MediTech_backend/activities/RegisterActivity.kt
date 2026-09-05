package com.example.meditechapp.activities

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.meditechapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
    }

    fun registerClick(view: View) {

        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        val roleGroup = findViewById<RadioGroup>(R.id.roleGroup)
        val selectedId = roleGroup.checkedRadioButtonId

        if (selectedId == -1) {
            Toast.makeText(this, "Select role", Toast.LENGTH_SHORT).show()
            return
        }

        val role = if (selectedId == R.id.doctor) "doctor" else "hospital"

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val userId = auth.currentUser?.uid
                    val db = FirebaseFirestore.getInstance()

                    val user = hashMapOf(
                        "email" to email,
                        "role" to role
                    )

                    db.collection("users")
                        .document(userId!!)
                        .set(user)

                    Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }
}