import java.net.MalformedURLException;
import java.time.LocalTime;

public class Main {


    public static void main(String[] args) throws MalformedURLException {

        LocalTime horaAtual = LocalTime.of(2, 20);

        horaAtual = horaAtual.plusHours(Math.round(2.2));
        horaAtual = horaAtual.minusMinutes(10);
        System.out.println(Math.round(-3));

    }
}