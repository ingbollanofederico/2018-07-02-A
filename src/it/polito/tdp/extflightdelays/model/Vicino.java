package it.polito.tdp.extflightdelays.model;

public class Vicino implements Comparable<Vicino> {
	
	private Airport vicino;
	private Double distanza;
	public Vicino(Airport vicino, Double distanza) {
		super();
		this.vicino = vicino;
		this.distanza = distanza;
	}
	public Airport getVicino() {
		return vicino;
	}
	public void setVicino(Airport vicino) {
		this.vicino = vicino;
	}
	public Double getDistanza() {
		return distanza;
	}
	public void setDistanza(Double distanza) {
		this.distanza = distanza;
	}
	@Override
	public String toString() {
		return vicino +" "+distanza;
	}
	@Override
	public int compareTo(Vicino o) {
		return -(this.distanza.compareTo(o.getDistanza()));
	}
	
	

}
