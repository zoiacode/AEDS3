package TP1;

import java.security.SecureRandom;

/**
 * Gerador de identificadores únicos em estilo NanoID
 * Gera códigos alfanuméricos aleatórios de tamanho configurável
 */
public class NanoID {
    
    private static final String ALPHABET = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_-";
    private static final SecureRandom random = new SecureRandom();
    private static final int DEFAULT_SIZE = 10;
    
    /**
     * Gera um novo NanoID com tamanho padrão (10 caracteres)
     * @return String com o identificador gerado
     */
    public static String generate() {
        return generate(DEFAULT_SIZE);
    }
    
    /**
     * Gera um novo NanoID com tamanho customizado
     * @param size Tamanho do identificador em caracteres
     * @return String com o identificador gerado
     */
    public static String generate(int size) {
        StringBuilder id = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            id.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return id.toString();
    }
}
