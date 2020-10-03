package com.taskagile.doman.common.security;

public interface PasswordEncryptor {
    /**
     * Encrypt a raw password
     * @param rawPassword
     * @return
     */
    String encrypt(String rawPassword);

}
