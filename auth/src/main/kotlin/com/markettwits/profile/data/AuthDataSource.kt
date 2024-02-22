package com.markettwits.profile.data

import com.markettwits.cloud.model.auth.sign_in.response.User
import com.markettwits.cloud.model.profile.update.ChangeProfileInfoRequest
import com.markettwits.profile.presentation.sign_in.SignInUiState
import com.markettwits.profile.presentation.sign_up.domain.SignUpStatement
import kotlinx.coroutines.flow.Flow

interface AuthDataSource {

    /**
     * Registers a new user with the provided sign-up statement.
     *
     * @param signUpStatement the sign-up statement containing user details
     * @return a Result object indicating the success or failure of the
     *     registration process
     */
    suspend fun register(signUpStatement: SignUpStatement) : Result<String>

    /**
     * Attempts to log in a user with the provided email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return a SignInUiState object representing the UI state after the login
     *     attempt
     */
    suspend fun logIn(email : String, password : String) : SignInUiState

    /**
     * Updates the password of the current user.
     *
     * @param password the new password to be set
     */
    suspend fun updatePassword(password: String)

    /**
     * Updates the authentication token.
     *
     * @return the updated authentication token
     */
    suspend fun updateToken(): Result<String>

    /**
     * Observes changes in the authenticated user's data.
     *
     * @return a Flow of Result objects containing the user data
     */
    suspend fun observeUser(): Flow<User>

    /**
     * Retrieves the authenticated user's data.
     *
     * @return a Result object containing the user data
     */
    suspend fun user(): Result<User>

    /**
     * Updates the profile information of the authenticated user.
     *
     * @param request the request containing the updated profile information
     * @return a Result object indicating the success or failure of the update
     *     operation
     */
    suspend fun updateUser(request: ChangeProfileInfoRequest): Result<Unit>

    /**
     * Retrieves the authenticated user's data.
     *
     * @return the authenticated user's data
     */
    suspend fun auth(): Result<User>

    /** Clears all locally stored user data. */
    suspend fun clear()
}