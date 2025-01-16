package com.example.apifix.service;

import com.example.apifix.fixclient.FixClient;
import com.example.apifix.model.Fix;
import com.example.apifix.repository.FixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FixService {

    @Autowired
    private FixRepository fixRepository;

    @Autowired
    private FixClient fixClient;

    public List<Fix> getAllFixes() { return fixRepository.findAll(); }

    public Fix saveFix(Fix fix) { return fixRepository.save(fix); }

    public void sendFixMessage(double price, int qty, String symbol, String CID) { fixClient.sendMessage(price, qty, symbol, CID); }
}
