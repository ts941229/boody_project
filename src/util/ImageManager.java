package util;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

public class ImageManager {

	public static ImageIcon getScaledIcon(Object obj, String filename, int width, int height) {
		ImageIcon icon=new ImageIcon(obj.getClass().getClassLoader().getResource(filename));
	    //이미지 크기를 줄이는 법
	    icon= new ImageIcon(icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
	    return icon;
	}
	
}
