public class ProgramData {

    private ProgramData(){
        //restrict the instantiation
    }
    public static final String GROUP_IP = "225.4.5.6";
    public static final int PACKET_SIZE = 512;
    public static final int MEM_SIZE = 16;   //16 slots for store bytes.
    public static final int PORT_NUMBER = 2000;
    public static final int MUL_PORT_NUMBER = 4446;
    public static final int RECEIVE_BUFFER_SIZE = PACKET_SIZE*2;

}
