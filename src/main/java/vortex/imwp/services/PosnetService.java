package vortex.imwp.services;

import org.springframework.stereotype.Service;
import vortex.imwp.models.Receipt;

import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

@Service
public class PosnetService {
    private final ReceiptService receiptService;
    public PosnetService(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    public Boolean sendToPosnet(Receipt receipt) {
        try{
            String command = receiptService.generateReceiptJson(receipt);

            /*try(Socket socket = new Socket("127.0.0.1", 8080) {
                OutputStream out = socket.getOutputStream();
                out.write(command.getBytes());
                out.f
            }*/

        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
