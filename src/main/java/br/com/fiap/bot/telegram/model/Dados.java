package br.com.fiap.bot.telegram.model;

public class Dados {

	private String temperature;
	private String wind_direction;
	private String wind_velocity;
	private String humidity;
	private String condition;
	private String pressure;
	private String icon;
	private String sensation;
	private String date;

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	
	public String getWind_direction() {
		return wind_direction;
	}

	public void setWind_direction(String wind_direction) {
		this.wind_direction = wind_direction;
	}

	public String getWind_velocity() {
		return wind_velocity;
	}

	public void setWind_velocity(String wind_velocity) {
		this.wind_velocity = wind_velocity;
	}

	public String getHumidity() {
		return humidity;
	}

	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSensation() {
		return sensation;
	}

	public void setSensation(String sensation) {
		this.sensation = sensation;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
