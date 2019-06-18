package it.polito.tdp.ufo.model;

public class TestModel {

	public static void main(String[] args) {
		Model model = new Model();
		int anno = 2011;
		String stato = "ak";
		
		model.creaGrafo(anno);
		System.out.println("\nStati raggiungibili: "+model.getReachable(stato).size()+", Elenco: ");
		
		for (State s : model.getReachable(stato)) {
			System.out.println(s);
		}
		System.out.println("\nStati prima e dopo: "+model.getBeforeAfter(stato).size());
		for (State s : model.getBeforeAfter(stato)) {
			System.out.println(s);
		}	
		//System.out.println(model.longestPath(stato));
	}

}
