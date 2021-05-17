package edu.ufp.inf.sd.rmi._02_calculator.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CalculatorImpl extends UnicastRemoteObject implements CalculatorRI {


    public CalculatorImpl() throws RemoteException {
        super();
    }


    @Override
    public double add(double a, double b) throws RemoteException {
        double soma = a + b;
        Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Soma = {0}", soma);
        return soma;
    }

    @Override
    public double add(ArrayList<Double> list) throws RemoteException {
        return 0;
    }

    @Override
    public double div(double a, double b) throws RemoteException {
        if(b == 0){
            throw new RemoteArithmeticException("Erro divisao por 0");
        }else {
            double div = a / b;
            Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Divisao = {0}", div);

            return div;
        }
    }
}
