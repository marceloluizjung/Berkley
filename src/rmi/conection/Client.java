package rmi.conection;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

public class Client extends UnicastRemoteObject implements RMI {

    //Atributos
    private static LocalTime horaAtual;
    private static int id = 0;

    protected Client() throws RemoteException, MalformedURLException {
    }

    @Override
    public int diferencaHorararioAtual(LocalTime server) throws RemoteException {
        int minutosServer = (server.getHour() * 60) + server.getMinute();
        int minutosCliente = (horaAtual.getHour() * 60) + horaAtual.getMinute();

        System.out.println("A diferença é: " + (minutosCliente - minutosServer));
        return minutosCliente - minutosServer;
    }

    @Override
    public void acertoDeHorararioClient(int diferenca) throws RemoteException {
        System.out.println("Diferença cliente: " + diferenca);
        LocalTime acerto = LocalTime.MIN.plus(Duration.ofMinutes(Math.abs(diferenca)));

        if (diferenca < 0) {
            horaAtual = horaAtual.minusHours(acerto.getHour());
            horaAtual = horaAtual.minusMinutes(acerto.getMinute());
        } else {
            horaAtual = horaAtual.plusHours(acerto.getHour());
            horaAtual = horaAtual.plusMinutes(acerto.getMinute());
        }
        System.out.println("Horario ajustado cliente: " + horaAtual);
    }

    public static void main(String[] args) {
        Random random = new Random();
        horaAtual = LocalTime.of(random.nextInt(24), random.nextInt(60));
        System.out.println("Horario atual Client: " + horaAtual);
        try {
            //Setar ID manualmente
            Naming.rebind("//localhost/client/" + id, new Client());
            System.err.println("Client ready");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
