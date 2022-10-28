package com.malikazizali.challengechapter6.view

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.malikazizali.challengechapter6.R
import com.malikazizali.challengechapter6.databinding.FragmentLoginBinding
import com.malikazizali.challengechapter6.model.SavedPreference
import com.malikazizali.challengechapter6.viewmodel.UserViewModel

class LoginFragment : Fragment() {
    lateinit var binding : FragmentLoginBinding
    lateinit var userViewModel : UserViewModel
    lateinit var username : String
    lateinit var password : String
    lateinit var mGoogleSignInClient: GoogleSignInClient
    val Req_Code:Int = 1
    var firebaseAuth = FirebaseAuth.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginProgressBar.visibility = View.GONE
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // Configure Google Sign In inside onCreate method
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        // getting the value of gso inside the GoogleSigninClient
        mGoogleSignInClient=GoogleSignIn.getClient(requireActivity(),gso)
        // initialize the firebaseAuth variable
        firebaseAuth= FirebaseAuth.getInstance()

        userViewModel.dataUser.observe(requireActivity()) {
            username = it.username
            password = it.password
        }

        binding.btnLogin.setOnClickListener {
            binding.loginProgressBar.visibility = View.VISIBLE
            val usernameInput = binding.etUsername.text.toString()
            val passwordInput = binding.etPassword.text.toString()
            if (usernameInput!=""&&passwordInput!="") {
                if(usernameInput == username && passwordInput == password){
                    userViewModel.editSession("true")
                    Toast.makeText(requireActivity(), context?.getString(R.string.success_login), Toast.LENGTH_SHORT).show()
                    binding.loginProgressBar.visibility = View.GONE
                    Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
                }else{
                    binding.loginProgressBar.visibility = View.GONE
                    Toast.makeText(requireActivity(), context?.getString(R.string.failed_login), Toast.LENGTH_SHORT).show()
                }
            }
            else{
                binding.loginProgressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), context?.getString(R.string.empty_login_input), Toast.LENGTH_SHORT).show()
            }
            binding.loginProgressBar.visibility = View.GONE
        }

        binding.btnRegister.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnLoginGoogle.setOnClickListener {
            signInGoogle()
        }

    }

    // signInGoogle() function
    private  fun signInGoogle(){

        val signInIntent:Intent=mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent,Req_Code)
    }
    // onActivityResult() function : this is where we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Req_Code){
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }
    // handleResult() function -  this is where we update the UI after Google signin takes place
    private fun handleResult(completedTask: Task<GoogleSignInAccount>){
        try {
            val account: GoogleSignInAccount? =completedTask.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e:ApiException){
            Toast.makeText(requireActivity(),e.toString(),Toast.LENGTH_SHORT).show()
        }
    }
    // UpdateUI() function - this is where we specify what UI updation are needed after google signin has taken place.
    private fun UpdateUI(account: GoogleSignInAccount){
        val credential= GoogleAuthProvider.getCredential(account.idToken,null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful) {
                SavedPreference.setEmail(requireActivity(),account.email.toString())
                SavedPreference.setUsername(requireActivity(),account.displayName.toString())
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }
    override fun onStart() {
        super.onStart()
        if(GoogleSignIn.getLastSignedInAccount(requireActivity())!=null){
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }
}