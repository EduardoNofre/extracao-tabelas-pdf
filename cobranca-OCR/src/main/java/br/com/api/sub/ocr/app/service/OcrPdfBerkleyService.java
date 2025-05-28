package br.com.api.sub.ocr.app.service;

import static br.com.api.sub.ocr.app.util.OcrPdfBerkleyUtil.EXTENSAOARQUIVO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.api.sub.ocr.app.handle.HandleException;
import br.com.api.sub.ocr.app.util.OcrPdfBerkleyUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OcrPdfBerkleyService {

	@Value("${ocr.pdf.imagem.servidor}")
	private transient String servidorImagem;

	@Value("${ocr.pdf.imagem}")
	private transient String repositorioImagem;

	@Value("${py.script.python.path}")
	private transient String scriptPython;

	@Value("${py.script.python.name}")
	private transient String filePythonNome;

	@Value("${ocr.pdf.csv.auditoria}")
	private transient String csvAuditoriaPath;

	public void imagemUploadManual(MultipartFile imagem) throws HandleException {

		log.info(" Inicio upload Manual arquivo  {} ", StringUtils.cleanPath(imagem.getOriginalFilename()));

		File file = new File(servidorImagem.concat(repositorioImagem));

		OcrPdfBerkleyUtil.validador(file, imagem);

		OcrPdfBerkleyUtil.validadorPython(servidorImagem.concat(scriptPython), filePythonNome);

		String arquivo = StringUtils.cleanPath(imagem.getOriginalFilename());

		String scriptPy = servidorImagem.concat(scriptPython).concat(filePythonNome);

		String diretorioPdf = servidorImagem.concat(repositorioImagem).concat(StringUtils.cleanPath(imagem.getOriginalFilename()));

		extracaoTabelaPDF(arquivo, scriptPy, diretorioPdf);
		
		OcrPdfBerkleyUtil.moveArquivo(diretorioPdf, servidorImagem.concat(csvAuditoriaPath).concat(arquivo));

	}

	public void imagemUploadSchedule(MultipartFile imagem) throws HandleException {

		log.info(" Inicio upload Schedule arquivo  {} ", StringUtils.cleanPath(imagem.getOriginalFilename()));
		
		List<File> filesMover = new ArrayList<>();

		File file = new File(servidorImagem.concat(repositorioImagem));

		OcrPdfBerkleyUtil.validador(file, imagem);

		OcrPdfBerkleyUtil.validadorPython(servidorImagem.concat(scriptPython), filePythonNome);

		String scriptPy = servidorImagem.concat(scriptPython).concat(filePythonNome);

		for (File pdf : file.listFiles()) {

			extracaoTabelaPDF(pdf.getName(), scriptPy, pdf.getAbsolutePath());
			
			filesMover.add(pdf);
		}
			
		for(File movePdf : filesMover) {
		
			OcrPdfBerkleyUtil.moveArquivo(movePdf.getAbsolutePath(), servidorImagem.concat(csvAuditoriaPath).concat(movePdf.getName()));
		}
		
	}

	private void extracaoTabelaPDF(String arquivoNome, String scriptPy, String arquivoPdfOrigem) throws HandleException {

		log.info(" Inicio da extração de dados da tabela PDF {} ", arquivoNome);

		List<String> linhasCsv = new ArrayList<>();

		String linha = null;

		try {

			ProcessBuilder pb = new ProcessBuilder("python", scriptPy, arquivoPdfOrigem);
			pb.redirectErrorStream(true);

			Process process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			while ((linha = reader.readLine()) != null) {
				linhasCsv.add(linha.replace(",", ";"));
			}

			int exitCode = process.waitFor();
			if (exitCode != 0) {
				throw new HandleException("⚠️ Ocorreu um erro ao executar o script Python.", HttpStatus.CONFLICT);
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

		gerarArquivoCSV(arquivoNome,linhasCsv, arquivoPdfOrigem);
	}

	private void gerarArquivoCSV(String arquivoNome, List<String> linhasCsv, String arquivoPdfOrigem) throws HandleException {

		try (FileWriter fw = new FileWriter(servidorImagem.concat(csvAuditoriaPath).concat(arquivoNome.toUpperCase().replace("PDF", EXTENSAOARQUIVO)))) {

			linhasCsv.stream().forEach((csv) -> {
				try {
					fw.write(String.valueOf(csv).concat("\n"));
				} catch (IOException e) {
					log.info("⚠️ Erro ao tentar escrever o arquivo cvs ");
				}

			});

			fw.close();
			
		} catch (IOException e) {
			throw new HandleException("⚠️  Erro ao tentar gera o arquivo CSV {} ", HttpStatus.CONFLICT);
		}
		
	}
}
