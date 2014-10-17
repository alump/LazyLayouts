package org.vaadin.alump.lazylayouts.demo;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Class used to generate components to layout
 */
public class ComponentGenerator {

    private static Random rand = new Random(0xDEADBEEF);

    public final static String BACON_IPSUM = "bacon ipsum dolor sit amet beef pancetta boudin pork loin rump t-bone "
            + "bresaola chicken ground round corned beef shoulder landjaeger t-bone turducken pig swine beef boudin "
            + "hamburger bresaola ground round cow pastrami shankle beef sirloin porchetta hamburger frankfurter "
            + "biltong turducken drumstick rump shankle ham hock sirloin tongue shank jerky turkey capicola short "
            + "loin shank capicola turkey meatloaf";

    public static Component createComponent(int index, int outOf) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.addStyleName("lazy-loaded-layout");
        layout.setSpacing(true);
        layout.setWidth("100%");

        Image image = new Image();
        image.setHeight("158px");
        image.setWidth("133px");
        image.setSource(new ThemeResource("images/pete.png"));
        image.addStyleName("lazy-loaded-image");
        layout.addComponent(image);

        VerticalLayout rightSide = new VerticalLayout();
        rightSide.setSpacing(true);
        layout.addComponent(rightSide);
        layout.setExpandRatio(rightSide, 1.0f);
        Label label = new Label("Hello World #" + index + " / " + outOf);
        label.addStyleName("header-label");
        rightSide.addComponent(label);

        label = new Label(generateText());
        rightSide.addComponent(label);

        label = new Label(generateTimestamp());
        label.addStyleName("timestamp");
        rightSide.addComponent(label);

        return layout;
    }

    public static String generateTimestamp() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    public static String generateText() {
        return generateText(15, 100);
    }

    public static String generateText(int minWords, int maxWords) {
        int words = minWords + rand.nextInt(maxWords - minWords);

        StringBuilder sb = new StringBuilder();
        String dict[] = BACON_IPSUM.split("\\s");
        for(int i = 0; i < words; ++i) {
            if(i > 0) {
                sb.append(" ");
            }
            sb.append(dict[rand.nextInt(dict.length)]);
        }
        return sb.toString();
    }
}
