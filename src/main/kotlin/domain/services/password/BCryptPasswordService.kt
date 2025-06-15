package com.br.domain.services.password

import at.favre.lib.crypto.bcrypt.BCrypt

class BCryptPasswordService {

    fun verifyPassword(password: CharArray, hashedPassword: String): Boolean {
        return BCrypt.verifyer().verify(password, hashedPassword).verified
    }

    fun hashedPassword(const: Int, password: String): String {
        return BCrypt.withDefaults().hash(const, password.toCharArray()).toString()
    }
}