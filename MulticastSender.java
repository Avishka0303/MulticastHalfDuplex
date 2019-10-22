import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MulticastSender extends Thread{

    private DatagramSocket socket;
    private InetAddress groupAddress;
    private boolean isSenderOnline = true;

    public MulticastSender() {
        readySender();
    }

    private void readySender() {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            System.out.println("Socket cannot be used.");
        }
    }

    @Override
    public void run() {

        int packetCount=0;

        while (isSenderOnline){

            try {

                if(VoiceCapture.dataReady){

                    if(packetCount==129) packetCount = 0;

                    //-------------------------Serialize the data packet---------------------.
                    DataPacket packet;
                    synchronized (VoiceCapture.tempBuffer){
                        packet = new DataPacket( (packetCount++)%ProgramData.MEM_SIZE ,VoiceCapture.tempBuffer );
                        VoiceCapture.dataReady=false;
                    }
                    ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                    ObjectOutputStream outputObject = new ObjectOutputStream(byteOutput);

                    outputObject.writeObject(packet);
                    outputObject.flush();

                    byte[] objectData = byteOutput.toByteArray();
                    DatagramPacket dataPacket = new DatagramPacket(objectData,objectData.length,groupAddress,ProgramData.MUL_PORT_NUMBER);
                    socket.send(dataPacket);
                    System.out.println("sent p "+packetCount);

                }

            } catch (IOException ex) {
                System.out.println("Error in multicast packet sending.");
            }

        }
    }

}
