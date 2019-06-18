package it.polito.tdp.ufo.model;

import java.time.LocalDateTime;
import java.util.List;

public class State {

	private String state_code;
	private List<Sighting> sightings;

	public State(String state_code, List<Sighting> sightings) {
		this.state_code = state_code;
		this.sightings = sightings;
	}

	public String getState_code() {
		return state_code;
	}

	public List<Sighting> getSightings() {
		return sightings;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state_code == null) ? 0 : state_code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State other = (State) obj;
		if (state_code == null) {
			if (other.state_code != null)
				return false;
		} else if (!state_code.equals(other.state_code))
			return false;
		return true;
	}

	public void setState_code(String state_code) {
		this.state_code = state_code;
	}

	public void setSightings(List<Sighting> sightings) {
		this.sightings = sightings;
	}

	@Override
	public String toString() {
		return "State [state_code=" + state_code + ", sightings=" + sightings.size() + "]";
	}

	public LocalDateTime getUltimoAvvistamentoAnno(int year) {
		LocalDateTime ultimo = LocalDateTime.MIN;
		for (Sighting s : this.sightings) {
			if (s.getDatetime().compareTo(ultimo) > 0 && s.getDatetime().getYear() == year) {
				ultimo = s.getDatetime();
			}
		}
		return ultimo;
	}

}
