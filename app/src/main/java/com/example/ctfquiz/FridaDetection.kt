package com.example.ctfquiz

import android.os.Build
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class FridaDetection {
    companion object {
        private const val FRIDA_SO_FILE = "libfrida-gadget.so"
        private const val TAG = "FridaDetection"

        fun detectFrida(): Boolean {
            return isFridaInstalled() || isFridaInRunningProcesses() ||
                    isFridaInClassPath() || isFridaInLinkerNamespaces()
        }

        private fun isFridaInstalled(): Boolean {
            val fridaFile = File("/data/local/tmp/$FRIDA_SO_FILE")
            return fridaFile.exists()
        }

        private fun isFridaInRunningProcesses(): Boolean {
            return try {
                val process = Runtime.getRuntime().exec("ls -l /proc")
                val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
                val processes = bufferedReader.readLines()
                processes.any { it.contains(FRIDA_SO_FILE) }
            } catch (e: IOException) {
                Log.e(TAG, "Error reading running processes", e)
                false
            }
        }

        private fun isFridaInClassPath(): Boolean {
            return try {
                val classLoader = ClassLoader::class.java
                val getClassPathMethod = classLoader.getDeclaredMethod("getClassPath")
                getClassPathMethod.isAccessible = true
                val classPath =
                    getClassPathMethod.invoke(ClassLoader.getSystemClassLoader()) as String
                classPath.contains(FRIDA_SO_FILE)
            } catch (e: Exception) {
                Log.e(TAG, "Error reading class path", e)
                false
            }
        }

        private fun isFridaInLinkerNamespaces(): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                return false
            }
            return try {
                val runtimeClass = Class.forName("dalvik.system.BaseDexClassLoader")
                val getClassLoaderMethod = runtimeClass.getDeclaredMethod("getLdLibraryPath")
                getClassLoaderMethod.isAccessible = true
                val classLoader = getClassLoaderMethod.invoke(runtimeClass.classLoader) as String
                classLoader.contains(FRIDA_SO_FILE)
            } catch (e: Exception) {
                Log.e(TAG, "Error reading linker namespaces", e)
                false
            }
        }
    }
}
