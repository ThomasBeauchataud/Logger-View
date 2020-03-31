package com.github.ffcfalcos.logger.view.util;

import com.github.ffcfalcos.logger.view.LoggerView;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ResourcesService {

    public static Image getImageFromResourcesName(String resourceName) {
        BufferedImage buff;
        try {
            buff = ImageIO.read(LoggerView.class.getResourceAsStream(resourceName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return SwingFXUtils.toFXImage(buff, null);
    }

}
