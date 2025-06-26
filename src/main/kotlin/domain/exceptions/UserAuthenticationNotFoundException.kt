package com.br.domain.exceptions

class UserAuthenticationNotFoundException(
    override val message: String?
) : RuntimeException()

class QrCodeServiceException(
    override val message: String?
): RuntimeException()