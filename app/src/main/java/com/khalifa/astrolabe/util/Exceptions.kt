package com.khalifa.astrolabe.util

import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.RuntimeException

/**
 * @author Ahmad Khalifa
 */

class NoInternetConnectionException : RuntimeException("Unable to connect to the internet")

class FragmentNotAttachedException : IllegalStateException("Fragment is not attached to activity")

class InvalidGetCapabilitiesUrl : IllegalArgumentException("Unable to extract WMS endpoint")
