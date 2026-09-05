import com.example.meditechapp.activities.DoctorDashboardActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.meditechapp.Doctor
import com.example.meditechapp.R
import com.example.meditechapp.activities.HospitalDashboardActivity
import com.example.meditechapp.activities.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
    }

    fun loginClick(view: View) {
        val email = findViewById<EditText>(R.id.email).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val userId = auth.currentUser?.uid
                    val db = FirebaseFirestore.getInstance()

                    db.collection("users")
                        .document(userId!!)
                        .get()
                        .addOnSuccessListener { document ->

                            if (document != null) {

                                val role = document.getString("role")

                                if (role == "doctor") {
                                    startActivity(Intent(this, DoctorDashboardActivity::class.java))
                                } else if (role == "hospital") {
                                    startActivity(Intent(this, HospitalDashboardActivity::class.java))
                                } else {
                                    Toast.makeText(this, "Role not found", Toast.LENGTH_SHORT).show()
                                }

                                finish()
                            }
                        }

                } else {
                    Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun goToRegister(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}