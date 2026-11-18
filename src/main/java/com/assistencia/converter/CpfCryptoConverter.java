// Define o pacote dos conversores personalizados de persistência
package com.assistencia.converter;

// Importa as interfaces e anotações do Jakarta Persistence (JPA) para conversão de atributos
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Importa as classes nativas de criptografia do Java (JCA)
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// Importa classes utilitárias para codificação de caracteres (UTF-8) e Base64
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Atende à Regra de Negócio 03 (RN03):
 * O CPF dos clientes deve ser armazenado de forma criptografada no banco de dados.
 * Converte de forma transparente os dados ao persistir e ao recuperar do banco.
 */
// Regista a classe como um conversor JPA aplicável a atributos de entidades
@Converter
// Implementa AttributeConverter<TipoNaEntidade, TipoNaBaseDeDados> (ambos String)
public class CpfCryptoConverter implements AttributeConverter<String, String> {

    // Define o algoritmo de cifra simétrica (AES), o modo de operação (ECB) e o preenchimento (PKCS5Padding)
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    // Chave secreta simétrica de 128 bits (16 caracteres em UTF-8) usada para cifrar e decifrar
    private static final byte[] SECRET_KEY = "MinhaChaveSec123".getBytes(StandardCharsets.UTF_8);

    // Método invocado automaticamente pelo JPA antes de gravar (INSERT/UPDATE) o CPF na base de dados
    @Override
    public String convertToDatabaseColumn(String cpf) {
        // Se o CPF for nulo ou contiver apenas espaços em branco, grava null na coluna
        if (cpf == null || cpf.isBlank()) {
            return null;
        }
        try {
            // Obtém uma instância do motor de cifra configurado para o algoritmo AES
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // Cria a especificação da chave secreta a partir do array de bytes definido
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
            // Inicializa o cifrador em modo de encriptação
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            // Remove espaços excedentes, converte para bytes UTF-8 e aplica a cifra AES
            byte[] encryptedBytes = cipher.doFinal(cpf.trim().getBytes(StandardCharsets.UTF_8));
            // Converte os bytes cifrados para uma String em Base64 para armazenamento seguro em coluna VARCHAR
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            // Interrompe a persistência caso ocorra qualquer falha técnica no processo de encriptação
            throw new IllegalStateException("Falha ao criptografar CPF para persistência", e);
        }
    }

    // Método invocado automaticamente pelo JPA após ler (SELECT) o valor cifrado da base de dados
    @Override
    public String convertToEntityAttribute(String dbData) {
        // Se o valor lido da base de dados for nulo ou estiver em branco, devolve null
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            // Instancia o motor de cifra com o mesmo algoritmo e chave secreta
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY, "AES");
            // Inicializa o cifrador em modo de desencriptação
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // Descodifica a String Base64 da base de dados de volta para o array de bytes cifrados
            byte[] decodedBytes = Base64.getDecoder().decode(dbData.trim());
            // Decifra os bytes e reconstrói o CPF original em texto legível (UTF-8)
            return new String(cipher.doFinal(decodedBytes), StandardCharsets.UTF_8);
        } catch (Exception e) {
            // Se o dado não estiver cifrado (por exemplo, migração legada), retorna o dado original
            // Evita erros na aplicação caso existam registos antigos inseridos manualmente sem cifra
            return dbData;
        }
    }
}