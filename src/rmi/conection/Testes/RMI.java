

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalTime;

public interface RMI extends Remote {

    int diferencaHorararioAtual(LocalTime server) throws RemoteException;
    void acertoDeHorararioClient(int referencia) throws RemoteException;

}
