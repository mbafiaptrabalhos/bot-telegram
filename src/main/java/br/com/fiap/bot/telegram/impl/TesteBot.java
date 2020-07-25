package br.com.fiap.bot.telegram.impl;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Random;

import br.com.fiap.bot.telegram.util.Filtro;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import br.com.fiap.bot.telegram.service.ClimatempoService;

public class TesteBot {
	private static final String ENDLINE = System.getProperty("line.separator");
	private static final String TOKEN_TELEGRAM = "1136362167:AAGauHGMgJtfX_r5a-ThatAysEm-KrEJtfo";
	private static TelegramBot bot;
	private static int messageOffset = 0;
	static Locale ptBr = new Locale("pt", "BR");

	private static Double VALOR_NOTA = 0.00;
	private static Boolean PEDIDO_FINAL = false;
	private static int PEDIDO_FRANGO = 0;
	private static int PEDIDO_CARNE = 0;
	private static int PEDIDO_COCA = 0;
	private static int PEDIDO_AGUA = 0;
	private static boolean TIME = false;

	public static void main(String[] args) {
		bot = new TelegramBot(TOKEN_TELEGRAM);
		System.out.println("TELEGRAM RUN");
		while (true) {
			GetUpdates getUpdates = new GetUpdates().limit(100).offset(messageOffset);
			GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
			List<Update> updates = updatesResponse.updates();
			updates.stream().forEach(update -> {
				messageOffset = update.updateId() + 1;
				System.out.println(update.updateId() + " Recebeu: " + update.message().text());
				String textoDigitado = update.message().text().toLowerCase();

				// Mensagem de Digitando
				bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));

				switch (Filtro.filtroTexto(textoDigitado)) {
				case "/help":
				case "/start":
					executaJornadaHelpCommand(update);
					break;
				case "/clima":
					TIME = true;
					executaJornadaClimatempo(update);
					break;
				case "/data":
					executaJornadaData(update);
					break;
				case "/cardapio":
					executaJornadaCardapio(update);
					break;
				case "/frango":
					PEDIDO_FRANGO += 1;
					executaJornadaSelecao(update, 20.00);
					break;
				case "/carne":
					PEDIDO_CARNE += 1;
					executaJornadaSelecao(update, 25.00);
					break;
				case "/cocacola":
					PEDIDO_COCA += 1;
					executaJornadaSelecao(update, 5.00);
					break;
				case "/agua":
					PEDIDO_AGUA += 1;
					executaJornadaSelecao(update, 3.00);
					break;
				case "/comanda":
					PEDIDO_FINAL = true;
					executaJornadaComanda(update, VALOR_NOTA);
					break;
				case "/entrega":
					if (PEDIDO_FINAL) {
						// Consultar informações de trânsito
					} else {
						bot.execute(new SendMessage(update.message().chat().id(),
								"Favor encerrar sua comanda antes de solicitar a entrega do pedido."));
					}

					break;
				case "/retirar":
					Random random = new Random();

					if (PEDIDO_FINAL) {
						bot.execute(new SendMessage(update.message().chat().id(), "A FIAP Food agradece a preferência."
								+ ENDLINE + ENDLINE + "Quando chegar no restaurante basta apresentar o código: " +  random.nextInt(100) + "."));
						VALOR_NOTA = 0.00;
					} else {
						bot.execute(new SendMessage(update.message().chat().id(),
								"Favor encerrar sua comanda antes de solicitar a retirada do pedido."));
					}
					break;
				case "/default":
					System.out.println(textoDigitado);
					if (!TIME) {
						bot.execute(new SendMessage(update.message().chat().id(), "Opção não encontrada!"));
					} else {
						TIME = false;
					}
					break;
				}
			});
			updates.clear();
		}

	}

	private static void executaJornadaData(Update update) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
		String textoResposta = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).format(formatter);
		bot.execute(new SendMessage(update.message().chat().id(), textoResposta));
	}

	private static void executaJornadaHelpCommand(Update update) {
		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);

		int hours = c.get(Calendar.HOUR_OF_DAY);
		String periodo = "";

		if (hours >= 1 && hours <= 12) {
			periodo = "Bom dia. ";
		} else if (hours >= 12 && hours <= 18) {
			periodo = "Boa tarde. ";
		} else if (hours >= 21 && hours <= 24) {
			periodo = "Boa noite. ";
		}

		String textoResposta = periodo + "Seja bem vindo ao restaurante Fiap Food!" + ENDLINE + ENDLINE
				+ "Caso queira ver o cardápio digite /cardapio, quando o seu pedido estiver finalizado digite /comanda."
				+ ENDLINE + ENDLINE + "Se precisar de ajuda digite /help." + ENDLINE + ENDLINE
				+ "Se quiser informações sobre o clima, digite /clima.";

		bot.execute(new SendMessage(update.message().chat().id(), textoResposta));
		bot.execute(new SendPhoto(update.message().chat().id(), "https://i.imgur.com/b1dZTyM.png"));
	}

	private static void executaJornadaClimatempo(Update update) {

		String cidade = null;

		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
		bot.execute(new SendMessage(update.message().chat().id(),
				"Digite a cidade desejada para obter um resumo"));
		GetUpdates getUpdates = new GetUpdates().limit(100).offset(messageOffset++);

		while (cidade == null) {
			GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
			List<Update> updates = updatesResponse.updates();
			Optional<Update> findFirst = updates.stream().findFirst();
			if (findFirst.isPresent()) {
				messageOffset = findFirst.get().updateId();
				cidade = findFirst.get().message().text();
				System.out.println("Cidade: " + cidade);
				try {
					bot.execute(new SendMessage(update.message().chat().id(),
							ClimatempoService.obtemDadosClimaFormatado(cidade)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private static void executaJornadaCardapio(Update update) {
		String cardapio = "Clique na opção desejada: " + ENDLINE + ENDLINE + "Comidas: " + ENDLINE
				+ "Frango: 20,00 - /frango" + ENDLINE + "Carne: 25,00 - /carne" + ENDLINE + ENDLINE + "Bebidas: "
				+ ENDLINE + "Coca-Cola: 5,00 - /cocacola" + ENDLINE + "Água: 3,00 - /agua" + ENDLINE + ENDLINE
				+ "Depois que selecionar todos os itens, basta selecionar sua comanda digitando /comanda.";

		bot.execute(new SendMessage(update.message().chat().id(), cardapio));
	}

	private static void executaJornadaSelecao(Update update, double valorPrato) {
		VALOR_NOTA = VALOR_NOTA + valorPrato;
		String valorTotalFormatado = NumberFormat.getCurrencyInstance(ptBr).format(VALOR_NOTA);
		bot.execute(new SendMessage(update.message().chat().id(), "Seu pedido está em " + valorTotalFormatado + ENDLINE
				+ ENDLINE + "Após selecionar tudo que deseja, basta solicitar sua comanda digitando /comanda."));
	}

	private static void executaJornadaComanda(Update update, double valorTotal) {
		String valorTotalFormatado = NumberFormat.getCurrencyInstance(ptBr).format(VALOR_NOTA);
		String pedido = "";

		if (valorTotal <= 0.00) {
			bot.execute(new SendMessage(update.message().chat().id(),
					"Sua comanda ainda esta vazia, deseja ver o cardápio? Se sim, digite /cardapio."));
		} else {
			if (PEDIDO_FRANGO > 0) {
				pedido = "Frango: " + Integer.toString(PEDIDO_FRANGO) + ENDLINE;
			}
			if (PEDIDO_CARNE > 0) {
				pedido = pedido + "Carne:  " + Integer.toString(PEDIDO_CARNE) + ENDLINE;
			}
			if (PEDIDO_AGUA > 0) {
				pedido = pedido + "Água: " + Integer.toString(PEDIDO_AGUA) + ENDLINE;
			}
			if (PEDIDO_COCA > 0) {
				pedido = pedido + "Coca-cola: " + Integer.toString(PEDIDO_COCA) + ENDLINE;
			}

			bot.execute(new SendMessage(update.message().chat().id(), "COMANDA: " + ENDLINE + ENDLINE + pedido + ENDLINE +  "O seu pedido ficou em: " + valorTotalFormatado
					+ ENDLINE + ENDLINE
					+ "Caso o seu pedido seja para entrega, digite /entrega para calcularmos a distância, se for retirar no restaurante digite /retirar."));
		}
	}
}
