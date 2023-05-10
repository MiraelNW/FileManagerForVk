package com.miraeldev.filemanagerforvk.presentation.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.miraeldev.filemanagerforvk.FileManagerForVkApp
import com.miraeldev.filemanagerforvk.utils.PermissionUtils
import com.miraeldev.filemanagerforvk.R
import com.miraeldev.filemanagerforvk.databinding.ActivityMainBinding
import com.miraeldev.filemanagerforvk.utils.ViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val component by lazy {
        (application as FileManagerForVkApp).component
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
        if (!PermissionUtils.hasPermissions(this@MainActivity)) {
            PermissionUtils.requestPermissions(this@MainActivity, PERMISSION_STORAGE)
        }
        if (PermissionUtils.hasPermissions(this)) {
            val path = Environment.getExternalStorageDirectory().path
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, FilesListFragment.newInstance(path))
                .addToBackStack(null)
                .commit()
        } else {
            makeToast("Разрешение не предоставлено")
            finish()
        }
    }

    private fun Activity.makeToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PERMISSION_STORAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (PermissionUtils.hasPermissions(this)) {
                    val path = Environment.getExternalStorageDirectory().path
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, FilesListFragment.newInstance(path))
                        .addToBackStack(null)
                        .commit()
                } else {
                    makeToast("Разрешение не предоставлено")
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_STORAGE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val path = Environment.getExternalStorageDirectory().path
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, FilesListFragment.newInstance(path))
                    .addToBackStack(null)
                    .commit()
            } else {
                makeToast("Разрешение не предоставлено")
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val PERMISSION_STORAGE = 101
    }
}