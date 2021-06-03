package sk.stuba.fei.weblab.matlabadapterclient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.stream.FileImageInputStream;

public class ZipExtractor {

//	private String dstDir;
//	private String zipFile;
//	
//	
//	public ZipExtractor(String dstDir, String zipFile) {
//		File dstDirFile = new File(dstDir);
//		if(!dstDirFile.isDirectory()) {
//			dstDirFile.mkdir();
//		}
//		this.dstDir = dstDirFile.getAbsolutePath();
//		this.zipFile = zipFile;
//	}
//	
//	@Deprecated
//	public void decompress() throws IOException {
//		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
//		ZipEntry ze;
//		while ((ze = zis.getNextEntry()) != null) {
//			String fileName = ze.getName();
//			File newFile = new File(dstDir+File.separator+fileName);
//			new File(newFile.getParent()).mkdirs();
//
//			try(FileOutputStream fos = new FileOutputStream(newFile)) {
//				byte buffer[] = new byte[1024];
//				int len;
//				while ((len = zis.read(buffer)) != -1) {
//					fos.write(buffer, 0, len);
//				}
//			} 
//		}
//		zis.closeEntry();
//		zis.close();
//	}
	
	public static void decompress(String dstDir, String zipFile) throws IOException {
		File dstDirFile = new File(dstDir);
		if(!dstDirFile.isDirectory()) {
			dstDirFile.mkdir();
		}
		String dstAbsDir = dstDirFile.getAbsolutePath();
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry ze;
		while ((ze = zis.getNextEntry()) != null) {
			String fileName = ze.getName();
			File newFile = new File(dstAbsDir+File.separator+fileName);
			new File(newFile.getParent()).mkdirs();

			try(FileOutputStream fos = new FileOutputStream(newFile)) {
				byte buffer[] = new byte[1024];
				int len;
				while ((len = zis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
			} 
		}
		zis.closeEntry();
		zis.close();
	}
	
	public static void decompress(String dstDir, InputStream in) throws IOException {
		File dstDirFile = new File(dstDir);
		if(!dstDirFile.isDirectory()) {
			dstDirFile.mkdir();
		}
		String dstAbsDir = dstDirFile.getAbsolutePath();
		ZipInputStream zis = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zis.getNextEntry()) != null) {
			String fileName = ze.getName();
			File newFile = new File(dstAbsDir+File.separator+fileName);
			new File(newFile.getParent()).mkdirs();

			try(FileOutputStream fos = new FileOutputStream(newFile)) {
				byte buffer[] = new byte[1024];
				int len;
				while ((len = zis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
			} 
		}
		zis.closeEntry();
		zis.close();
	}
	
	public static void decompress(String dstDir, byte[] data) throws IOException {
		File dstDirFile = new File(dstDir);
		if(!dstDirFile.isDirectory()) {
			dstDirFile.mkdir();
		}
		String dstAbsDir = dstDirFile.getAbsolutePath();
		ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(data));
		ZipEntry ze;
		while ((ze = zis.getNextEntry()) != null) {
			String fileName = ze.getName();
			File newFile = new File(dstAbsDir+File.separator+fileName);
			new File(newFile.getParent()).mkdirs();

			try(FileOutputStream fos = new FileOutputStream(newFile)) {
				byte buffer[] = new byte[1024];
				int len;
				while ((len = zis.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
			} 
		}
		zis.closeEntry();
		zis.close();
	}
}
