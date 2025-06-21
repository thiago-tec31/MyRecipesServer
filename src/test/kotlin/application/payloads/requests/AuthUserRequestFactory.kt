package application.payloads.requests

import com.br.application.payloads.requests.AuthUserRequest

class AuthUserRequestFactory {
    fun create(email: String, password: String) =
        AuthUserRequest(email, password)
}