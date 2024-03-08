package com.danamon.robby.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.danamon.robby.R
import com.danamon.robby.databinding.LoginFragmentBinding
import com.danamon.robby.ui.dashboard.DashboardActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: LoginFragmentBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)
        binding.btLoginSignup.text = "Login"
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.observer.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()

                viewModel.observer.value = null

            }
        })

        viewModel.observerSuccess.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it) {
                    val intent = Intent(activity, DashboardActivity::class.java)
                    startActivity(intent)
                }

            }
        })

        viewModel.observerData.observe(viewLifecycleOwner, Observer {
            if (it) {
                if (viewModel.isLogin) {
                    binding.tvTitle.text = "SignUp"
                    binding.btLoginSignup.text = "SignUp"
                    binding.spLoginSignup.visibility = View.VISIBLE
                    binding.textView.text = "Click here to Login"

                } else {
                    //todo test this
                    binding.tvTitle.text = "Login"
                    binding.btLoginSignup.text = "Login"
                    binding.spLoginSignup.visibility = View.GONE
                    binding.textView.text = "Click here to Signup"
                }
                viewModel.isLogin = !viewModel.isLogin
                viewModel.observerData.value = false
            }
        })

    }


}
