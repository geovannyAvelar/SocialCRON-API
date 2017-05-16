package br.com.agenciacodeplus.socialcron.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.web.multipart.MultipartFile;

import br.com.agenciacodeplus.socialcron.exceptions.EmptyFileException;

public class FileUtils {
  
  public static File saveFile(MultipartFile multipartFile, String path) throws IOException, 
                                                                               EmptyFileException {
    if (!multipartFile.isEmpty()) {
      byte[] bytes = multipartFile.getBytes();
      File file = new File(path);
      BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file));
      stream.write(bytes);
      stream.close();
      return file;
    } else {
      throw new EmptyFileException();
    }
  }

  public static String generateUniqueFilename(String seed, String extension) {
    String dataToHash = new Long(new Date().getTime()).toString() + seed;
    String filename = Base64.encodeBase64URLSafeString(dataToHash.getBytes()) + extension;
    return filename;
  }
  
  public static String getExtension(MultipartFile file) {
    
    if(file == null || file.isEmpty()) {
      throw new IllegalArgumentException("File cannot be null or empty");
    }
    
    String filename = file.getOriginalFilename();
    int length = filename.length();
    
    if(length < 5) {
      throw new IllegalArgumentException("Invalid filename. A file name should be greater than 5");
    }
    
    return filename.substring(length - 4, length);
    
  }
  
}
