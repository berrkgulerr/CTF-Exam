package com.example.ctfquiz

import android.content.Context
import android.content.pm.PackageManager
import com.scottyab.rootbeer.RootBeer
import java.io.*
import java.util.*

class RootDetection {
    private fun isPackageInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun isRootManagementAppInstalled(context: Context): Boolean {
        val packageNames = listOf(
            "eu.chainfire.supersu", "com.noshufou.android.su",
            "com.koushikdutta.superuser", "com.thirdparty.superuser", "com.yellowes.su"
        )
        return packageNames.any { isPackageInstalled(context, it) }
    }

    private fun isSUbinaryPresent(): Boolean {
        return File("/system/bin/su").exists() ||
                File("/system/xbin/su").exists()
    }

    private fun isBusyboxInstalled(): Boolean {
        return File("/system/bin/busybox").exists() ||
                File("/system/xbin/busybox").exists()
    }

    private fun isSystemRW(): Boolean {
        return File("/system/bin/rw-system").exists()
    }

    private fun isSUInPath(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime().exec("which su")
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            bufferedReader.readLine() != null
        } catch (e: IOException) {
            false
        } finally {
            process?.destroy()
        }
    }

    private fun isRootedWithRootBeer(context: Context): Boolean {
        val rootBeer = RootBeer(context)
        return rootBeer.isRooted
    }

    private fun isSuperuserAPKInstalled(): Boolean {
        val file = File("/system/app/Superuser.apk")
        return file.exists()
    }

    private fun isBuildPropValueExist(context: Context): Boolean {
        return try {
            val buildProp = Properties()
            val inputStream = FileInputStream(File(context.filesDir, "build.prop"))
            buildProp.load(inputStream)
            buildProp.getProperty("ro.secure") == "0"
        } catch (e: IOException) {
            false
        }
    }

    private fun isRootedWithMagisk(): Boolean {
        return File("/sbin/magisk").exists() || File("/magisk").exists() ||
                File("/sbin/.core/img/.magisk").exists()
    }

    private fun isRootedWithSuperSU(): Boolean {
        return File("/system/xbin/daemonsu").exists() ||
                File("/system/bin/.ext/.su").exists()
    }

    private fun isRootedWithChainfireSU(): Boolean {
        return File("/system/xbin/su").exists()
    }

    private fun isRootedWithKingRoot(): Boolean {
        return File("/system/bin/kinguser").exists() ||
                File("/system/xbin/kinguser").exists()
    }

    fun isDeviceRooted(context: Context): Boolean {
        return isRootManagementAppInstalled(context) || isSUbinaryPresent() ||
                isBusyboxInstalled() || isSystemRW() || isSUInPath() ||
                isRootedWithRootBeer(context) || isSuperuserAPKInstalled() ||
                isBuildPropValueExist(context) || isRootedWithMagisk() ||
                isRootedWithSuperSU() || isRootedWithChainfireSU() || isRootedWithKingRoot()
    }
}
