package boody.chat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import boody.vo.Member;

public class ServerMsgThread extends Thread{
	Member loginMember;
	
	BoodyServer boodyServer;
	Socket socket;
	
	BufferedReader buffr;
	BufferedWriter buffw;
	
	boolean flag = true;
	
	public ServerMsgThread(Socket socket, BoodyServer boodyServer) {
		this.socket = socket;
		this.boodyServer = boodyServer;
		
		try {
			buffr = new BufferedReader(new InputStreamReader(socket.getInputStream())); //Server에 접속한 클라이언트와 1:1 교신기
			buffw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //Server에 접속한 클라이언트와 1:1 교신기
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
				
				if(type.equals("login")) { //로그인 정보가 전송되어 오면
					
					//-----------------로그인한 사람 정보 가져오기
					System.out.println("클라이언트가 로그인 정보를 보냈습니다\n");
					loginMember=new Member();
					
					JSONObject obj=(JSONObject)packet.get("member");//json 의 member 안에있는
					loginMember.setMember_id(Integer.parseInt((String) obj.get("member_id")));//pk 넣기
					loginMember.setUser_emotion(Integer.parseInt((String) obj.get("user_emotion")));//pk 넣기
					loginMember.setUser_id((String)obj.get("user_id"));//id 넣기
					loginMember.setUser_name((String)obj.get("user_name"));//name 넣기
					loginMember.setUser_msg((String)obj.get("user_msg"));//msg 넣기
					loginMember.setUser_email((String)obj.get("user_email"));//email 넣기
					loginMember.setUser_filename((String)obj.get("user_filename"));//filename 넣기
					loginMember.setUser_regdate((String)obj.get("user_regdate"));//regdate 넣기
					
					//boodyServer.memberList.add(loginMember);
					//Member member=boodyServer.memberList.lastElement();
					
					//boodyServer.area.append(member.getUser_name()+"님이 출현하였다!! \n");
//					boodyServer.area.append("현재 접속자 수는 : "+boodyServer.memberList.size()+"명\n");
					
					//------------------접속 리스트 보내기
					
					StringBuilder sb = new StringBuilder();
					sb.append("{");
					sb.append("\"type\" : \"login\",");
					sb.append("\"memberList\":[");
					for (int i = 0; i < boodyServer.clientList.size(); i++) {
						ServerMsgThread msgThread=boodyServer.clientList.get(i);
						
						sb.append("{");
						sb.append("\"member_id\" : \""+msgThread.loginMember.getMember_id()+"\",");
						sb.append("\"user_emotion\" : \""+msgThread.loginMember.getUser_emotion()+"\",");
						sb.append("\"user_id\" : \""+msgThread.loginMember.getUser_id()+"\",");
						sb.append("\"user_name\" : \""+msgThread.loginMember.getUser_name()+"\",");
						sb.append("\"user_msg\" : \""+msgThread.loginMember.getUser_msg()+"\",");
						sb.append("\"user_email\" : \""+msgThread.loginMember.getUser_email()+"\",");
						sb.append("\"user_filename\" : \""+msgThread.loginMember.getUser_filename()+"\",");
						sb.append("\"user_regdate\" : \""+msgThread.loginMember.getUser_regdate()+"\"");
						if(i<boodyServer.clientList.size()-1) {
							sb.append("},");
						}else {
							sb.append("}");
						}
					}
					sb.append("]");
					sb.append("}");
					
					for (int i = 0; i < boodyServer.clientList.size(); i++) {
						ServerMsgThread smt=boodyServer.clientList.get(i);
						smt.send(sb.toString());
					}
					
				}else if(type.equals("chat")) { //대화의 메시지가 전송되어 오면 
					System.out.println("클라이언트가 메시지 정보를 보냈습니다\n");
					String me = (String)packet.get("me");
					String you = (String)packet.get("you");
					String my_name = (String)packet.get("my_name");
					String message = (String)packet.get("msg");
					
					for(int i=0; i<boodyServer.clientList.size(); i++) {
						ServerMsgThread smt = boodyServer.clientList.get(i);
						System.out.println("유저 아이디 : "+smt.loginMember);
						
						boodyServer.area.append(me+"님이 "+you+"님에게 : "+message+"\n");
						//나와 상대 채팅에만 보내기
						System.out.println("겟유저아이디는 : "+smt.loginMember);
						if(smt.loginMember.getUser_id().equals(me)||smt.loginMember.getUser_id().equals(you)) {
							StringBuilder sb = new StringBuilder();
							sb.append("{");
							sb.append("\"type\" : \"chat\",");
							sb.append("\"me\" : \""+me+"\",");
							sb.append("\"you\" : \""+you+"\",");
							sb.append("\"my_name\" : \""+my_name+"\",");
							sb.append("\"msg\" : \""+message+"\"");
							sb.append("}");
							smt.send(sb.toString());
							System.out.println(me+"가 "+you+"에게 메시지 전송완료");
						}else {
							System.out.println("틀렸어!");
						}
					}
//					String message=(String)packet.get("message");
//					
//					//대화일때 broadcasting !!!
//					for(int i=0;i<boodyServer.clientList.size();i++) {
//						ServerMsgThread msgThread=boodyServer.clientList.get(i);
//						msgThread.send(member.getUser_id()+"의 말 : "+message);
//					}
//					boodyServer.area.append(member.getUser_id()+"의 말 : "+message+"\n");//area에 로그 남기기 
				}else if(type.equals("emo")) { //이모티몬을 전송한 거라면
					
				}else if(type.equals("add_friend")) {//친구추가요청이라면
				}else if(type.equals("present")) { //선물보내기 라면...
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			
			
		} catch (IOException e) {
			//e.printStackTrace();
			flag=false; //쓰레드 dead 상태로 두기 
			boodyServer.clientList.remove(this);//명단에서도 제거
			boodyServer.area.append("현재까지 참여자 수 : "+boodyServer.clientList.size()+"\n");
		}
	}
	
	//서버에서 클라이언트들의 메시지를 받아 모두에게 뿌려줘야함 (브로드캐스팅)
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
