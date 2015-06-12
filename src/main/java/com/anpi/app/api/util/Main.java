package com.anpi.app.api.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

public class Main {

	public void createPdf(String inputFile, String outputFile) {
		System.out.println("Convert to pdf");
		String pdfFilePath = outputFile;
		try {
			String fileExtension = FilenameUtils.getExtension(inputFile);
			System.out.println("fileE"+fileExtension);
			 Rectangle pageSize = new Rectangle(3500, 3000);
		     Document pdfDocument = new Document(pageSize);
			/*if(fileExtension.equals("doc") || fileExtension.equals("docx")){
				doc2Pdf(inputFile,outputFile);
			}
			else{*/
			FileOutputStream fileOutputStream = new FileOutputStream(pdfFilePath);
			PdfWriter writer = null;
			writer = PdfWriter.getInstance(pdfDocument, fileOutputStream);
			writer.open();
			pdfDocument.open();
			if (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png") || fileExtension.equals("gif") || fileExtension.equals("tif")) {
				pdfDocument.add(Image.getInstance(inputFile));
			} else {
				File file = new File(inputFile);
				System.out.println("readFileToString:"+FileUtils.readFileToString(file));
				pdfDocument.add(new Paragraph(FileUtils.readFileToString(file)));
			}
			pdfDocument.close();
			writer.close();
//			}
		} catch (Exception exception) {
			System.out.println("Document Exception!" + exception);
			exception.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws DocumentException, IOException {
		new Main().createPdf("F://eclipse/IMG_20150529_165824.jpg","F://eclipse/newFile.pdf");
	}
	
	
}
