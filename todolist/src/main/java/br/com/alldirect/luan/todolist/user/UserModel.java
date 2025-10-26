package br.com.alldirect.luan.todolist.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_users") // agora 'tb_users' é o nome da TABELA
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // generator padrão para UUID no Hibernate 6
    private UUID id;

    @Column(name = "USUARIO", unique = true)
    private String userName;

    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
