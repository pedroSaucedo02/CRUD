// Define o pacote da camada de acesso a dados (repositórios)
package com.assistencia.repository;

// Importa a entidade Agendamento que será gerida por este repositório
import com.assistencia.model.Agendamento;
// Importa a interface base do Spring Data JPA que fornece métodos CRUD prontos (save, findById, delete, etc.)
import org.springframework.data.jpa.repository.JpaRepository;
// Importa a anotação @Query para escrever consultas personalizadas em JPQL
import org.springframework.data.jpa.repository.Query;
// Indica ao Spring que esta interface é um componente de persistência (Bean)
import org.springframework.stereotype.Repository;

// Importa a interface List para retornar coleções de agendamentos
import java.util.List;

// Regista a interface como um repositório Spring Data
@Repository
// Estende JpaRepository especificando a entidade (Agendamento) e o tipo da sua chave primária (Long)
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // RN06: Listagem ordenada por data de agendamento exibindo todas as informações de cliente e equipamento
    // Consulta JPQL que carrega os agendamentos com junção imediata (LEFT JOIN FETCH) de cliente e equipamento,
    // evitando o problema de múltiplas consultas (N+1) e ordenando da data mais antiga para a mais recente (ASC)
    @Query("SELECT a FROM Agendamento a " +
           "LEFT JOIN FETCH a.cliente c " +
           "LEFT JOIN FETCH a.equipamento e " +
           "ORDER BY a.dataAgendamento ASC")
    List<Agendamento> findAllOrderByDataAgendamentoAsc();
}