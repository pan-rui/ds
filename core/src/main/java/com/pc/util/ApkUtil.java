package com.pc.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

public class ApkUtil {

	private static final Namespace NS = Namespace.getNamespace("http://schemas.android.com/apk/res/android");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, Object> getApkInfo(String apkPath) {
		Map<String, Object> apkInfo = new HashMap<String, Object>();
		SAXBuilder builder = new SAXBuilder();
		Document document = null;
		try {
			document = builder.build(getXmlInputStream(apkPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();// 跟节点-->manifest
		apkInfo.put("versionCode", root.getAttributeValue("versionCode", NS));
		apkInfo.put("versionName", root.getAttributeValue("versionName", NS));
		apkInfo.put("apkPackage", root.getAttributeValue("package"));
		return apkInfo;
	}

	private static InputStream getXmlInputStream(String apkPath) {
		InputStream inputStream = null;
		InputStream xmlInputStream = null;
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(apkPath);
			ZipEntry zipEntry = new ZipEntry("AndroidManifest.xml");
			inputStream = zipFile.getInputStream(zipEntry);
			AXMLPrinter xmlPrinter = new AXMLPrinter();
			xmlPrinter.startPrinf(inputStream);
			xmlInputStream = new ByteArrayInputStream(xmlPrinter.getBuf().toString().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			try {
				inputStream.close();
				zipFile.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return xmlInputStream;
	}

}
