// NavigationUtils.kt
package com.hm.picplz.navigation

import android.os.Bundle
import androidx.navigation.NavController

fun NavController.navigateWithBundle(route: String, args: Bundle) {
    val nodeId = this.graph.findNode(route)?.id
    if (nodeId != null) {
        this.navigate(nodeId, args)
    } else {
        this.navigate(route)
    }
}
