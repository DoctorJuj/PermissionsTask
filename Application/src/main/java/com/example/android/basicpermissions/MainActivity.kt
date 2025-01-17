/*
* Copyright 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.basicpermissions

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.android.basicpermissions.camera.CameraPreviewActivity
import com.example.android.basicpermissions.util.checkSelfPermissionCompat
import com.example.android.basicpermissions.util.requestPermissionsCompat
import com.example.android.basicpermissions.util.shouldShowRequestPermissionRationaleCompat
import androidx.fragment.app.DialogFragment
import com.example.android.basicpermissions.util.showSnackbar
import com.google.android.material.snackbar.Snackbar

const val PERMISSION_REQUEST_CAMERA = 0

/**
 * Launcher Activity that demonstrates the use of runtime permissions for Android M.
 * This Activity requests permissions to access the camera
 * ([android.Manifest.permission.CAMERA])
 * when the 'Show Camera Preview' button is clicked to start  [CameraPreviewActivity] once
 * the permission has been granted.
 *
 * <p>First, the status of the Camera permission is checked using [ActivityCompat.checkSelfPermission]
 * If it has not been granted ([PackageManager.PERMISSION_GRANTED]), it is requested by
 * calling [ActivityCompat.requestPermissions]. The result of the request is
 * returned to the
 * [android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback], which starts
 * if the permission has been granted.
 *
 * <p>Note that there is no need to check the API level, the support library
 * already takes care of this. Similar helper methods for permissions are also available in
 * ([ActivityCompat],
 * [android.support.v4.content.ContextCompat] and [android.support.v4.app.Fragment]).
 */
class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {

    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.main_layout)

        // Register a listener for the 'Show Camera Preview' button.
        findViewById<Button>(R.id.button_open_camera).setOnClickListener {

            when {
                checkSelfPermissionCompat(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                    startCamera()
                }
                checkSelfPermissionCompat(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED -> {
                    val b = shouldShowRequestPermissionRationaleCompat(Manifest.permission.CAMERA)
                    if (b)  {
                    val text = "для продовження роботи потрібно надати доступ до камери"
                    val duration = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(applicationContext, text, duration)
                    toast.show()
                    }
                    requestPermissionsCompat(arrayOf(Manifest.permission.CAMERA), PERMISSION_REQUEST_CAMERA)
                }

            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if ((grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            startCamera()
        } else {
            val text = "доступ до камери заборонений.продовження неможливе. потрібно надати доступ..."
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraPreviewActivity::class.java)
        startActivity(intent)
    }
}
