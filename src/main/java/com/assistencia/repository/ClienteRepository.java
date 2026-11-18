// Define o pacote dos repositórios da aplicação
package com.assistencia.repository;

// Importa a entidade Cliente
import com.assistencia.model.Cliente;
// Importa classes e anotações do Spring Data JPA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// Importa a interface List para retorno dos resultados da pesquisa
import java.util.List;

// Marca a interface como um componente de repositório gerido pelo Spring
@Repository
// Estende JpaRepository para a entidade Cliente com chave primária do tipo Long
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // RN05: Busca de clientes por termo (nome, telefone ou email)
    // Consulta JPQL que verifica se o texto pesquisado (:termo) consta no nome, no email ou no telefone;
    // utiliza LOWER() no nome e no email para tornar a pesquisa insensível a maiúsculas e minúsculas,
    // e verifica IS NOT NULL nos campos opcionais para evitar falhas na comparação
    @Query("SELECT c FROM Cliente c WHERE " +
           "LOWER(c.nome) LIKE LOWER(CONCAT('%', :termo, '%')) OR " +
           "(c.email IS NOT NULL AND LOWER(c.email) LIKE LOWER(CONCAT('%', :termo, '%'))) OR " +
           "(c.telefone IS NOT NULL AND c.telefone LIKE CONCAT('%', :termo, '%'))")
    List<Cliente> buscarPorTermo(@Param("termo") String termo);

    // Método de consulta derivada (Query Method): o Spring Data gera automaticamente o SQL
    // equivalente a "WHERE LOWER(nome) LIKE LOWER('%nome%')" com base no nome do método
    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}