package com.example.apifix.controller;

import com.example.apifix.model.Fix;
import com.example.apifix.service.FixService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fix")
public class FixController {

    @Autowired
    private FixService fixService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllMessages() {
        try {
            List<Fix> fixMessages = fixService.getAllFixes();

            if (fixMessages.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(fixMessages);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao obter as mensagens.");
        }
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendFixMessage(
            @RequestParam double price,
            @RequestParam int qty,
            @RequestParam String symbol,
            @RequestParam String CID) {
        try {
            fixService.sendFixMessage(price, qty, symbol, CID);
            return ResponseEntity.ok("Mensagem FIX enviada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao enviar a mensagem FIX: " + e.getMessage());
        }
    }

}
