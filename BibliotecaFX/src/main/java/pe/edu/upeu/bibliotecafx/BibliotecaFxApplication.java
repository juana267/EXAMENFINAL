package pe.edu.upeu.bibliotecafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class BibliotecaFxApplication extends Application {

	private static ConfigurableApplicationContext configurableApplicationContext;
	private Parent parent;


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(BibliotecaFxApplication.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		configurableApplicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/main_principal.fxml"));
		fxmlLoader.setControllerFactory(configurableApplicationContext::getBean);
		parent= fxmlLoader.load();
	}
	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(parent);
		scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		stage.setScene(scene);
		stage.setTitle("Biblioteca Spring Java-FX");
		stage.setResizable(false);
		stage.show();
	}
	@Override
	public void stop() throws Exception {
		configurableApplicationContext.close();
	}


	/*@Bean
	public CommandLineRunner run(ApplicationContext context) { return args -> {
		//mx = context.getBean(MainX.class);
		MainY mx = context.getBean(MainY.class);
		mx.menu();
		};
	}*/




}
