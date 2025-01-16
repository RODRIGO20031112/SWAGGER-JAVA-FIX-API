package com.example.apifix.fixclient;

import quickfix.Application;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.field.OrdStatus;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

public class FixClientApplication implements Application {

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

    @Override
    public void fromAdmin(Message message, SessionID sessionID) {
        //System.out.println("Received Admin message: " + message);
    }

    @Override
    public void toApp(Message message, SessionID sessionID) {
        //System.out.println("Sending App message: " + message);
    }

    @Override
    public void fromApp(Message message, SessionID sessionID) throws quickfix.FieldNotFound {
        //System.out.println("Received App message: " + message);
        if (message instanceof ExecutionReport) {
            ExecutionReport executionReport = (ExecutionReport) message;
            if (executionReport.getOrdStatus().getValue() == OrdStatus.NEW) {
                //System.out.println("New Order Execution Report: " + executionReport);
            }
        }
    }
}
