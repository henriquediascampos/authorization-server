package com.br.authorizationserver.core.banner;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;

public class CustomBanner implements Banner {
    private static final String TEXT_RESET = "\u001B[0m";
    private static final String TEXT_GREEN = "\u001B[32m";
    private static final String TEXT_MAGENTA= "\u001b[35m";
    
    protected final String TITLE ="\n" +""+
            "\n" +"    _   _   _ _____ _   _  ___  ____  ___ _____   _  _____ ___ ___  _   _      ____  _____ ______     _______ ____  "+
            "\n" +"   / \\ | | | |_   _| | | |/ _ \\|  _ \\|_ _|__  /  / \\|_   _|_ _/ _ \\| \\ | |    / ___|| ____|  _ \\ \\   / | ____|  _ \\ "+
            "\n" +"  / _ \\| | | | | | | |_| | | | | |_) || |  / /  / _ \\ | |  | | | | |  \\| |____\\___ \\|  _| | |_) \\ \\ / /|  _| | |_) |"+
            "\n" +" / ___ | |_| | | | |  _  | |_| |  _ < | | / /_ / ___ \\| |  | | |_| | |\\  |________) | |___|  _ < \\ V / | |___|  _ < "+
            "\n" +"/_/   \\_\\___/  |_| |_| |_|\\___/|_| \\_|___/____/_/   \\_|_| |___\\___/|_| \\_|    |____/|_____|_| \\_\\ \\_/  |_____|_| \\_\\"+
            "\n" +"                                                                                                                    ";

    @Override
    public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
        out.println(TEXT_MAGENTA);
        out.println(TITLE);
        out.println(TEXT_GREEN);
        out.println(collectEnvironmentInfo(environment, sourceClass));
        out.println(TEXT_RESET);
        
    }

    private String collectEnvironmentInfo(final Environment environment, final Class<?> sourceClass) {
        
        final var properties = System.getProperties();
        final Map<String, Object> info = new HashMap<>();

        info.put("Java Home", "\t\t"+properties.get("java.home"));
        info.put("Java Vendor", "\t\t"+properties.get("java.vendor"));
        info.put("Java Version", "\t\t"+properties.get("java.version"));
        
        info.put("OS Architecture", "\t"+properties.get("os.arch"));
        info.put("OS Name", "\t\t"+properties.get("os.name"));
        info.put("OS Version", "\t\t"+properties.get("os.version"));
        info.put("OS Date/Time", "\t\t"+LocalDateTime.now(ZoneId.systemDefault()));
        
        final var runtime = Runtime.getRuntime();
        info.put("JVM Free Memory", "\t"+runtime.freeMemory());
        info.put("JVM Maximum Memory", "\t"+runtime.maxMemory());
        info.put("JVM Total Memory", "\t"+runtime.totalMemory());

        info.put("Spring Boot Version", "\t"+SpringBootVersion.getVersion());
        info.put("Spring Version", "\t"+SpringVersion.getVersion());

        return info.entrySet().stream()
            .map(prop -> String.format("\n%s: %s", prop.getKey(), prop.getValue().toString()))
            .sorted()
            .reduce((accu, curr) -> accu + curr).orElse("other");
    }

}
