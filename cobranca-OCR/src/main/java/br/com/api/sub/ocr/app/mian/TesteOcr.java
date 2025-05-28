package br.com.api.sub.ocr.app.mian;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TesteOcr {

	public static void main(String[] args) {

		String pdfPath = "C:\\OCR\\pdf\\20000575 - KP37271.pdf"; // Altere aqui
		String pythonScriptPath = "C:\\OCR\\script-pytohn\\script-extrair-tabela-pdf.py"; // Altere aqui

		try {
			ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, pdfPath);
			pb.redirectErrorStream(true); // Captura erros junto com saída padrão

			Process process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			String line;
			System.out.println("📄 Tabelas extraídas:");
			while ((line = reader.readLine()) != null) {
				System.out.println(line.replace(",", ";"));
			}

			int exitCode = process.waitFor();
			if (exitCode != 0) {
				System.out.println("⚠️ Ocorreu um erro ao executar o script Python.");
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
