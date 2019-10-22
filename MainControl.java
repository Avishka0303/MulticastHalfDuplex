import org.omg.PortableServer.THREAD_POLICY_ID;

import javax.sound.sampled.AudioFormat;
import java.util.Scanner;

public class MainControl {


    public static void main(String[] args) throws InterruptedException {

        Scanner scan = new Scanner(System.in);
        //check sender or receiver
        System.out.println("Your group ip is : "+ProgramData.GROUP_IP);
        System.out.println("Group member type: Sender-Enter s , Receiver-Enter r");

        switch (scan.next()){
            case "s":
                System.out.println("Sender Selected.");
                senderReady();
                break;
            case "r":
                System.out.println("Receiver Selected. ");
                receiverReady();
                break;
            default:
                System.out.println("Enter a valid character.");
                break;
        }
    }

    private static void senderReady() throws InterruptedException {
        VoiceCapture capture = new VoiceCapture();
        capture.start();
        Thread.sleep(1200);
        MulticastSender sender = new MulticastSender();
        sender.start();
    }

    private static void receiverReady() throws InterruptedException {
        VoicePlay play = new VoicePlay();
        play.start();
        Thread.sleep(1200);
        MulticastReceiver receiver = new MulticastReceiver();
        receiver.start();
    }

    public static AudioFormat getAudioFormat() {
        float sampleRate = 16000.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }
}
