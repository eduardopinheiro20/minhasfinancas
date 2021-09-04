package com.pinheiro.minhasfinancas.exception;

public class ErroAutenticacaoException extends RuntimeException {

    public ErroAutenticacaoException(final String msg) {
        super(msg);
    }
}
