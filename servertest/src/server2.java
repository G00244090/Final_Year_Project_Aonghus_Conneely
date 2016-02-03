// Server that receives and sends packets from/to a client.
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class server2 extends JFrame {
    private JTextArea displayArea;
    private DatagramSocket socket;

    // set up GUI and DatagramSocket
    public server2()
    {
        super( "Server" );

        displayArea = new JTextArea();
        getContentPane().add( new JScrollPane( displayArea ),
                BorderLayout.CENTER );
        setSize( 400, 300 );
        setVisible( true );

        // create DatagramSocket for sending and receiving packets
        try {
            socket = new DatagramSocket( 27 );
        }

        // process problems creating DatagramSocket
        catch( SocketException socketException ) {
            socketException.printStackTrace();
            System.exit( 1 );
        }

    } // end Server constructor

    // wait for packets to arrive, display data and echo packet to client
    private void waitForPackets()
    {
        while ( true ) { // loop forever

            // receive packet, display contents, return copy to client
            try {

                // set up packet
                byte data[] = new byte[ 100 ];
                DatagramPacket receivePacket =
                        new DatagramPacket( data, data.length );

                socket.receive( receivePacket ); // wait for packet

                // display information from received packet
                displayMessage( "\nPacket received:" +
                        "\nFrom host: " + receivePacket.getAddress() +
                        "\nHost port: " + receivePacket.getPort() +
                        "\nLength: " + receivePacket.getLength() +
                        "\nContaining:\n\t" + new String( receivePacket.getData(),
                        0, receivePacket.getLength() ) );

                sendPacketToClient( receivePacket ); // send packet to client
            }

            // process problems manipulating packet
            catch( IOException ioException ) {
                displayMessage( ioException.toString() + "\n" );
                ioException.printStackTrace();
            }

        } // end while

    } // end method waitForPackets

    // echo packet to client
    private void sendPacketToClient( DatagramPacket receivePacket )
            throws IOException
    {
        displayMessage( "\n\nEcho data to client..." );

        // create packet to send
        DatagramPacket sendPacket = new DatagramPacket(
                receivePacket.getData(), receivePacket.getLength(),
                receivePacket.getAddress(), receivePacket.getPort() );

        socket.send( sendPacket ); // send packet
        displayMessage( "Packet sent\n" );
    }

    // utility method called from other threads to manipulate
    // displayArea in the event-dispatch thread
    private void displayMessage( final String messageToDisplay )
    {
        // display message from event-dispatch thread of execution
        SwingUtilities.invokeLater(
                new Runnable() {  // inner class to ensure GUI updates properly

                    public void run() // updates displayArea
                    {
                        displayArea.append( messageToDisplay );
                        displayArea.setCaretPosition(
                                displayArea.getText().length() );
                    }

                }  // end inner class

        ); // end call to SwingUtilities.invokeLater
    }

    public static void main( String args[] )
    {
        server2 application = new server2();
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.waitForPackets();
    }

} // end class Server
