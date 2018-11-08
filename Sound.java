import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
    public static void play (String fileName)
    {
        try {
            AudioInputStream inputStream = AudioSystem.getAudioInputStream (new File(fileName));
            
            Clip clip = AudioSystem.getClip();
            clip.stop();
            clip.open(inputStream);
            clip.start();
        }
        catch (Exception e) {
        		System.out.println("Exception!");
        }
    }
    
}
