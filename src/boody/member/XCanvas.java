package boody.member;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

//캔버스의 paint 메서드를 재정의하기 위한 클래스
public class XCanvas extends Canvas{
   Toolkit kit=Toolkit.getDefaultToolkit();
   Image image;
   
   //스트링으로 경로를 넘겨받아사ㅓ 이미지를 생성하자
   public void createImage(String path) {
      image=kit.getImage(path);
   }
   //썸네일 패널로부터 이미지를 전달받기 위해..
   public void setImage(Image image) {
      this.image = image;
      System.out.println("전달받은 이미지는 "+image);
   }
   @Override
   public void paint(Graphics g) {
      g.drawImage(image, 0, 0, 290, 190, this);
   }
}