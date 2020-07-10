package br.com.fiap.bot.telegram.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.fiap.bot.telegram.model.Cidade;
import br.com.fiap.bot.telegram.model.Clima;
import br.com.fiap.bot.telegram.model.ClimaCidade;

public class ClimatempoService {

	private static final String TOKEN_CLIMATEMPO = "bbcaddef1e33822fee0643220e905c2b";
	private static final String ENDLINE = System.getProperty("line.separator");

	private static RestTemplate restTemplate = new RestTemplate();

	private static void cadastraCidadeToken(String idCidade) {
		String url = "http://apiadvisor.climatempo.com.br/api-manager/user-token/" + TOKEN_CLIMATEMPO + "/locales";

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);

		Map<String, List<String>> req_payload = new LinkedMultiValueMap<String, String>();
		req_payload.put("localeId[]", List.of(idCidade));

		HttpEntity<Map<String, List<String>>> request = new HttpEntity<Map<String, List<String>>>(req_payload, headers);
		restTemplate.exchange(url, HttpMethod.PUT, request, Object.class);
	}

	private static String getCidadeId(String cidade) throws Exception {

		Cidade[] cidadeObj = restTemplate.getForObject("http://apiadvisor.climatempo.com.br/api/v1/locale/city?name="
				+ cidade.toLowerCase() + "&token=" + TOKEN_CLIMATEMPO, Cidade[].class);

		final String cidadePesquisar = cidade;
		Optional<Cidade> optionalCidade = Arrays.asList(cidadeObj).stream()
				.filter(c -> c.getName().equalsIgnoreCase(cidadePesquisar)).findFirst();

		if (optionalCidade.isPresent()) {
			return optionalCidade.get().getId();
		} else {
			throw new Exception("Cidade não encontrada");
		}

	}

	private static ClimaCidade obtemDadosClima(String cidade) throws Exception {

		String cidadeId = getCidadeId(cidade);
		cadastraCidadeToken(cidadeId);

		ClimaCidade climaAtual = restTemplate.getForObject("http://apiadvisor.climatempo.com.br/api/v1/weather/locale/"
				+ cidadeId + "/current?token=" + TOKEN_CLIMATEMPO, ClimaCidade.class);

		return climaAtual;
	}

	public static String obtemDadosClimaFormatado(String cidade) throws Exception {
		if (cidade.equalsIgnoreCase("Brasil")) {
			return restTemplate.getForObject(
					"http://apiadvisor.climatempo.com.br/api/v1/anl/synoptic/locale/BR?token=" + TOKEN_CLIMATEMPO,
					Clima[].class)[0].getText();
		}

		ClimaCidade climaAtual = obtemDadosClima(cidade);
		String clima = "Cidade: " + climaAtual.getName() + ENDLINE + "Temperatura: "
				+ climaAtual.getDadosClima().getTemperature() + ENDLINE + "Sensação: "
				+ climaAtual.getDadosClima().getSensation() + ENDLINE + climaAtual.getDadosClima().getCondition();

		return clima;

	}

}
