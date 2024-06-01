package com.semester.tinder;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Objects;

@SpringBootApplication
public class TinderApplication {

	public static void main(String[] args) {

//    ClassLoader classLoader =TinderApplication.class.getClassLoader();
//		File file =new File(Objects.requireNonNull(classLoader.getResource("serviceAccountKey.json")).getFile());
InputStream inputStream = TinderApplication.class.getClassLoader().getResourceAsStream("serviceAccountKey.json");
try {
	//FileInputStream serviceAccount =new FileInputStream(file.getAbsolutePath());

	// config connect for project:
    assert inputStream != null;
    FirebaseOptions options = new FirebaseOptions.Builder()
			.setCredentials(GoogleCredentials.fromStream(inputStream)) // config credentials contains data.
			.setStorageBucket("tinder-b4b61.appspot.com") // config storage contains files.
			.build();

	FirebaseApp.initializeApp(options);

}catch (Exception e){
throw new RuntimeException(e.getMessage());
}

		SpringApplication.run(TinderApplication.class, args);


	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000");
			}
		};
	}

}
