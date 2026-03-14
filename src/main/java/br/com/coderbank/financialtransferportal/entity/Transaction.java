package br.com.coderbank.financialtransferportal.entity;

import br.com.coderbank.financialtransferportal.enums.TransactionStatus;
import br.com.coderbank.financialtransferportal.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "CB_TRANSACTION")
@Data
public class Transaction {

        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.AUTO)
        private UUID id;

        @Column(name = "type", nullable = false)
        private TransactionType type;

        @Column(name = "amount", nullable = false)
        private BigDecimal amount;

        @Column(name = "source_account")
        private UUID sourceAccount;

        @Column(name = "destination_account")
        private UUID destinationAccount;

        @Column(name = "description")
        private String description;

        @Column(name = "date_time")
        @CreationTimestamp
        private LocalDate dateTime;

        @Column(name = "status")
        private TransactionStatus status;
}
