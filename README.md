

![sonar](apresentador bancada .png)


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
   - Instalar bibliotecas Python
   - Criar o script Python extrair_tabela.py
   - E ter um arquivo pdf para testes.
# Passo a Passo 
## Criar o script Python com o nome extrair_tabela.py

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



