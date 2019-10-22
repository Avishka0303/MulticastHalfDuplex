import sun.applet.Main;

import javax.sound.sampled.*;

public class VoicePlay extends Thread{

    SourceDataLine sourceDataLine;
    AudioFormat audioFormat;
    boolean isOnline = true;
    static boolean isDataReady = false;

    public VoicePlay() {
        readyPlaySetup();
    }

    private void readyPlaySetup() {

        try {
            audioFormat = MainControl.getAudioFormat();

            DataLine.Info dataLineInfo1 = new DataLine.Info(SourceDataLine.class, audioFormat);
            sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo1);
            sourceDataLine.open(audioFormat);
            sourceDataLine.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

        //Setting the maximum volume
        FloatControl control = (FloatControl)sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
        control.setValue(control.getMaximum());
    }

    @Override
    public void run() {
        while(isOnline){
            synchronized (VoicePlay.class){
                if(isDataReady){
                    sourceDataLine.write(MulticastReceiver.voiceBuffer , 0, ProgramData.PACKET_SIZE);
                    isDataReady = false;
                }
            }
        }
    }
}
