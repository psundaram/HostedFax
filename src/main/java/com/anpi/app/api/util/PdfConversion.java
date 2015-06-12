package com.anpi.app.api.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;


/**
 * 
 * Converts html,txt,jpg,png,eml,gif to pdf 
 *
 */
public class PdfConversion {
	/**
	 * This method is used to convert the given file to a PDF format
	 * 
	 * @param inputFile
	 *            - Name and the path of the file
	 * @param outputFile
	 *            - Name and the path where the PDF file to be saved
	 * @param isPictureFile
	 */
	public void createPdf(String inputFile, String outputFile) {
		System.out.println("Convert to pdf");
		 Rectangle pageSize = new Rectangle(3500, 3000);
	     Document pdfDocument = new Document(pageSize);
		String pdfFilePath = outputFile;
		try {
			String fileExtension = FilenameUtils.getExtension(inputFile);
			System.out.println("fileE"+fileExtension);
			if(fileExtension.equals("doc") || fileExtension.equals("docx")){
				doc2Pdf(inputFile,outputFile);
			}
			else{
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
			}
		} catch (Exception exception) {
			System.out.println("Document Exception!" + exception);
			exception.printStackTrace();
		}
	}

	public void doc2Pdf(String inputFile,String outputFile) {
		 try {
	            // 1) Load DOCX into XWPFDocument
	            InputStream is = new FileInputStream(new File(
	                   inputFile));
	            XWPFDocument document = new XWPFDocument(is);
	 
	            // 2) Prepare Pdf options
	            PdfOptions options = PdfOptions.create();
	 
	            // 3) Convert XWPFDocument to Pdf
	            OutputStream out = new FileOutputStream(new File(
	                    outputFile));
	            PdfConverter.getInstance().convert(document, out, options);
	             
	        } catch (Throwable e) {
	            e.printStackTrace();
	        }
		
	}

	public static void main(String args[]) {
		PdfConversion doc2Pdf2 = new PdfConversion();
		doc2Pdf2.createPdf("F://index.jsp", "F:/demopdf.pdf");
//		doc2Pdf2.createPdf("C://Users/sarulsekar/Downloads/FAX000000001.tif", "F:/demopdf.pdf");
	}
}