<h1 align="center"> 
  Extracao de tabela arquivo pdf 
</h1>

<p align="center">
  <img src="https://github.com/EduardoNofre/extracao-tabelas-pdf/blob/main/apresentador%20bancada%20.png"/>  
</p>

<h1 align="center">
   O que será usado aqui neste estudo.
</h1>

1 - Ide eclipse sts.<br>
2 - Java 17.<br>
3 - lombok.<br>
4 - Spring boot 3.4.5.<br>
5 - Python script
6 - micro-serviço

# Um exemplo simples de como extrair dados do PDF.

## requisitos
   - Instalar o Tesseract-OCR 
   - Instalar o python na maquina
   - bibliotecas do bibliotecas
   - Instalar bibliotecas Python
   - Criar o script Python extrair_tabela.py
   - E ter um arquivo pdf para testes.
     
# Passo a Passo 

## Passo 01: Instalar o Python.
  - Faça o download e instale https://www.python.org/downloads/release/python-3132/
  - Versão utilizada aqui foi a 3.13 python-3.13.3-amd64.exe *Windows*
  - Para prosseguir deixa como mostra a imagem abaixo.

  <p align="center">
  <img src="https://github.com/EduardoNofre/extracao-tabelas-pdf/blob/main/03.png"/>  
</p>

## Passo 02: Instalar as bibliotecas necessarias do Python.
   - Execute o seguinte comando.
   - *pip install pdfplumber pytesseract pillow opencv-python*

## Passo 03: Instalar Tesseract-OCR.
  - Faça o download  e instale https://github.com/UB-Mannheim/tesseract/wiki
  - Versão utilizada aqui foi a 3tesseract-ocr-w64-setup-5.5.0.20241111.exe para *Windows*
  - next,next e finish.

## Passo 04: Criar o script Python com o nome extrair_tabela.py

            import sys
            import logging
            import pdfplumber
            import pytesseract
            from PIL import Image
            import unicodedata
            
            pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'
            
            # remove os log de nivel info
            logging.getLogger("pdfplumber").setLevel(logging.ERROR)
            logging.getLogger("pdfminer").setLevel(logging.ERROR)
            
            # remove acentuação da string
            def remove_acentos(texto):
                return unicodedata.normalize('NFKD', texto).encode('ASCII', 'ignore').decode('ASCII')
            
            # Extração de tabela do PDF caso o pdf tenha alguma tabela 
            def extract_tables_from_pdf(pdf_path):
                tables_text = []
            
                with pdfplumber.open(pdf_path) as pdf:
                    for page_num, page in enumerate(pdf.pages):
                        if page.extract_text():
                            tables = page.extract_tables()
                            for table in tables:
                                for row in table:
                                    linha = ",".join([cell if cell else "" for cell in row])
                                    tables_text.append(remove_acentos(linha))
                        else:
                            # OCR fallback
                            image = page.to_image(resolution=300).original
                            text = pytesseract.image_to_string(image, lang='por')
                            tables_text.append(remove_acentos(text))
            
                return "\n".join(tables_text)
            
            if __name__ == "__main__":
                if len(sys.argv) < 2:
                    print("Usage: python extract_tables.py <path_to_pdf>")
                    sys.exit(1)
            
                pdf_path = sys.argv[1]
                print(extract_tables_from_pdf(pdf_path))

## Passo 05:  Classe Java para executar o script

              import java.io.*;
              
              public class PdfTableExtractor {
              
                  public static void main(String[] args) {
                      String pdfPath = "C:\\caminho\\para\\seu\\arquivo.pdf"; // Altere aqui
                      String pythonScriptPath = "C:\\caminho\\para\\extract_tables.py"; // Altere aqui
              
                      try {
                          ProcessBuilder pb = new ProcessBuilder("python", pythonScriptPath, pdfPath);
                          pb.redirectErrorStream(true); // Captura erros junto com saída padrão
              
                          Process process = pb.start();
                          BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
              
                          String line;
                          System.out.println("📄 Tabelas extraídas:");
                          while ((line = reader.readLine()) != null) {
                              System.out.println(line);
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
  
# Observação: o projeto que está nesta branch é uma API e está mais completo e organizado.
   - Controlle
   - Service
   - Gera csv

💡 Dica de uso para destacar no terminal
Você pode usar símbolos visuais como:

✅ Status e Resultados<br>
Emoji	Significado<br>
✅	Sucesso, operação concluída<br>
❌	Erro, falha<br>
⚠️	Alerta, atenção<br>
ℹ️	Informação<br>
⏳	Em andamento, aguardando<br>
🔄	Tentando novamente, processo em loop<br>
🔁	Repetição<br>
⏹️	Parado<br>
⏯️	Pausado<br>
⏱️	Cronômetro, tempo decorrido<br>
🚫	Proibido, acesso negado<br>
🛑	Parada crítica<br>

🧪 Processos e Execução<br>
Emoji	Significado<br>
🧪	Teste<br>
🧹	Limpeza de arquivos/temp<br>
📦	Instalação de pacotes<br>
🔧	Ajustes, configuração<br>
🔍	Verificando, analisando<br>
🧰	Ferramentas, utilitários<br>

📂 Arquivos e Diretórios<br>
Emoji	Significado<br>
📂	Abrindo pasta<br>
📁	Diretório<br>
📄	Documento<br>
🗃️	Arquivo armazenado<br>
🗑️	Arquivo excluído<br>
📝	Anotação ou log<br>

🔐 Segurança e Permissões<br>
Emoji	Significado<br>
🔒	Acesso restrito<br>
🔓	Acesso liberado<br>
🛡️	Segurança ativada<br>
🚨	Alerta de segurança ou violação<br>

🧠 Lógica e Controle<br>

Emoji	Significado<br>
🧠	Decisão ou lógica<br>
🎯	Alvo atingido<br>
🔀	Caminho alternativo<br>
🧭	Navegação no código ou estrutura<br>

🔔 Notificações e Eventos<br>
Emoji	Significado<br>
🔔	Notificação ativada<br>
🔕	Notificação desativada<br>
📣	Mensagem importante<br>
📢	Broadcast<br>

🧭 Ambiente e Contexto<br>
Emoji	Significado<br>
🌐	Requisição web ou rede<br>
📡	Comunicação, API<br>
🌍	Ambiente global<br>
🖥️	Ambiente local/desktop<br>
☁️	Cloud/Servidor<br>

💡 Dicas e Informações<br>
Emoji	Significado<br>
💡	Dica<br>
🧾	Documentação<br>
❓	Ajuda necessária<br>
❗	Atenção extra<br>
🔍 (buscando)<br>
📝 (log)<br>

