package boody.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import boody.member.StatusForm;
import boody.page.Page_Boody;
import boody.vo.Member;

public class ClientMsgThread extends Thread{
	private BoodyClient boodyClient;
	Socket socket;
	
	StatusForm statusForm;
	


	Page_Boody page_boody;
	
	
	BufferedReader buffr;
	BufferedWriter buffw;
	
	Vector<Member> onlineMemberList;
	
	boolean flag = true;
	
	public ClientMsgThread(Socket socket, StatusForm statusForm) {
		this.socket = socket;
		this.statusForm = statusForm;
		page_boody = (Page_Boody) statusForm.pages[0];
		
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Server와 1:1 수신기
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //Server와 1:1 수신기
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void listen() {
		String msg = null;
		try {
			msg= buffr.readLine();
	
			JSONParser jsonParser=new JSONParser();
			try {
				JSONObject packet=(JSONObject)jsonParser.parse(msg);
				String type=(String)packet.get("type");
				System.out.println("클라 msg 리슨 정보 "+ msg);
				
				if(type.equals("login")) { //로그인 정보가 전송되어 오면
					//-----------------접속한 사람 정보 가져오기
					System.out.println("서버가 접속 정보를 보냈습니다\n");
					JSONArray memberArr=(JSONArray)packet.get("memberList");
					
					System.out.println("memberArr size = "+memberArr.size());

					onlineMemberList = new Vector<Member>();
					
					for (int i = 0; i < memberArr.size(); i++) {
						Member onlineMember=new Member();
						JSONObject obj=(JSONObject)memberArr.get(i);//json 의 배역 안에있는
						
						System.out.println("member_id"+obj.get("member_id"));
						
						onlineMember.setMember_id(Integer.parseInt((String) obj.get("member_id")));//pk 넣기
						onlineMember.setUser_emotion(Integer.parseInt((String) obj.get("user_emotion")));//pk 넣기
						onlineMember.setUser_id((String)obj.get("user_id"));//id 넣기
						onlineMember.setUser_name((String)obj.get("user_name"));//name 넣기
						onlineMember.setUser_msg((String)obj.get("user_msg"));//msg 넣기
						onlineMember.setUser_email((String)obj.get("user_email"));//email 넣기
						onlineMember.setUser_filename((String)obj.get("user_filename"));//filename 넣기
						onlineMember.setUser_regdate((String)obj.get("user_regdate"));//regdate 넣기
						
						onlineMemberList.add(onlineMember);
					}
					
					System.out.println("console 접속자 수 : "+onlineMemberList.size());
					System.out.println("부디 : "+page_boody);
					System.out.println("라벨 : "+page_boody.onlineCount);
					
					page_boody.onlineCount.setText("현재 접속자 수 : "+onlineMemberList.size()+" 명");
//					boodyServer.area.append(member.getUser_name()+"님이 출현하였다!! \n");
//					.area.append("현재 접속자 수는 : "+boodyServer.memberList.size()+"명\n");
					
					
				}else if(type.equals("chat")) { //대화의 메시지가 전송되어 오면 
					String me = (String)packet.get("me");
					String you = (String)packet.get("you");
					String my_name = (String)packet.get("my_name");
					String message = (String)packet.get("msg");
					System.out.println("유저 아이디 : "+statusForm.getMember().getUser_id());
					
					if(me.equals(statusForm.getMember().getUser_id())||you.equals(statusForm.getMember().getUser_id())) {
						boodyClient.area.append(my_name+" : "+message+"\n");
					}
					//page_boody.getBoodyClient().area.append(me+" : "+message+"\n");
				}else if(type.equals("emo")) { //이모티몬을 전송한 거라면
				}else if(type.equals("add_friend")) {//친구추가요청이라면
				}else if(type.equals("present")) { //선물보내기 라면...
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBoodyClient(BoodyClient boodyClient) {
		this.boodyClient = boodyClient;
	}
	
	public void send(String msg) {
		try {
			buffw.write(msg+"\n");
			buffw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(flag) {
			listen();
		}
		if(buffr != null) {
			try {
				buffr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(buffw != null) {
			try {
				buffw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
