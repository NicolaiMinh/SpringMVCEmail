package com.spring.mvc.email;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

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

}
