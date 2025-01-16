package com.example.apifix.fixclient;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import quickfix.*;

import java.io.IOException;
import java.io.InputStream;

@Component
public class FixClient {

    private SocketInitiator initiator;
    private SessionID sessionId;

    public FixClient() {
        // Construtor vazio
    }

    public synchronized void initialize() throws ConfigError, IOException {
        if (initiator == null) {
            // Carregar configurações do arquivo client.cfg
            ClassPathResource resource = new ClassPathResource("client.cfg");
            InputStream configFile = resource.getInputStream();
            SessionSettings settings = new SessionSettings(configFile);

            // Inicializa a fábrica de sessões e a fábrica de logs
            FileStoreFactory storeFactory = new FileStoreFactory(settings);
            FileLogFactory logFactory = new FileLogFactory(settings);
            DefaultMessageFactory messageFactory = new DefaultMessageFactory();

            // Criar a instância do SocketInitiator
            initiator = new SocketInitiator(new FixClientApplication(), storeFactory, settings, logFactory, messageFactory);
            sessionId = new SessionID("FIX.4.4", "CLIENT", "SERVER");

            System.out.println("FIX Client inicializado.");
        }
    }

    public synchronized void startSession() throws ConfigError {
        if (initiator != null && !initiator.isLoggedOn()) {
            initiator.start();
            System.out.println("Sessão FIX iniciada.");
        } else if (initiator != null && initiator.isLoggedOn()) {
            System.out.println("Sessão FIX já está ativa.");
        }
    }

    private boolean waitForSessionActive(int timeoutMillis) {
        int waited = 0;
        int interval = 500; // Intervalo de verificação (em milissegundos)

        while (waited < timeoutMillis) {
            if (Session.doesSessionExist(sessionId) && Session.lookupSession(sessionId).isLoggedOn()) {
                return true;
            }
            try {
                Thread.sleep(interval);
                waited += interval;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }

    public void sendMessage(double price, int qty, String symbol, String CID) {
        try {
            // Inicializar se necessário
            initialize();

            // Iniciar sessão se ainda não estiver ativa
            startSession();

            // Aguarda até que a sessão fique ativa
            if (!waitForSessionActive(5000)) { // Timeout de 5 segundos
                System.out.println("Sessão FIX não ficou ativa dentro do tempo limite.");
                return;
            }

            // Criar a mensagem FIX
            Message message = new Message();
            message.getHeader().setField(new quickfix.field.BeginString("FIX.4.4"));
            message.getHeader().setField(new quickfix.field.MsgType("D"));
            message.getHeader().setField(new quickfix.field.SenderCompID("CLIENT"));
            message.getHeader().setField(new quickfix.field.TargetCompID("SERVER"));
            message.setField(new quickfix.field.ClOrdID(CID));
            message.setField(new quickfix.field.Side(quickfix.field.Side.BUY));
            message.setField(new quickfix.field.OrderQty(qty));
            message.setField(new quickfix.field.Price(price));
            message.setField(new quickfix.field.Symbol(symbol));
            message.setField(new quickfix.field.OrdType(quickfix.field.OrdType.LIMIT));
            message.setField(new quickfix.field.TransactTime());

            // Enviar a mensagem
            Session.sendToTarget(message);
            System.out.println("Mensagem enviada com sucesso!");

        } catch (ConfigError | IOException | SessionNotFound e) {
            e.printStackTrace();
        }
    }
}