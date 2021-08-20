package by.prus.LabProject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {

    @Autowired
    private Environment env; // у хибернейта тоже есть Environment. Не путать!

    public String getTokenSecret(){
        return env.getProperty("tokenSecret");
    }
}
