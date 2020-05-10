package fr.uge.soundroid.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Template for an activity that needs to acquire a dangerous permission
 * @author Vincent_Agullo
 */
open class RequiringPermissionActivity : AppCompatActivity() {

    /** Store here the permission requests with the associated onSuccess and onFailure runnable  */
    private var permissionRequests: HashMap<Int, Array<Runnable>> = HashMap()
    private var nextPermissionRequestCode = 1

    /** Run code that requires a prior permission grant
     *  @param permissionName name of the permission to claim
     *  @param rationale message to display to explain to the user why this permission must be used
     * 	@param onFailure code to execute if the permission is refused
     *  @param onSuccess code to execute if the permission is granted
     */
    fun runWithPermission(permissionName:String, rationale: String?, onFailure:Runnable, onSuccess:Runnable){
        // Is the permission already granted?
        if (ContextCompat.checkSelfPermission(this, permissionName) != PackageManager.PERMISSION_GRANTED){
            // The permission is not granted yet
            // Should we show an explanation?
            if ( rationale != null && ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName)) {
                // show an explanation with a dialog
                val alertDialog: AlertDialog = AlertDialog.Builder(this).create()
                alertDialog.setTitle("Permission information")
                alertDialog.setMessage(rationale)
                alertDialog.setIcon(android.R.drawable.ic_dialog_info)
                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK") { _, _ ->  runWithPermission(permissionName, null, onFailure, onSuccess) }
                alertDialog.show()
            }
            else {
                // use an unique id for the permission request
                val code = nextPermissionRequestCode++
                permissionRequests[code] = listOf(onFailure, onSuccess).toTypedArray()
                // request asynchronously the permission
                ActivityCompat.requestPermissions(this, arrayOf(permissionName),code)
            }
        }
        else {
            onSuccess.run()
        }
    }

    /** This method is called when the user has answered to the permission request  */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val runnable: Array<Runnable>? = permissionRequests[requestCode]
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runnable?.get(1)?.run() // run onSuccess since the permission has been granted
        }
        else runnable?.get(0)?.run()
    }
}