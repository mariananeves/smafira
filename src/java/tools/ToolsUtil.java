package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolsUtil {

//	public static void writeTextToFile(String text, String dest){
//		try {
//			File file = new File(dest);
//			file.createNewFile();
//			
//			FileWriter fw = new FileWriter(file);
//			BufferedWriter bw = new BufferedWriter(fw);
//			
//			bw.write(text);
//			
//
//			bw.close();
//			fw.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}	
//	}
	
	public static void saveText(String path, String content){
		try {
			File saveFile = new File(path);
			File parentFile = new File(saveFile.getParent());
			
			if(!parentFile.exists())
				parentFile.mkdirs();
			
			if(!saveFile.exists())
				saveFile.createNewFile();
			
			PrintWriter out = new PrintWriter(path);
			out.println(content);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static boolean isInteger(String str){
		try{
			Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			return false;
		}
		
		return true;
	}

	public static boolean isNumeric(Character c){
		try{
			double d = Double.parseDouble(String.valueOf(c));
		}
		catch(NumberFormatException nfe){
			return false;
		}

		return true;
	}
	
	
	public static void addNewValueWithCount(HashMap<String, Integer> map, String newElement){
		int count;
		
		if(map.containsKey(newElement)){
			count = map.get(newElement);
			count++;
			map.put(newElement, count);
		}
		else
			map.put(newElement, 1);
			
	}
	
	
	public static void copyFiles(String sourceDir, String[] fileNames, String targetDir){
		Path sourcePath, targetPath;
				
		(new File(targetDir)).mkdirs();
		for(String fileName: fileNames){
			sourcePath = FileSystems.getDefault().getPath(sourceDir, fileName);
			targetPath = FileSystems.getDefault().getPath(targetDir, fileName);
			try {
				Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String[] getListOfFileNames(String listFilePath){
		ArrayList<String> list = new ArrayList<String>();
		String[] listArray;
		
		try {
			File file = new File(listFilePath);		
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			
			while((line=br.readLine()) != null){
				line = line.trim();
				if(!line.isEmpty() && !line.startsWith("//")){
					list.add(line);
				}
			}
			
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		listArray = new String[list.size()];
		list.toArray(listArray);
		
		return listArray;
	}
	
	
	public static String getFirstStringOfNumbersInText(String text){
		
		Pattern p = Pattern.compile("([0-9]+)");
		Matcher m = p.matcher(text);
		
		if(m.find()){
			return m.group();
		}
		else
			return "";
	}
	
	public static void writeListContentToFile(String targetPath, ArrayList<String> list){
		String content = "";
		
		for(String listElement: list){
			content += listElement + "\n";
		}
		
		content = content.trim();
		
		saveText(targetPath, content);
	}
	
	public static String escapeString(String text, String toEscape){
		text = text.replace(toEscape, "\\" + toEscape);
		
		return text;
	}
	
	
	public static ArrayList<String> getLinesAsList(String filePath){
		
		ArrayList<String> lines = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			String line;
			
			while((line = reader.readLine()) != null){
				lines.add(line);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
}
