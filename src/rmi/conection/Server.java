package rmi.conection;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

public class Server {

    private static LocalTime horaAtual;
    private static HashMap<Integer, String> idURLClientes = new HashMap();
    private static HashMap<Integer, Integer> idDiferencaHorario = new HashMap();
    private static RMI look_up;
    private static int somartorioMinuto;
    private static LocalTime acerto;
    private static int mediaMinutos;

    private static void soma(int minutos) {
        somartorioMinuto += minutos;
    }

    private static void acertarHorarioServer() {
        int size = idURLClientes.size() + 1;
        System.out.println("Lista: " + size);
        mediaMinutos = Math.round(somartorioMinuto / size);
        acerto = LocalTime.MIN.plus(Duration.ofMinutes(Math.abs(mediaMinutos)));

        if (mediaMinutos < 0) {
            horaAtual = horaAtual.minusHours(acerto.getHour());
            horaAtual = horaAtual.minusMinutes(acerto.getMinute());
        } else {
            horaAtual = horaAtual.plusHours(acerto.getHour());
            horaAtual = horaAtual.plusMinutes(acerto.getMinute());
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        //Add clients (id,URL)
        idURLClientes.put(0, "//localhost/client/");

        //Meu horário atual
        Random random = new Random();
        horaAtual = LocalTime.of(random.nextInt(25), random.nextInt(61));
        System.out.println("Horário Server atual: " + horaAtual);

        //Array de clients
        Set set = idURLClientes.entrySet();
        Iterator iterator = set.iterator();

        while (iterator.hasNext()) {
            Map.Entry hash = (Map.Entry) iterator.next();
            look_up = (RMI) Naming.lookup(hash.getValue().toString() + hash.getKey().toString());
            soma(look_up.diferencaHorararioAtual(horaAtual));
            idDiferencaHorario.put((Integer) hash.getKey(), look_up.diferencaHorararioAtual(horaAtual));
        }

        acertarHorarioServer();
        System.out.println("Horário Server ajustado: " + horaAtual);

        set = idURLClientes.entrySet();
        iterator = set.iterator();
        while (iterator.hasNext()) {
            Map.Entry hash = (Map.Entry) iterator.next();
            look_up = (RMI) Naming.lookup(hash.getValue().toString() + hash.getKey().toString());
            look_up.acertoDeHorararioClient((idDiferencaHorario.get(hash.getKey()) * -1) + mediaMinutos);
        }
    }
}
