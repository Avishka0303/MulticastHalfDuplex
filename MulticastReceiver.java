import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;

public class MulticastReceiver extends Thread{

    private boolean isOnline = true;

    private DatagramPacket datagramPacket;
    private MulticastSocket multicastSocket;
    private InetAddress localHost;
    private InetAddress groupIP;

    public static byte[] voiceBuffer;
    private byte[] buffer;

    public MulticastReceiver() {
        readyReceiver();
    }

    public synchronized byte[] getVoiceBuffer() {
        return voiceBuffer;
    }

    private void readyReceiver() {

        try {
            /*Set the group ip for the socket.*/
            groupIP = InetAddress.getByName(ProgramData.GROUP_IP);
            buffer = new byte[ ProgramData.RECEIVE_BUFFER_SIZE ];
            multicastSocket = new MulticastSocket(ProgramData.MUL_PORT_NUMBER);
            datagramPacket = new DatagramPacket(buffer, ProgramData.RECEIVE_BUFFER_SIZE);
            //join with group.
            multicastSocket.joinGroup(groupIP);

        } catch (SocketException e) {
            System.out.println("Socket cannot be used : PORT : "+ProgramData.PORT_NUMBER);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){

        while (isOnline){

            System.out.println("Server is online");

            try{

                //--------------------- Recieve the data packet----------------------------------
                multicastSocket.receive(datagramPacket);
                System.out.println("Yeah i am in.");

                //--------------------- Deserialize the object ----------------------------------
                ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
                ObjectInputStream inputObject = new ObjectInputStream(inputStream);
                DataPacket packet = (DataPacket)inputObject.readObject();

                //--------------------- generate the hashcode for the user ----------------------
                InetAddress senderIp = datagramPacket.getAddress();

                //--------------------------------------------------------------------------------
                int pIndex = packet.packetIndex;

                System.out.println(pIndex);
                synchronized (voiceBuffer){
                    voiceBuffer = packet.voice_buffer;
                    VoicePlay.isDataReady =true;
                }

            }catch(IOException ex){
                System.out.println("Error in multicast receive.");
                ex.printStackTrace();
            }catch(ClassNotFoundException ex1){
                System.out.println("Error in read object");
            }
        }

        multicastSocket.close();
    }
}
