package br.com.fiap.bot.telegram.util;

import java.util.Arrays;
import java.util.List;

public class Filtro {

    private static List<String> listaComando = Arrays.asList("help", "start","clima","data","cardapio",
                "frango","carne","cocacola","agua","comanda","entrega","retirar");

    public static String filtroTexto(String texto) {
        String retorno = "/default";
        for (String comando : listaComando) {
            if (texto.matches(".*(?i)/"+ comando + ".*")) {
                retorno = texto.replaceAll(".*(?i)/"+ comando + ".*", "/" + comando);
                break;
            }
        }
        return retorno;
    }
}
