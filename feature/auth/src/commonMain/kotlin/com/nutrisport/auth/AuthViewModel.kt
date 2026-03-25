package com.nutrisport.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrisport.data.domain.CustomerRepository
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

/**
 * ViewModel responsible for handling authentication-related operations.
 *
 * This ViewModel acts as a bridge between the UI layer and the [CustomerRepository],
 * specifically for creating a customer record after successful authentication.
 *
 * Responsibilities:
 * - Delegates customer creation to [CustomerRepository].
 * - Executes operations on a background thread using [Dispatchers.IO].
 * - Uses [viewModelScope] to ensure lifecycle-aware coroutine execution.
 *
 * Flow:
 * 1. Receives authenticated [FirebaseUser] from the UI layer.
 * 2. Calls [CustomerRepository.createCustomer].
 * 3. Propagates success or error callbacks back to the UI.
 *
 * @property customerRepository Repository responsible for handling customer-related data operations.
 *
 * @see CustomerRepository
 * @see FirebaseUser
 */
class AuthViewModel(
    private val customerRepository: CustomerRepository
) : ViewModel() {

    /**
     * Creates a customer record in the data source using the provided [FirebaseUser].
     *
     * This function runs on the IO dispatcher and delegates the actual implementation
     * to [CustomerRepository.createCustomer].
     *
     * @param user The authenticated Firebase user. Can be null if authentication failed.
     * @param onSuccess Callback invoked when customer creation is successful.
     * @param onError Callback invoked when an error occurs, providing an error message.
     */
    fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) = viewModelScope.launch(Dispatchers.IO) {
        customerRepository.createCustomer(
            user = user,
            onSuccess = onSuccess,
            onError = onError
        )
    }
}