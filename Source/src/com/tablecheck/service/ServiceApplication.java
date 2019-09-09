package com.tablecheck.service;

import java.io.IOException;
import java.net.Socket;
import java.util.Properties;

import org.springframework.beans.PropertyEditorRegistrySupport;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.tablecheck.service.controller.ServiceController;

@Configuration
@ComponentScan("com.tablecheck")
@SpringBootApplication
public class ServiceApplication {
        public static void exitApplication(ConfigurableApplicationContext ctx) {
            int exitCode = SpringApplication.exit(ctx, new ExitCodeGenerator() {
             @Override
             public int getExitCode() {
              // no errors
              return 0;
             }
            });
            System.exit(exitCode);
        }
	public static void main(String[] args) throws IOException {
        //faro lines from here to start   
		System.out.println("for code strat from here");
		//PropertyEditorRegistrySupport.class.getClassLoader();
		
		/*
		 * FileSystemXmlApplicationContext context= new
		 * FileSystemXmlApplicationContext("bean2.xml");
		 * 
		 * ServiceController hello1 =(ServiceController)context.getBean("sController");
		 */
		
		// end 
		String ip = "127.0.0.1";
            int port=8080;
            if (args.length==2) {
                ip = args[0];
                port = Integer.parseInt(args[1]);
            }
             // faro lines 
           
		/*
		 * Socket socket1 = new Socket(ip, port); if(socket1 !=null) {
		 * 
		 * System.out.println("values here "); }else { System.out.println("im zero "); }
		 */
            try (Socket socket = new Socket(ip, port)) {
                socket.close();
                System.out.println(">>>>> spring port is already created! port: " + port);
            } catch (Exception e) {
                SpringApplication app = new SpringApplication(ServiceApplication.class);
                Properties properties = new Properties();
                properties.setProperty("spring.resources.staticLocations", "classpath:/pages/");
                properties.setProperty("server.error.whitelabel.enabled", "false");
                app.setDefaultProperties(properties);
                app.run(args);
                //application.addListeners(new ApplicationPidFileWriter("./bin/app.pid"));
                //ConfigurableApplicationContext ctx = app.run(args);
                //exitApplication(ctx);
                System.out.println(">>>>> Spring App started >>>");
            }
//            SpringApplication app = new SpringApplication(ServiceApplication.class);
//            Properties properties = new Properties();
//            properties.setProperty("spring.resources.staticLocations", "classpath:/pages/");
//            properties.setProperty("server.error.whitelabel.enabled", "false");
//            app.setDefaultProperties(properties);
//            app.run(args);
//            System.out.println(">>>>> Spring App started >>>");

//            MulticastPublisher publisher = new MulticastPublisher();
//            new Thread(publisher).start();
	}

	// @Bean
	public FilterRegistrationBean requestFilterRegistrationBean() {
            FilterRegistrationBean registrationBean = new FilterRegistrationBean();
            registrationBean.setName("RequestFilter");
            RequestFilter greetingFilter = new RequestFilter();
            registrationBean.setFilter(greetingFilter);
            registrationBean.setOrder(1);
            return registrationBean;
	}

}
