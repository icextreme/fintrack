package ca.sfu.iat.fintrack.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import ca.sfu.iat.fintrack.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var database: DatabaseReference = Firebase.database.reference
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var users = database.child("users")
    private lateinit var firstName: TextInputEditText
    private lateinit var lastName: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var newPassword: TextInputEditText
    private lateinit var updateButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        firstName = view.findViewById(R.id.input_first_name_editText)
        lastName = view.findViewById(R.id.input_last_name_editText)
        email = view.findViewById(R.id.input_email_editText)
        newPassword = view.findViewById(R.id.input_new_password_editText)
        updateButton = view.findViewById(R.id.button_update)

        val user = auth.currentUser
        val uid = user?.uid
        val databaseRef = Firebase.database.reference.child("users/$uid")

        updateButton.setOnClickListener {
            if (!firstName.text.isNullOrBlank()) {
                databaseRef.child("firstName")
                    .setValue(firstName.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("firstNameUpdated::", "Success")
                            Toast.makeText(
                                context, "Successfully updated first name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            if (!lastName.text.isNullOrBlank()) {
                databaseRef.child("lastName")
                    .setValue(lastName.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("lastNameUpdated::", "Success")
                            Toast.makeText(
                                context, "Successfully updated last name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            if (!email.text.isNullOrBlank()) {
                databaseRef.child("email")
                    .setValue(email.text.toString())
                user?.updateEmail(email.text.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("emailUpdated::", "Success")
                            Toast.makeText(
                                context, "Successfully updated email",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }

            if (!newPassword.text.isNullOrBlank()) {
                user?.updatePassword(newPassword.text.toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("passwordUpdated::", "Success")
                        }
                    }
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}