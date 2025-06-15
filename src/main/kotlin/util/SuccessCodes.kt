package com.br.util

enum class SuccessCodes(val message: String) {

    //User
    LOGIN_SUCCESS("Login realizado com sucesso!"),
    REGISTRATION_COMPLETED("Cadastro efetuado com sucesso"),

    //Recipe
    RECIPE_REGISTRATION_SUCCESS("Receita Cadastrada com sucesso"),
    RECIPE_DELETION_SUCCESS("Receita deletada com sucesso"),
    RECIPE_UPDATE_SUCCESS("Receita atualizada com sucesso"),

    //Qrcode
    QR_CODE_SUCCESS("O qrcode foi lido com sucesso"),
    VALIDATION_SUCCESS("Validação feita com sucesso"),

    //UserConnections
    CONNECTION_SUCCESS("Conexão realizada com sucesso!"),
    CONNECTION_REMOVED_SUCCESS("Conexão removida com sucesso!"),

    CONNECTION_CLOSED_SUCCESS("Conexão fechada com sucesso"),
    EXPIRED_QR_CODE("QR Code expirado. Por favor, gere um novo."),

    //General
    VALID_REGISTRATION("Cadastro válido"),
    WAIT_FOR_USER_TO_ACCEPT_CONNECTION ("Aguarde o usuário aceitar a conexão")

}