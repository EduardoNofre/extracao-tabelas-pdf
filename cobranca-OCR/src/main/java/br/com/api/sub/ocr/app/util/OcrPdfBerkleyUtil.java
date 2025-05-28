package br.com.api.sub.ocr.app.util;

import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import br.com.api.sub.ocr.app.handle.HandleException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OcrPdfBerkleyUtil {

	public static final String EXTENSAOARQUIVO = "csv";

	/**
	 * 
	 * Validador
	 * 
	 * Verificar se o diretorio existe. <br>
	 * 
	 * Verificar se a imagem é um pdf.<br>
	 * 
	 * @param file
	 * @param imagem
	 * @throws HandleException
	 */
	public static void validador(File file, MultipartFile imagem) throws HandleException {

		if (!file.isDirectory()) {
			throw new HandleException(" CAMINHO INVALIDO ", HttpStatus.CONFLICT);
		}

		log.info("CAMINHO VALIDO {} ", file.getAbsolutePath());

		if (!StringUtils.cleanPath(imagem.getOriginalFilename()).toUpperCase().endsWith(".PDF")) {
			throw new HandleException(" ARQUIVO NÃO ENCONTRADO PDF", HttpStatus.CONFLICT);
		}

		log.info("CAMINHO PDF ENCONTRADO {} ", StringUtils.cleanPath(imagem.getOriginalFilename()).toUpperCase());
	}

	/**
	 * 
	 * Verificar se o diretorio existe. <br>
	 * 
	 * Verificar se a imagem é um PY. <br>
	 * 
	 * Verificar se o arquivo PY existe no diretorio <br>
	 * 
	 * @param file
	 * @param imagem
	 * @throws HandleException
	 */
	public static void validadorPython(String path, String imagem) throws HandleException {

		File file = new File(path);

		if (!file.isDirectory()) {
			throw new HandleException(" CAMINHO INVALIDO ", HttpStatus.CONFLICT);
		}

		log.info("CAMINHO VALIDO {} ", file.getAbsolutePath());

		if (!imagem.toUpperCase().endsWith(".PY")) {
			throw new HandleException(" ARQUIVO NÃO ENCONTRADO SCRIPT .PY", HttpStatus.CONFLICT);
		}

		log.info(".PY ARQUIVO VALIDO {} ", imagem.toUpperCase());

		for (File arquivoExistePY : file.listFiles()) {

			if (arquivoExistePY.exists()) {
				log.info(".PY SCRIPT ENCONTRADO {} ", arquivoExistePY.getAbsolutePath());
				break;
			}
		}
	}

	public static void moveArquivo(String origemFile, String destinoFile) throws HandleException {

		File  origem = new File(origemFile);

		File destino = new File(destinoFile);
		
		if (origem.renameTo(destino)) {
			log.info("Arquivo movido com sucesso de: {} para : ", origem, destino);
			return;
		}
		log.info("Erro o tentar mover o arquivo de: {} para : ", origem, destino);

	}
}
