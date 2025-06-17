package com.br.util

enum class ErrorCodes(val message: String) {

    //User
    INCORRECT_PASSWORD("Senha incorreta"),
    USER_EMAIL_NOT_FOUND("Nenhum usuário com esse e-mail encontrado"),
    EMAIL_ALREADY_USED("E-mail já utilizado"),
    REGISTRATION_ERROR("Erro ao fazer o cadastro"),
    USER_NOT_FOUND("Usuario não encontrado"),
    USER_NOT_LOGGED_IN("Não foi possível obter o usuário logado"),


    //Recipe
    RECIPE_REGISTRATION_ERROR("Erro ao cadastrar receita"),
    RECIPE_DELETION_DENIED("Voce não pode deletar essa receita"),
    RECIPE_DELETION_ERROR("Erro ao deletar receita"),
    RECIPE_UPDATE_DENIED("Voce não pode atualizar essa receita"),
    RECIPE_UPDATE_ERROR("Erro ao atualizar receita"),
    NO_RECIPE_FOUND("nenhuma receita encontrada!"),

    //UserConnections
    CONNECTION_ERROR("Erro ao se conectar, faça o processo novamente"),
    DELETION_QRCODE_ERROR("Erro ao deletar qrcode"),
    CONNECTION_REFUSED("Usuario recusou a conexão"),
    CONNECTION_REMOVE_ERROR("Erro ao remover conexão!"),
    USER_NOT_EXIST_TO_DISCONNECT("Não existe esse usuario para desconectar"),

    //Qrcode
    CONNECTION_DENIED("Voce não pode realizar essa conexão. Cancele a operação!"),
    EXISTING_CONNECTION("Voce já possui uma conexão com esse usuario"),
    CODE_NOT_FOUND("Codigo não encontrado, tente novamente!"),
    QR_CODE_GENERATION_FAILED("Erro ao gerar o QR Code"),


    //General
    UNKNOWN_ERROR("Um erro ocorreu."),
    INVALID_TOKEN("Token inválido"),
    TOKEN_GENERATION_ERROR("Erro ao gerar o token"),
    SESSION_TERMINATION_ERROR("Erro ao fechar a sessão e remover a conexão"),
    INVALID_PARAMETERS("Parâmetros inválidos. Certifique-se de fornecer valores válidos para todos os parâmetros."),
    INVALID_REQUEST_ERROR("Requisição inválida. Por favor, verifique os dados enviados."),
    OBJECT_ID_ERROR("O id fornecido não é um ObjectId válido. Deve ser uma string hexadecimal de 24 caracteres."),

    NAME_REQUIRED("O nome deve ser informado"),
    EMAIL_REQUIRED("O e-mail deve ser informado"),
    INVALID_EMAIL("O email é invalido."),
    PASSWORD_REQUIRED("O senha deve ser informada"),
    PASSWORD_TOO_SHORT("A senha deve conter no minuimo 6 caracteres"),
    PHONE_REQUIRED("O telefone deve ser informado"),
    INVALID_PHONE_FORMAT("O telefone deve estar no formato XX X XXXX-XXXX"),

    USER_ID_NOT_FOUND("Id do usuario logado não encontrado"),
    TITLE_REQUIRED("O titulo deve ser informado"),
    CATEGORY_REQUIRED("A categoria deve ser informado"),
    PREPARATION_TIME_REQUIRED("O tempo de preparo deve ser informado"),
    PREPARATION_MODE_REQUIRED("O modo de preparo deve ser informado"),
    INGREDIENTS_REQUIRED("Forneça os ingredientes da receita"),

    DATABASE_ERROR("Um erro no banco de dados ocorreu. Tente novamente e verifique seus parâmetros."),

}