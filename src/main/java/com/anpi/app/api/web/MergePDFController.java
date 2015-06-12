package com.anpi.app.api.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anpi.app.api.domain.Fax;
import com.anpi.app.api.service.MergePdfService;
import com.anpi.app.api.util.DbConnection;
import com.anpi.app.api.util.PdfConversion;
import com.anpi.app.api.util.SendMail;

@Controller
@RequestMapping(value = "/merge")
public class MergePDFController {

	private static List<String> filesToBeDeleted = new ArrayList<String>();

	@RequestMapping(value = "/files", method = RequestMethod.POST)
	public @ResponseBody
	String handleFileUpload(@RequestParam("to") String to,@RequestParam("from") String from,@RequestParam("file") List<MultipartFile> files,HttpServletResponse res) throws Exception {
		Fax fax = new Fax();
	//	fax.setFromDid("12097574934@172.31.16.33");
	//	fax.setToDid("12097574934@172.31.16.33");
		String response = null;
		fax.setFromDid(from);
		fax.setToDid(to);
		System.out.println("Fax:" + fax.toString());
		System.out.println("Enter Merge files - Upload file");
		List filesList = new ArrayList();
		String name = String.valueOf(System.currentTimeMillis()) + ".pdf";
		if(files!=null && files.size()>0){
			for (int i = 0; i < files.size(); i++) {
				MultipartFile multipartFile = files.get(i);
				System.out.println(multipartFile.getName());
				System.out.println(multipartFile.getOriginalFilename());
				String fileName = multipartFile.getOriginalFilename();
				fax.setOriginalFileName(multipartFile.getOriginalFilename());

				try {
					byte[] bytes = multipartFile.getBytes();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(System.getProperty("user.dir") + "/" + fileName)));
					stream.write(bytes);
					stream.close();
					System.out.println("name:" + fileName);
					filesList.add(fileName);
					filesToBeDeleted.add(fileName);
					System.out.println("File successfully uploaded " + fileName + " !");

					String originalUuid = new MergePdfService().uploadFile(System.getProperty("user.dir") + "/" + fileName);
					fax.setOriginalUuid(originalUuid);

				} catch (Exception e) {
					System.out.println("Failed to upload " + e.getMessage());
				}
			}
			System.out.println("User directory:" + System.getProperty("user.dir"));
			String mergedFileName = mergeDocuments(filesList, name);
			System.out.println("Mergedfilename:" + mergedFileName);
			fax.setFileName(mergedFileName);

			SendMail st = new SendMail(fax,true);
			st.start();
			System.out.println("Interrupted ");
			String uuid = new MergePdfService().uploadFile(System.getProperty("user.dir") + "/" + mergedFileName);
			System.out.println("uuid --> " + uuid);
			fax.setUuid(uuid);
			Thread.sleep(5000);
			filesToBeDeleted.add(System.getProperty("user.dir") + "/" + mergedFileName);
			System.out.println("FilesTobeDeleted:" + filesToBeDeleted);
			new MergePdfService().deleteFiles(filesToBeDeleted);
			System.out.println("Files deleted");
			int id = insertIntoDb(fax);
			res.setStatus(HttpServletResponse.SC_OK);
			response = "File uploaded Successfully";
		}else{
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response = "Error in uploading";
		}
		return response;
	}

	
	@RequestMapping(value = "/files/dev", method = RequestMethod.POST)
	public @ResponseBody
	String handleFileUploadDev(@RequestParam("to") String to,@RequestParam("from") String from,@RequestParam("file") List<MultipartFile> files,HttpServletResponse res) throws Exception {
		Fax fax = new Fax();
	//	fax.setFromDid("12097574934@172.31.16.33");
	//	fax.setToDid("12097574934@172.31.16.33");
		String response = null;
		fax.setFromDid(from);
		fax.setToDid(to);
		System.out.println("Fax:" + fax.toString());
		System.out.println("Enter Merge files - Upload file");
		List filesList = new ArrayList();
		String name = String.valueOf(System.currentTimeMillis()) + ".pdf";
		if(files!=null && files.size()>0){
			for (int i = 0; i < files.size(); i++) {
				MultipartFile multipartFile = files.get(i);
				System.out.println(multipartFile.getName());
				System.out.println(multipartFile.getOriginalFilename());
				String fileName = multipartFile.getOriginalFilename();
				fax.setOriginalFileName(multipartFile.getOriginalFilename());

				try {
					byte[] bytes = multipartFile.getBytes();
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(System.getProperty("user.dir") + "/" + fileName)));
					stream.write(bytes);
					stream.close();
					System.out.println("name:" + fileName);
					filesList.add(fileName);
					filesToBeDeleted.add(fileName);
					System.out.println("File successfully uploaded " + fileName + " !");

					String originalUuid = new MergePdfService().uploadFile(System.getProperty("user.dir") + "/" + fileName);
					fax.setOriginalUuid(originalUuid);

				} catch (Exception e) {
					System.out.println("Failed to upload " + e.getMessage());
				}
			}
			System.out.println("User directory:" + System.getProperty("user.dir"));
			String mergedFileName = mergeDocuments(filesList, name);
			System.out.println("Mergedfilename:" + mergedFileName);
			fax.setFileName(mergedFileName);

			SendMail st = new SendMail(fax,false);
			st.start();
			System.out.println("Interrupted ");
			String uuid = new MergePdfService().uploadFile(System.getProperty("user.dir") + "/" + mergedFileName);
			System.out.println("uuid --> " + uuid);
			fax.setUuid(uuid);
			Thread.sleep(5000);
			filesToBeDeleted.add(System.getProperty("user.dir") + "/" + mergedFileName);
			System.out.println("FilesTobeDeleted:" + filesToBeDeleted);
			new MergePdfService().deleteFiles(filesToBeDeleted);
			System.out.println("Files deleted");
			int id = insertIntoDb(fax);
			res.setStatus(HttpServletResponse.SC_OK);
			response = "File uploaded Successfully";
		}else{
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			response = "Error in uploading";
		}
		return response;
	}

	private int insertIntoDb(Fax fax) throws Exception {
		/*String columns = "";
		String val = "";
		Method[] methods = Fax.class.getMethods();
		for (Method method : methods) {
//            System.out.println(method.getName());
            if (method.getName().startsWith("get")) {
            	if(!(method.getName().equals("getClass"))){
                System.out.println(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, method.getName().replace("get", "")));
            	String key = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, method.getName().replace("get", ""));
            	System.out.println(method.invoke(fax));
            	columns += "`" + key + "`" + ",";
    			val += "'" + method.invoke(fax) + "'" + ",";
            }
            }
        }*/
		String sql = "INSERT INTO fax_storage (`file_name`,`to_did` ,`from_did`,`uuid`,`original_file_name`,`original_uuid`) VALUES ('"+fax.getFileName()+"','"+fax.getToDid()+"','"+fax.getFromDid()+"','"+fax.getUuid()+"','"+fax.getOriginalFileName()+"','"+fax.getOriginalUuid()+"');";
		System.out.println("sql:"+sql);
		int id = new DbConnection().insert(sql);
		return id;
	}

	public static void main(String[] args) throws Exception {
		Fax fax = new Fax();
		fax.setToDid("!23er");
		new MergePDFController().insertIntoDb(fax);
	}

	public static String mergeDocuments(List<String> filesToMerge, String fileName) throws COSVisitorException, IOException, UnrecoverableKeyException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException {
		System.out.println("Merge Documents");
		PDFMergerUtility ut = new PDFMergerUtility();
		System.out.println("filesToMerge" + filesToMerge);
		for (String file : filesToMerge) {
			String fileExtension = FilenameUtils.getExtension(file);
			System.out.println("fileExtension" + fileExtension);
			if (fileExtension.equals("pdf")) {
				ut.addSource(System.getProperty("user.dir") + "/" + file);
			} else {
				String newFile = String.valueOf(System.currentTimeMillis()) + ".pdf";
				new PdfConversion().createPdf(System.getProperty("user.dir") + "/" + file, newFile);
				ut.addSource(System.getProperty("user.dir") + "/" + newFile);
				filesToBeDeleted.add(newFile);
			}
		}
		System.out.println("Destination fileName --> " + fileName);
		ut.setDestinationFileName(fileName);
		ut.mergeDocuments();
		System.out.println(System.getProperty("user.dir"));

		return String.valueOf(fileName);

	}
	

}
