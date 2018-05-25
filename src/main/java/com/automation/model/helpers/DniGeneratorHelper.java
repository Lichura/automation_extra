package com.automation.model.helpers;

public class DniGeneratorHelper {
	private static final char[] LETRAS_NIF = { 'T', 'R', 'W', 'A', 'G', 'M', 'Y', 'F', 'P', 'D', 'X', 'B', 'N', 'J', 'Z', 'S', 'Q', 'V', 'H', 'L', 'C', 'K', 'E'};

	public static String generaNif(String seed) {
		if(seed != null) {
			try {
				Integer.parseInt(seed);
			} catch(NumberFormatException ex) {
				return "KO";
			}
		} else {
			seed = "";
		}
		String numeroDNI = String.valueOf(Math.random()).concat(seed);
		String fullDNI = numeroDNI.substring(numeroDNI.length() - 8);

		int dniInt = Integer.valueOf(fullDNI);
		fullDNI = fullDNI + LETRAS_NIF[dniInt % 23];
		return fullDNI;
	}

	public static String calculaLetra(String nif) {
		if(nif.length() != 8) { return ("Nif Inválido"); }
		return generaNif(nif).substring(8);
	}

	public static String validaNif(String nif) {
		if(nif.substring(0, 8).length() == 8) {
			return nif.equalsIgnoreCase(generaNif(nif.substring(0, 8))) ? "OK" : "KO";
		} else {
			return ("Nif Inválido");
		}
	}
}
