package com.spring.mvc.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class EmailController {

	final String APIKey="6CAA20423FDF8159B27CB6620B0A07";//Dang ky tai khoan tai esms.vn de lay Key
	final String SecretKey="78229B4D4CBAA86971C2F284BF099E"; 

	public static String emailToReceive, emailSubject, emailMessage;// dia chi
																	// nguoi
																	// nhan,emailsubject,
																	// email
																	// message
	public static String emailFromSend = "norelay@systemgear-vietnam.com";// dia chi
																	// nguoi gui

	static ModelAndView modelAndView;


	@Autowired
	private JavaMailSender mailSender;// JavaMailSender o library

	@RequestMapping(value = { "/", "emailForm" }, method = RequestMethod.GET)
	public ModelAndView showEmailForm() {
		modelAndView = new ModelAndView("emailForm");// request to emailForm.jsp
		return modelAndView;
	}

	@RequestMapping(value = { "messageForm" }, method = RequestMethod.GET)
	public ModelAndView showMessageForm() {
		modelAndView = new ModelAndView("messageForm");// request to emailForm.jsp
		return modelAndView;
	}

	// This Method Is Used To Prepare The Email Message And Send It To The
	// Client
	@RequestMapping(value = "sendEmail", method = RequestMethod.POST)
	public ModelAndView sendEmailToClient(HttpServletRequest request, // reuest
																		// den
																		// mailserver
			final @RequestParam LinkedList<CommonsMultipartFile>  attachFileObj) {// CommonsMultipartFile
																		// ho
																		// tro
																		// attach
																		// file

		// Reading Email Form Input Parameters From View
		// get by name="mailTo" from jsp
		emailSubject = request.getParameter("subject");
		emailToReceive = request.getParameter("mailTo");
		emailMessage = request.getParameter("message");
		// Logging The Email Form Parameters For Debugging Purpose
		System.out.println("\nEmail receive?= " + emailToReceive + ", Subject?= " + emailSubject + ", Message?= "
				+ emailMessage + "\n");


		// send message action
		mailSender.send(new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				// TODO Auto-generated method stub
				MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelper.setTo(emailToReceive);// send to emailReceive
				mimeMessageHelper.setFrom(emailFromSend);// send from email send
//				mimeMessageHelper.setText(emailMessage, true);// send text to
				mimeMessageHelper.setText("aa5a5a55", emailMessage);// emailMessage
				mimeMessageHelper.setSubject(emailSubject);// email subject

				// Determine If There Is An File Upload. If Yes, Attach It To
				// The Client Email
				if ((attachFileObj != null) && (attachFileObj.size() > 0) && (!attachFileObj.equals(""))) {

					for (CommonsMultipartFile commonsMultipartFile : attachFileObj) {
						mimeMessageHelper.addAttachment(commonsMultipartFile.getOriginalFilename(), new InputStreamSource() {
												@Override
												public InputStream getInputStream() throws IOException {
													// TODO Auto-generated method stub
													return commonsMultipartFile.getInputStream();

												}
											});
					}
//					System.out.println("\nAttachment Name?= " + attachFileObj.getOriginalFilename() + "\n");
//					mimeMessageHelper.addAttachment(attachFileObj.getOriginalFilename(), new InputStreamSource() {
//
//						@Override
//						public InputStream getInputStream() throws IOException {
//							// TODO Auto-generated method stub
//							return attachFileObj.getInputStream();
//
//						}
//					});
				} else {
					System.out.println("\nNo Attachment Is Selected By The User. Sending Text Email!\n");
				}

			}
		});
		System.out.println("\nMessage Send Successfully.... Hurrey!\n");
		modelAndView = new ModelAndView("success", "messageObj", "Thank You! Your Email Has Been Sent!");
		return modelAndView;
	}


	@RequestMapping(value = { "sendMessage" }, method = RequestMethod.POST)
	public ModelAndView sendMessage(HttpServletRequest request) {
		String phoneNumber = request.getParameter("messageTo");
		String messageText = request.getParameter("messageText");
		System.out.println("\nPhone number to?= " + phoneNumber +  ", Message?= "
				+ messageText + "\n");

		try {
			sendGetJSON(phoneNumber, messageText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("\nMessage Send Successfully.... Hurrey!\n");
		modelAndView = new ModelAndView("success", "messageObj", "Thank You! Your Message Has Been Sent!");
		return modelAndView;
	}

	public String sendGetJSON(String phoneNumber, String message) throws IOException {

		String url = "http://rest.esms.vn/MainService.svc/json/SendMultipleMessage_V4_get?ApiKey=" + URLEncoder.encode(APIKey, "UTF-8") + "&SecretKey=" + URLEncoder.encode(SecretKey, "UTF-8") + "&SmsType=8&Phone=" + URLEncoder.encode(phoneNumber, "UTF-8") + "&Content=" + URLEncoder.encode(message, "UTF-8");

		URL obj;
		try {
			obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			//you need to encode ONLY the values of the parameters

			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
			if(responseCode==200)//Ä�Ã£ gá»�i URL thÃ nh cÃ´ng, tuy nhiÃªn báº¡n pháº£i tá»± kiá»ƒm tra CodeResult xem tin nháº¯n cÃ³ gá»­i thÃ nh cÃ´ng khÃ´ng, vÃ¬ cÃ³ thá»ƒ tÃ i khoáº£n báº¡n khÃ´ng Ä‘á»§ tiá»�n thÃ¬ sáº½ tháº¥t báº¡i
			{
				System.out.println("Send message succesfull!!");
				//Check CodeResult from response
			}else{
				System.out.println("Send message fail!!");
			}
			//Ä�á»�c Response
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			JSONObject json = (JSONObject)new JSONParser().parse(response.toString());
			System.out.println("CodeResult=" + json.get("CodeResult"));
			System.out.println("SMSID=" + json.get("SMSID"));
			System.out.println("ErrorMessage=" + json.get("ErrorMessage"));
		//document.getElementsByTagName("CountRegenerate").item(0).va
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "SUCCESS";

	}
}
