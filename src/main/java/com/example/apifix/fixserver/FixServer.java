package com.example.apifix.fixserver;

import com.example.apifix.model.Fix;
import com.example.apifix.service.FixService;
import org.springframework.beans.factory.annotation.Autowired;
import quickfix.*;

import org.springframework.stereotype.Component;
import quickfix.field.Symbol;

import java.time.LocalDateTime;

@Component
public class FixServer extends MessageCracker implements Application {

    @Override
    public void onCreate(SessionID sessionID) {
        //System.out.println("Session created: " + sessionID);
    }

    @Override
    public void onLogon(SessionID sessionID) {
        //System.out.println("Logon: " + sessionID);
    }

    @Override
    public void onLogout(SessionID sessionID) {
        //System.out.println("Logout: " + sessionID);
    }

    @Override
    public void toAdmin(Message message, SessionID sessionID) {
        //System.out.println("Sending Admin message: " + message);
    }

    @Autowired
    private FixService fixService;

    @Override
    public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound {
        System.out.println("Received Admin message: " + message);

        // Criar e preencher a instância de Fix
    }

    @Override
    public void toApp(Message message, SessionID sessionID) {
        System.out.println("Sending App message: " + message);
        try {
            // Log da sessão para verificação
            //System.out.println("Sending message to session: " + sessionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws FieldNotFound {

        try {
            System.out.println("Received message from session: " + sessionID);
            System.out.println("Received message from client: " + message);

            // Usando o número do campo diretamente
            String symbol = message.getString(55);
            String price = message.getString(44);
            String qty = message.getString(38);

            // Exibindo o valor extraído
            System.out.println("Valor da tag 55 (Symbol): " + symbol);
            System.out.println("Valor da tag 44 (Price): " + price);
            System.out.println("Valor da tag 38 (Qty): " + qty);

            Fix fix = new Fix();

            // Preenchendo os campos com os dados da mensagem
            fix.setBruteFixMsg(message.toString());
            fix.setBeginString(message.getHeader().getString(8));
            fix.setBodyLength(message.getHeader().getInt(9));
            fix.setMsgType(message.getHeader().getString(35));
            fix.setSenderCompID(message.getHeader().getString(49));
            fix.setTargetCompID(message.getHeader().getString(56));
            fix.setSendingTime(message.getHeader().getUtcTimeStamp(52));
            fix.setPrice(message.getDouble(44));
            fix.setSymbol(message.getString(55));
            fix.setOrderQty(message.getInt(38));
            fix.setClOrdID(message.getString(11));
            fix.setTransactTime(message.getUtcTimeStamp(60));
            fix.setOrdType(message.getInt(40));
            fix.setSeqNum(message.getHeader().getInt(34));
            fix.setChecksum(message.getTrailer().getString(10));
            fix.setSide(message.getInt(54));

            if (fix != null && !symbol.isEmpty()) {
                fixService.saveFix(fix);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startServer(SessionSettings settings) {
        try {
            // Inicializa o servidor FIX
            FileStoreFactory storeFactory = new FileStoreFactory(settings);
            FileLogFactory logFactory = new FileLogFactory(settings);
            DefaultMessageFactory messageFactory = new DefaultMessageFactory(); // Fábrica de mensagens FIX

            // Corrigido: agora estamos usando o MessageFactory correto
            SocketAcceptor acceptor = new SocketAcceptor(this, storeFactory, settings, logFactory, messageFactory);
            acceptor.start();
            //System.out.println("FIX Server started...");
        } catch (ConfigError e) {
            e.printStackTrace();
        }
    }
}