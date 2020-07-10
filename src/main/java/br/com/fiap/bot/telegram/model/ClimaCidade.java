package br.com.fiap.bot.telegram.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ClimaCidade extends Cidade {

	@JsonProperty("data")
	private Dados dadosClima;

	@JsonGetter("data")
	public Dados getDadosClima() {
		return dadosClima;
	}
	
	@JsonSetter("data")
	public void setDadosClima(Dados dadosClima) {
		this.dadosClima = dadosClima;
	}

}
