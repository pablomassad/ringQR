package com.pp.ringqr

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentDeclined:Boolean,
    onDismiss: ()-> Unit,
    onOkClick: ()-> Unit,
    onGoToAppSettingsClick:()->Unit,
    modifier: Modifier= Modifier 
){
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton =  {
            Column(
                modifier= Modifier.fillMaxWidth()
            ) {
                Divider()
                Text(
                    text = if (isPermanentDeclined) {
                        "Grant Permission"
                    }
                    else{
                        "OK"
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentDeclined){
                                 onGoToAppSettingsClick()
                            }
                            else
                            {
                                onOkClick( )
                            }
                        }
                        .padding(16.dp)
                )
            }
        },
        title = {
            Text(
                text="Permission required"
            )
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(isPermanentDeclined = isPermanentDeclined)
            )
        },
        modifier = modifier
    )
}

interface PermissionTextProvider   {
    fun getDescription(isPermanentDeclined: Boolean):String
}

class CameraPermissionTextProvider : PermissionTextProvider{
    override fun getDescription(isPermanentDeclined: Boolean): String {
         return if(isPermanentDeclined){
             "It seems you permanent declined camera permission "+
                     "You can go to the app settings to grant it."
         }
        else{
            "This app needs access to this feature "
         }
    }
}


class LocationPermissionTextProvider : PermissionTextProvider{
    override fun getDescription(isPermanentDeclined: Boolean): String {
        return if(isPermanentDeclined){
            "It seems you permanent declined location permission "+
                    "You can go to the app settings to grant it."
        }
        else{
            "This app needs access to this feature "
        }
    }
}