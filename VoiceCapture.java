import javax.sound.sampled.*;

public class VoiceCapture extends Thread{

    boolean stopCapture = false;
    AudioFormat audioFormat;
    TargetDataLine targetDataLine;

    static boolean dataReady = false;
    static byte tempBuffer[] = new byte[ProgramData.PACKET_SIZE];

    //Initialize the mic of the system
    public VoiceCapture() {
        readyCaptureSetup();
    }

    private void readyCaptureSetup() {

        //take available mixers in the audio stream.
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        System.out.println("Available mixers:");

        Mixer mixer = null;
        for (int cnt = 0; cnt < mixerInfo.length; cnt++) {

            System.out.println(cnt + " " + mixerInfo[cnt].getName());
            mixer = AudioSystem.getMixer(mixerInfo[cnt]);

            Line.Info[] lineInfos = mixer.getTargetLineInfo();

            if (lineInfos.length >= 1 && lineInfos[0].getLineClass().equals(TargetDataLine.class)) {
                System.out.println(cnt + " Mic is supported!");
                break;
            }

        }

        //get the audio format.
        audioFormat = MainControl.getAudioFormat();

        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);

        try {

            targetDataLine = (TargetDataLine) mixer.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();

        } catch (LineUnavailableException e) {
            System.out.println("Line is not available");
            System.exit(1);
        }

    }

    @Override
    public void run(){
        System.out.println("Mic is online now.....");
        stopCapture = false;
        int readCount;
        while (!stopCapture) {
            synchronized (tempBuffer){
                readCount = targetDataLine.read(tempBuffer, 0, tempBuffer.length);  //capture sound into tempBuffer
                if (readCount > 0) {
                    dataReady=true;
                }else
                    dataReady = false;
            }
        }
    }
}
