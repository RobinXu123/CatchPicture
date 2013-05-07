import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CatchPicture {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			String permissionField = "goals:access";
//			String resourceNameField = permissionField.split(":")[0];
//			String permission = permissionField.split(":")[1];
//			System.out.println("resourceNameField:" + resourceNameField);
//			System.out.println("permissionField:" + permission);
			Document doc = Jsoup.connect("http://photography.nationalgeographic.com/photography/photo-of-the-day/").get();
			Elements pic = doc.select("div.primary_photo img");
			List<Picture> list=new CatchPicture().getPicture(pic);
			System.out.println(list.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		/*
		//����ץȡͼƬ�� ������ʽ
		String regular="[*]<b>.*?</b><br/><img src=\"(.*?)\" border=0 alt=\'(.*?)\' style=\".*?\" class=\".*?\">"; //  ^/photo-of-the-day/[.]*
		List<Picture> list=new CatchPicture().lookWeiboPic("http://gaoxiao.jokeji.cn/GrapHtml/dongtai/20120921221658.htm","GBK",regular,"2,1"); //  http://photography.nationalgeographic.com/photography/photo-of-the-day/
		System.out.println(list.size());
		*/
	}
	
	public List<Picture> getPicture(Elements pic) throws Exception {
		List<Picture> list = new ArrayList<Picture>();
		
		String url = pic.attr("src");
		
		String[] suffix;
		suffix = url.trim().split("cn");
		String httphread = "";
		if (suffix.length > 1) {
			httphread = suffix[0] + "cn";

		} else {
			suffix = url.trim().split("com");
			httphread = suffix[0] + "com";
		}
		
		Picture picture = new Picture();
		// �ж��Ǿ���·���������·��
		String[] pathType = url.split(":");
		if (pathType.length > 1) {
			// ����·��
			picture.setSource(url);
		} else {
			// �ж����·���Ƿ���..
			pathType = url.split("\\.\\.");
			if (pathType.length > 1) {
				picture.setSource(httphread + pathType[1]);
			} else {
				if (url.startsWith("/")) {
					picture.setSource(httphread+ pathType[0]);
				} else {
					picture.setSource(httphread + "/"+ pathType[0]);
				}
			}
		}
		String upPath = upload(picture.getSource(), "d:\\image\\");
		picture.setUpPath(upPath);
		list.add(picture);
		
		return list;
	}
	
	/**
	 * �ϴ� ͼƬ
	 * 
	 * @param urlStr
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public String upload(String urlStr, String path) throws Exception {
		Calendar calendar = Calendar.getInstance();
		String month = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1);
		String filename = java.util.UUID.randomUUID().toString() + getExtension(urlStr);
		path = path + month + "/";
		download(urlStr, filename, path);
		return path + month + "/" + filename;
	}

	/**
	 * ����·�� ����ͼƬ Ȼ�� ���浽��Ӧ��Ŀ¼��
	 * 
	 * @param urlString
	 * @param filename
	 * @param savePath
	 * @return
	 * @throws Exception
	 */
	public void download(String urlString, String filename, String savePath)
			throws Exception {
		// ����URL
		URL url = new URL(urlString);
		// ������
		URLConnection con = url.openConnection();
		// ���������·��
		con.setConnectTimeout(5 * 1000);
		// ������
		InputStream is = con.getInputStream();

		// 1K�����ݻ���
		byte[] bs = new byte[1024];
		// ��ȡ�������ݳ���
		int len;
		// ������ļ���
		File sf = new File(savePath);
		if (!sf.exists()) {
			sf.mkdirs();
		}
		OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
		// ��ʼ��ȡ
		while ((len = is.read(bs)) != -1) {
			os.write(bs, 0, len);
		}
		// ��ϣ��ر���������
		os.close();

		is.close();
	}

	/**
	 * �����ļ��� ��ȡ�ļ��ĺ�׺��
	 * 
	 * @param fileUrl
	 * @return
	 */
	public String getExtension(String fileUrl) {
		return fileUrl.substring(fileUrl.lastIndexOf("."), fileUrl.length());
	}
}
