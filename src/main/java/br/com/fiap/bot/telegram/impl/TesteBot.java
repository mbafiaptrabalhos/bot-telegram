package br.com.fiap.bot.telegram.impl;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import br.com.fiap.bot.telegram.service.ClimatempoService;

public class TesteBot {
	private static final String ENDLINE = System.getProperty("line.separator");
	private static final String TOKEN_TELEGRAM = "1272262215:AAEsDyneOcuI4ZngPqM1iuM-HtvG6s0EZO0";
	private static TelegramBot bot;
	private static int messageOffset = 0;

	public static void main(String[] args) {
		bot = new TelegramBot(TOKEN_TELEGRAM);
		while (true) {
			GetUpdates getUpdates = new GetUpdates().limit(100).offset(messageOffset);
			GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
			List<Update> updates = updatesResponse.updates();
			updates.stream().forEach(update -> {
				messageOffset = update.updateId() + 1;
				System.out.println(update.updateId() + " Recebeu: " + update.message().text());
				String textoDigitado = update.message().text().toLowerCase();

				if (textoDigitado.contains("/help") || textoDigitado.contains("/start")) {
					executaJornadaHelpCommand(update);
				}

				if (textoDigitado.contains("/clima")) {
					executaJornadaClimatempo(update);
				}
				
			

				if (textoDigitado.contains("/data")) {
					executaJornadaData(update);
				}
			});
		}

	}

	private static void executaJornadaData(Update update) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss");
		String textoResposta = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).format(formatter);
		bot.execute(new SendMessage(update.message().chat().id(), textoResposta));
	}

	private static void executaJornadaHelpCommand(Update update) {
		String textoResposta = "/clima - Obtêm informações referente a temperatura atual" + ENDLINE
				+ "/data - Obtêm informações do dia atual" + ENDLINE;

		bot.execute(new SendMessage(update.message().chat().id(), textoResposta));
	}

	private static void executaJornadaClimatempo(Update update) {

		String cidade = null;

		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
		bot.execute(new SendMessage(update.message().chat().id(), "Digite a cidade desejada ou \"Brasil\" para obter um resumo"));
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
}
