package com.example.apifix.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fix")
@Getter
@Setter
public class Fix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String BruteFixMsg;
    private String beginString;  // 8
    private int bodyLength;      // 9
    private String msgType;      // 35
    private int seqNum;          // 34
    private String senderCompID; // 49
    private LocalDateTime sendingTime; // 52
    private String targetCompID; // 56
    private String clOrdID;      // 11
    private int orderQty;        // 38
    private int ordType;         // 40
    private double price;        // 44
    private int side;            // 54
    private String symbol;       // 55
    private LocalDateTime transactTime; // 60
    private String checksum;        // 10

    // Construtor padrão
    public Fix() {
    }
}
