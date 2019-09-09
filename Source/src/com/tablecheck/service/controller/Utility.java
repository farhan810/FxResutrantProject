package com.tablecheck.service.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import tblcheck.model.Config;

public class Utility {

	private static String getDirectory() {
		return Config.getInstance()
				.getLogDirectory();/*
									 * File dir = new File(
									 * Utility.class.getResource("") .getFile().
									 * replace(
									 * "com/tablecheck/service/controller", ""))
									 * .getParentFile();
									 */
		// return dir;
		// if(new File(dir.getAbsolutePath() + "\\run.bat").exists()){
		// re
		// }
	}

	public static boolean emailFiles(String file, String startDt, String endDt, String email) {
		String dts = convertDate(startDt);
		String dte = convertDate(endDt);
		List<String> fileNames = new ArrayList<>();
		if (dts != null && dte != null) {
			File directory = new File(getDirectory());
			// System.out.println(directory.getAbsolutePath());
			File[] files = null;
			if ("table".equalsIgnoreCase(file) || "staff".equalsIgnoreCase(file)) {
				files = directory.listFiles((d, s) -> {
					return s.toLowerCase().startsWith(file + "_log");
				});
			} else {
				files = directory.listFiles((d, s) -> {
					return s.toLowerCase().startsWith("table_log") || s.toLowerCase().startsWith("staff_log");
				});
			}
			// System.out.println(files);
			for (File fi : files) {
				fileNames.add(fi.getName());
			}
		} else if (dts != null && dte == null) {
			if ("table".equalsIgnoreCase(file)) {
				fileNames.add("table_log" + dts + ".txt");
			} else if ("staff".equalsIgnoreCase(file)) {
				fileNames.add("staff_log" + dts + ".txt");
			} else {
				File directory = new File(getDirectory());
				File[] files = directory.listFiles((d, s) -> {
					return s.toLowerCase().equalsIgnoreCase("table_log" + dts + ".txt")
							|| s.toLowerCase().equalsIgnoreCase("staff_log" + dts + ".txt");
				});
				for (File fi : files) {
					fileNames.add(fi.getName());
				}
			}
		}
		return sendEmail(email, fileNames);
	}

	public static byte[] downloadFiles(String file, String startDt, String endDt, StringBuilder type) {
		String dts = convertDate(startDt);
		String dte = convertDate(endDt);
		if (dts != null && dte != null) {
			File directory = new File(getDirectory());
			System.out.println(directory.getAbsolutePath());
			File[] files = null;

			if ("table".equalsIgnoreCase(file) || "staff".equalsIgnoreCase(file)) {
				files = directory.listFiles((d, s) -> {
					return s.toLowerCase().startsWith(file + "_log");
				});
			} else {
				files = directory.listFiles((d, s) -> {
					return s.toLowerCase().startsWith("table_log") || s.toLowerCase().startsWith("staff_log");
				});
			}
			System.out.println(files);

			type.append(".zip");
			return generateZip(startDt, endDt, files, type);
		} else if (dts != null && dte == null) {
			if ("table".equalsIgnoreCase(file)) {
				type.append("table_log" + dts + ".txt");
				return readFileContent("table_log" + dts + ".txt");
			} else if ("staff".equalsIgnoreCase(file)) {
				type.append("staff_log" + dts + ".txt");
				return readFileContent("staff_log" + dts + ".txt");
			} else {
				File directory = new File(getDirectory());
				File[] files = directory.listFiles((d, s) -> {
					return s.toLowerCase().equalsIgnoreCase("table_log" + dts + ".txt")
							|| s.toLowerCase().equalsIgnoreCase("staff_log" + dts + ".txt");
				});
				type.append(".zip");
				return generateZip(startDt, endDt, files, type);
			}
		}

		return null;
	}

	private static byte[] generateZip(String startDt, String endDt, File[] files, StringBuilder type) {
		System.out.println(files);
		if (files != null && files.length > 0) {
			System.out.println(files);
			Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
			Date dtEnd = convertToDate(endDt, true);
			Date dtStart = convertToDate(startDt, false);
			System.out.println(dtEnd + " " + dtStart);

			try {
				List<File> filFiles = new ArrayList<>();
				for (final File f : files) {
					System.out.println(f.getName());
					if (f.isFile() && f.getName().contains("_log")) {

						String dt = f.getName().substring(f.getName().indexOf("_log") + 4, 17);
						System.out.println("dt " + dt);
						Date mod = null;
						try {
							mod = new SimpleDateFormat("ddMMyyyy").parse(dt);
							System.out.println("dt mod " + mod);
							if (mod.equals(dtStart) || mod.equals(dtEnd) || (mod.before(dtEnd) && mod.after(dtStart))) {
								System.out.println("dt " + dtStart + " fff " + f.getName());
								filFiles.add(f);
							}
						} catch (ParseException e) {
							e.printStackTrace();
						}
						// long date =
						// Long.parseLong(f.getName().substring(f.getName().indexOf("_log")
						// + 4, 17));
						// Date mod = new Date(date);//f.lastModified());
					}

				}
				System.out.println("filesize " + filFiles.size());
				String fiName = "searched" + System.nanoTime() + ".zip";
				if (filFiles.size() < 1) {
					return null;
				} else if (filFiles.size() == 1) {
					type.setLength(0);
					type.append(filFiles.get(0).getName());
					System.out.println("filesize  rypw " + type);
					return Files.readAllBytes(Paths.get(filFiles.get(0).getAbsolutePath()));
				} else {

					FileOutputStream fos = new FileOutputStream(getDirectory() + fiName);
					ZipOutputStream zipOut = new ZipOutputStream(fos);
					for (File f : filFiles) {
						System.out.println("filesize  zoip " + f);
						FileInputStream fis = new FileInputStream(f);
						ZipEntry zipEntry = new ZipEntry(f.getAbsolutePath());
						zipOut.putNextEntry(zipEntry);

						byte[] bytes = new byte[1024];
						int length;
						while ((length = fis.read(bytes)) >= 0) {
							zipOut.write(bytes, 0, length);
						}
						fis.close();
					}
					System.out.println("filesize  comes out");
					zipOut.close();
					fos.close();
				}
				return Files.readAllBytes(Paths.get(getDirectory() + fiName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("filesize  null");
		return null;
	}

	private static String convertDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date dt = null;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			try {
				sdf.applyPattern("MM/dd/yyyy");
				dt = sdf.parse(date);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		if (dt != null) {
			sdf.applyPattern("ddMMyyyy");
			String dts = sdf.format(dt);
			return dts;
		}
		return null;
	}

	private static Date convertToDate(String date, boolean isEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
		try {
			date = convertDate(date);
			if (date != null) {
				Date dt = sdf.parse(date);
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				if (isEnd) {
					c.set(Calendar.HOUR_OF_DAY, 23);
					c.set(Calendar.MINUTE, 59);
					c.set(Calendar.SECOND, 59);
				} else {
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
				}
				return c.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// private void getDates() {
	// File directory = new File(Utility.class.getResource("").getPath());
	// File[] files = directory.listFiles();
	// Arrays.sort(files, Comparator.comparingLong(File::lastModified));
	//
	//
	// final Map<File, Long> staticLastModifiedTimes = new HashMap<File,
	// Long>();
	// for (final File f : Files.) {
	// staticLastModifiedTimes.put(f, f.lastModified());
	// }
	// Collections.sort(files, new Comparator<File>() {
	// @Override
	// public int compare(final File f1, final File f2) {
	// return
	// staticLastModifiedTimes.get(f1).compareTo(staticLastModifiedTimes.get(f2));
	// }
	// });
	// }

	public static List<String> readFile(String fileName) {
		List<String> lst = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			lst = stream.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lst;
	}

	public static byte[] readFileContent(String fileName) {

		try {
			return Files.readAllBytes(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "No activity on that day".getBytes();
	}

	public static boolean appendToFile(String fileName, String content) {
		try {
			if (!(new File(getDirectory() + fileName)).exists()) {
				writeFileContent(getDirectory() + fileName, null);
			}
			Files.write(Paths.get(getDirectory() + fileName), ("\n" + content).getBytes(), StandardOpenOption.APPEND);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean writeFileContent(String fileName, byte[] bytes) {

		try {
			deleteFile(fileName);
			Files.write(Paths.get(fileName), bytes != null ? bytes : "".getBytes(), StandardOpenOption.CREATE);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteFile(String fileName) {
		try {
			if ((new File(getDirectory() + fileName)).exists()) {
				Files.delete(Paths.get(getDirectory() + fileName));
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean deleteFiles(Date dtBefore) {
		try {
			File directory = new File(getDirectory());
			final File[] listFiles = directory.listFiles((d, s) -> {
				return (s.toLowerCase().endsWith(".zip") && s.toLowerCase().contains("searched"))
						|| s.toLowerCase().startsWith("table_log") || s.toLowerCase().startsWith("staff_log");
			});
			if (listFiles != null) {
				for (File file : listFiles) {
					if (file.isFile() && ((file.getName().endsWith(".zip") && file.getName().contains("searched"))
							|| ((file.getName().startsWith("table_log") || file.getName().startsWith("Staff_log"))
									&& file.lastModified() < dtBefore.getTime()))) {
						Files.delete(Paths.get(file.getName()));
					}
				}
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean sendEmail(String toEmail, List<String> fileNames) {

		Properties props = new Properties();
		if (Config.getInstance().getEmail_smtp_host() != null)
			props.put("mail.smtp.host", Config.getInstance().getEmail_smtp_host());
		if (Config.getInstance().getEmail_protocol() != null)
			props.put("mail.transport.protocol", Config.getInstance().getEmail_protocol());
		if (Config.getInstance().isEmail_tls_enabled())
			props.put("mail.smtp.starttls.enable", "true");
		if (Config.getInstance().getEmail_smtp_port() > 0)
			props.put("mail.smtp.port", Config.getInstance().getEmail_smtp_port());
		Session session = Session.getDefaultInstance(props);

		try {
			InternetAddress fromAddress = new InternetAddress(Config.getInstance().getEmail_from());
			InternetAddress toAddress = new InternetAddress(toEmail);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(fromAddress);
			msg.setRecipient(Message.RecipientType.TO, toAddress);
			msg.setSubject(Config.getInstance().getEmail_sub());
			msg.setSentDate(new Date());

			Multipart multipart = new MimeMultipart();
			MimeBodyPart messagePart = new MimeBodyPart();
			messagePart.setText(Config.getInstance().getEmail_content());
			multipart.addBodyPart(messagePart);

			for (String fileName : fileNames) {
				FileDataSource fileDataSource = new FileDataSource(fileName);
				MimeBodyPart attachmentPart = new MimeBodyPart();
				attachmentPart.setDataHandler(new DataHandler(fileDataSource));
				attachmentPart.setFileName(fileDataSource.getName());
				multipart.addBodyPart(attachmentPart);
			}
			msg.setContent(multipart);
			if (Config.getInstance().getEmail_smtp_username() != null) {
				Transport.send(msg, Config.getInstance().getEmail_smtp_username(),
						Config.getInstance().getEmail_smtp_password());
				return true;
			} else {
				Transport.send(msg);
				return true;
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		byte[] b =readFileContent("tablestat.json");
		System.out.println("staff_log28092017.txt".indexOf("_log") + 4);
		System.out.println("staff_log28092017.txt".substring(9, 17));
	}

	public static void mains(String[] args) {
		// Recipient's email ID needs to be mentioned.
		String to = "tablecheckemail@gmail.com";// change accordingly

		// Sender's email ID needs to be mentioned
		String from = "tablecheckemail@gmail.com";// change accordingly
		final String username = "tablecheckemail@gmail.com";// change
															// accordingly
		final String password = "kcehcelbat";// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net
		String host = "smtp.gmail.com";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "587");

		// Get the Session object.
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Testing Subject");

			// Now set the actual message
			message.setText("Hello, this is sample for to check send " + "email using JavaMailAPI ");

			// Send message
			Transport.send(message);

			//System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
