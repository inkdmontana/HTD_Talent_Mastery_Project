package htd.mastery;

import htd.mastery.data.*;
import htd.mastery.domain.GuestService;
import htd.mastery.domain.HostService;
import htd.mastery.domain.ReservationService;
import htd.mastery.ui.ConsoleIO;
import htd.mastery.ui.Controller;
import htd.mastery.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


@ComponentScan
@PropertySource("classpath:data.properties")
public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(App.class);
        Controller controller = context.getBean(Controller.class);
        controller.run();
        }
    }