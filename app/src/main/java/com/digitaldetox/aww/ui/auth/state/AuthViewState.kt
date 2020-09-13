package com.digitaldetox.aww.ui.auth.state

import android.os.Parcelable
import com.digitaldetox.aww.models.AuthToken
import kotlinx.android.parcel.Parcelize

const val AUTH_VIEW_STATE_BUNDLE_KEY = "com.digitaldetox.openapi.ui.auth.state.AuthViewState"

@Parcelize
data class AuthViewState(
    var registrationFields: RegistrationFields? = null,

    var loginFields: LoginFields? = null,

    var authToken: AuthToken? = null

) : Parcelable


@Parcelize
data class RegistrationFields(
    var registration_email: String? = null,
    var registration_username: String? = null,
    var registration_firstname: String? = null,
    var registration_lastname: String? = null,
    var registration_location: String? = null,
    var registration_aadharcard: String? = null,
    var registration_age: Int? = null,
    var registration_savingtarget: Int? = null,
    var registration_password: String? = null
) : Parcelable {

    class RegistrationError {
        companion object {

            fun mustFillAllFields(): String {
                return "All fields are required."
            }

            fun passwordsDoNotMatch(): String {
                return "Passwords must match."
            }

            fun none(): String {
                return "None"
            }

        }
    }

    fun isValidForRegistration(): String {
        if (registration_email.isNullOrEmpty()
            || registration_username.isNullOrEmpty()
            || registration_age.toString().isNullOrEmpty()
            || registration_savingtarget.toString().isNullOrEmpty()
            || registration_firstname.isNullOrEmpty()
            || registration_lastname.isNullOrEmpty()
            || registration_location.isNullOrEmpty()
            || registration_aadharcard.isNullOrEmpty()
            || registration_password.isNullOrEmpty()
        ) {
            return RegistrationError.mustFillAllFields()
        }

//        if (!registration_password.equals(registration_confirm_password)) {
//            return RegistrationError.passwordsDoNotMatch()
//        }
        return RegistrationError.none()
    }
}

@Parcelize
data class LoginFields(
    var login_email: String? = null,
    var login_password: String? = null
) : Parcelable {
    class LoginError {

        companion object {

            fun mustFillAllFields(): String {
                return "You can't login without an email and password."
            }

            fun none(): String {
                return "None"
            }

        }
    }

    fun isValidForLogin(): String {

        if (login_email.isNullOrEmpty()
            || login_password.isNullOrEmpty()
        ) {

            return LoginError.mustFillAllFields()
        }
        return LoginError.none()
    }

    override fun toString(): String {
        return "LoginState(email=$login_email, password=$login_password)"
    }
}

