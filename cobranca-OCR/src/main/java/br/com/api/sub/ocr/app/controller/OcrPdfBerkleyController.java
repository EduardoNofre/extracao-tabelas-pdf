package br.com.api.sub.ocr.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.api.sub.ocr.app.handle.HandleException;
import br.com.api.sub.ocr.app.service.OcrPdfBerkleyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/ocr")
@Tag(name = "OcrPdfBerkleyController", description = "Recurso Berkley ocr de documentos PDF controller Api")
public class OcrPdfBerkleyController {

	@Autowired
	private OcrPdfBerkleyService  ocrPdfBerkleyService;

	@Operation(summary = "upload imagem", description = "upload de documentos pdf")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Criado", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Void.class)) }),
			@ApiResponse(responseCode = "204", description = "Sem conteudo", content = @Content),
			@ApiResponse(responseCode = "400", description = "Erro processar a requisição", content = @Content),
			@ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
			@ApiResponse(responseCode = "404", description = "Pagina não encontrado", content = @Content),
			@ApiResponse(responseCode = "409", description = "Não conformidade na regra de negocio", content = @Content),
			@ApiResponse(responseCode = "500", description = "Interno sem causa mapeada.", content = @Content),
			@ApiResponse(responseCode = "504", description = "Gateway Time-Out", content = @Content) })
	@PostMapping(consumes = "multipart/form-data")
	public ResponseEntity<Void> ImagemUpload(@Parameter(name = "imagem", description = "MultipartFile imagem", example = "imagem PDF") @RequestParam(required = true) MultipartFile imagem) throws HandleException {

		ocrPdfBerkleyService.imagemUploadManual(imagem);

		return ResponseEntity.ok().build();
	}
}
