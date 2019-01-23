package com.khalifa.astrolabe.exception

import java.lang.IllegalStateException

/**
 * @author Ahmad Khalifa
 */

class NoInternetConnectionException : Exception("Unable to connect to the internet")

class FragmentNotAttachedException : IllegalStateException("Fragment is not attached to activity")
